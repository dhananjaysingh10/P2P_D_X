package com.example.service;

import com.example.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> getAllTransactions(String shardKey);

    Optional<Transaction> getTransactionById(String shardKey, String transactionId);

    void createTransaction(String shardKey, Transaction transaction);

    void updateTransaction(String shardKey, String transactionId, Transaction transaction);

    boolean transactionExists(String shardKey, String transactionId);

    List<Transaction> getTransactionsByDonorId(String shardKey, Long donorId);

    List<Transaction> getTransactionsByCampaignId(String shardKey, Long campaignId);

    List<Transaction> getTransactionsByStatus(String shardKey, String status);
}
