package org.scoalaonline.api.exception.grade;

public class GradeInvalidValueException extends Exception{
  public GradeInvalidValueException() {
  }

  public GradeInvalidValueException(String message) {
    super(message);
  }
}
