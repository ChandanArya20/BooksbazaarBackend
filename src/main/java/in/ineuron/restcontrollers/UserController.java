package in.ineuron.restcontrollers;

import java.util.List;

import in.ineuron.dto.*;
import in.ineuron.services.OTPSenderService;
import in.ineuron.services.OTPStorageService;
import in.ineuron.services.UserService;
import in.ineuron.utils.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import in.ineuron.models.Address;
import in.ineuron.models.User;


import javax.mail.MessagingException;

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
				
				response.setToken((user.getPhone()+user.getName()).replace(" ",""));
				
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
				response.setUser(userResponse);
				
				response.setToken((user.getEmail()+user.getName()).replace(" ", ""));
				
				return ResponseEntity.ok(response);
	        }
		}
		

    }	
	
	@GetMapping("/{user-id}")
	public ResponseEntity<?> getWholeUserData( @PathVariable(name = "user-id") Long userId){
		
		UserResponse userResponse = userService.fetchUserDetails(userId);
		
		if(userResponse!=null)	
			return ResponseEntity.ok(userResponse);	
		else 			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User id not found");
	}
	
	@PostMapping("/{user-id}/save-address")
	public ResponseEntity<String> saveUserAddress(@RequestBody AddressRequest address,  @PathVariable(name = "user-id") Long userId){
		
		userService.insertUserAddress(address, userId);
		
		return ResponseEntity.ok("Address saved successfully...");	
	}
	
	@GetMapping("/{user-id}/address")
	public ResponseEntity<?> getUserAddress( @PathVariable(name = "user-id")  Long userId){
		
		List<Address> addresses = userService.fetchAddressByUserId(userId);
		
		if(!addresses.isEmpty())
			return ResponseEntity.ok(addresses);
		else
			return ResponseEntity.badRequest().body("Failed..., UserId not found");
	}

	@GetMapping("/send-otp")
	public ResponseEntity<String> sendOTPByPhone(@RequestParam("user-name") String userName ) throws MessagingException {

		if (!userName.isBlank()) {
			if(userService.isUserAvailableByUserName(userName)){
				boolean isEmail = EmailValidator.isEmail(userName);
				Integer OTP=-1;
				if(isEmail){
					OTP = otpSender.sendOTPByEmail(userName);
				} else {

					System.out.println(userName);
					OTP = otpSender.sendOTPByPhone(userName);
				}
				otpStorage.storeOTP(userName, String.valueOf(OTP));

				return ResponseEntity.ok("Sent OTP: "+OTP);
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for "+userName);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input...");
    }


	@GetMapping("/verify-otp")
	public ResponseEntity<String> verifyOTPByPhone(
			@RequestParam("user-name") String userName,
			@RequestParam String otp ) throws MessagingException {

		if (!userName.isBlank() && !otp.isBlank() ){
			if(userService.isUserAvailableByUserName(userName)){

				if(otpStorage.verifyOTP(userName, otp)){
					otpStorage.removeOTP(userName);
					return ResponseEntity.ok("verified successfully.. ");
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP verification failed.. ");
				}

			} else{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for "+userName);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input...");
    }
    @PostMapping("/otp-verified/update-password")
    public ResponseEntity<?> UpdateUserPasswordAfterOTPVerified(
			@RequestBody UpdateUserPasswordDTO userCredential
	){
		System.out.println(userCredential);
		String userName=userCredential.getUserName();
		String newPassword=userCredential.getNewPassword();

		if (!userName.isBlank() && !newPassword.isBlank() ) {

            User user= userService.fetchUserByUserName(userName);

			if(user!=null){
				String encodedPass = passwordEncoder.encode(newPassword);
				userService.updateUserPassword(user.getId(), encodedPass);

				LoginResponse response = new LoginResponse();

				UserResponse userResponse = new UserResponse();
				BeanUtils.copyProperties(user, userResponse);
				response.setUser(userResponse);

				boolean isEmail = EmailValidator.isEmail(userName);
				if(isEmail){
					response.setToken((user.getEmail()+user.getName()).replace(" ",""));
				} else {
					response.setToken((user.getPhone()+user.getName()).replace(" ",""));
				}

				return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User name not found...");
			}
        }
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input...");
    }

	
}




