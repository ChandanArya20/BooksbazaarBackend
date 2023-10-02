package in.ineuron.models;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private LocalDate deliveryDate;

//    @Enumerated(EnumType.STRING)
//	  {Pending, Confirmed, Shipped, Delivered, Cancelled, Returned} 
    @Column(nullable = false)
    private String status="Confirmed";
    
    @OneToOne
    private Book book;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @OneToOne
    private Address deliveryAddress;
    
    @ManyToOne
    private User user;
       
}

   
