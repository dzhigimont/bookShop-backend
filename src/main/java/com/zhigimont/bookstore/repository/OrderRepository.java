package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.Order;
import com.zhigimont.bookstore.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByUser(User user);
}
