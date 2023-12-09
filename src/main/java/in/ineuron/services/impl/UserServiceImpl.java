package in.ineuron.services.impl;

import in.ineuron.dto.AddressRequest;
import in.ineuron.dto.UserResponse;
import in.ineuron.models.Address;
import in.ineuron.models.User;
import in.ineuron.repositories.UserRepository;
import in.ineuron.services.UserService;
import in.ineuron.utils.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Boolean isUserAvailableByPhone(String phone) {

        return userRepo.existsByPhone(phone);
    }

    @Override
    public Boolean isUserAvailableByEmail(String email) {

        return userRepo.existsByEmail(email);
    }

    @Override
    public Boolean isUserAvailableByUserName(String userName) {

        boolean isEmail = EmailValidator.isEmail(userName);

        if(isEmail){
            return userRepo.existsByEmail(userName);
        }else {
            return userRepo.existsByPhone(userName);
        }
    }

    @Override
    public void registerUser(User user) {

        userRepo.save(user);
    }

    @Override
    public User fetchUserByPhone(String phone) {

        return userRepo.findByPhone(phone);
    }

    @Override
    public User fetchUserByEmail(String email) {

        return userRepo.findByEmail(email);
    }

    @Override
    public User fetchUserByUserName(String userName) {
        boolean isEmail = EmailValidator.isEmail(userName);

        if(isEmail){
            return userRepo.findByEmail(userName);
        }else {
            return userRepo.findByPhone(userName);
        }
    }

    @Override
    public UserResponse fetchUserDetails(Long userId) {

        Optional<User> userOptional = userRepo.findById(userId);
        if(userOptional.isPresent()) {

            User user = userOptional.get();
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);

            return userResponse;
        }

        return null;
    }

    @Override 
    public void insertUserAddress(AddressRequest address, Long userId) {

        System.out.println(address);
        Optional<User> userOptional = userRepo.findById(userId);

        if(userOptional.isPresent()) {

            User user = userOptional.get();
            List<Address> userAddress = user.getAddress();

            Address addressObj = new Address();
            BeanUtils.copyProperties(address, addressObj);
            userAddress.add(addressObj);

            userRepo.save(user);
        }
    }

    @Override
    public boolean updateUserPassword(Long userId, String newPassword) {

        Optional<User> userOptional = userRepo.findById(userId);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setPassword(newPassword);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<Address> fetchAddressByUserId(Long userId) {

        User user = userRepo.findById(userId).orElse(null);
        List<Address> address=new ArrayList<>();

        if(user!=null)	{

            address=user.getAddress();
        }

        return address;
    }



}
