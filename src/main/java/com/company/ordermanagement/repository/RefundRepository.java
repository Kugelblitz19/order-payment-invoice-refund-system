package com.company.ordermanagement.repository;

import com.company.ordermanagement.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Ref;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund,Long> {
    Optional<Refund> findByOrderId(Long orderId);
}
