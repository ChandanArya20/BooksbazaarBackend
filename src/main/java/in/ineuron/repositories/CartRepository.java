package in.ineuron.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ineuron.models.Cart;
import in.ineuron.models.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

	public List<Cart> findByUser(User user);
}
