package in.ineuron.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.ineuron.models.Book;
import in.ineuron.models.BookOrder;
import in.ineuron.models.User;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long>{
	
	List<BookOrder> findByUser(User user);
	
	@Modifying
	@Query("UPDATE BookOrder b SET b.status = :status WHERE b.id = :orderId")
	Integer updateOrderStatus(Long orderId, String status);
	
	@Query("SELECT o FROM BookOrder o WHERE o.book IN :books")
    List<BookOrder> findByBook(@Param("books") List<Book> books);

}
