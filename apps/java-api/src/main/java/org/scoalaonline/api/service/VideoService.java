package org.scoalaonline.api.service;

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

  @Override
  public List<Video> getAll() { return videoRepository.findAll(); }

  @Override
  public Video getOneById( String id ) throws VideoNotFoundException {
    return videoRepository.findById(id).orElseThrow(
      () -> new VideoNotFoundException("Method getOneById: Video not found")
    );
  }

  @Override
  public Video add(Video entry) throws VideoInvalidURLException, VideoInvalidTitleException, VideoInvalidLengthException, VideoInvalidTeacherURLException, VideoInvalidTeacherImageURLException, VideoInvalidTranscriptException, VideoInvalidSummaryException, VideoInvalidThumbnailException {
    Video videoToSave = new Video();

    if (entry.getVideoURL() != null && !entry.getVideoURL().equals(""))
      videoToSave.setVideoURL(entry.getVideoURL());
    else
      throw new VideoInvalidURLException("Method add: Video URL field can't be null.");

    if (entry.getVideoTitle() != null && !entry.getVideoTitle().equals(""))
      videoToSave.setVideoURL(entry.getVideoTitle());
    else
      throw new VideoInvalidTitleException("Method add: Video Title field can't be null.");

    if (entry.getVideoLength() != null && !entry.getVideoLength().equals(""))
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
      videoToUpdate.setVideoURL(entry.getVideoTitle());
    else
      throw new VideoInvalidTitleException("Method add: Video Title field can't be null.");

    if (entry.getVideoLength() != null && !entry.getVideoLength().equals(""))
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

  @Override
  public void delete(String id) throws VideoNotFoundException {
    if(videoRepository.findById(id).isPresent())
      videoRepository.deleteById(id);
    else
      throw new VideoNotFoundException("Method delete: Video Not Found");
  }
}
