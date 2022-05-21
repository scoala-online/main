package org.scoalaonline.api.DTO;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class RegisterForm {

  @NotBlank(message = "Name is required.")
  @Size(max = 80, message = "The name cannot exceed 80 characters.")
  private String name;

  @NotBlank(message = "Username is required.")
  @Size(max = 255, message = "The username cannot exceed 255 characters.")
  @Pattern(
    regexp = "^(?=.{1,64}@)[A-Za-z0-9_!#$%&'*+-=?^_`{|}~\\/]+(\\.[A-Za-z0-9_=?^_`{|}~\\/-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})*$",
    message = "The username does not match the required pattern."  
  )
  private String username;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, max = 128, message = "The password has to be between 8 and 128 characters.")
  @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_]).{8,}", message = "The password doesn't match the required pattern.")
  private String password;
}
