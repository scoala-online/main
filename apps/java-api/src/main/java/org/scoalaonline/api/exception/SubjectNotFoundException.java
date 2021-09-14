package org.scoalaonline.api.exception;

/**
 * Exception class for when the Subject entity cannot be found.
 */
public class SubjectNotFoundException extends Exception {

  public SubjectNotFoundException() {
  }

  public SubjectNotFoundException(String message) {
    super(message);
  }
}
