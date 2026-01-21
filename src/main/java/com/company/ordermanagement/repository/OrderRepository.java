package com.company.ordermanagement.repository;

import com.company.ordermanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
