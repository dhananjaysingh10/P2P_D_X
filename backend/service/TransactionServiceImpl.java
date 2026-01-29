package com.example.service;

import com.example.dao.TransactionStore;
import com.example.entity.Transaction;
import com.example.service.TransactionService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionStore transactionStore;

    @Override
    public List<Transaction> getAllTransactions(String shardKey) {
        return transactionStore.getAll(shardKey);
    }

    @Override
    public Optional<Transaction> getTransactionById(String shardKey, String transactionId) {
        return transactionStore.getById(shardKey, transactionId);
    }

    @Override
    public void createTransaction(String shardKey, Transaction transaction) {
        transactionStore.create(shardKey, transaction);
    }

    @Override
    public void updateTransaction(String shardKey, String transactionId, Transaction transaction) {
        if (!transactionStore.exists(shardKey, transactionId)) {
            throw new RuntimeException("Transaction not found: " + transactionId);
        }
        transactionStore.update(shardKey, transactionId, transaction);
    }

    @Override
    public boolean transactionExists(String shardKey, String transactionId) {
        return transactionStore.exists(shardKey, transactionId);
    }

    @Override
    public List<Transaction> getTransactionsByDonorId(String shardKey, Long donorId) {
        return transactionStore.getByDonorId(shardKey, donorId);
    }

    @Override
    public List<Transaction> getTransactionsByCampaignId(String shardKey, Long campaignId) {
        return transactionStore.getByCampaignId(shardKey, campaignId);
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String shardKey, String status) {
        return transactionStore.getByStatus(shardKey, status);
    }
}
