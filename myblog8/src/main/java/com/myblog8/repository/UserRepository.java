package com.myblog8.repository;

import com.myblog8.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Here custom query for sql written down
    Optional<User> findByUsername(String userName);

    //Whenever we call this incomplete method like this , Spring Boot internally generates HQL query
    // (Hibernate query language) for this, and it will start searching for the database
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String userName, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}