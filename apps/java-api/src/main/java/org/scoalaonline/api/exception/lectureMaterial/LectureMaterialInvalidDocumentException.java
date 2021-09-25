package org.scoalaonline.api.exception.lectureMaterial;

public class LectureMaterialInvalidDocumentException extends Exception{
  public LectureMaterialInvalidDocumentException(){
  }

  public LectureMaterialInvalidDocumentException(String message){
    super(message);
  }
}
