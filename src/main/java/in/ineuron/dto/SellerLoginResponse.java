package in.ineuron.dto;

import in.ineuron.models.BookSeller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SellerLoginResponse {

	String token;
	BookSeller seller;
}
