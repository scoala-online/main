package org.scoalaonline.api.DTO;

import lombok.Data;
import javax.validation.constraints.*;

import org.scoalaonline.api.validator.ValidatorPatterns;

@Data
public class Password {

  @NotBlank(message = "Password is required.")
  @Size(min = 8, max = 128, message = "The password has to be between 8 and 128 characters.")
  @Pattern(regexp = ValidatorPatterns.PASSWORD_PATTERN, message = "The password doesn't match the required pattern.")
  private String value;
}
