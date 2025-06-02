package com.example.pasir_jurczak_michal.repository;

import com.example.pasir_jurczak_michal.model.Transaction;
import com.example.pasir_jurczak_michal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
    List<Transaction> findByUser(User user);
}
