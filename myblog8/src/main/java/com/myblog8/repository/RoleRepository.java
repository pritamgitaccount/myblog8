package com.myblog8.repository;

import com.myblog8.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}