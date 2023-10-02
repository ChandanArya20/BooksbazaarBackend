package in.ineuron.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import in.ineuron.dto.BookOrderResponse;
import in.ineuron.dto.BookResponse;
import in.ineuron.models.Book;
import in.ineuron.models.BookOrder;

@Component
public class BookUtils {
	
	@Value("${baseURL}")
	private  String baseURL;
	
	
	public BookResponse getBookResponse(Book book){
		
		BookResponse bookResponse = new BookResponse();
		BeanUtils.copyProperties(book, bookResponse);
		bookResponse.setImageURL(baseURL+"/api/image/"+book.getCoverImage().getId());
				
		return bookResponse;
		
	}
	
	public List<BookResponse> getBookResponse(Collection<Book> books){
		
		List<BookResponse> bookResponses = new ArrayList<>(); 
		
		books.forEach(book->{
			
			BookResponse bookResponse = getBookResponse(book);
			
			bookResponses.add(bookResponse);
		});
		
		return bookResponses;
		
	}
	
	public List<BookOrderResponse> getBookOrderResponse(List<BookOrder> orderList){
		
		List<BookOrderResponse> orders = new ArrayList<>(); 
		
			orderList.forEach(order->{
			
			BookOrderResponse orderResponse = new BookOrderResponse();
			BeanUtils.copyProperties(order, orderResponse);
			
			int year = order.getOrderDateTime().getYear();
			int month = order.getOrderDateTime().getMonthValue();
			int day = order.getOrderDateTime().getDayOfMonth();
			
			LocalDate orderDate= LocalDate.of(year, month, day);
			orderResponse.setOrderDate(orderDate);
			
			
			BookResponse bookResponse = new BookResponse();
			BeanUtils.copyProperties(order.getBook(), bookResponse);
			bookResponse.setImageURL(baseURL+"/api/image/"+order.getBook().getCoverImage().getId());
			
			orderResponse.setBook(bookResponse);
			orders.add(orderResponse);
		});	
		
		return orders;
		
	}
	
	public List<String> getExactMatchedContainingStrings(List<String> textList, String key){
		 
		 List<String> exactMatchTextNames=new ArrayList<>();
		 
		 for (String text : textList) {
       	 
            // Replace all special characters with spaces
            String sanitizedTextName = text.replaceAll("[^a-zA-Z0-9\\s]", " ");
            
            String[] words = sanitizedTextName.split("\\s+");
            
			// Check if any word in the book name is an exact match to the query
            for (String word : words) {
           	 
                if (word.equalsIgnoreCase(key)) {
               	 
               	 exactMatchTextNames.add(text);
                    break;
                }
            }
        }
		 
		 return exactMatchTextNames;
	}
	
	public List<Book> getExactTitleMatchedContainingBooks(List<Book> bookList, String key){
		
		List<Book> exactMatchBooks=new ArrayList<>();
		
		for (Book book : bookList) {
			
			// Replace all special characters with spaces
			String sanitizedTextName = book.getTitle().replaceAll("[^a-zA-Z0-9\\s]", " ");
			
			String[] words = sanitizedTextName.split("\\s+");
			
			// Check if any word in the book name is an exact match to the query
			for (String word : words) {
				
				if (word.equalsIgnoreCase(key)) {
					
					exactMatchBooks.add(book);
					break;
				}
			}
		}
		
		return exactMatchBooks;
	}
	public List<Book> getExactCategoryMatchedContainingBooks(List<Book> bookList, String key){
		
		List<Book> exactMatchBooks=new ArrayList<>();
		
		for (Book book : bookList) {
			
			// Replace all special characters with spaces
			String sanitizedTextName = book.getCategory().replaceAll("[^a-zA-Z0-9\\s]", " ");
			
			String[] words = sanitizedTextName.split("\\s+");
			
			// Check if any word in the book name is an exact match to the query
			for (String word : words) {
				
				if (word.equalsIgnoreCase(key)) {
					
					exactMatchBooks.add(book);
					break;
				}
			}
		}
		
		return exactMatchBooks;
	}
	public List<Book> getExactDescriptionMatchedContainingBooks(List<Book> bookList, String key){
		
		List<Book> exactMatchBooks=new ArrayList<>();
		
		for (Book book : bookList) {
			
			// Replace all special characters with spaces
			String sanitizedTextName = book.getDescription().replaceAll("[^a-zA-Z0-9\\s]", " ");
			
			String[] words = sanitizedTextName.split("\\s+");
			
			// Check if any word in the book name is an exact match to the query
			for (String word : words) {
				
				if (word.equalsIgnoreCase(key)) {
					
					exactMatchBooks.add(book);
					break;
				}
			}
		}
		
		return exactMatchBooks;
	}
	
	public List<String> filterStopWords(String[] list){

			Set<String> stopWords=new HashSet<>(Arrays.asList("and","in","the","a","for"));
			
			//Filters stopWords
			List<String> filteredList = Arrays
										   .stream(list)
										   .filter(token->!stopWords.contains(token))
										   .toList();
			return filteredList;
	}
	

	
}
