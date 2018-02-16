package com.zhigimont.bookstore.service;

import com.zhigimont.bookstore.domain.Book;
import com.zhigimont.bookstore.domain.CartItem;
import com.zhigimont.bookstore.domain.ShoppingCart;
import com.zhigimont.bookstore.domain.User;

import java.util.List;

public interface CartItemService {
    CartItem addBookToCartItem(Book book, User user, int qty);
    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
//    List<CartItem> findByOrder(Order order);
    CartItem updateCartItem(CartItem cartItem);
    void removeCartItem(CartItem cartItem);
    CartItem findById(long id);
    CartItem save(CartItem cartItem);
}
