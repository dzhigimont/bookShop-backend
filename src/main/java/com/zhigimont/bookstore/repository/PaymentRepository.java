package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
