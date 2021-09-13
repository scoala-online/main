package org.scoalaonline.api.exception;

/**
 * Exception class for when the subject's value is invalid.
 */
public class SubjectInvalidValueException extends Exception {
  public SubjectInvalidValueException() {
  }

  public SubjectInvalidValueException(String message) {
    super(message);
  }
}
