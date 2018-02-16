package com.zhigimont.bookstore.service;

import com.zhigimont.bookstore.domain.UserPayment;

public interface UserPaymentService {
    UserPayment findById(Long id);

    void removeById(Long id);
}
