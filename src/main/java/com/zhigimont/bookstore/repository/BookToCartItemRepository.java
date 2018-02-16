package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.BookToCartItem;
import com.zhigimont.bookstore.domain.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long> {
    void deleteByCartItem(CartItem cartItem);
}
