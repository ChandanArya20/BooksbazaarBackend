package in.ineuron.services.impl;

import in.ineuron.dto.BookOrderResponse;
import in.ineuron.models.Book;
import in.ineuron.models.BookOrder;
import in.ineuron.models.User;
import in.ineuron.repositories.BookOrderRepository;
import in.ineuron.repositories.SellerRepository;
import in.ineuron.services.BookOrderService;
import in.ineuron.services.BookService;
import in.ineuron.utils.BookUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookOrderServiceImpl implements BookOrderService {

    @Autowired
    private BookOrderRepository orderRepo;

    @Autowired
    private SellerRepository sellerRepo;

    @Autowired
    BookUtils bookUtils;

    @Autowired
    private BookService bookService;



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

        List<Book> bookList = bookService.fetchBooksBySellerId(id);

        List<BookOrder> orderList = orderRepo.findByBook(bookList);

        List<BookOrderResponse> orders = bookUtils.getBookOrderResponse(orderList);

        return orders;
    }

    @Override
    public BookOrder getOrderById(Long orderId) {

        return orderRepo.findById(orderId).orElse(null);
    }

}
