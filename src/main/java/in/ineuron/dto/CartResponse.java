package in.ineuron.dto;

import in.ineuron.models.Book;
import lombok.Data;
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
