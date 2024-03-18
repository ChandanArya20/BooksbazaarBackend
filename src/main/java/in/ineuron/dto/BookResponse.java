package in.ineuron.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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

}
