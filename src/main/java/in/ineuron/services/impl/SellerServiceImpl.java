package in.ineuron.services.impl;

import in.ineuron.models.BookSeller;
import in.ineuron.repositories.SellerRepository;
import in.ineuron.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepo;

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


}
