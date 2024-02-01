package in.ineuron.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.*;

@Getter
@Setter
@ToString
public class LoginRequest {

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
             message = "Space is allowed")
	String password;

}
