package in.ineuron.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@ToString
public class AddressRequest {

	@NotBlank(message = "Name should not be empty")
	@Size(min=3, message = "Name should be greater than 2")
	String name;
	
	@NotBlank(message = "Phone No. should not be empty")
	@Size(min=10,max=10, message = "Invalid phone number")
	String phone;
	
	@NotBlank(message = "Pincode should not be empty")
	@Size(min=6,max=6, message = "Invalid pincode" )
	String pincode;
	
	@NotBlank(message = "StreetName should not be empty")
	@Size(min=3, message = "StreetName should be greater than 2")
	String streetName;
	
	@NotBlank(message = "City should not be empty")
	@Size(min=3, message = "City should be greater than 2")
	String city;
	
	@NotBlank(message = "State should not be empty")
	@Size(min=3, message = "State should be greater than 2")
	String state;
	
	@NotBlank
	String addressType;
	
}
