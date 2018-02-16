package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.CartItem;
import com.zhigimont.bookstore.domain.Order;
import com.zhigimont.bookstore.domain.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
    List<CartItem> findByOrder(Order order);
}
