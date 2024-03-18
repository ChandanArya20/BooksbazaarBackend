package in.ineuron.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@ToString
public class RegisterRequest {

	@NotBlank(message = "Name should not be empty")
	@Size(min=3, message = "Name should be greater than 2")
	String name;
	
	@NotBlank(message = "Phone No. should not be empty")
	@Pattern(regexp = "^[6-9][0-9]*$", 
						message="invalid phone!")
	String phone;
	
	@NotBlank(message = "Email should not be empty")
	@Email(regexp = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", 
	message="invalid email!")
	String email;
	
	@NotBlank(message = "Password should not be empty")
    @Pattern(regexp = "^(?!.*\\s).*$",
             message = "Space is not allowed between")
	String password;


}
