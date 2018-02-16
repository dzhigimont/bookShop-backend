package com.zhigimont.bookstore.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhigimont.bookstore.domain.*;
import com.zhigimont.bookstore.service.CartItemService;
import com.zhigimont.bookstore.service.OrderService;
import com.zhigimont.bookstore.service.ShoppingCartService;
import com.zhigimont.bookstore.service.UserService;
import com.zhigimont.bookstore.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/checkout")
public class CheckoutResource {
    private Order order = new Order();

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private MailConstructor mailConstructor;

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public Order checkoutPost(
            @RequestBody HashMap<String,Object> mapper,
            Principal principal
            ){
        ObjectMapper om = new ObjectMapper();

        ShippingAddress shippingAddress = om.convertValue(mapper.get("shippingAddress"), ShippingAddress.class);
        BillingAddress billingAddress = om.convertValue(mapper.get("billingAddress"), BillingAddress.class);
        Payment payment = om.convertValue(mapper.get("payment"), Payment.class);
        String shippingMethod = (String)mapper.get("shippingMethod");
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
        }
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        Order order = orderService.createOrder(shoppingCart,shippingAddress,billingAddress,payment, shippingMethod,user);
        mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
        shoppingCartService.clearShoppingCart(shoppingCart);
        LocalDate today = LocalDate.now();
        LocalDate estimatedDelieveryDate;
        if (shippingMethod.equals("groundShipping")){
            estimatedDelieveryDate = today.plusDays(5);
        }else {
            estimatedDelieveryDate = today.plusDays(3);
        }
       this.order = order;
        return order;
    }
}
