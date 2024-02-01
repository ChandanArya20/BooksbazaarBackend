package in.ineuron.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import in.ineuron.models.Address;
import lombok.Data;
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
