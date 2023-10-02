package in.ineuron.dto;

import in.ineuron.models.BookSeller;
import lombok.Data;

@Data
public class SellerLoginResponse {

	String token;
	BookSeller seller;
}
