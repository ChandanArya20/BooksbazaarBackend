package in.ineuron.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OTPEntry {
    private String otp;
    private long creationTime;

    public OTPEntry(String otp, long creationTime) {
        this.otp = otp;
        this.creationTime = creationTime;
    }

}

