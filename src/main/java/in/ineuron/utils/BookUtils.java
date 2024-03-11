package in.ineuron.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import in.ineuron.dto.BookOrderResponse;
import in.ineuron.dto.BookResponse;
import in.ineuron.models.Book;
import in.ineuron.models.BookOrder;

@Component
public class BookUtils {

	@Autowired
	private AppUtils appUtils;

	public BookResponse getBookResponse(Book book){
		
		BookResponse bookResponse = new BookResponse();
		BeanUtils.copyProperties(book, bookResponse);
		bookResponse.setImageURL(appUtils.getBaseURL()+"/api/image/"+book.getCoverImage().getId());
				
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
			
			LocalDate orderDate= order.getOrderDateTime().toLocalDate();
			orderResponse.setOrderDate(orderDate);

			BookResponse bookResponse = new BookResponse();
			BeanUtils.copyProperties(order.getBook(), bookResponse);
			bookResponse.setImageURL(appUtils.getBaseURL()+"/api/image/"+order.getBook().getCoverImage().getId());
			
			orderResponse.setBook(bookResponse);
			orders.add(orderResponse);
		});	
		
		return orders;
	}
	
	public List<String> getExactMatchedContainingStrings(List<String> textList, String key){
		 
		 List<String> exactMatchTexts=new ArrayList<>();
		 
		 for (String text : textList) {
            // Replace all special characters with spaces
            String[] sanitizedTexts = text.replaceAll("[^a-zA-Z0-9\\s]", " ")
					.split("\\s+");

			// Check if any word in the book name is an exact match to the query
            for (String word : sanitizedTexts) {
                if (word.equalsIgnoreCase(key)) {
					 exactMatchTexts.add(text);
						break;
                }
            }
        }

		 return exactMatchTexts;
	}

	public List<String> filterStopWords(String[] list){

			Set<String> stopWords=Set.of("and","in","the","a","for");
			
			//Filters stopWords
			return Arrays.stream(list)
					.filter(token->!stopWords.contains(token))
					.toList();

	}
	

	
}
