package in.ineuron.services;

import javax.mail.MessagingException;

public interface OTPSenderService {
    Integer sendOTPByEmail(String email ) throws MessagingException;
    Integer sendOTPByPhone(String phone);
}
