package org.scoalaonline.api.exception.grade;

public class GradeNotFoundException extends Exception{
  public GradeNotFoundException() {
  }

  public GradeNotFoundException(String message) {
    super(message);
  }
}
