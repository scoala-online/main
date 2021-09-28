package org.scoalaonline.api.exception.user;

public class UserInvalidUsernameException extends Exception{
  public UserInvalidUsernameException() {
  }

  public UserInvalidUsernameException(String message) {
    super(message);
  }
}
