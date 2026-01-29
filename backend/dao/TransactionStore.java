package com.example.dao;

import com.example.entity.Transaction;
import io.appform.dropwizard.sharding.dao.RelationalDao;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TransactionStore {

    private static final int MAX_FETCH_COUNT = 100;
    private final RelationalDao<Transaction> transactionRelationalDao;

    public List<Transaction> getAll(String shardKey) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Transaction.class);
            return transactionRelationalDao.select(shardKey, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transactions", e);
        }
    }

    public Optional<Transaction> getById(String shardKey, String transactionId) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Transaction.class)
                    .add(Restrictions.eq("transactionId", transactionId));
            List<Transaction> transactions = transactionRelationalDao.select(shardKey, criteria, 0, 1);
            return transactions.isEmpty() ? Optional.empty() : Optional.of(transactions.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transaction: " + transactionId, e);
        }
    }

    public void create(String shardKey, Transaction transaction) {
        try {
            transactionRelationalDao.save(shardKey, transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transaction", e);
        }
    }

    public void update(String shardKey, String transactionId, Transaction updatedTransaction) {
        try {
            transactionRelationalDao.update(shardKey,
                    DetachedCriteria.forClass(Transaction.class)
                            .add(Restrictions.eq("transactionId", transactionId)),
                    transaction -> {
                        transaction.setDonorId(updatedTransaction.getDonorId());
                        transaction.setCampaignId(updatedTransaction.getCampaignId());
                        transaction.setAmount(updatedTransaction.getAmount());
                        transaction.setUpiId(updatedTransaction.getUpiId());
                        transaction.setStatus(updatedTransaction.getStatus());
                        transaction.setFailureReason(updatedTransaction.getFailureReason());
                        transaction.setIsAnonymous(updatedTransaction.getIsAnonymous());
                        transaction.setDonorMessage(updatedTransaction.getDonorMessage());
                        transaction.setReceiptNumber(updatedTransaction.getReceiptNumber());
                        transaction.setReceiptUrl(updatedTransaction.getReceiptUrl());
                        return transaction;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update transaction: " + transactionId, e);
        }
    }

    public boolean exists(String shardKey, String transactionId) {
        return getById(shardKey, transactionId).isPresent();
    }

    public List<Transaction> getByDonorId(String shardKey, Long donorId) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Transaction.class)
                    .add(Restrictions.eq("donorId", donorId));
            return transactionRelationalDao.select(shardKey, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transactions for donor: " + donorId, e);
        }
    }

    public List<Transaction> getByCampaignId(String shardKey, Long campaignId) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Transaction.class)
                    .add(Restrictions.eq("campaignId", campaignId));
            return transactionRelationalDao.select(shardKey, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transactions for campaign: " + campaignId, e);
        }
    }

    public List<Transaction> getByStatus(String shardKey, String status) {
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Transaction.class)
                    .add(Restrictions.eq("status", status));
            return transactionRelationalDao.select(shardKey, criteria, 0, MAX_FETCH_COUNT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transactions with status: " + status, e);
        }
    }
}
