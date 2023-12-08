package in.ineuron.dto;

import lombok.Data;

@Data
public class LoginResponse {
	
	String token;
	UserResponse user;

}
