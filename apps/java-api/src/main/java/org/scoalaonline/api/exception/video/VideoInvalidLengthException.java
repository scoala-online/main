package org.scoalaonline.api.exception.video;

/**
 * Exception class for when the Video Length field is null, 0, or negative.
 */

public class VideoInvalidLengthException extends Exception {
  public VideoInvalidLengthException() {
  }

  public VideoInvalidLengthException( String message ) { super( message ); }
}
