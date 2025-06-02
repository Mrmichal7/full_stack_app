package com.example.pasir_jurczak_michal.service;

import com.example.pasir_jurczak_michal.dto.BalanceDTO;
import com.example.pasir_jurczak_michal.dto.TransactionDTO;
import com.example.pasir_jurczak_michal.model.Transaction;
import com.example.pasir_jurczak_michal.model.TransactionType;
import com.example.pasir_jurczak_michal.model.User;
import com.example.pasir_jurczak_michal.repository.TransactionRepository;
import com.example.pasir_jurczak_michal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono zalogowanego użytkownika"));
    }

    public List<Transaction> getAllTransactions() {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID "+id ));
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setUser(getCurrentUser());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }


    public Transaction updateTransaction(long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());

        return transactionRepository.save(transaction);
    }


    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("Nie znaleziono transakcji o ID " + id);
        }
        transactionRepository.deleteById(id);
    }

    public BalanceDTO getUserBalance(User user) {
        List<Transaction> userTransactions = transactionRepository.findByUser((user));

        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new BalanceDTO(income, expense, income - expense);
    }
}
