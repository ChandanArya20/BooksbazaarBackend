package in.ineuron.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserLoginResponse {

	Long id;

	String name;
	
	String phone;
	
	String email;
	
}
