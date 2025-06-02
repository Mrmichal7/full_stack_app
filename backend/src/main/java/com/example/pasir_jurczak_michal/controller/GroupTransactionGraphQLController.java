package com.example.pasir_jurczak_michal.controller;

import com.example.pasir_jurczak_michal.dto.GroupTransactionDTO;
import com.example.pasir_jurczak_michal.model.User;
import com.example.pasir_jurczak_michal.service.GroupTransactionService;
import com.example.pasir_jurczak_michal.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GroupTransactionGraphQLController {
    private final GroupTransactionService groupTransactionService;
    private final TransactionService transactionService;

    public GroupTransactionGraphQLController(GroupTransactionService groupTransactionService, TransactionService transactionService) {
        this.groupTransactionService = groupTransactionService;
        this.transactionService = transactionService;
    }

    @MutationMapping
    public Boolean addGroupTransaction(@Argument GroupTransactionDTO groupTransactionDTO) {
        User user = transactionService.getCurrentUser();
        groupTransactionService.addGroupTransaction(groupTransactionDTO, user);
        return true;
    }
}
