package in.ineuron.dto;

import java.time.LocalDate;

import in.ineuron.models.BookSeller;
import lombok.Data;

@Data
public class BookAddRequest {


	private Long id;
	
    private String title;

    
    private String author;

   
    private String description;
    
   
    private String isbn;

   
    private byte[] coverImage;

    
    private double price;

   
    private String language;

    
    private String category;

   
    private String publishingYear;

    private int pages;

   
    private String publisher;

   
    private String format;

  
    private int stockAvailability;
    
   
    private int deliveryTime;
    
    
    private String edition;
    
    private BookSeller bookSeller;
    
        
}

