package com.hrms.repository;

import com.hrms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByTitle(String title);
    
    List<Role> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT COUNT(r) FROM Role r")
    long countAllRoles();
    
    boolean existsByTitle(String title);
}
