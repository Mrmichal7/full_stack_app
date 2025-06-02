package com.example.pasir_jurczak_michal.repository;

import com.example.pasir_jurczak_michal.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByGroupId(Long groupId);
}
