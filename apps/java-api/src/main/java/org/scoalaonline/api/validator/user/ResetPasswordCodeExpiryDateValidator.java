package org.scoalaonline.api.validator.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ResetPasswordCodeExpiryDateValidator
    implements ConstraintValidator<ValidateResetPasswordCodeExpiryDate, Instant> {
  private static final Integer EXPIRY_TIME_OFFSET = 30; 

  @Override
  public boolean isValid(Instant value, ConstraintValidatorContext context) {
    final Instant minInstant = Instant.now();
    final Instant maxInstant = Instant.now().plus(EXPIRY_TIME_OFFSET, ChronoUnit.MINUTES);

    return value.isBefore(minInstant) && value.isAfter(maxInstant);
  }
}