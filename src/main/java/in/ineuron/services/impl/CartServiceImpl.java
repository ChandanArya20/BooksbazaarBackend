package in.ineuron.services.impl;

import in.ineuron.models.Cart;
import in.ineuron.models.User;
import in.ineuron.repositories.CartRepository;
import in.ineuron.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepo;


    @Override
    public void insertCartData(Cart cart) {

        cartRepo.save(cart);
    }

    @Override
    public List<Cart> getAllCartDataByUser(User user) {

        return cartRepo.findByUser(user);
    }

    @Override
    public void updateCartItemQuantity(Cart cart) {

        Optional<Cart> existingCartItem = cartRepo.findById(cart.getId());

        if (existingCartItem.isPresent()) {

            Cart dbCartItem = existingCartItem.get();

            dbCartItem.setQuantity(cart.getQuantity());

            // Save the updated cart item
            cartRepo.save(dbCartItem);
        }
    }

    @Override
    public void deleteCartItems(Cart[] carts) {

        cartRepo.deleteAllInBatch(Arrays.asList(carts));
    }


}
