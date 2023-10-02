package in.ineuron.models;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String description;
    
   
    @CreationTimestamp
    private LocalDateTime bookListingTime;
    
    
    private String isbn;

    @Lob
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private ImageFile coverImage;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String publishingYear;

    private int pages;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String format;

    @Column(nullable = false)
    private int stockAvailability;
    
    @Column(nullable = false)
    private int deliveryTime;
    
    private String edition;
    
    @ManyToOne
    private BookSeller bookSeller;
    
    @Column(nullable = false)
    private Boolean status=true;
       
}

