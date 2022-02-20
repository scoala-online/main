package org.scoalaonline.api.exception.video;

/**
 * Exception class for when the Video TeacherURL field is null
 */

public class VideoInvalidTeacherURLException extends Exception {
  public VideoInvalidTeacherURLException() {
  }

  public VideoInvalidTeacherURLException( String message ) { super( message ); }
}
