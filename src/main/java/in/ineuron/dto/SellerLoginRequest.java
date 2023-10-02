package in.ineuron.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SellerLoginRequest {

	@NotBlank(message = "SellerId should not be empty")
    @Pattern(regexp = "^(?!.*\\s).*$",
             message = "Space is not allowed between")
	String sellerId;
	
	@NotBlank(message = "Password should not be empty")
	@Pattern(regexp = "^(?!.*\\s).*$",
	message = "Space is not allowed between")
	String password;

	
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId.trim();
	}
		
	public void setPassword(String password) {
		this.password = password.trim();
	}
	
}
