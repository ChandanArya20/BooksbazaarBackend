package in.ineuron.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.ineuron.models.BookSeller;
import lombok.Data;

@Data
public class BookResponse {
	
	    private Long id;

	    private String title;
	    
	    private String author;

	    private String description;
	    
	    private LocalDateTime bookListingTime;
	      
	    private String isbn;

	    private String imageURL;

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
	    
	    private Boolean status;
	    
//	    @JsonIgnore
//	    private BookSeller bookSeller;
	     

//	    @Column(nullable = false)
//	    private double rating;
	    
//	    private String comments;
	    

	
}
