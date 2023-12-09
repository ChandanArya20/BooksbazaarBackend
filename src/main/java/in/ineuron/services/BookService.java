package in.ineuron.services;

import in.ineuron.dto.BookResponse;
import in.ineuron.models.Book;
import in.ineuron.models.ImageFile;

import java.util.List;
import java.util.Optional;

public interface BookService {

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
}
