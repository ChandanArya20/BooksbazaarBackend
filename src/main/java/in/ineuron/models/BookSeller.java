package in.ineuron.models;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book_sellers")
@Setter
@ToString
@NoArgsConstructor
public class BookSeller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(unique =  true, nullable = false)
    private String email;
    
    @Column(unique =  true)
    private String phone;
  
    @Column(unique = true, nullable = false)
    private String sellerId;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "bookSeller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> books;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<Address> address;

    
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getSellerId() {
		return sellerId;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public List<Book> getBooks() {
		return books;
	}

	public List<Address> getAddress() {
		return address;
	}
       
    
    

}

