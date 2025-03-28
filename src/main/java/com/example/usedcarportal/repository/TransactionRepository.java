package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Additional query methods can be defined here if needed in the future.
}
