package in.ineuron.services.impl;

import in.ineuron.models.BookSeller;
import in.ineuron.models.User;
import in.ineuron.repositories.SellerRepository;
import in.ineuron.services.SellerService;
import in.ineuron.utils.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Boolean isSellerAvailableBySellerName(String sellerName) {

        boolean isEmail = EmailValidator.isEmail(sellerName);

        if(isEmail){
            return sellerRepo.existsByEmail(sellerName);
        }else {
            return sellerRepo.existsByPhone(sellerName);
        }
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

    @Override
    public BookSeller fetchSellerByUserName(String userName) {
        boolean isEmail = EmailValidator.isEmail(userName);

        if(isEmail){
            return sellerRepo.findByEmail(userName);
        }else {
            return sellerRepo.findByPhone(userName);
        }
    }

    @Override
    public boolean updateSellerPassword(Long id, String newPassword) {
        Optional<BookSeller> sellerOptional = sellerRepo.findById(id);

        if(sellerOptional.isPresent()){
            BookSeller bookSeller = sellerOptional.get();
            bookSeller.setPassword(newPassword);
            sellerRepo.save(bookSeller);
            return true;
        }
        return false;
    }


}
