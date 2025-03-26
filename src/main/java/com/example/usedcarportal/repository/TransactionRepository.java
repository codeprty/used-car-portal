package com.example.usedcarportal.repository;

import com.example.usedcarportal.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
