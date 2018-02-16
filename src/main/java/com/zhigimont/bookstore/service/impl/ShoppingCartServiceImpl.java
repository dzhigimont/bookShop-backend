package com.zhigimont.bookstore.service.impl;

import com.zhigimont.bookstore.domain.CartItem;
import com.zhigimont.bookstore.domain.ShoppingCart;
import com.zhigimont.bookstore.repository.ShoppingCartRepository;
import com.zhigimont.bookstore.service.CartItemService;
import com.zhigimont.bookstore.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Override
    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
        BigDecimal cartTotal = new BigDecimal(0);
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        for(CartItem cartItem: cartItemList){
            if(cartItem.getBook().getInStockNumber()>0){
                cartItemService.updateCartItem(cartItem);
                cartTotal = cartTotal.add(cartItem.getSubtotal());
            }
        }
        shoppingCart.setGrandTotal(cartTotal);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        for(CartItem cartItem: cartItemList){
            cartItem.setShoppingCart(null);
            cartItemService.save(cartItem);
            shoppingCart.setGrandTotal(new BigDecimal(0));
            shoppingCartRepository.save(shoppingCart);
        }
    }
}
