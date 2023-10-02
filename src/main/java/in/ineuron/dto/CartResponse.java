package in.ineuron.dto;

import in.ineuron.models.Book;
import lombok.Data;

@Data
public class CartResponse {

	Long id;
	
	BookResponse book;
	
	Integer quantity;
}
