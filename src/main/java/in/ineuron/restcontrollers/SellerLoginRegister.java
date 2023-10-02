package in.ineuron.restcontrollers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ineuron.dto.SellerLoginRequest;
import in.ineuron.dto.SellerLoginResponse;
import in.ineuron.dto.SellerRegisterRequest;
import in.ineuron.models.BookSeller;
import in.ineuron.services.BookstoreService;

@RestController
@RequestMapping("/api/seller")
public class SellerLoginRegister {

	@Autowired
	BookstoreService service;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	@PostMapping("/register") 
	public ResponseEntity<?> registerUser(@RequestBody SellerRegisterRequest requestData){
			
		
		 if (service.isSellerAvailableByPhone(requestData.getEmail()))			 
			 return ResponseEntity.badRequest().body("Email No. already registerd with another account");
	
		 if (requestData.getPhone()!=null && service.isSellerAvailableByEmail(requestData.getPhone()))	
			 return ResponseEntity.badRequest().body("Phone already registerd with another account");
			 
		 if (service.isSellerAvailableBySellerId(requestData.getSellerId())) {	
			 return ResponseEntity.badRequest().body("UserId not available");
			 
		 }else {
			 
			 BookSeller seller = new BookSeller();
			 BeanUtils.copyProperties(requestData, seller);
			 
			 //encript password
			 String encodedPassword = passwordEncoder.encode(seller.getPassword());
			 seller.setPassword(encodedPassword);
			 service.registerSeller(seller);
			 
			 SellerLoginResponse response = new SellerLoginResponse();
			 
			 //create token for login
			 response.setToken((seller.getEmail()+seller.getLocation()).replace(" ",""));				 
			 response.setSeller(seller);
			
			 return ResponseEntity.ok(response);
		 }        
	}
		 
	
	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody SellerLoginRequest loginData) {
	
		System.out.println("Encoded password : "+passwordEncoder.encode("2002mkm+"));
		BookSeller seller = service.fetchSellerBySellerId(loginData.getSellerId());
		
		if(seller==null) {
		
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for this user id");
		}
		else if (!passwordEncoder.matches(loginData.getPassword(), seller.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
			
		}else {
			
			SellerLoginResponse response = new SellerLoginResponse();
			response.setToken((seller.getEmail()+seller.getLocation()).replace(" ",""));
			response.setSeller(seller);
			
			return ResponseEntity.ok(response);
		}
			
	}
}
