package in.ineuron.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	String phone;

	@Column(nullable = false)
	String pincode;

	@Column(nullable = false)
	String streetName;

	@Column(nullable = false)
	String city;

	@Column(nullable = false)
	String state;

	String addressType;
	
}








