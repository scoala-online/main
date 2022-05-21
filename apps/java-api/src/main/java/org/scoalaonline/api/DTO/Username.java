package org.scoalaonline.api.DTO;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class Username {

  @NotBlank(message = "Username is required.")
  @Size(max = 255, message = "The username cannot exceed 255 characters.")
  @Pattern(
    regexp = "^(?=.{1,64}@)[A-Za-z0-9_!#$%&'*+-=?^_`{|}~\\/]+(\\.[A-Za-z0-9_=?^_`{|}~\\/-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})*$", 
    message = "The username does not match the required pattern."
  )
  private String value;
}
