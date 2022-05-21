package org.scoalaonline.api.validator.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ResetPasswordCodeExpiryDateValidator.class)
@Documented
public @interface ValidateResetPasswordCodeExpiryDate {

  String message() default "Reset password code expiry date is invalid.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}