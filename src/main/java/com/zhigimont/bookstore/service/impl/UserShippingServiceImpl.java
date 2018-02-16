package com.zhigimont.bookstore.service.impl;

import com.zhigimont.bookstore.domain.UserShipping;
import com.zhigimont.bookstore.repository.UserShippingRepository;
import com.zhigimont.bookstore.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImpl implements UserShippingService {
    @Autowired
    private UserShippingRepository userShippingRepository;
    @Override
    public UserShipping findById(Long id) {
        return userShippingRepository.findOne(id);
    }

    @Override
    public void removeById(Long id) {
        userShippingRepository.delete(id);
    }
}
