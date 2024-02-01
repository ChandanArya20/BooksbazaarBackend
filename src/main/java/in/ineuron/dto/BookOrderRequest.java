package in.ineuron.dto;

import in.ineuron.models.Address;
import in.ineuron.models.Book;
import in.ineuron.models.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BookOrderRequest {
    
    private Book book;
    
    private Integer quantity;
    
    private Address deliveryAddress;
    
    private User user;
    
}
