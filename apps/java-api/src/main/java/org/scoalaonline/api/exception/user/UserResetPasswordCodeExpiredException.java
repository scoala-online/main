package org.scoalaonline.api.exception.user;

public class UserResetPasswordCodeExpiredException extends Exception{
  public UserResetPasswordCodeExpiredException() {
  }

  public UserResetPasswordCodeExpiredException(String message) {
    super(message);
  }
}
