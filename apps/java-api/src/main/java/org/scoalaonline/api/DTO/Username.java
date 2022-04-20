package org.scoalaonline.api.DTO;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class Username {

  @NotBlank(message = "Username is required")
  @Size(max = 255)
  @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_!#$%&'*+-=?^_`{|}~\\/]+(\\.[A-Za-z0-9_=?^_`{|}~\\/-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})*$")
  private String username;
}
