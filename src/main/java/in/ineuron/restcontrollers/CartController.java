package in.ineuron.restcontrollers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import in.ineuron.dto.BookResponse;
import in.ineuron.dto.CartResponse;
import in.ineuron.models.Cart;
import in.ineuron.models.User;
import in.ineuron.services.BookstoreService;

@RestController
@RequestMapping("api/cart")
public class CartController {
	
	@Autowired
	private BookstoreService service;
	
	@Value("${baseURL}")
	private String baseURL;
	
	
	
	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody Cart cart){
		
		service.insertCartData(cart);
		
		return ResponseEntity.ok("Item inserted into cart successfully...");
	}
		 
	@GetMapping("/user/{userId}/allCartData")
	public ResponseEntity<List<CartResponse>> getAllCartDataByUser(@PathVariable Long userId){
		
		User user = new User();
		user.setId(userId);
		
		List<Cart> cartList = service.getAllCartDataByUser(user);

		
		List<CartResponse> cartResponseList=new ArrayList<>();
		
		cartList.forEach((cart)->{
			
			CartResponse cartResponse = new CartResponse();
			BeanUtils.copyProperties(cart, cartResponse);
			
			BookResponse bookResponse = new BookResponse();
			BeanUtils.copyProperties(cart.getBook(), bookResponse);
			bookResponse.setImageURL(baseURL+"/api/image/"+cart.getBook().getCoverImage().getId());
			
			cartResponse.setBook(bookResponse);
			cartResponseList.add(cartResponse);
		});
		
		return ResponseEntity.ok(cartResponseList);
	}
	
	@PatchMapping("/updateCartQuantity")
	public ResponseEntity<String> updateCart(@RequestBody Cart cart){
		
		service.updateCartItemQuantity(cart);
		
		return ResponseEntity.ok("Cart Item quantity got updated...");
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteCart(@RequestBody Cart[] carts){
		
		service.deleteCartItems(carts);
		
		return ResponseEntity.ok("Cart Item quantity got updated...");		
	}
	
	
}





