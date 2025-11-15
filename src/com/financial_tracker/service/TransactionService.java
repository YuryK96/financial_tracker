package com.financial_tracker.service;

import com.financial_tracker.domain.CategorizedTransaction;
import com.financial_tracker.domain.Transaction;
import com.financial_tracker.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }


    public CategorizedTransaction createTransaction(CategorizedTransaction transaction, UUID accountId) throws IllegalArgumentException {
        try {
            Transaction foundTransaction = transactionRepository.getById(transaction.getId());

            if(foundTransaction != null){
                throw new IllegalArgumentException("Transaction already exist");
            }

            accountService.addTransaction(transaction, accountId);
            return transactionRepository.save(transaction);

        } catch (Exception e) {
            accountService.removeTransaction(transaction,accountId);
            transactionRepository.delete(transaction.getId());
            throw e;
        }

    }

    public boolean deleteTransaction(UUID transactionId, UUID accountId) throws IllegalArgumentException {

        CategorizedTransaction foundTransaction = transactionRepository.getById(transactionId);
        try {
            if(foundTransaction == null){
                throw new IllegalArgumentException("Transaction not exist");
            }

            accountService.removeTransaction(foundTransaction, accountId);
            return transactionRepository.delete(foundTransaction.getId());

        } catch (Exception e) {
            if(foundTransaction != null){
            accountService.removeTransaction(foundTransaction,accountId);
            transactionRepository.delete(foundTransaction.getId());}
            throw e;
        }

    }

    public List<CategorizedTransaction> getAccountTransactionByCreatedAt (UUID accountId, LocalDateTime start, LocalDateTime end){
        return  this.transactionRepository.getAccountTransactionByCreatedAt(accountId, start, end);
    }

    public List<CategorizedTransaction> getAccountTransactionLastDays (UUID accountId, long days){
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return  this.transactionRepository.getAccountTransactionByCreatedAt(accountId, start, end);
    }
}
