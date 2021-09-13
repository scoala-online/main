package org.scoalaonline.api.controller;


import org.scoalaonline.api.exception.SubjectInvalidValueException;
import org.scoalaonline.api.exception.SubjectNotFoundException;
import org.scoalaonline.api.model.Subject;
import org.scoalaonline.api.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class for the Subject controller. Contains the following methods:
 * GET:	"/lecture-materials"	retrieves all the entries;
 * GET:	"/lecture-materials/{id}"	retrieves the entry with the provided id;
 * POST: "/lecture-materials"	creates a new entry;
 * PATCH:	"/lecture-materials/{id}	edits the entry with the provided id;
 * DELETE:	"/lecture-materials/{id}	deletes the entry with the provided id.
 */
@CrossOrigin
@RestController
@RequestMapping("/subjects")
public class SubjectController {
  @Autowired
  SubjectService subjectService;

  /**
   * Sends an HTTP status Response Entity with all the lecture material entries.
   * @return a Response Entity with HTTP Status OK and a list of the Subject entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<Subject>> getAllSubjects () {
    List<Subject> subjects = subjectService.getAll();
    return new ResponseEntity<>(subjects, HttpStatus.OK);
  }

  /**
   * Sends an HTTP Response Entity with a specific Subject material entry, along with HTTP status OK.
   * Sends HTTP status not found if there is no entry with the provided id.
   * @param id the id of the specific Subject.
   * @return the Response Entity with a Status Code and a body.
   */
  @GetMapping(value="/{id}")
  public ResponseEntity<Subject> getSubjectById(@PathVariable("id") String id) {
    Subject subject;
      try {
        subject=subjectService.getOneById(id);
      } catch (SubjectNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET: Subject is not found", e);
      }
      return new ResponseEntity<>(subject, HttpStatus.OK);
  }

  /**
   * Sends a HTTP status Response Entity with the subject entry that has been created, along with HTTP Status CREATED.
   * Sends HTTP status Invalid data if the subject to be posted is invalid.
   * @param subject the subject to be added in the db
   * @return the Response Entity with a Status Code and a body
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<Subject> addSubject (@RequestBody Subject subject) {
    Subject savedSubject;
    try {
      savedSubject = subjectService.add(subject);
    } catch (SubjectInvalidValueException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Subject Invalid Data", e);
    }
    return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
  }

  /**
   * Sends an HTTP Response Entity with the Subject material entry that has been updated, along with HTTP Status OK
   * Sends HTTP status Not Found if the subject cannot be found.
   * Sends HTTP status Invalid data if the subject to be posted is invalid.
   * @param id the id of the subject to be updated
   * @param subject the subject to be updated
   * @return the Response Entity with a Status Code with a body
   */
  @PatchMapping(value = "/{id}")
  public ResponseEntity<Subject> updateSubject (@PathVariable ("id") String id, @RequestBody Subject subject) {
    Subject updatedSubject;
    try {
      updatedSubject = subjectService.update(id, subject);
    } catch (SubjectNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PATCH: Subject Not Found", e);
    } catch (SubjectInvalidValueException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PATCH: Subject Invalid data", e);
    }

    return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
  }

  /**
   * Sends an HTTP Response Entity with the Subject material entry that has been deleted, along with HTTP Status OK
   * Sends HTTP status Not Found if there is no entry of the provided id
   * @param id the id of the subject to be deleted
   * @return the Response Entity with a Status Code with a body
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<HttpStatus> deleteSubject (@PathVariable("id") String id) {
    try {
      subjectService.delete(id);
    }
      catch (SubjectNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DELETE: Subject Not Found", e);
      }
      return new ResponseEntity<>(HttpStatus.OK);
  }

}