package in.ineuron.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ineuron.models.Book;
import in.ineuron.models.BookSeller;
import in.ineuron.models.User;

public interface SellerRepository extends JpaRepository<BookSeller, Long> {
	
	 public boolean existsByPhone(String phone);
	 public boolean existsByEmail(String email);
	 public boolean existsBySellerId(String sellerId);
	 public BookSeller findByPhone(String phone);
	 public BookSeller findByEmail(String email);
	 public BookSeller findBySellerId(String sellerId);
	

}
