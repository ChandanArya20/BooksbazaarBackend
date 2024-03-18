package in.ineuron.services.impl;

import in.ineuron.services.EmailSenderService;
import in.ineuron.services.OTPSenderService;
import in.ineuron.services.SmsSenderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;

@Service
@AllArgsConstructor
public class OTPSenderServiceImpl implements OTPSenderService {

    private EmailSenderService emailSender;
    private SmsSenderService smsSender;

    public Integer sendOTPByEmail(String email ) throws MessagingException {

        Random random = new Random();
        int OTP = random.nextInt(100000, 999999);

        emailSender.sendEmail(email,"to send/verify OTP", "Your OTP is : "+OTP);

        return OTP;

    }

    @Override
    public Integer sendOTPByPhone(String phone) {

        Random random = new Random();
        int OTP = random.nextInt(100000, 999999);

        smsSender.sendSMS(phone,"Your OTP is : "+OTP);

        return OTP;
    }
}

