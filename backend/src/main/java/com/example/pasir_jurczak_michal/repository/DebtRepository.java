package com.example.pasir_jurczak_michal.repository;

import com.example.pasir_jurczak_michal.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByGroupId(Long groupId);
}