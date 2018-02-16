package com.zhigimont.bookstore.service;

import com.zhigimont.bookstore.domain.User;
import com.zhigimont.bookstore.domain.UserBilling;
import com.zhigimont.bookstore.domain.UserPayment;
import com.zhigimont.bookstore.domain.UserShipping;
import com.zhigimont.bookstore.domain.security.UserRole;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface UserService {

  User createUser(User user, Set<UserRole> userRole);
  User findByUsername(String username);
  User findByEmail(String email);
  User save(User user);
  User findById(Long id);
  void updateUserPaymentInfo(UserBilling userBilling, UserPayment userPayment, User user);
  void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
  void setUserDefaultPayment(long id, User user);
  void updateUserShipping(UserShipping userShipping, User user);
  void setUserDefaultShipping(long id, User user);
}
