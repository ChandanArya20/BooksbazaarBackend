package in.ineuron.services;

import in.ineuron.models.Cart;
import in.ineuron.models.User;

import java.util.List;

public interface CartService {

    public void insertCartData(Cart cart);
    public List<Cart> getAllCartDataByUser(User user);
    public void updateCartItemQuantity(Cart cart);
    public void deleteCartItems(Cart[] carts);
}
