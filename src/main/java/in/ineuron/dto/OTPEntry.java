package in.ineuron.dto;

public class OTPEntry {
    private String otp;
    private long creationTime;

    public OTPEntry(String otp, long creationTime) {
        this.otp = otp;
        this.creationTime = creationTime;
    }

    public String getOtp() {
        return otp;
    }

    public long getCreationTime() {
        return creationTime;
    }

}

