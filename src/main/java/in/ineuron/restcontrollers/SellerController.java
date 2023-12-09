package in.ineuron.restcontrollers;

import in.ineuron.dto.*;
import in.ineuron.models.User;
import in.ineuron.services.BookService;
import in.ineuron.services.OTPSenderService;
import in.ineuron.services.OTPStorageService;
import in.ineuron.services.SellerService;
import in.ineuron.utils.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import in.ineuron.models.BookSeller;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private OTPSenderService otpSender;

	@Autowired
	private OTPStorageService otpStorage;

	
	
	@PostMapping("/register") 
	public ResponseEntity<?> registerUser(@RequestBody SellerRegisterRequest requestData){
			
		
		 if (sellerService.isSellerAvailableByPhone(requestData.getEmail()))
			 return ResponseEntity.badRequest().body("Email No. already registerd with another account");
	
		 if (requestData.getPhone()!=null && sellerService.isSellerAvailableByEmail(requestData.getPhone()))
			 return ResponseEntity.badRequest().body("Phone already registerd with another account");
			 
		 if (sellerService.isSellerAvailableBySellerId(requestData.getSellerId())) {
			 return ResponseEntity.badRequest().body("UserId not available");
			 
		 }else {
			 
			 BookSeller seller = new BookSeller();
			 BeanUtils.copyProperties(requestData, seller);
			 
			 //encript password
			 String encodedPassword = passwordEncoder.encode(seller.getPassword());
			 seller.setPassword(encodedPassword);
			 sellerService.registerSeller(seller);
			 
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
		BookSeller seller = sellerService.fetchSellerBySellerId(loginData.getSellerId());
		
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

	@GetMapping("/send-otp")
	public ResponseEntity<String> sendOTPByPhone(@RequestParam("seller-name") String sellerName ) throws MessagingException {

		if (!sellerName.isBlank()) {
			if(sellerService.isSellerAvailableBySellerName(sellerName)){
				boolean isEmail = EmailValidator.isEmail(sellerName);
				Integer OTP=-1;
				if(isEmail){
					System.out.println("Email: "+sellerName);
					OTP = otpSender.sendOTPByEmail(sellerName);
				} else {
					System.out.println("phone: "+sellerName);
					OTP = otpSender.sendOTPByPhone(sellerName);
				}

				otpStorage.storeOTP("seller-"+sellerName, String.valueOf(OTP));

				return ResponseEntity.ok("Sent OTP: "+OTP);
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for "+sellerName);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input...");
	}

	@GetMapping("/verify-otp")
	public ResponseEntity<String> verifyOTPByPhone(
			@RequestParam("seller-name") String sellerName,
			@RequestParam String otp ) throws MessagingException {

		if (!sellerName.isBlank() && !otp.isBlank() ){
			if(sellerService.isSellerAvailableBySellerName(sellerName)){

				if(otpStorage.verifyOTP("seller-"+sellerName, otp)){
					otpStorage.removeOTP("seller-"+sellerName);
					return ResponseEntity.ok("verified successfully.. ");
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP verification failed.. ");
				}

			} else{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for "+sellerName);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input...");
	}

	@PostMapping("/otp-verified/update-password")
	public ResponseEntity<?> UpdateUserPasswordAfterOTPVerified(
			@RequestBody UpdateSellerPasswordDTO sellerCredential
	){
		System.out.println(sellerCredential);
		String userName=sellerCredential.getSellerName();
		String newPassword=sellerCredential.getNewPassword();

		if (!userName.isBlank() && !newPassword.isBlank() ) {

			BookSeller seller = sellerService.fetchSellerByUserName(userName);

			if(seller!=null){
				String encodedPass = passwordEncoder.encode(newPassword);
				sellerService.updateSellerPassword(seller.getId(), encodedPass);

				SellerLoginResponse response = new SellerLoginResponse();
				response.setToken((seller.getEmail()+seller.getLocation()).replace(" ",""));
				response.setSeller(seller);

				return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User name not found...");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input...");
	}



}
