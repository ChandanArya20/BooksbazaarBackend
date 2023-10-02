package in.ineuron.dto;

import javax.validation.constraints.Email;
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


	public void setPhone(String phone) {
		this.phone = phone.trim();
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}
	
	public void setPassword(String password) {
		this.password = password.trim();
	}
	
	
}
