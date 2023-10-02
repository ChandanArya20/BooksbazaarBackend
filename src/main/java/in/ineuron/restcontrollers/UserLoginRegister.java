package in.ineuron.restcontrollers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ineuron.config.BCryptEncoder;
import in.ineuron.dto.AddressRequest;
import in.ineuron.dto.LoginRequest;
import in.ineuron.dto.LoginResponse;
import in.ineuron.dto.RegisterRequest;
import in.ineuron.dto.UserResponse;
import in.ineuron.models.Address;
import in.ineuron.models.User;
import in.ineuron.services.BookstoreService;

@RestController
@RequestMapping("/api/user")
public class UserLoginRegister {

	@Autowired
	BookstoreService service;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
		
	@PostMapping("/register") 
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest requestData){
					
		 if (service.isUserAvailableByPhone(requestData.getPhone())) {		 
			 
			 return ResponseEntity.badRequest().body("Phone No. already registerd with another account");
		 }
	
		 if (requestData.getEmail()!=null && service.isUserAvailableByEmail(requestData.getEmail())) {	
			 
			 return ResponseEntity.badRequest().body("Email already registerd with another account");
			 
		 }else {
			 
			 User user = new User();
			 BeanUtils.copyProperties(requestData, user);
			 
			 //encript password
			 String encodedPwd = passwordEncoder.encode(user.getPassword());
			 user.setPassword(encodedPwd);
			 
			 service.registerUser(user);
			 
			 LoginResponse response = new LoginResponse();
			 if(user.getPhone()!=null)
				 response.setToken((user.getPhone()+user.getName()).replace(" ",""));
			 else
				 response.setToken((user.getEmail()+user.getName()).replace(" ",""));
				 
			 UserResponse userResponse = new UserResponse();
			 BeanUtils.copyProperties(user, userResponse);
			 response.setUser(userResponse);
			
			 return ResponseEntity.ok(response);
		 }        
	}
		 
	
	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginData) {
		   	
		if(loginData.getPhone()!=null) {
			
			User user = service.fetchUserByPhone(loginData.getPhone());
		
			
			if(user==null) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for this phone No.");
			}
			else if (!passwordEncoder.matches(loginData.getPassword(), user.getPassword()) ) {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
				
			}else {
				
				LoginResponse response = new LoginResponse();
				
				UserResponse userResponse = new UserResponse();
				BeanUtils.copyProperties(user, userResponse);
				response.setUser(userResponse);
				
				response.setToken((user.getPhone()+user.getName()).replace(" ",""));
				
				return ResponseEntity.ok(response);
			}
			
		}else {
			
			User user = service.fetchUserByEmail(loginData.getEmail());
			
			if(user==null) {
	        	
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for this email");
	        }
	        else if (!passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
	        	
	        }else {
	        	LoginResponse response = new LoginResponse();
	        	
	        	UserResponse userResponse = new UserResponse();
				BeanUtils.copyProperties(user, userResponse);
				response.setUser(userResponse);
				response.setUser(userResponse);
				
				response.setToken((user.getEmail()+user.getName()).replace(" ", ""));
				
				return ResponseEntity.ok(response);
	        }
		}
		

    }	
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getWholeUserData( @PathVariable Long userId){
		
		UserResponse userResponse = service.fetchUserDetails(userId);
		
		if(userResponse!=null)	
			return ResponseEntity.ok(userResponse);	
		else 			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User id not found");
	}
	
	@PostMapping("/{userId}/saveAddress")
	public ResponseEntity<String> saveUserAddress(@RequestBody AddressRequest address,  @PathVariable Long userId){
		
		service.insertUserAddress(address, userId);
		
		return ResponseEntity.ok("Address saved successfully...");	
	}
	
	@GetMapping("/{userId}/address")
	public ResponseEntity<?> getUserAddress( @PathVariable Long userId){
		
		List<Address> addresses = service.fetchAddressByUserId(userId);
		
		if(!addresses.isEmpty())
			return ResponseEntity.ok(addresses);
		else
			return ResponseEntity.badRequest().body("Failed..., UserId not found");
	}
	
	
	
}




