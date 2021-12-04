package org.scoalaonline.api.exception.lecture;

/**
 * Exception class for when the Lecture entity cannot be found.
 */
public class LectureNotFoundException extends Exception {

  public LectureNotFoundException() {
  }

  public LectureNotFoundException(String message) {
    super(message);
  }
}
