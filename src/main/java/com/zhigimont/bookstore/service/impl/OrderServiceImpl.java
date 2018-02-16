package com.zhigimont.bookstore.service.impl;

import com.zhigimont.bookstore.domain.*;
import com.zhigimont.bookstore.repository.BillingAddressRepository;
import com.zhigimont.bookstore.repository.OrderRepository;
import com.zhigimont.bookstore.repository.PaymentRepository;
import com.zhigimont.bookstore.repository.ShippingAddressRepository;
import com.zhigimont.bookstore.service.BookService;
import com.zhigimont.bookstore.service.CartItemService;
import com.zhigimont.bookstore.service.OrderService;
import com.zhigimont.bookstore.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BillingAddressRepository billingAddressRepository;
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private BookService bookService;
    @Autowired
    private MailConstructor mailConstructor;


    @Override
    public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
                             BillingAddress billingAddress, Payment payment, String shippingMethod, User user) {
        Order order = new Order();
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);
        order.setPayment(payment);
        order.setOrderStatus("created");
        order.setShippingMethod(shippingMethod);

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

       for (CartItem cartItem: cartItemList){
           Book book = cartItem.getBook();
           cartItem.setOrder(order);
           book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
       }
       order.setCartItemList(cartItemList);
       order.setOrderDate(Calendar.getInstance().getTime());
       order.setOrderTotal(shoppingCart.getGrandTotal());
       shippingAddress.setOrder(order);
       billingAddress.setOrder(order);
       payment.setOrder(order);
       order.setUser(user);
       order = orderRepository.save(order);
        return order;
    }

    public Order findOne(Long id){
        return orderRepository.findOne(id);
    }
}
