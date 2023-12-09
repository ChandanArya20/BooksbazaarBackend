package in.ineuron.services;

import in.ineuron.dto.BookOrderResponse;
import in.ineuron.models.BookOrder;
import in.ineuron.models.User;

import java.util.List;

public interface BookOrderService {

    public Boolean insertOrder(List<BookOrder> orders);
    public List<BookOrderResponse> fetchOrdersByUser(User user);
    public List<BookOrderResponse> fetchOrdersBySellerId(Long id);
    public Boolean changeOrderStatus(Long orderId, String status);
    public BookOrder getOrderById(Long OrderId);
}
