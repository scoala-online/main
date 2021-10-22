package org.scoalaonline.api.exception.lectureMaterial;

public class LectureMaterialNotFoundException extends Exception{
  public LectureMaterialNotFoundException(){
  }

  public LectureMaterialNotFoundException(String message){
    super(message);
  }
}
