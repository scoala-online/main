package org.scoalaonline.api.controller;

import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.service.LectureMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lecture-materials")
public class LectureMaterialController {

  @Autowired
  LectureMaterialService lectureMaterialService;

  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<LectureMaterial>> getAllLectureMaterials () {
    List<LectureMaterial> lectureMaterials = lectureMaterialService.getAll();
    return new ResponseEntity<>(lectureMaterials, HttpStatus.OK);
  }

}
