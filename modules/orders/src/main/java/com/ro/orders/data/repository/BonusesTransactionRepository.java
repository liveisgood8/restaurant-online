package com.ro.orders.data.repository;

import com.ro.orders.data.model.BonusesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusesTransactionRepository extends JpaRepository<BonusesTransaction, Long> {
}
