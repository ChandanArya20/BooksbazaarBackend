package in.ineuron.services.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import in.ineuron.services.SmsSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderServiceImpl implements SmsSenderService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void sendSMS(String to, String message) {
        Twilio.init(accountSid, authToken);

        Message smsMessage = Message.creator(
                        new PhoneNumber("+91"+to),
                        new PhoneNumber(twilioPhoneNumber),
                        message)
                .create();
    }
}
