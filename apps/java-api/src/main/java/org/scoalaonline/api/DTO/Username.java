package org.scoalaonline.api.DTO;

import lombok.Data;
import javax.validation.constraints.*;

import org.scoalaonline.api.validator.ValidatorPatterns;

@Data
public class Username {

  @NotBlank(message = "Username is required.")
  @Size(max = 255, message = "The username cannot exceed 255 characters.")
  @Pattern(
    regexp = ValidatorPatterns.EMAIL_PATTERN, 
    message = "The username does not match the required pattern."
  )
  private String value;
}
