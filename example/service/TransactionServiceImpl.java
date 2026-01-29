package com.example.service;

import com.example.dao.CampaignStore;
import com.example.dao.TransactionStore;
import com.example.entity.Campaign;
import com.example.entity.Transaction;
import com.example.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionStore transactionStore;
    private final CampaignStore campaignStore;

    @Override
    public List<Transaction> getAllTransactions(String shardKey) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            return transactionStore.getAll(shardKey);
        } catch (IllegalArgumentException e) {
            log.error("Invalid shard key provided: {}", shardKey, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all transactions for shard: {}", shardKey, e);
            throw new RuntimeException("Failed to retrieve transactions", e);
        }
    }

    @Override
    public Optional<Transaction> getTransactionById(String shardKey, String transactionId) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (transactionId == null || transactionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID cannot be null or empty");
            }
            return transactionStore.getById(shardKey, transactionId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters - shardKey: {}, transactionId: {}", shardKey, transactionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get transaction by ID: {} for shard: {}", transactionId, shardKey, e);
            throw new RuntimeException("Failed to retrieve transaction", e);
        }
    }

    @Transactional
    @Override
    public void createTransaction(String shardKey, Transaction transaction) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }
            if (transaction.getCampaignId() == null) {
                throw new IllegalArgumentException("Campaign ID cannot be null");
            }
            if (transaction.getAmount() == null || transaction.getAmount().signum() <= 0) {
                throw new IllegalArgumentException("Transaction amount must be positive");
            }

            Campaign campaign = campaignStore.getById(shardKey, transaction.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Campaign not found: " + transaction.getCampaignId()));

            campaign.setFundRaised(campaign.getFundRaised().add(transaction.getAmount()));

            transactionStore.create(shardKey, transaction);
            campaignStore.update(shardKey, transaction.getCampaignId(), campaign);


            log.info("Transaction created successfully for campaign: {}", transaction.getCampaignId());
        } catch (RuntimeException e) {
            log.error("Failed to create transaction for campaign: {}",
                    transaction != null ? transaction.getCampaignId() : "null", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating transaction for shard: {}", shardKey, e);
            throw new RuntimeException("Failed to create transaction", e);
        }
    }

    @Override
    public void updateTransaction(String shardKey, String transactionId, Transaction transaction) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (transactionId == null || transactionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID cannot be null or empty");
            }
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }

            if (!transactionStore.exists(shardKey, transactionId)) {
                throw new RuntimeException("Transaction not found: " + transactionId);
            }

            transactionStore.update(shardKey, transactionId, transaction);
            log.info("Transaction updated successfully: {}", transactionId);
        } catch (RuntimeException e) {
            log.error("Failed to update transaction: {} for shard: {}", transactionId, shardKey, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating transaction: {} for shard: {}", transactionId, shardKey, e);
            throw new RuntimeException("Failed to update transaction", e);
        }
    }

    @Override
    public boolean transactionExists(String shardKey, String transactionId) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (transactionId == null || transactionId.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID cannot be null or empty");
            }
            return transactionStore.exists(shardKey, transactionId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters - shardKey: {}, transactionId: {}", shardKey, transactionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to check transaction existence: {} for shard: {}", transactionId, shardKey, e);
            throw new RuntimeException("Failed to check transaction existence", e);
        }
    }

    @Override
    public List<Transaction> getTransactionsByDonorId(String shardKey, Long donorId) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (donorId == null) {
                throw new IllegalArgumentException("Donor ID cannot be null");
            }
            return transactionStore.getByDonorId(shardKey, donorId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters - shardKey: {}, donorId: {}", shardKey, donorId, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get transactions by donor ID: {} for shard: {}", donorId, shardKey, e);
            throw new RuntimeException("Failed to retrieve transactions by donor", e);
        }
    }

    @Override
    public List<Transaction> getTransactionsByCampaignId(String shardKey, Long campaignId) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (campaignId == null) {
                throw new IllegalArgumentException("Campaign ID cannot be null");
            }
            return transactionStore.getByCampaignId(shardKey, campaignId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters - shardKey: {}, campaignId: {}", shardKey, campaignId, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get transactions by campaign ID: {} for shard: {}", campaignId, shardKey, e);
            throw new RuntimeException("Failed to retrieve transactions by campaign", e);
        }
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String shardKey, String status) {
        try {
//            if (shardKey == null || shardKey.trim().isEmpty()) {
//                throw new IllegalArgumentException("Shard key cannot be null or empty");
//            }
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException("Status cannot be null or empty");
            }
            return transactionStore.getByStatus(shardKey, status);
        } catch (IllegalArgumentException e) {
            log.error("Invalid parameters - shardKey: {}, status: {}", shardKey, status, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to get transactions by status: {} for shard: {}", status, shardKey, e);
            throw new RuntimeException("Failed to retrieve transactions by status", e);
        }
    }
}
