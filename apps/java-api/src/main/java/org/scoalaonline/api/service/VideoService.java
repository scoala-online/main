package org.scoalaonline.api.service;

import org.scoalaonline.api.exception.subject.SubjectNotFoundException;
import org.scoalaonline.api.exception.video.*;
import org.scoalaonline.api.model.Video;
import org.scoalaonline.api.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains the Video related logic needed for the API
 */
@Service
public class VideoService implements ServiceInterface<Video>{
  @Autowired
  VideoRepository videoRepository;

  /**
   * Retrieves a list of all Video entries found in the DB.
   * @return the list of Video entries.
   */
  @Override
  public List<Video> getAll() { return videoRepository.findAll(); }

  /**
   * Retrieves one Video entry with the given id from the DB.
   * Throws an exception if no entry with that id is found.
   * @param id - id of the Video entry.
   * @return the Video entry.
   * @throws VideoNotFoundException when the Video entry has not been found.
   */
  @Override
  public Video getOneById( String id ) throws VideoNotFoundException {
    return videoRepository.findById(id).orElseThrow(
      () -> new VideoNotFoundException("Method getOneById: Video not found")
    );
  }

  /**
   * Adds a Video entry into the DB based on the received object.
   * Throws an exception if the value is invalid.
   * @param entry the Video entry.
   * @return the Video entry that has been saved in the db.
   * @throws VideoInvalidURLException when the videoURL entry is invalid.
   * @throws VideoInvalidTitleException when the videoTitle entry is invalid.
   * @throws VideoInvalidLengthException when the videoLength entry is invalid.
   * @throws VideoInvalidTeacherURLException when the teacherURL entry is invalid.
   * @throws VideoInvalidTeacherImageURLException when the teacherImageURL entry is invalid.
   * @throws VideoInvalidTranscriptException when the transcript entry is invalid.
   * @throws VideoInvalidSummaryException when the summary entry is invalid.
   * @throws VideoInvalidThumbnailException when the thumbnail entry is invalid.
   */
  @Override
  public Video add(Video entry) throws VideoInvalidURLException, VideoInvalidTitleException, VideoInvalidLengthException, VideoInvalidTeacherURLException, VideoInvalidTeacherImageURLException, VideoInvalidTranscriptException, VideoInvalidSummaryException, VideoInvalidThumbnailException {
    Video videoToSave = new Video();

    if (entry.getVideoURL() != null && !entry.getVideoURL().equals(""))
      videoToSave.setVideoURL(entry.getVideoURL());
    else
      throw new VideoInvalidURLException("Method add: Video URL field can't be null.");

    if (entry.getVideoTitle() != null && !entry.getVideoTitle().equals(""))
      videoToSave.setVideoTitle(entry.getVideoTitle());
    else
      throw new VideoInvalidTitleException("Method add: Video Title field can't be null.");

    if (entry.getVideoLength() != null && !entry.getVideoLength().isZero() && !entry.getVideoLength().isNegative())
      videoToSave.setVideoLength(entry.getVideoLength());
    else
      throw new VideoInvalidLengthException("Method add: Video Length field can't be null.");

    if(entry.getTeacherURL() != null && !entry.getTeacherURL().equals(""))
      videoToSave.setTeacherURL(entry.getTeacherURL());
    else
      throw new VideoInvalidTeacherURLException("Method add: Video TeacherURL field can't be null.");

    if (entry.getTeacherImageURL() != null && !entry.getTeacherImageURL().equals(""))
      videoToSave.setTeacherImageURL(entry.getTeacherImageURL());
    else
      throw new VideoInvalidTeacherImageURLException("Method add: Video TeacherImageURL field can't be null.");

    if (entry.getTranscript() != null && !entry.getTranscript().equals(""))
      videoToSave.setTranscript(entry.getTranscript());
    else
      throw new VideoInvalidTranscriptException("Method add: Video Transcript field can't be null.");

    if (entry.getSummary() != null && !entry.getSummary().equals(""))
      videoToSave.setSummary(entry.getSummary());
    else
      throw new VideoInvalidSummaryException("Method add: Video Summary field can't be null.");

    if (entry.getThumbnail() != null && !entry.getThumbnail().equals(""))
      videoToSave.setThumbnail(entry.getThumbnail());
    else
      throw new VideoInvalidThumbnailException("Method add: Video Thumbnail field can't be null.");

    return videoRepository.save(videoToSave);
  }

