package org.scoalaonline.api.exception.subject;

/**
 * Exception class for when the Subject's value is invalid.
 */

public class SubjectInvalidValueException extends Exception {
  public SubjectInvalidValueException() {
  }

  public SubjectInvalidValueException(String message) {
    super(message);
  }
}
