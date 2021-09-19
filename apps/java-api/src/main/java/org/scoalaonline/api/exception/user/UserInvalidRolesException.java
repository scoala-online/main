package org.scoalaonline.api.exception.user;

public class UserInvalidRolesException extends Exception{
  public UserInvalidRolesException() {
  }

  public UserInvalidRolesException(String message) {
    super(message);
  }
}
