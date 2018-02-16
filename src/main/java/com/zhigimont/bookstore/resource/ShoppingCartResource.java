package com.zhigimont.bookstore.resource;

import com.zhigimont.bookstore.domain.Book;
import com.zhigimont.bookstore.domain.CartItem;
import com.zhigimont.bookstore.domain.ShoppingCart;
import com.zhigimont.bookstore.domain.User;
import com.zhigimont.bookstore.service.BookService;
import com.zhigimont.bookstore.service.CartItemService;
import com.zhigimont.bookstore.service.ShoppingCartService;
import com.zhigimont.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/cart")
public class ShoppingCartResource {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addItem(
            @RequestBody HashMap<String, String > mapper, Principal principal) {
        long bookId = Long.parseLong(mapper.get("bookId"));
        int qty = Integer.parseInt(mapper.get("qty"));
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
        }
        Book book = bookService.findOne(bookId);
        if(qty > book.getInStockNumber()){
          return new ResponseEntity(new HashMap<String,String>().put("message","Not enough Stock"), HttpStatus.BAD_REQUEST);
        }
        CartItem cartItem = cartItemService.addBookToCartItem(book, user, qty);

        return new ResponseEntity(new HashMap<String,String>().put("message","Book Added Successfully"), HttpStatus.OK);
    }

    @RequestMapping("/getCartItemList")
    public List<CartItem> getCartItemList(Principal principal) {
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
        }
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        shoppingCartService.updateShoppingCart(shoppingCart);

        return cartItemList;
    }

    @RequestMapping("/getShoppingCart")
    public ShoppingCart getShoppingCart(Principal principal) {
        User user = new User();
        if(principal != null){
            user = userService.findByUsername(principal.getName());
            ShoppingCart shoppingCart = user.getShoppingCart();
            shoppingCartService.updateShoppingCart(shoppingCart);
            return shoppingCart;
        }

        return null;
    }

    @RequestMapping("/removeItem")
    public ResponseEntity removeItem(@RequestBody String id) {
        cartItemService.removeCartItem(cartItemService.findById(Long.parseLong(id)));
        return new ResponseEntity(new HashMap<String,String>().put("message","Cart Item Remove Successfully"), HttpStatus.OK);
    }

    @RequestMapping("/updateCartItem")
    public ResponseEntity updateCartItem(@RequestBody HashMap<String, String > mapper) {
        long cartItemId = Long.parseLong(mapper.get("cartItemId"));
        int qty = Integer.parseInt(mapper.get("qty"));
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQty(qty);
        cartItemService.updateCartItem(cartItem);
        return new ResponseEntity(new HashMap<String,String>().put("message","Cart Item Update Successfully"), HttpStatus.OK);
    }


}
