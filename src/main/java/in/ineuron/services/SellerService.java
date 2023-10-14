package in.ineuron.services;

import in.ineuron.models.BookSeller;

public interface SellerService {

    public Boolean isSellerAvailableByPhone(String phone);
    public Boolean isSellerAvailableByEmail(String email);
    public Boolean isSellerAvailableBySellerId(String sellerId);
    public void registerSeller(BookSeller seller);
    public BookSeller fetchSellerByPhone(String phone);
    public BookSeller fetchSellerByEmail(String email);
    public BookSeller fetchSellerBySellerId(String sellerId);
}
