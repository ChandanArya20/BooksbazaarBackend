package in.ineuron.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.*;

@Getter
@Setter
@ToString
public class SellerLoginRequest {

	@NotBlank(message = "SellerId should not be empty")
    @Pattern(regexp = "^(?!.*\\s).*$",
             message = "Space is not allowed between")
	String sellerId;
	
	@NotBlank(message = "Password should not be empty")
	@Pattern(regexp = "^(?!.*\\s).*$",
	message = "Space is not allowed between")
	String password;
	
}
