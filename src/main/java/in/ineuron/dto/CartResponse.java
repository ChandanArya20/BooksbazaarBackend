package in.ineuron.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartResponse {

	Long id;
	
	BookResponse book;
	
	Integer quantity;
}
