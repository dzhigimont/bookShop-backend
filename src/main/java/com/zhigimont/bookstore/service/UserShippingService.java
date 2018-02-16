package com.zhigimont.bookstore.service;

import com.zhigimont.bookstore.domain.UserShipping;

public interface UserShippingService {
    UserShipping findById(Long id);
    void removeById(Long id);
}
