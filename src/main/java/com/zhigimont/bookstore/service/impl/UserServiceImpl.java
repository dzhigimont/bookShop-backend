package com.zhigimont.bookstore.service.impl;
import com.zhigimont.bookstore.domain.*;
import com.zhigimont.bookstore.domain.security.UserRole;
import com.zhigimont.bookstore.repository.*;
import com.zhigimont.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserBillingRepository userBillingRepository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Override
    @Transactional
    public User createUser(User user, Set<UserRole> userRole) {
        User localUser = userRepository.findByUsername(user.getUsername());
        if(localUser!=null){
            LOG.info("User with user name {} already exist. Nothing will be done",user.getUsername());
        }else {
            for (UserRole ur : userRole){
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRole);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);

            user.setUserPaymentList(new ArrayList<UserPayment>());
            user.setUserShippingList(new ArrayList<UserShipping>());
            localUser = userRepository.save(user);
        }

        return localUser;

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public void updateUserPaymentInfo(UserBilling userBilling, UserPayment userPayment, User user) {
        save(user);
        userBillingRepository.save(userBilling);
        userPaymentRepository.save(userPayment);
    }

    @Override
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
        userPayment.setUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);
        userBilling.setUserPayment(userPayment);
        user.getUserPaymentList().add(userPayment);
        save(user);
    }

    @Override
    public void setUserDefaultPayment(long id, User user) {
       List<UserPayment>  userPaymentList = userRepository.findOne(user.getId()).getUserPaymentList();
       for (UserPayment userPayment: userPaymentList){
           if (userPayment.getId() == id){
               userPayment.setDefaultPayment(true);
               userPaymentRepository.save(userPayment);
           }else {
               userPayment.setDefaultPayment(false);
               userPaymentRepository.save(userPayment);
           }
       }
    }

    @Override
    public void updateUserShipping(UserShipping userShipping, User user) {
        userShipping.setUser(user);
        userShipping.setUserShippingDefault(true);
        user.getUserShippingList().add(userShipping);
        save(user);
    }

    @Override
    public void setUserDefaultShipping(long id, User user) {
        List<UserShipping>  userShippingList = userRepository.findOne(user.getId()).getUserShippingList();
        for (UserShipping userShipping: userShippingList){
            if (userShipping.getId() == id){
                userShipping.setUserShippingDefault(true);
                userShippingRepository.save(userShipping);
            }else {
                userShipping.setUserShippingDefault(false);
                userShippingRepository.save(userShipping);
            }
        }
    }
}
