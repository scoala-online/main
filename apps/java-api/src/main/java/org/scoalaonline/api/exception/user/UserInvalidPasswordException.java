package org.scoalaonline.api.exception.user;

public class UserInvalidPasswordException extends Exception{
  public UserInvalidPasswordException() {
  }

  public UserInvalidPasswordException(String message) {
    super(message);
  }
}
