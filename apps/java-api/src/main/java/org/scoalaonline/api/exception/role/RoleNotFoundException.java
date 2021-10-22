package org.scoalaonline.api.exception.role;

public class RoleNotFoundException extends Exception{
  public RoleNotFoundException() {
  }

  public RoleNotFoundException(String message) {
    super(message);
  }
}
