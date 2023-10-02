package in.ineuron.restcontrollers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.ineuron.dto.BookAddRequest;
import in.ineuron.dto.BookResponse;
import in.ineuron.models.Book;
import in.ineuron.models.ImageFile;
import in.ineuron.services.BookstoreService;
import in.ineuron.utils.BookUtils;

@RestController
@RequestMapping("/api/book")
public class BookController {
	
	@Autowired
	private BookstoreService service;
	
	@Value("${baseURL}")
	private String baseURL;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	BookUtils bookUtils;

	
	@PostMapping("seller/addBook") 
	public ResponseEntity<?> updateBookData(@RequestParam MultipartFile coverImage, @RequestParam String bookInfo) throws IOException{
	
		//convert string into BookAddRequest object
		BookAddRequest bookRequest = mapper.readValue(bookInfo, BookAddRequest.class);
		
		Book book = new Book();
		BeanUtils.copyProperties(bookRequest, book);
		
		ImageFile imageFile = new ImageFile();
		imageFile.setName(coverImage.getOriginalFilename());
		imageFile.setType(coverImage.getContentType());
		imageFile.setImageData(coverImage.getBytes());
		
		book.setCoverImage(imageFile);
		
		service.insertBookInfo(book);	
		
		return ResponseEntity.ok("Book info inserted successfully...");					
	}
	
	@GetMapping("seller/{sellerId}/allBook")
	public ResponseEntity<List<BookResponse>> getBookBySeller(@PathVariable Long sellerId){
		 		
		List<Book> bookList = service.fetchBooksBySellerId(sellerId);	
		
		List<BookResponse> bookResponseList = bookUtils.getBookResponse(bookList);
		
		return ResponseEntity.ok(bookResponseList);
		
	}	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBookById(@PathVariable Long id) {
		
		Optional<Book> bookOptional = service.fetchBookById(id);	
		
		if(bookOptional.isPresent()) {
			
			Book book = bookOptional.get();
			
			BookResponse bookResponse = new BookResponse();
			BeanUtils.copyProperties(book, bookResponse);
			bookResponse.setImageURL(baseURL+"/api/image/"+book.getCoverImage().getId());
			return ResponseEntity.ok(bookResponse);
			
		}else {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("book not found for this book id");		
		}
	}
		
	@PutMapping("seller/updateBook")
	public ResponseEntity<String> updateBook(@RequestParam MultipartFile coverImage, @RequestParam String bookInfo) throws IOException{
		
		//convert string into BookAddRequest object
		BookAddRequest bookRequest = mapper.readValue(bookInfo, BookAddRequest.class);
		
		Book book = new Book();
		BeanUtils.copyProperties(bookRequest, book);
		
		ImageFile imageFile = new ImageFile();
		imageFile.setName(coverImage.getOriginalFilename());
		imageFile.setType(coverImage.getContentType());
		imageFile.setImageData(coverImage.getBytes());
		
		book.setCoverImage(imageFile);
		
		Book updatedBook = service.updateBook(book);
		
		String msg="";
		
		if(updatedBook!=null) 
			msg="Books Info updated successfully";		
		else 	
			msg="Books Info updation unsuccessfully";			
		
		return ResponseEntity.ok(msg);
	}
	
	@PatchMapping("/{id}/changeStatus")
	public ResponseEntity<String> changeBookStatus(@PathVariable Long id) {
		
		Boolean status=service.checkBookStatus(id);
		
		if(status)	
			service.deactivateBookStatus(id);
		else
			service.activateBookStatus(id);

		return ResponseEntity.ok("Status changed successfully");
		
	}
	
	@GetMapping("/search/title")
	public ResponseEntity<List<BookResponse>> getAllBooksByTitle( 
			@RequestParam  String query,
			@RequestParam  Integer page,
			@RequestParam  Integer size){
		
		List<BookResponse> searchedBooks=new ArrayList<>();
		
		if(!query.isBlank()) {
			
			searchedBooks = service.searchBooksByTitle(query.trim(), page, size);
		}
		
		return ResponseEntity.ok(searchedBooks);
	}
	
	@GetMapping("/search/category")
	public ResponseEntity<List<BookResponse>> getAllBooksByCategory( 
			@RequestParam  String query,
			@RequestParam  Integer page,
			@RequestParam  Integer size){
			
		List<BookResponse> searchedBooks=new ArrayList<>();
		
		if(!query.isBlank()) {
			System.out.println(query+" "+page+" "+size);
			searchedBooks = service.searchBooksByCategory(query.trim(), page, size);
		}
		return ResponseEntity.ok(searchedBooks);
	}
	
	@GetMapping("/search/description")
	public ResponseEntity<List<BookResponse>> getAllBooksByDescription( 
			@RequestParam  String query,
			@RequestParam  Integer page,
			@RequestParam  Integer size){
			
		List<BookResponse> searchedBooks=new ArrayList<>();
		if(!query.isBlank()) {
			
			searchedBooks = service.searchBooksByDescription(query.trim(), page, size);
		}
		return ResponseEntity.ok(searchedBooks);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<BookResponse>> getSearchedBooks( 
			@RequestParam  Integer page, 
			@RequestParam  Integer size, 
			@RequestParam  String query){

		List<BookResponse> searchedBooks=new ArrayList<>();
		if(!query.isBlank()) {
			
			searchedBooks = service.searchBooks(query,page,size);
		}
		return ResponseEntity.ok(searchedBooks);
	}
	
	
	@GetMapping("/suggest_book_names")
	public ResponseEntity<List<String>> getSuggestedBookNames(@RequestParam String query, @RequestParam Integer size){
		
		List<String> suggestedBooks=new ArrayList<>();
		
		if(!query.isBlank()) {
			
			suggestedBooks = service.getSuggestedBookNamesByTitle(query.trim(), size);
		}
		
		return ResponseEntity.ok(suggestedBooks);
	}
	
}











