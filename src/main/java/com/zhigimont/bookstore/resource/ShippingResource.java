package com.zhigimont.bookstore.resource;

import com.zhigimont.bookstore.domain.User;
import com.zhigimont.bookstore.domain.UserShipping;
import com.zhigimont.bookstore.service.UserService;
import com.zhigimont.bookstore.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shipping")
public class ShippingResource {
    @Autowired
    private UserService userService;

    @Autowired
    UserShippingService userShippingService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addNewShipping(@RequestBody UserShipping userShipping, Principal principal){
        User user = new User();
        if(principal!=null){
            user =userService.findByUsername(principal.getName());
        }
        userService.updateUserShipping(userShipping,user);
        return new ResponseEntity(new HashMap<String,String>().put("message","Shipping Added(Update) Successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity removeUserShipping(@RequestBody String id){
        Long shippingId = Long.parseLong(id);
        userShippingService.removeById(shippingId);
        return new ResponseEntity(new HashMap<String,String>().put("message","Shipping Remove Successfully"), HttpStatus.OK);
    }

    @RequestMapping(value = "/getUserShippingList", method = RequestMethod.GET)
    public List<UserShipping> removeUserShipping(Principal principal){
        User user = new User();
        if(principal!=null){
            user =userService.findByUsername(principal.getName());
        }
        List<UserShipping> userShippingList = user.getUserShippingList();

        return userShippingList;
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    public ResponseEntity setDefaultUserShipping(@RequestBody String id, Principal principal){
        Long shippingId = Long.parseLong(id);
        User user = new User();
        if(principal!=null){
            user =userService.findByUsername(principal.getName());
        }
        userService.setUserDefaultShipping(shippingId, user);
        return new ResponseEntity(new HashMap<String,String>().put("message","Shipping Set Default Successfully"), HttpStatus.OK);
    }
}
