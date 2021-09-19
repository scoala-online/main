package org.scoalaonline.api.exception.user;

public class UserInvalidNameException extends Exception{
  public UserInvalidNameException() {
  }

  public UserInvalidNameException(String message) {
    super(message);
  }
}
