package in.ineuron.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateSellerPasswordDTO {

    private String sellerName;
    private String newPassword;
}
