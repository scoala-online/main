package org.scoalaonline.api.controller;


import org.scoalaonline.api.exception.subject.SubjectInvalidValueException;
import org.scoalaonline.api.exception.subject.SubjectNotFoundException;
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
 * GET:	"/subjects"	retrieves all the entries;
 * GET:	"/subjects/{id}"	retrieves the entry with the provided id;
 * POST: "/subjects"	creates a new entry;
 * PATCH:	"/subjects/{id}	edits the entry with the provided id;
 * DELETE:	"/subjects/{id}	deletes the entry with the provided id.
 */
@CrossOrigin
@RestController
@RequestMapping("/subjects")
public class SubjectController {
  @Autowired
  SubjectService subjectService;

  /**
   * Sends HTTP status Response Entity with all the Subject entries.
   * @return a Response Entity with HTTP Status OK and a list of the Subject entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<Subject>> getAllSubjects () {
    List<Subject> subjects = subjectService.getAll();
    return new ResponseEntity<>(subjects, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with a specific Subject entry.
   * Sends HTTP status Not Found if there is no entry with the provided id.
   * @param id the id of the specific Subject.
   * @return the Response Entity with a Status Code and a body.
   */
  @GetMapping(value="/{id}")
  public ResponseEntity<Subject> getSubjectById(@PathVariable("id") String id) {
    Subject subject;
      try {
        subject=subjectService.getOneById(id);
      } catch (SubjectNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET: Subject Not Found", e);
      }
      return new ResponseEntity<>(subject, HttpStatus.OK);
  }

  /**
   * Sends HTTP status Response Entity with the Subject entry that has been created.
   * Sends HTTP status Invalid Value if the Subject to be posted is invalid.
   * @param subject the Subject to be added in the db.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<Subject> addSubject (@RequestBody Subject subject) {
    Subject savedSubject;
    try {
      savedSubject = subjectService.add(subject);
    } catch (SubjectInvalidValueException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Subject Invalid Value", e);
    }
    return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
  }

  /**
   * Sends HTTP status Response Entity with the Subject entry that has been updated.
   * Sends HTTP status Not Found if the Subject cannot be found.
   * Sends HTTP status Invalid Value if the Subject to be posted is invalid.
   * @param id the id of the Subject to be updated.
   * @param subject the Subject to be updated.
   * @return the Response Entity with a Status Code with a body.
   */
  @PatchMapping(value = "/{id}")
  public ResponseEntity<Subject> updateSubject (@PathVariable ("id") String id, @RequestBody Subject subject) {
    Subject updatedSubject;
    try {
      updatedSubject = subjectService.update(id, subject);
    } catch (SubjectNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PATCH: Subject Not Found", e);
    } catch (SubjectInvalidValueException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PATCH: Subject Invalid value", e);
    }

    return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with the Subject entry that has been deleted.
   * Sends HTTP status Not Found if there is no entry of the provided id.
   * @param id the id of the Subject to be deleted.
   * @return the Response Entity with a Status Code with a body.
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
