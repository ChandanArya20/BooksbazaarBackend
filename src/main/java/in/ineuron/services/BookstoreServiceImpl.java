package in.ineuron.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
import in.ineuron.repositories.BookOrderRepository;
import in.ineuron.repositories.BookRepositery;
import in.ineuron.repositories.CartRepository;
import in.ineuron.repositories.ImageFileRepository;
import in.ineuron.repositories.SellerRepository;
import in.ineuron.repositories.UserRepository;

import in.ineuron.utils.*;

@Service
@Transactional
public class BookstoreServiceImpl implements BookstoreService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SellerRepository sellerRepo;
	
	@Autowired
	private BookRepositery bookRepo;
	
	@Autowired
	private ImageFileRepository imageFileRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private BookOrderRepository orderRepo;
	
	@Autowired
	private EntityManager entityManager;
	
	@Value("${baseURL}")
	private String baseURL;
	
	@Autowired
	BookUtils bookUtils;
	
	
	
	
	// User Repository Operations---------------------------------------------------->
	
	@Override
	public Boolean isUserAvailableByPhone(String phone) {
		
		return userRepo.existsByPhone(phone);
	}
	
	@Override
	public Boolean isUserAvailableByEmail(String email) {
		
		return userRepo.existsByEmail(email);
	}

	@Override
	public void registerUser(User user) {
		
		userRepo.save(user);
	}
	
	@Override
	public User fetchUserByPhone(String phone) {
		
		return userRepo.findByPhone(phone);
	}
	
	@Override
	public User fetchUserByEmail(String email) {
		
		return userRepo.findByEmail(email);
	}
	
	@Override
	public UserResponse fetchUserDetails(Long userId) {

		Optional<User> userOptional = userRepo.findById(userId);
		if(userOptional.isPresent()) {
			
			User user = userOptional.get();
			UserResponse userResponse = new UserResponse();
			BeanUtils.copyProperties(user, userResponse);
			
			return userResponse;
		}
		
		return null;		
	}
	
	@Override
	public void insertUserAddress(AddressRequest address, Long userId) {
		
		System.out.println(address);
		Optional<User> userOptional = userRepo.findById(userId);
		
		if(userOptional.isPresent()) {
			
			User user = userOptional.get();
			List<Address> userAddress = user.getAddress();
			
			Address addressObj = new Address();
			BeanUtils.copyProperties(address, addressObj);
			userAddress.add(addressObj);
			
			userRepo.save(user);		
		}
	}
	
	@Override
	public List<Address> fetchAddressByUserId(Long userId) {
		
		User user = userRepo.findById(userId).orElse(null);
		List<Address> address=new ArrayList<>();
		
		if(user!=null)	{
			
			address=user.getAddress();			
		} 
		
		return address;
	}
	
	
	
	
	// Seller Repository Operations---------------------------------------------------->
	
	@Override
	public Boolean isSellerAvailableByPhone(String phone) {
		
		return sellerRepo.existsByPhone(phone);
	}
	
	@Override
	public Boolean isSellerAvailableByEmail(String email) {
		
		return sellerRepo.existsByEmail(email);
	}
	
	@Override
	public Boolean isSellerAvailableBySellerId(String sellerId) {
		
		return sellerRepo.existsBySellerId(sellerId);
	}
	
	@Override
	public void registerSeller(BookSeller seller) {
		
		sellerRepo.save(seller);	
	}
	
	@Override
	public BookSeller fetchSellerByPhone(String phone) {
		
		return sellerRepo.findByPhone(phone);
	}
	
	@Override
	public BookSeller fetchSellerByEmail(String email) {
		
		return sellerRepo.findByEmail(email);
	}
	
	@Override
	public BookSeller fetchSellerBySellerId(String sellerId) {
		
		return sellerRepo.findBySellerId(sellerId);
	}
	
	
	
	// Book Repository Operations---------------------------------------------------->
	
	@Override
	public void insertBookInfo(Book book) {	
		
		bookRepo.save(book);
	}
	
	@Override
	public List<Book> fetchBooksBySellerId(Long sellerId) {
		
		BookSeller seller = new BookSeller();
		seller.setId(sellerId);
		return bookRepo.findByBookSeller(seller);
	}
	
	@Override
	public Optional<ImageFile> fetchBookImageById(Long id) {
		
		return imageFileRepo.findById(id);
	}
	
	@Override
	public Optional<Book> fetchBookById(Long id) {
		
		return bookRepo.findById(id);
	}
	
	@Override
	public Book updateBook(Book book) {	
		
		return bookRepo.save(book);
	}
	
	@Override
	public Boolean checkBookStatus(Long id) {
		
		return bookRepo.findBookStatusById(id);
	}

	@Override
	public Integer activateBookStatus(Long id) {
		
		return bookRepo.activateBookStatusById(id);
	}
	
	@Override
	public Integer deactivateBookStatus(Long id) {
		
		return bookRepo.deactivateBookStatusById(id);
	}
	
	@Override
	public List<BookResponse> searchBooksByTitle(String query, Integer page, Integer size) {
		
	    // To hold unique books without duplication, maintaining insertion order.
	    Set<Book> uniqueBooks = new LinkedHashSet<>();

	    // Try to fetch books with titles that start with the query.
	    List<Book> books = bookRepo.findBooksStartWith(query, PageRequest.of(0, size));
	    uniqueBooks.addAll(books);
	    
	    // Tokenize the query to handle multi-word searches.
	    String[] tokens = query.toLowerCase().split("\\s+");
	               
        List<String> searchQueryList = bookUtils.filterStopWords(tokens);
        
        
        // Search for each individual query term.
        for (String singleQuery : searchQueryList) {
            
            List<Book> allBooks = bookRepo.
            		findByTitleContainingIgnoreCaseAndStatus(singleQuery, PageRequest.of(page-1,size*5), true); 

            List<Book> exactMatchedBooks = bookUtils.getExactTitleMatchedContainingBooks(allBooks, singleQuery);
            
            uniqueBooks.addAll(exactMatchedBooks); 
            
            if(uniqueBooks.size()>size) {
            	break;
            }
        }
	    
        if(uniqueBooks.size()>size) {
        	return bookUtils.getBookResponse(uniqueBooks).subList(0, size-1);
        }
	    return bookUtils.getBookResponse(uniqueBooks);
	}

	@Override
	public List<BookResponse> searchBooksByCategory(String query, Integer page, Integer size) {
		
		// To hold unique books without duplication, maintaining insertion order.
	    Set<Book> uniqueBooks = new LinkedHashSet<>();
	 	    
	    // Tokenize the query to handle multi-word searches.
	    String[] tokens = query.toLowerCase().split("\\s+");
	               
        List<String> searchQueryList = bookUtils.filterStopWords(tokens);
        
        
        // Search for each individual query term.
        for (String singleQuery : searchQueryList) {
            
            List<Book> allBooks = bookRepo.
            		findByCategoryContainingIgnoreCaseAndStatus(singleQuery, PageRequest.of(page-1,size*5), true); 

            List<Book> exactMatchedBooks = bookUtils.getExactCategoryMatchedContainingBooks(allBooks, singleQuery);
            
            uniqueBooks.addAll(exactMatchedBooks); 
            
            if(uniqueBooks.size()>size) {
            	break;
            }
        }
	    
        if(uniqueBooks.size()>size) {
        	return bookUtils.getBookResponse(uniqueBooks).subList(0, size-1);
        }
	    return bookUtils.getBookResponse(uniqueBooks);
	}

	@Override
	public List<BookResponse> searchBooksByDescription(String query, Integer page, Integer size) {
		
		// To hold unique books without duplication, maintaining insertion order.
	    Set<Book> uniqueBooks = new LinkedHashSet<>();
	    
	    // Tokenize the query to handle multi-word searches.
	    String[] tokens = query.toLowerCase().split("\\s+");
	               
        List<String> searchQueryList = bookUtils.filterStopWords(tokens);
        
        
        // Search for each individual query term.
        for (String singleQuery : searchQueryList) {
            
            List<Book> allBooks = bookRepo.
            		findByDescriptionContainingIgnoreCaseAndStatus(singleQuery, PageRequest.of(page-1,size*5), true); 

            List<Book> exactMatchedBooks = bookUtils.getExactDescriptionMatchedContainingBooks(allBooks, singleQuery);
            
            uniqueBooks.addAll(exactMatchedBooks); 
            
            if(uniqueBooks.size()>size) {
            	break;
            }
        }
	    
        if(uniqueBooks.size()>size) {
        	return bookUtils.getBookResponse(uniqueBooks).subList(0, size-1);
        }
	    return bookUtils.getBookResponse(uniqueBooks);
	}

	@Override
	public List<BookResponse> searchBooks(String query, Integer page, Integer size) {
		
		System.out.println(page+" "+size);
		
		Set<BookResponse> bookList = new LinkedHashSet<>();	
		List<BookResponse> searchedBooks;
		
		searchedBooks = searchBooksByTitle(query, page,size);
		bookList.addAll(searchedBooks);
		
		
		searchedBooks = searchBooksByCategory(query, page,size);
		bookList.addAll(searchedBooks);
		
		searchedBooks = searchBooksByDescription(query, page,size);
		bookList.addAll(searchedBooks);
		
	    
		List<BookResponse> pagedResults=new ArrayList<>();
		
		System.out.println("First time  :"+bookList.size());
		for(BookResponse b:bookList) {
			System.out.println(b.getId()+" ");
		}
				
			
		if(bookList.size()<size) {
			List<Book> books;

			books=bookRepo.findByTitleContainingIgnoreCaseAndStatus(query, PageRequest.of(page-1, size),true);
			searchedBooks = bookUtils.getBookResponse(books);
			bookList.addAll(searchedBooks);
			System.out.println("Book length: "+books.size());
			for(Book b:books) {
				System.out.println(b.getId()+" ");
			}
			
			
			books=bookRepo.findByCategoryContainingIgnoreCaseAndStatus(query, PageRequest.of(page-1, size),true);
			searchedBooks = bookUtils.getBookResponse(books);
			bookList.addAll(searchedBooks);
			System.out.println("Book length: "+books.size());
			for(Book b:books) {
				System.out.println(b.getId()+" ");
			}
			
			books=bookRepo.findByDescriptionContainingIgnoreCaseAndStatus(query, PageRequest.of(page-1, size),true);
			searchedBooks = bookUtils.getBookResponse(books);
			bookList.addAll(searchedBooks);
			System.out.println("Book length: "+books.size());
			for(Book b:books) {
				System.out.println(b.getId()+" ");
			}
			
		}
		
		int startIndex = (page - 1) * size; 
		int endIndex = Math.min(startIndex + size, bookList.size()); // Ensure endIndex doesn't exceed the list size
		
		if(startIndex>endIndex) {
			return pagedResults;
		}else {		
			pagedResults.addAll(bookList);
			System.out.println("Page :"+pagedResults.size());
			System.out.println(startIndex+" "+endIndex);
			return pagedResults.subList(startIndex, endIndex);
		}
					
	}
	
	@Override
	public Boolean increaseBookStock(Long bookId, Integer stockValue) {
		 
		Optional<Book> bookOptional = bookRepo.findById(bookId);
		if(bookOptional.isPresent()) {
			
			Book book = bookOptional.get();
			book.setStockAvailability(book.getStockAvailability()+stockValue);
			bookRepo.save(book);
		}
		return null;
	}
	
	@Override
	public Boolean decreaseBookStock(Long bookId, Integer stockValue) {
		Optional<Book> bookOptional = bookRepo.findById(bookId);
		if(bookOptional.isPresent()) {
			
			Book book = bookOptional.get();
			book.setStockAvailability(book.getStockAvailability()-stockValue);
			bookRepo.save(book);
		}
		return null;
	}
	
	 
	 @Override
	 public List<String> getSuggestedBookNamesByTitle(String query, Integer size) {
		 
		 PageRequest pageRequest = PageRequest.of(0, size); // Limit to the first 10 results
		 
		 List<String> bookList = bookRepo.findBookNamesStartWith(query, pageRequest);
		 
		 return bookList;
	 }

	 @Override
	 public List<String> getSuggestedBookNamesByExactMatch(String query, Integer size) {
		 
	     List<String> exactMatchBookNames = new ArrayList<>();
	     PageRequest pageRequest = PageRequest.of(0, 5*size);  //Fetchs large amount of data at a time
	     
	     while (exactMatchBookNames.size() < size) {
	         
	         List<String> bookNames = bookRepo.findBookNamesContains(query, pageRequest);
	         
	         // If database doesn't have more data
             if (bookNames.isEmpty()) {
                 break;
             }
	        
	         //Filter the exact matched book names
	         List<String> exactMatchedStrings = bookUtils.getExactMatchedContainingStrings(bookNames, query);
	         exactMatchBookNames.addAll(exactMatchedStrings);
	         	         	        
	         // Increment the page number to fetch the next set of data
	         pageRequest = PageRequest.of(pageRequest.getPageNumber() + 1, 5*size);
	     }
	     
	     // Slice the list to contain only the first 'size' elements before returning
	     if (exactMatchBookNames.size() > size) {
	         exactMatchBookNames = exactMatchBookNames.subList(0, size);
	     }
	     
	     return exactMatchBookNames;
	 }
	 
	 

	
	 
	 
	 
	// Cart Repository Operations---------------------------------------------------->
	 
	@Override
	public void insertCartData(Cart cart) {
		
		cartRepo.save(cart);		
	}
	
	@Override
	public List<Cart> getAllCartDataByUser(User user) {
		
		return cartRepo.findByUser(user);
	}
	
	@Override
	public void updateCartItemQuantity(Cart cart) {
		
	    Optional<Cart> existingCartItem = cartRepo.findById(cart.getId());

	    if (existingCartItem.isPresent()) {
	    	
	        Cart dbCartItem = existingCartItem.get();
	        
	        dbCartItem.setQuantity(cart.getQuantity());

	        // Save the updated cart item
	        cartRepo.save(dbCartItem);	      
	    } 	    
	}
	
	@Override
	public void deleteCartItems(Cart[] carts) {
		
		cartRepo.deleteAllInBatch(Arrays.asList(carts));	
	}
	
	 
	 
	
	// BookOrder Repository Operations---------------------------------------------------->
	
	@Override
	public Boolean insertOrder(List<BookOrder> orders) {
		
		List<BookOrder> savedOrders = orderRepo.saveAll(orders);
		
		return !savedOrders.isEmpty();
	}
	
	@Override
	public List<BookOrderResponse> fetchOrdersByUser(User user) {
		
		List<BookOrder> orderList = orderRepo.findByUser(user);
		
		List<BookOrderResponse> orders = bookUtils.getBookOrderResponse(orderList);
	
		return orders;
	}

	@Override
	public Boolean changeOrderStatus(Long orderId, String status) {
		
		Integer updateOrderStatus = orderRepo.updateOrderStatus(orderId, status);
		
		return updateOrderStatus==1;
	}

	@Override
	public List<BookOrderResponse> fetchOrdersBySellerId(Long id) {
		
		List<Book> bookList = fetchBooksBySellerId(id);
		
		List<BookOrder> orderList = orderRepo.findByBook(bookList);
		
		List<BookOrderResponse> orders = bookUtils.getBookOrderResponse(orderList);
		
		return orders;
	}

	@Override
	public BookOrder getOrderById(Long orderId) {
		
		return orderRepo.findById(orderId).orElse(null);
	}
	
	

	

}





