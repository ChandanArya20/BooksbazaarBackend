package in.ineuron.dto;

import in.ineuron.models.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {
	
	String token;
	UserResponse user;

}
