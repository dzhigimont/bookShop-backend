package com.zhigimont.bookstore.resource;

import com.zhigimont.bookstore.domain.User;
import com.zhigimont.bookstore.domain.UserBilling;
import com.zhigimont.bookstore.domain.UserPayment;
import com.zhigimont.bookstore.service.UserPaymentService;
import com.zhigimont.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentResource {
    @Autowired
    private UserService userService;
    @Autowired
    UserPaymentService userPaymentService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addNewCreditCarPost(
            @RequestBody UserPayment userPayment,
            Principal principal
            )
    {
        User user = new User();
        if(principal != null){
        user = userService.findByUsername(principal.getName());
        }

        UserBilling userBilling = userPayment.getUserBilling();
        userService.updateUserBilling(userBilling, userPayment, user);
        return new ResponseEntity(new HashMap<String,String>().put("message","Payment Added(Update) Successfully"), HttpStatus.OK);

    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity removePaymentPost(
            @RequestBody String id, Principal principal
    ){
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
        }
        userPaymentService.removeById(Long.parseLong(id));
        return new ResponseEntity(new HashMap<String,String>().put("message","Payment Remove Successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    public ResponseEntity setDefaultPaymentPost(
            @RequestBody String id, Principal principal
    ){
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
        }
        userService.setUserDefaultPayment(Long.parseLong(id), user);
        return new ResponseEntity(new HashMap<String,String>().put("message","Payment Set Default Successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/getUserPaymentList", method = RequestMethod.GET)
    public List<UserPayment> getUserPaymentList(Principal principal){
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
        }
        List<UserPayment> userPaymentList = user.getUserPaymentList();
        return userPaymentList;
    }
}
