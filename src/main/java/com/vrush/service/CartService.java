package com.vrush.service;

import com.vrush.model.Cart;
import com.vrush.model.CartItem;
import com.vrush.request.AddCartItemRequest;

public interface CartService {
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception;
    public CartItem updateCartItemQuantity(Long cartItemId,int quantity) throws Exception;

    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;

    public Long calculateCartTotal(Cart cart) throws Exception;

    public Cart findCartById(Long cartId) throws Exception;

    public Cart findCartByUserId(Long userId) throws Exception;

    //after making successful order clear the cart method
    public Cart clearCart(Long userId) throws Exception;

}
