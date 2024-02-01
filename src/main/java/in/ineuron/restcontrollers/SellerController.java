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
			 return ResponseEntity.badRequest().body("Email No. already registered with another account");
	
		 if (requestData.getPhone()!=null && sellerService.isSellerAvailableByEmail(requestData.getPhone()))
			 return ResponseEntity.badRequest().body("Phone already registered with another account");
			 
		 if (sellerService.isSellerAvailableBySellerId(requestData.getSellerId())) {
			 return ResponseEntity.badRequest().body("UserId not available");
			 
		 }else {
			 
			 BookSeller seller = new BookSeller();
			 BeanUtils.copyProperties(requestData, seller);
			 
			 //encrypt password
			 seller.setPassword(passwordEncoder.encode(seller.getPassword()));
			 seller = sellerService.registerSeller(seller);

			 return ResponseEntity.ok(seller);
		 }        
	}

	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody SellerLoginRequest loginData) {

		BookSeller seller = sellerService.fetchSellerBySellerId(loginData.getSellerId());
		
		if(seller==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for this user id");
		}
		else if (!passwordEncoder.matches(loginData.getPassword(), seller.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
			
		}else {
			
			return ResponseEntity.ok(seller);
		}
	}

	@GetMapping("/send-otp")
	public ResponseEntity<String> sendOTPByPhone(@RequestParam("seller-name") String sellerName ) throws MessagingException {

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

	@GetMapping("/verify-otp")
	public ResponseEntity<String> verifyOTPByPhone(
			@RequestParam("seller-name") String sellerName,
			@RequestParam String otp ) throws MessagingException {

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

	@PostMapping("/otp-verified/update-password")
	public ResponseEntity<?> UpdateUserPasswordAfterOTPVerified(
			@RequestBody UpdateSellerPasswordDTO sellerCredential
	){

		String userName=sellerCredential.getSellerName();
		String newPassword=sellerCredential.getNewPassword();

		BookSeller seller = sellerService.fetchSellerByUserName(userName);

		if(seller!=null){
			String encodedPass = passwordEncoder.encode(newPassword);
			sellerService.updateSellerPassword(seller.getId(), encodedPass);

			return ResponseEntity.ok(seller);

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User name not found...");
		}
	}

}
