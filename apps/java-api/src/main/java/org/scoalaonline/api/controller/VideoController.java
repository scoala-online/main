package org.scoalaonline.api.controller;

import org.scoalaonline.api.exception.video.*;
import org.scoalaonline.api.model.Video;
import org.scoalaonline.api.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Class for the Video controller. Contains the following methods:
 * GET:	"/videos"	retrieves all the entries;
 * GET:	"/videos/{id}"	retrieves the entry with the provided id;
 * POST: "/videos"	creates a new entry;
 * PATCH:	"/videos/{id}	edits the entry with the provided id;
 * DELETE:	"/videos/{id}	deletes the entry with the provided id.
 */

@CrossOrigin
@RestController
@RequestMapping("/videos")

public class VideoController {
  @Autowired
  VideoService videoService;

  /**
   * Sends HTTP status Response Entity with all the Video entries.
   * @return a Response Entity with HTTP Status OK and a list of the Video entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<Video>> getAllVideos () {
    List<Video> videos = videoService.getAll();
    return new ResponseEntity<>(videos, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with a specific Video entry.
   * Sends HTTP status Not Found if there is no entry with the provided id.
   * @param id the id of the specific Video.
   * @return the Response Entity with a Status Code and a body.
   */
  @GetMapping(value="/{id}")
  public ResponseEntity<Video> getVideoById(@PathVariable("id") String id) {
    Video video;
    try {
      video= videoService.getOneById(id);
    } catch (VideoNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET: Video Not Found", e);
    }
    return new ResponseEntity<>(video, HttpStatus.OK);
  }

  /**
   * Sends HTTP status Response Entity with the Video entry that has been created.
   * Sends HTTP status Invalid Value if the Video to be posted is invalid.
   * @param video the Video to be added in the db.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<Video> addVideo (@RequestBody Video video) {
    Video savedVideo;
    try {
      savedVideo = videoService.add(video);
    } catch ( VideoInvalidURLException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid URL.", e);
    } catch (VideoInvalidTitleException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid title.", e);
    } catch (VideoInvalidLengthException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid length.", e);
    } catch (VideoInvalidTeacherURLException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid teacher URL.", e);
    } catch (VideoInvalidTeacherImageURLException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid teacher image URL.", e);
    } catch (VideoInvalidTranscriptException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid transcript.", e);
    } catch (VideoInvalidSummaryException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid summary.", e);
    } catch (VideoInvalidThumbnailException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid thumbnail.", e);
    }
    return new ResponseEntity<>(savedVideo, HttpStatus.CREATED);
  }

  /**
   * Sends HTTP status Response Entity with the Video entry that has been updated.
   * Sends HTTP status Not Found if the Video cannot be found.
   * Sends HTTP status Invalid Value if the Video to be posted is invalid.
   * @param id the id of the Video to be updated.
   * @param video the Video to be updated.
   * @return the Response Entity with a Status Code with a body.
   */
  @PatchMapping(value = "/{id}")
  public ResponseEntity<Video> updateVideo (@PathVariable ("id") String id, @RequestBody Video video) {
    Video updatedVideo;
    try {
      updatedVideo = videoService.update(id, video);
    } catch (VideoNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PATCH: Video Not Found", e);
    } catch ( VideoInvalidURLException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid URL.", e);
    } catch (VideoInvalidTitleException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid title.", e);
    } catch (VideoInvalidLengthException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid length.", e);
    } catch (VideoInvalidTeacherURLException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid teacher URL.", e);
    } catch (VideoInvalidTeacherImageURLException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid teacher image URL.", e);
    } catch (VideoInvalidTranscriptException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid transcript.", e);
    } catch (VideoInvalidSummaryException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid summary.", e);
    } catch (VideoInvalidThumbnailException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Video invalid thumbnail.", e);
    }
    return new ResponseEntity<>(updatedVideo, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with the Video entry that has been deleted.
   * Sends HTTP status Not Found if there is no entry of the provided id.
   * @param id the id of the Video to be deleted.
   * @return the Response Entity with a Status Code with a body.
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<HttpStatus> deleteVideo (@PathVariable("id") String id) {
    try {
      videoService.delete(id);
    }
    catch (VideoNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DELETE: Video Not Found", e);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
