package in.ineuron.dto;

import in.ineuron.models.Address;
import in.ineuron.models.Book;
import in.ineuron.models.User;
import lombok.Data;

@Data
public class BookOrderRequest {
    
    private Book book;
    
    private Integer quantity;
    
    private Address deliveryAddress;
    
    private User user;
    
}
