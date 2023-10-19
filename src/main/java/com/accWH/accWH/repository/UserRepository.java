package com.accWH.accWH.repository;

import com.accWH.accWH.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByRole(String role_user);

    User findBylname(String lname);
}