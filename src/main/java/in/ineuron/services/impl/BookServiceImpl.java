package in.ineuron.services.impl;

import in.ineuron.dto.BookResponse;
import in.ineuron.exception.StockNotAvailableException;
import in.ineuron.models.Book;
import in.ineuron.models.BookSeller;
import in.ineuron.models.ImageFile;
import in.ineuron.repositories.BookRepositery;
import in.ineuron.repositories.ImageFileRepository;
import in.ineuron.services.BookService;
import in.ineuron.utils.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepositery bookRepo;

    @Autowired
    private ImageFileRepository imageFileRepo;

    @Autowired
    BookUtils bookUtils;

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
    public List<BookResponse> searchBooks(String query, Integer page, Integer size) {

            List<Book> allBooks = bookRepo.
                   searchInTitleCategoryDescription(query, PageRequest.of(page-1,size));

        return bookUtils.getBookResponse(allBooks);
    }

    @Override
    public List<BookResponse> enhancedSearchBooks(String query, Integer page, Integer size) {

        // To hold unique books without duplication, maintaining insertion order.
        Set<Book> uniqueBooks = new LinkedHashSet<>();

        // Tokenize the query to handle multi-word searches.
        String[] tokens = query.toLowerCase().split("\\s+");

        List<String> searchQueryList = bookUtils.filterStopWords(tokens);

        int currentPage = 0;
        int finalSize = page * size;

        while ( uniqueBooks.size() < finalSize) {
            int fetchedRecords = 0;
            int iteration=0;
            for (String singleQuery : searchQueryList) {

                // Fetch additional results starting from page 0.
                List<Book> booksForTerm = bookRepo
                        .searchInTitleCategoryDescription(singleQuery, PageRequest.of(currentPage, size * 5));

                // Add the remaining unique books.
                uniqueBooks.addAll(booksForTerm);

                fetchedRecords += booksForTerm.size();
                iteration++;

                if (uniqueBooks.size() >= finalSize) {
                    break;
                }
            }

            if (uniqueBooks.size() >= finalSize || fetchedRecords < iteration*size * 5) {
                break;
            }

            // Move to the next page.
            currentPage++;
        }

        System.out.println("Size after fetching :"+uniqueBooks.size());

        // Limit the result to the desired size and keep the last 'size' elements.
        List<Book> lastUniqueBooks = uniqueBooks.stream()
                .limit(finalSize)
                .skip(Math.max(0, finalSize - size))
                .toList();

        System.out.println("Size after final: " + lastUniqueBooks.size());

        return bookUtils.getBookResponse(lastUniqueBooks);
    }

    @Override
    public Boolean increaseBookStock(Long bookId, Integer stockValue) {

        Optional<Book> bookOptional = bookRepo.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();

            synchronized (book){
                book.setStockAvailability(book.getStockAvailability()+stockValue);
                bookRepo.save(book);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean decreaseBookStock(Long bookId, Integer stockValue) {
        Optional<Book> bookOptional = bookRepo.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();

            synchronized (book) {  // Synchronized block on the book object
                if(book.getStockAvailability() - stockValue >= 0) {
                    book.setStockAvailability(book.getStockAvailability() - stockValue);
                    bookRepo.save(book);
                    return true;
                } else {
                    throw new StockNotAvailableException("Stock not available for requested quantity " + stockValue);
                }
            }
        }
        return false;
    }


    @Override
    public List<String> getSuggestedBookNamesByTitle(String query, Integer size) {

        PageRequest pageRequest = PageRequest.of(0, size); // Limit to the first 10 results

        return bookRepo.findBookNamesStartWith(query, pageRequest);
    }



}
