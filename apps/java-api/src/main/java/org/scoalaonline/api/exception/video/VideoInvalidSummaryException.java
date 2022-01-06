package org.scoalaonline.api.exception.video;

/**
 * Exception class for when the Video Summary field is null
 */

public class VideoInvalidSummaryException extends Exception {
  public VideoInvalidSummaryException() {
  }

  public VideoInvalidSummaryException( String message ) { super( message ); }
}
