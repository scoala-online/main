package org.scoalaonline.api.exception.user;

public class UserInvalidResetPasswordCodeException extends Exception{
  public UserInvalidResetPasswordCodeException() {
  }

  public UserInvalidResetPasswordCodeException(String message) {
    super(message);
  }
}
