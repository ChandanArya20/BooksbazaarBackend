package in.ineuron.services;

public interface OTPStorageService {
    void storeOTP(String email, String otp);
    String getStoredOTP(String email);

    boolean verifyOTP(String email, String otp);

    void removeOTP(String email);
}

