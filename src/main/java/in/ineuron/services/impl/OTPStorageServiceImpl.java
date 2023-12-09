package in.ineuron.services.impl;

import in.ineuron.dto.OTPEntry;
import in.ineuron.services.OTPStorageService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OTPStorageServiceImpl implements OTPStorageService {
    private final Map<String, OTPEntry> otpMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


    public OTPStorageServiceImpl() {
        // Schedule a cleanup task to remove expired OTPs every 10 minutes.
        executorService.scheduleAtFixedRate(this::cleanUpExpiredOTP, 1, 1, TimeUnit.MINUTES);
    }

    public void storeOTP(String userName, String otp) {

        OTPEntry otpEntry = new OTPEntry(otp, System.currentTimeMillis());
        otpMap.put(userName, otpEntry);
    }

    public String getStoredOTP(String userName) {
        OTPEntry otpEntry = otpMap.get(userName);
        return otpEntry!=null ? otpEntry.getOtp() : "-1";  // Return -1 if OTP is not found.
    }

    @Override
    public boolean verifyOTP(String userName, String otp) {

        String storedOTP = getStoredOTP(userName);
        return storedOTP.equals(otp);
    }

    @Override
    public void removeOTP(String userName) {
        otpMap.remove(userName);
    }

    private void cleanUpExpiredOTP() {
        System.out.println("cleanUpExpiredOTP() runs");
        // Iterate through the OTP map and remove entries older than 10 minutes.
        long currentTime = System.currentTimeMillis();
        otpMap.entrySet().removeIf(entry -> (currentTime - entry.getValue().getCreationTime() > 600_000));
    }

}
