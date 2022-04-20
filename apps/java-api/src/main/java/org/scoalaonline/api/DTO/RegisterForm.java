package org.scoalaonline.api.DTO;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class RegisterForm {

  @NotBlank(message = "Name is required")
  @Size(max = 80)
  private String name;

  @NotBlank(message = "Username is required")
  @Size(max = 255)
  @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_!#$%&'*+-=?^_`{|}~\\/]+(\\.[A-Za-z0-9_=?^_`{|}~\\/-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})*$")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 128)
  @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_]).{8,}")
  private String password;
}
