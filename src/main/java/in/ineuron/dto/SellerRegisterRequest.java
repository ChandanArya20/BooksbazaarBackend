package in.ineuron.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SellerRegisterRequest {

	@NotBlank(message = "Name should not be empty")
	@Size(min=3, message = "Name should be greater than 2")
	String name;
	
	@NotBlank(message = "Location should not be empty")
	@Size(min=3, message = "Location should be greater than 2")
	String location;
	
	@NotBlank(message = "Phone No. should not be empty")
	@Pattern(regexp = "^[6-9][0-9]*$", 
						message="invalid phone!")
	String phone;
	
	@NotBlank(message = "Email should not be empty")
	@Email(regexp = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", 
	message="invalid email!")
	String email;
	
	@NotBlank(message = "SellerId should not be empty")
    @Pattern(regexp = "^(?!.*\\s).*$",
             message = "Space is not allowed between")
	String sellerId;
	
	@NotBlank(message = "Password should not be empty")
    @Pattern(regexp = "^(?!.*\\s).*$",
             message = "Space is not allowed between")
	String password;


	public void setName(String name) {
		this.name = name.trim();
	}

	
	public void setPhone(String phone) {
		this.phone = phone.trim();
	}
	
	public void setEmail(String email) {
		this.email = email.trim();
	}


	public void setPassword(String password) {
		this.password = password.trim();
	}


	public void setLocation(String location) {
		this.location = location.trim();
	}


	public void setSellerId(String sellerId) {
		this.sellerId = sellerId.trim();
	}
	
	
}
