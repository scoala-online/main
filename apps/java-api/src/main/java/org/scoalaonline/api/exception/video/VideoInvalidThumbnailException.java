package org.scoalaonline.api.exception.video;

/**
 * Exception class for when the Video Thumbnail field is null
 */

public class VideoInvalidThumbnailException extends Exception {
  public VideoInvalidThumbnailException() {
  }

  public VideoInvalidThumbnailException( String message ) { super( message ); }
}
