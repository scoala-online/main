package org.scoalaonline.api.controller;

import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.service.LectureMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
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

  @GetMapping(value = "/{id}")
  public ResponseEntity<LectureMaterial> getArtistById(@PathVariable("id") String id) {
    LectureMaterial artist = lectureMaterialService.getOneById(id)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "GET: Lecture Material not found.", new LectureMaterialNotFoundException()
      ));
    return new ResponseEntity<>(artist, HttpStatus.OK);
  }

  @PostMapping(value = {"", "/"})
  public ResponseEntity<LectureMaterial> addArtist (@RequestBody LectureMaterial lectureMaterial) {
    LectureMaterial savedArtist = lectureMaterialService.add(lectureMaterial);
    return new ResponseEntity<>(savedArtist, HttpStatus.CREATED);
  }

  @PutMapping( value = "/{id}" )
  public ResponseEntity<LectureMaterial> update( @PathVariable( "id" ) String id, @RequestBody LectureMaterial lectureMaterial ) {
    LectureMaterial updatedLectureMaterial;
    try
    {
      updatedLectureMaterial = lectureMaterialService.update( id, lectureMaterial );
    } catch ( LectureMaterialNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "PUT: Lecture Material Not Found", e );
    }

    return new ResponseEntity<>( updatedLectureMaterial, HttpStatus.OK );
  }

  @DeleteMapping( value = "/{id}" )
  public ResponseEntity<HttpStatus> delete( @PathVariable( "id" ) String id ) {
    try
    {
      lectureMaterialService.delete( id );
    } catch ( LectureMaterialNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "DELETE: History Not Found", e );
    }

    return new ResponseEntity<>( HttpStatus.OK );
  }

}
