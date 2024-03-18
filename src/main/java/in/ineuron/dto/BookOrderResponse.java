package in.ineuron.dto;

import java.time.LocalDate;

import in.ineuron.models.Address;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookOrderResponse {
	
    private Long id;

    private LocalDate deliveryDate;

    private LocalDate orderDate;
    
    private String status;
    
    private BookResponse book;
    
    private Integer quantity;
    
    private Address deliveryAddress;

}
