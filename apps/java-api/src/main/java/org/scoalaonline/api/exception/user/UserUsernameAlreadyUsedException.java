package org.scoalaonline.api.exception.user;

public class UserUsernameAlreadyUsedException extends Exception{
  public UserUsernameAlreadyUsedException() {
  }

  public UserUsernameAlreadyUsedException(String message) {
    super(message);
  }
}
