package in.ineuron.repositories;

import in.ineuron.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

     public boolean existsByPhone(String phone);
	 public boolean existsByEmail(String email);
	 public User findByPhone(String phone);
	 public User findByEmail(String email);
	 
	
	 
}
