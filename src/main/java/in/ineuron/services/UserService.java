package in.ineuron.services;

import in.ineuron.dto.UserResponse;
import in.ineuron.models.User;

public interface UserService {

    public Boolean isUserAvailableByPhone(String phone);
    public Boolean isUserAvailableByEmail(String email);
    public Boolean isUserAvailableByUserName(String userName);
    public void registerUser(User user);
    public User fetchUserByPhone(String phone);
    public User fetchUserByEmail(String email);
    public User fetchUserByUserName(String userName);
    public UserResponse fetchUserDetails(String userId);

}
