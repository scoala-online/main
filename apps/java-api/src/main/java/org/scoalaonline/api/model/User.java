package org.scoalaonline.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.scoalaonline.api.validator.ValidatorPatterns;
import org.scoalaonline.api.validator.user.ValidateResetPasswordCodeExpiryDate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 *Represents the User node from the graph database.
 * It contains the following properties: Name, Username, Password and Roles.
 */
@Node("User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Username is required.")
  @Size(max = 255, message = "Username cannot be longer than 255 characters.")
  @Pattern(
    regexp = ValidatorPatterns.EMAIL_PATTERN, 
    message = "The username does not match the required pattern."
  )
  @Property("username")
  private String username;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, max = 128, message = "The password has to be between 8 and 128 characters.")
  @Pattern(regexp = ValidatorPatterns.PASSWORD_PATTERN, message = "The password doesn't match the required pattern.")
  @Property("password")
  private String password;
  
  @NotBlank(message = "Name is required.")
  @Size(max = 80, message = "The name cannot be longer than 128 characters.")
  @Property("name")
  private String name;
  
  @NotBlank(message = "Validated is required.")
  @Property("validated")
  private Boolean validated;
  
  @NotNull(message = "Validation code is required.")
  @Property("validation_code")
  private String validationCode;
  
  @NotNull(message = "Reset password code is required.")
  @Property("reset_password_code")
  private String resetPasswordCode;
  
  @NotBlank(message = "Reset password code expiry date is required.")
  @ValidateResetPasswordCodeExpiryDate
  @Property("reset_password_code_expiry_date")
  private Instant resetPasswordCodeExpiryDate;

  @Property("validated_at")
  private Instant validatedAt;

  @CreatedDate
  @Property("created_at")
  private Instant createdAt;

  @LastModifiedDate
  @Property("last_modified_at")
  private Instant lastModifiedAt;
  
  @NotBlank(message = "Role is required.")
  @Relationship(type = "HAS_ROLE", direction = Relationship.Direction.OUTGOING)
  private List<Role> roles;
}
