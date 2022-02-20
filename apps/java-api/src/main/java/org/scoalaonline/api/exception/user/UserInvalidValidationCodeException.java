package org.scoalaonline.api.exception.user;

public class UserInvalidValidationCodeException extends Exception{
  public UserInvalidValidationCodeException() {
  }

  public UserInvalidValidationCodeException(String message) {
    super(message);
  }
}
