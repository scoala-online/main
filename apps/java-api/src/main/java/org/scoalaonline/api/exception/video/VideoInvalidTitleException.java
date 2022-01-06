package org.scoalaonline.api.exception.video;

/**
 * Exception class for when the Video Title field is null
 */

public class VideoInvalidTitleException extends Exception {
  public VideoInvalidTitleException() {
  }

  public VideoInvalidTitleException( String message ) { super( message ); }
}
