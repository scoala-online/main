package org.scoalaonline.api.controller;

import org.scoalaonline.api.exception.grade.GradeInvalidValueException;
import org.scoalaonline.api.exception.grade.GradeNotFoundException;
import org.scoalaonline.api.model.Grade;
import org.scoalaonline.api.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class for the Grade controller. Contains the following methods:
 GET:	"/grades"	retrieves all the entries;
 GET:	"/grades/{id}"	retrieves the entry with the provided id;
 POST: "/grades"	creates a new entry;
 PATCH:	"/grades/{id}	edits the entry with the provided id;
 DELETE:	"/grades/{id}	deletes the entry with the provided id;
 */
@CrossOrigin
@RestController
@RequestMapping("/grades")
public class GradeController {
  @Autowired
  GradeService gradeService;

  /**
   * Sends an HTTP Response Entity with all the Grade entries.
   * @return a Response Entity with HTTP Status OK and a list of the Grade entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<Grade>> getAllGrades () {
    List<Grade> grades = gradeService.getAll();
    return new ResponseEntity<>(grades, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with a specific Grade entry.
   * Sends HTTP Status Not Found if there is no entry with the provided id.
   * @param id the id of the specific Grade.
   * @return the Response Entity with a Status Code and a Body.
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<Grade> getGradeById(@PathVariable("id") String id) {
    Grade grade;
    try
    {
      grade = gradeService.getOneById(id);
    } catch (GradeNotFoundException e)
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "GET: Grade not found.", e );
    }
    return new ResponseEntity<>(grade, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with the Grade entry that has been created.
   * Sends HTTP status Bad Request if the Grade to be posted is invalid.
   * @param grade the Grade to be added in the db.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<Grade> addGrade (@RequestBody Grade grade) {
    Grade savedGrade;
    try {
      savedGrade = gradeService.add(grade);
    } catch (GradeInvalidValueException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Grade invalid value.", e );
    }
    return new ResponseEntity<>(savedGrade, HttpStatus.CREATED);
  }

  /**
   * Sends HTTP Response Entity with the Grade entry that has been updated.
   * Sends HTTP status Not Found if the Grade cannot be found.
   * Sends HTTP status Bad Request if the Grade to be posted is invalid.
   * @param id the id of the Grade to be updated.
   * @param grade the Grade to be updated.
   * @return the Response Entity with a Status Code with a body.
   */
  @PatchMapping( value = "/{id}" )
  public ResponseEntity<Grade> updateGrade (@PathVariable( "id" ) String id, @RequestBody Grade grade ) {
    Grade updatedGrade;
    try
    {
      updatedGrade = gradeService.update( id, grade );
    } catch ( GradeNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "PATCH: Grade not found.", e );
    } catch ( GradeInvalidValueException e){
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: Grade invalid value.", e );
    }

    return new ResponseEntity<>( updatedGrade, HttpStatus.OK );
  }

  /**
   * Sends HTTP Response Entity with the Grade entry that has been deleted.
   * Sends HTTP status Not Found if there is no entry of the provided id.
   * @param id the id of the Grade to be deleted
   * @return the Response Entity with a Status Code with a body.
   */
  @DeleteMapping( value = "/{id}" )
  public ResponseEntity<HttpStatus> deleteGrade( @PathVariable( "id" ) String id ) {
    try
    {
      gradeService.delete( id );
    } catch ( GradeNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "DELETE: Grade not found.", e );
    }
    return new ResponseEntity<>( HttpStatus.OK );
  }
}
