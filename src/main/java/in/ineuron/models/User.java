package in.ineuron.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false)
	String name;

	@Column(unique = true, nullable = false)
	String phone;

	@Column(unique = true)
	String email;
	
	@Column(nullable = false)
	String password;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<Address> address;
	
	@OneToMany( mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<BookOrder> orders;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<Cart> cart;

	@JsonIgnore
	public List<BookOrder> getOrders() {
		return orders;
	}
		
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	public List<Cart> getCart() {
		return cart;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", password=" + password
				+ ", address=" + address + "]";
	}
}





