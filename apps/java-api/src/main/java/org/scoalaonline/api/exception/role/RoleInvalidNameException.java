package org.scoalaonline.api.exception.role;

public class RoleInvalidNameException extends Exception{
  public RoleInvalidNameException() {
  }

  public RoleInvalidNameException(String message) {
    super(message);
  }
}
