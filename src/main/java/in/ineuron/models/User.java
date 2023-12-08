package in.ineuron.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class User {

	@Id
	@GenericGenerator(name = "gen",strategy = "in.ineuron.idgenerator.IdGenerator")
	@GeneratedValue(generator = "gen")
	String id;
	
	@Column(nullable = false)
	String name;
	
	@Column(unique = true, nullable = false)
	String phone;
	
	@Column(unique = true)
	String email;
	
	@Column(nullable = false)
	String password;
	


}





