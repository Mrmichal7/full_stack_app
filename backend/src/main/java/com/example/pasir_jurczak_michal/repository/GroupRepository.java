package com.example.pasir_jurczak_michal.repository;

import com.example.pasir_jurczak_michal.model.Group;
import com.example.pasir_jurczak_michal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMemberships_User(User user);
}