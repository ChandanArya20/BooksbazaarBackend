package in.ineuron.dto;

import lombok.Data;

@Data
public class UpdateSellerPasswordDTO {

    private String sellerName;
    private String newPassword;
}
