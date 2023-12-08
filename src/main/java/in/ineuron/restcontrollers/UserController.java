package in.ineuron.restcontrollers;

import in.ineuron.dto.*;
import in.ineuron.models.User;
import in.ineuron.services.OTPSenderService;
import in.ineuron.services.OTPStorageService;
import in.ineuron.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private OTPSenderService otpSender;

	@Autowired
	private OTPStorageService otpStorage;
	
	
		
	@PostMapping("/register") 
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest requestData){
					
		 if (userService.isUserAvailableByPhone(requestData.getPhone())) {		 
			 
			 return ResponseEntity.badRequest().body("Phone No. already registered with another account");
		 }
	
		 if (requestData.getEmail()!=null && userService.isUserAvailableByEmail(requestData.getEmail())) {	
			 
			 return ResponseEntity.badRequest().body("Email already registered with another account");
			 
		 }else {
			 
			 User user = new User();
			 BeanUtils.copyProperties(requestData, user);
			 
			 //encript password
			 String encodedPwd = passwordEncoder.encode(user.getPassword());
			 user.setPassword(encodedPwd);
			 
			 userService.registerUser(user);
			
			 return ResponseEntity.ok("User registered successfully...");
		 }        
	}
		 
	
	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginData) {
		   	
		if(loginData.getPhone()!=null) {
			
			User user = userService.fetchUserByPhone(loginData.getPhone());

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
				
				response.setToken(UUID.randomUUID().toString());
				
				return ResponseEntity.ok(response);
			}
			
		}else {
			
			User user = userService.fetchUserByEmail(loginData.getEmail());
			
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
				
				response.setToken(UUID.randomUUID().toString());
				
				return ResponseEntity.ok(response);
	        }
		}
		

    }	
	
	@GetMapping("/{user-id}")
	public ResponseEntity<?> getWholeUserData( @PathVariable(name = "user-id") String userId){
		
		UserResponse userResponse = userService.fetchUserDetails(userId);
		
		if(userResponse!=null)	
			return ResponseEntity.ok(userResponse);
		else 			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User id not found");
	}


	
}




