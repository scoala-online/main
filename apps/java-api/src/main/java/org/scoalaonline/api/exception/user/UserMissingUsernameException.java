package org.scoalaonline.api.exception.user;

public class UserMissingUsernameException extends Exception{
  public UserMissingUsernameException() {
  }

  public UserMissingUsernameException(String message) {
    super(message);
  }
}
