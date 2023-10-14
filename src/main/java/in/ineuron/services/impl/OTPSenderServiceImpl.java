package in.ineuron.services.impl;

import in.ineuron.services.EmailSenderService;
import in.ineuron.services.OTPSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;

@Service
public class OTPSenderServiceImpl implements OTPSenderService {
    @Autowired
    private EmailSenderService emailSender;


    public Integer sendOTPByEmail(String email ) throws MessagingException {

        Random random = new Random();
        int OTP = random.nextInt(10000, 99999);

        emailSender.sendEmail(email,"to send/verify OTP", "Your OTP is : "+OTP);

        return OTP;

    }
}

