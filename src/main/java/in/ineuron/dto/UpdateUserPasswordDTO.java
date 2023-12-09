package in.ineuron.dto;

import lombok.Data;

@Data
public class UpdateUserPasswordDTO {

    private String userName;
    private String newPassword;
}
