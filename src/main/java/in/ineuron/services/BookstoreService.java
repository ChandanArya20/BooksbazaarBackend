package in.ineuron.services;

import java.util.List;
import java.util.Optional;

import in.ineuron.dto.AddressRequest;
import in.ineuron.dto.BookOrderResponse;
import in.ineuron.dto.BookResponse;
import in.ineuron.dto.UserResponse;
import in.ineuron.models.Address;
import in.ineuron.models.Book;
import in.ineuron.models.BookOrder;
import in.ineuron.models.BookSeller;
import in.ineuron.models.Cart;
import in.ineuron.models.ImageFile;
import in.ineuron.models.User;

public interface BookstoreService {

	// User Repository methods
	public Boolean isUserAvailableByPhone(String phone);
	public Boolean isUserAvailableByEmail(String email);
	public void registerUser(User user);
	public User fetchUserByPhone(String phone);
	public User fetchUserByEmail(String email);
	public UserResponse fetchUserDetails(Long userId);
	public List<Address>  fetchAddressByUserId(Long userId);
	public void insertUserAddress(AddressRequest address,  Long userId);
	
	
	// Seller Repository methods
	public Boolean isSellerAvailableByPhone(String phone);
	public Boolean isSellerAvailableByEmail(String email);
	public Boolean isSellerAvailableBySellerId(String sellerId);
	public void registerSeller(BookSeller seller);
	public BookSeller fetchSellerByPhone(String phone);
	public BookSeller fetchSellerByEmail(String email);
	public BookSeller fetchSellerBySellerId(String sellerId);
	
	
	// Book Repository methods
	public void insertBookInfo(Book book);
	public List<Book> fetchBooksBySellerId(Long sellerId);
	public Optional<ImageFile> fetchBookImageById(Long id);
	public Optional<Book> fetchBookById(Long id);
	public Book updateBook(Book book);
	public Boolean checkBookStatus(Long id);
	public Integer activateBookStatus(Long id);
	public Integer deactivateBookStatus(Long id);
	public List<BookResponse> searchBooksByTitle(String query, Integer page, Integer size);
	public List<BookResponse> searchBooksByCategory(String query, Integer page, Integer size);
	public List<BookResponse> searchBooksByDescription(String query, Integer page, Integer size);
	public List<BookResponse> searchBooks(String query, Integer page, Integer size);
	public Boolean increaseBookStock(Long bookId, Integer stockValue);
	public Boolean decreaseBookStock(Long bookId, Integer stockValue);
	public List<String> getSuggestedBookNamesByTitle(String query, Integer size);
	public List<String> getSuggestedBookNamesByExactMatch(String query, Integer size);
	
	
	// Cart Repository methods
	public void insertCartData(Cart cart);
	public List<Cart> getAllCartDataByUser(User user);
	public void updateCartItemQuantity(Cart cart);
	public void deleteCartItems(Cart[] carts);
	
	
	// BookOrder Repository methods
	public Boolean insertOrder(List<BookOrder> orders);
	public List<BookOrderResponse> fetchOrdersByUser(User user);
	public List<BookOrderResponse> fetchOrdersBySellerId(Long id);
	public Boolean changeOrderStatus(Long orderId, String status);
	public BookOrder getOrderById(Long OrderId);
	
	
	

}
















