package in.ineuron.dto;

import in.ineuron.models.User;
import lombok.Data;

@Data
public class LoginResponse {
	
	String token;
	UserResponse user;

}
