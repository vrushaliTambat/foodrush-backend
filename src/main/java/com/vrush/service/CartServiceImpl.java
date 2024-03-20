package com.vrush.service;

import com.vrush.model.Cart;
import com.vrush.model.CartItem;
import com.vrush.model.Food;
import com.vrush.model.User;
import com.vrush.repository.CartItemRepository;
import com.vrush.repository.CartRepository;
import com.vrush.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private FoodService foodService;
    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Food newFood=foodService.findFoodById(req.getFoodId());
        Cart cart=cartRepo.findByCustomerId(user.getId());

        //if cart item already exists, we update the quantity otherwise we create new cart item
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getFood().equals(newFood)) {

                int newQuantity = cartItem.getQuantity() + req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(),newQuantity);
            }
        }
        CartItem newCartItem=new CartItem();
        newCartItem.setFood(newFood);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity()*newFood.getPrice());

      CartItem savedCartItem=cartItemRepo.save(newCartItem);
      //after saving the cart item we need to add it inside the Cart as well
        cart.getItems().add(savedCartItem);

      return savedCartItem;
     }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItemOpt = cartItemRepo.findById(cartItemId);
        if(cartItemOpt.isEmpty()) {
            throw new Exception("cart item not exist");
        }
        CartItem item=cartItemOpt.get();
        item.setQuantity(quantity);
        item.setTotalPrice(quantity*item.getFood().getPrice());

        return cartItemRepo.save(item);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepo.findByCustomerId(user.getId());
        Optional<CartItem> cartItemOpt = cartItemRepo.findById(cartItemId);
        if(cartItemOpt.isEmpty()) {
            throw new Exception("cart item not exist");
        }
        CartItem item= cartItemOpt.get();
        cart.getItems().remove(item);
        return cartRepo.save(cart);

    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        Long total = 0L;
        for (CartItem cartItem : cart.getItems()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {
        Optional<Cart> optionalCart = cartRepo.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new Exception("cart not found");
        }
        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
//        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartRepo.findByCustomerId(userId);
        cart.setTotal(calculateCartTotal(cart));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
      //  User user=userService.findUserByJwtToken(jwt);
        Cart cart = findCartByUserId(userId);

        cart.getItems().clear();
        return cartRepo.save(cart);
    }
}
