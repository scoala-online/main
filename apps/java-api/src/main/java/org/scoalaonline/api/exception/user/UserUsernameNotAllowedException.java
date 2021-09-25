package org.scoalaonline.api.exception.user;

public class UserUsernameNotAllowedException extends Exception{
  public UserUsernameNotAllowedException() {
  }

  public UserUsernameNotAllowedException(String message) {
    super(message);
  }
}
