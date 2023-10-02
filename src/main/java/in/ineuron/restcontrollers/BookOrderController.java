package in.ineuron.restcontrollers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ineuron.dto.BookOrderRequest;
import in.ineuron.dto.BookOrderResponse;
import in.ineuron.models.BookOrder;
import in.ineuron.models.User;
import in.ineuron.services.BookstoreService;

@RestController
@RequestMapping("/api/order")
public class BookOrderController {

	@Autowired
	BookstoreService service;
	
	@Value("${baseURL}")
	private String baseURL;
	
	
	@PostMapping("placeOrder")
	public ResponseEntity<String> submitOrder( @RequestBody BookOrderRequest[] orderData){
		 
		List<BookOrder> bookList=new ArrayList<>();
		
		for(BookOrderRequest order:orderData) {
			
			BookOrder bookOrder = new BookOrder();
			BeanUtils.copyProperties(order, bookOrder);
			
			int deliveryTime=order.getBook().getDeliveryTime();
			LocalDate deliverydate=LocalDate.now().plusDays(deliveryTime);
			bookOrder.setDeliveryDate(deliverydate);
			
			bookList.add(bookOrder);
			service.decreaseBookStock(bookOrder.getBook().getId(), bookOrder.getQuantity());
			
		}
		service.insertOrder(bookList);
		return  ResponseEntity.ok("Order submitted successfully...");
	}
	
	@GetMapping("/user/{userId}/allOrders")
	public ResponseEntity<List<BookOrderResponse>> getOrdersByUser(@PathVariable Long userId)  {
		
		User user = new User();
		user.setId(userId);
		
		List<BookOrderResponse> orders = service.fetchOrdersByUser(user);
		Collections.reverse(orders);
			
		return ResponseEntity.ok(orders);
		
	}
	
	@GetMapping("/seller/{sellerId}/allOrders")
	public ResponseEntity<List<BookOrderResponse>> getOrdersBySeller(@PathVariable Long sellerId)  {
		
		List<BookOrderResponse> sellerOrders = service.fetchOrdersBySellerId(sellerId);
		Collections.reverse(sellerOrders);
		
		return ResponseEntity.ok(sellerOrders);
		
	}
	
	
	@PatchMapping("/{orderId}")
	public ResponseEntity<String> changeBookOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
		
		if(status.equals("Cancelled") || status.equals("Returned") ) {
			
			BookOrder order = service.getOrderById(orderId);
			service.increaseBookStock(order.getBook().getId(), order.getQuantity());
		}
		
		Boolean changeOrderStatus = service.changeOrderStatus(orderId, status);
		
		if(changeOrderStatus)		
			return ResponseEntity.ok("status updated with "+status);		
		else	
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("status updation failed...");	 
	}
	
	
}






