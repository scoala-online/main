package org.scoalaonline.api.exception.user;

public class UserAlreadyValidatedException extends Exception{
  public UserAlreadyValidatedException() {
  }

  public UserAlreadyValidatedException(String message) {
    super(message);
  }
}
