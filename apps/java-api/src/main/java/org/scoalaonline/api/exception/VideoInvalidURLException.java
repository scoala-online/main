package org.scoalaonline.api.exception;

/**
 * Exception class for when the Video URL field is null
 */

public class VideoInvalidURLException extends Exception {
  public VideoInvalidURLException() {
  }

  public VideoInvalidURLException( String message ) { super( message ); }
}
