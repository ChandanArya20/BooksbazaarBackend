package in.ineuron.services;

import in.ineuron.dto.AddressRequest;
import in.ineuron.dto.UserResponse;
import in.ineuron.models.Address;
import in.ineuron.models.User;

import java.util.List;

public interface UserService {

    public Boolean isUserAvailableByPhone(String phone);
    public Boolean isUserAvailableByEmail(String email);
    public Boolean isUserAvailableByUserName(String userName);
    public void registerUser(User user);
    public User fetchUserByPhone(String phone);
    public User fetchUserByEmail(String email);
    public User fetchUserByUserName(String userName);
    public UserResponse fetchUserDetails(Long userId);
    public List<Address> fetchAddressByUserId(Long userId);
    public void insertUserAddress(AddressRequest address, Long userId);

    public boolean updateUserPassword(Long userId, String newPassword);
}
