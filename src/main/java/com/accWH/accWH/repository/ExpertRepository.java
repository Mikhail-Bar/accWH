package com.accWH.accWH.repository;

import com.accWH.accWH.model.Expert;
import com.accWH.accWH.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Optional<User> findByUsername(String username);
}