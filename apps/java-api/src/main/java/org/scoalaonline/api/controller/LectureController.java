package org.scoalaonline.api.controller;


import org.scoalaonline.api.exception.LectureInvalidTitleException;
import org.scoalaonline.api.exception.LectureNotFoundException;
import org.scoalaonline.api.model.Lecture;
import org.scoalaonline.api.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class for the Lecture controller. Contains the following methods:
 * GET:	"/lectures"	retrieves all the entries;
 * GET:	"/lectures/{id}"	retrieves the entry with the provided id;
 * POST: "/lectures"	creates a new entry;
 * PATCH:	"/lectures/{id}	edits the entry with the provided id;
 * DELETE:	"/lectures/{id}	deletes the entry with the provided id.
 */
@CrossOrigin
@RestController
@RequestMapping("/lectures")
public class LectureController {
  @Autowired
  LectureService lectureService;

  /**
   * Sends HTTP status Response Entity with all the Lecture entries.
   * @return a Response Entity with HTTP Status OK and a list of the Lecture entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<Lecture>> getAllLectures () {
    List<Lecture> lectures = lectureService.getAll();
    return new ResponseEntity<>(lectures, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with a specific Lecture entry.
   * Sends HTTP status not found if there is no entry with the provided id.
   * @param id the id of the specific Lecture.
   * @return the Response Entity with a Status Code and a body.
   */
  @GetMapping(value="/{id}")
  public ResponseEntity<Lecture> getLectureById(@PathVariable("id") String id) {
    Lecture lecture;
    try {
      lecture = lectureService.getOneById(id);
    } catch (LectureNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET: Lecture Not Found", e);
    }
    return new ResponseEntity<>(lecture, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with the Lecture entry that has been created.
   * Sends HTTP status Bad Request if the Lecture to be posted is invalid.
   * @param lecture the Lecture to be added in the db.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<Lecture> addLecture (@RequestBody Lecture lecture) {
    Lecture savedLecture;
    try {
      savedLecture = lectureService.add(lecture);
    } catch (LectureInvalidTitleException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Lecture Invalid Data", e);
    }
    return new ResponseEntity<>(savedLecture, HttpStatus.CREATED);
  }

  /**
   * Sends HTTP status Response Entity with the Lecture entry that has been updated.
   * Sends HTTP status Not Found if the Lecture cannot be found.
   * Sends HTTP status Invalid Data if the Lecture to be posted is invalid.
   * @param id the id of the Lecture to be updated.
   * @param lecture the Lecture to be updated.
   * @return the Response Entity with a Status Code with a body.
   */
  @PatchMapping(value = "/{id}")
  public ResponseEntity<Lecture> updateLecture (@PathVariable ("id") String id, @RequestBody Lecture lecture) {
    Lecture updatedLecture;
    try {
      updatedLecture = lectureService.update(id, lecture);
    } catch (LectureNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PATCH: Lecture Not Found", e);
    } catch (LectureInvalidTitleException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PATCH: Lecture Invalid Data", e);
    }

    return new ResponseEntity<>(updatedLecture, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with the Lecture entry that has been deleted.
   * Sends HTTP status Not Found if there is no entry of the provided id.
   * @param id the id of the Lecture to be deleted.
   * @return the Response Entity with a Status Code with a body.
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<HttpStatus> deleteLecture (@PathVariable("id") String id) {
    try {
      lectureService.delete(id);
    }
    catch (LectureNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DELETE: Lecture Not Found", e);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