  /**
   * Updates the Video entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found.
   * Throws an exception if any of its parameters is invalid.
   * @param id the id of the entry to be updated.
   * @param entry the Video entry to be updated.
   * @return the updated Video saved in the db.
   * @throws VideoNotFoundException when the Video entry has not been found.
   * @throws VideoInvalidURLException when the videoURL entry is invalid.
   * @throws VideoInvalidTitleException when the videoTitle entry is invalid.
   * @throws VideoInvalidLengthException when the videoLength entry is invalid.
   * @throws VideoInvalidTeacherURLException when the teacherURL entry is invalid.
   * @throws VideoInvalidTeacherImageURLException when the teacherImageURL entry is invalid.
   * @throws VideoInvalidTranscriptException when the transcript entry is invalid.
   * @throws VideoInvalidSummaryException when the summary entry is invalid.
   * @throws VideoInvalidThumbnailException when the thumbnail entry is invalid.
   */
  @Override
  public Video update(String id, Video entry) throws VideoNotFoundException, VideoInvalidURLException, VideoInvalidTitleException, VideoInvalidLengthException, VideoInvalidTeacherURLException, VideoInvalidTeacherImageURLException, VideoInvalidTranscriptException, VideoInvalidSummaryException, VideoInvalidThumbnailException {
    Video videoToUpdate = videoRepository.findById(id).orElseThrow(
      () -> new VideoNotFoundException("Method update: Video not found")
    );

    if (entry.getVideoURL() != null && !entry.getVideoURL().equals(""))
      videoToUpdate.setVideoURL(entry.getVideoURL());
    else
      throw new VideoInvalidURLException("Method add: Video URL field can't be null.");

    if (entry.getVideoTitle() != null && !entry.getVideoTitle().equals(""))
      videoToUpdate.setVideoTitle(entry.getVideoTitle());
    else
      throw new VideoInvalidTitleException("Method add: Video Title field can't be null.");

    if (entry.getVideoLength() != null && !entry.getVideoLength().isZero() && !entry.getVideoLength().isNegative())
      videoToUpdate.setVideoLength(entry.getVideoLength());
    else
      throw new VideoInvalidLengthException("Method add: Video Length field can't be null.");

    if(entry.getTeacherURL() != null && !entry.getTeacherURL().equals(""))
      videoToUpdate.setTeacherURL(entry.getTeacherURL());
    else
      throw new VideoInvalidTeacherURLException("Method add: Video TeacherURL field can't be null.");

    if (entry.getTeacherImageURL() != null && !entry.getTeacherImageURL().equals(""))
      videoToUpdate.setTeacherImageURL(entry.getTeacherImageURL());
    else
      throw new VideoInvalidTeacherImageURLException("Method add: Video TeacherImageURL field can't be null.");

    if (entry.getTranscript() != null && !entry.getTranscript().equals(""))
      videoToUpdate.setTranscript(entry.getTranscript());
    else
      throw new VideoInvalidTranscriptException("Method add: Video Transcript field can't be null.");

    if (entry.getSummary() != null && !entry.getSummary().equals(""))
      videoToUpdate.setSummary(entry.getSummary());
    else
      throw new VideoInvalidSummaryException("Method add: Video Summary field can't be null.");

    if (entry.getThumbnail() != null && !entry.getThumbnail().equals(""))
      videoToUpdate.setThumbnail(entry.getThumbnail());
    else
      throw new VideoInvalidThumbnailException("Method add: Video Thumbnail field can't be null.");

    return videoRepository.save(videoToUpdate);
  }

  /**
   * Deletes the Video entry with the given id from the db.
   * Throws an exception if no entry with that id can be found.
   * @param id the id of the entry to be deleted.
   * @throws VideoNotFoundException when the Video entry has not been found.
   */
  @Override
  public void delete(String id) throws VideoNotFoundException {
    if(videoRepository.findById(id).isPresent())
      videoRepository.deleteById(id);
    else
      throw new VideoNotFoundException("Method delete: Video Not Found");
  }
}
