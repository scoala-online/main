package org.scoalaonline.api.exception.video;

/**
 * Exception class for when the Video entity cannot be found.
 */

public class VideoNotFoundException extends Exception {

    public VideoNotFoundException() {
    }

    public VideoNotFoundException( String message ) { super( message ); }
}
