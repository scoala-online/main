package org.scoalaonline.api.exception;

/**
 * Exception class for when the Video Transcript field is null
 */

public class VideoInvalidTranscriptException extends Exception {
  public VideoInvalidTranscriptException() {
  }

  public VideoInvalidTranscriptException( String message ) { super( message ); }
}
