package com.example.pasir_jurczak_michal.controller;

import com.example.pasir_jurczak_michal.dto.BalanceDTO;
import com.example.pasir_jurczak_michal.dto.TransactionDTO;
import com.example.pasir_jurczak_michal.model.Transaction;
import com.example.pasir_jurczak_michal.model.User;
import com.example.pasir_jurczak_michal.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public Transaction addTransaction(@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(
            @Argument Long id,
            @Valid @Argument TransactionDTO transactionDTO
    ) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @MutationMapping
    public Boolean deleteTransaction(@Argument Long id) {
        try {
            transactionService.deleteTransaction(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @QueryMapping
    public BalanceDTO userBalance(){
        User user = transactionService.getCurrentUser();
        return transactionService.getUserBalance(user);
    }
}
