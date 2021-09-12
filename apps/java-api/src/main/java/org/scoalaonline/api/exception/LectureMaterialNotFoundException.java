package org.scoalaonline.api.exception;

public class LectureMaterialNotFoundException extends Exception{
  public LectureMaterialNotFoundException(){
  }
  public LectureMaterialNotFoundException(String message){
    super(message);
  }
}
