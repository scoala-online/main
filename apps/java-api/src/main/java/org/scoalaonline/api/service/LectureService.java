package org.scoalaonline.api.service;


import org.scoalaonline.api.exception.LectureInvalidTitleException;
import org.scoalaonline.api.exception.LectureNotFoundException;
import org.scoalaonline.api.model.Lecture;
import org.scoalaonline.api.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains the Lecture related logic needed for the API.
 */
@Service
public class LectureService implements ServiceInterface<Lecture> {

  @Autowired
  LectureRepository lectureRepository;

  /**
   * Retrieves a list of all Lecture entries found in the DB.
   * @return the list of Lecture entries.
   */
  @Override
  public List<Lecture> getAll() {
    return lectureRepository.findAll();
  }

  /**
   * Retrieves one Lecture entry with the given id from the DB.
   * Throws an exception if no entry with that id is found.
   * @param id - the id of the Lecture entry.
   * @return the Lecture entry.
   * @throws LectureNotFoundException when the Lecture entry has not been found.
   */
  @Override
  public Lecture getOneById(String id) throws LectureNotFoundException {
    return lectureRepository.findById(id).orElseThrow(
      () -> new LectureNotFoundException("Method getOneById: Lecture not found")
    );
  }

  /**
   * Adds a Lecture entry into the DB based on the received object.
   * Throws an exception if the title is invalid.
   * @param entry the Lecture entry.
   * @return the Lecture entry that has been saved into the db.
   * @throws LectureInvalidTitleException when the title attribute is invalid.
   */
  @Override
  public Lecture add(Lecture entry) throws LectureInvalidTitleException {
    Lecture lecture = new Lecture();
    if (entry.getTitle() != null && !entry.getTitle().equals(""))
      lecture.setTitle(entry.getTitle());
    else
      throw new LectureInvalidTitleException("Method add: Title field can't be invalid");

    return lectureRepository.save(lecture);
  }

  /**
   * Updates the Lecture entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found.
   * Throws an exception if the title is invalid.
   * @param id - the id of the entry to be updated.
   * @param entry - the Lecture entry to be updated.
   * @return the updated Lecture saved in the db.
   * @throws LectureNotFoundException when the Lecture entry has not been found.
   * @throws LectureInvalidTitleException when the title attribute is invalid.
   */
  @Override
  public Lecture update(String id, Lecture entry) throws LectureNotFoundException, LectureInvalidTitleException {
    Lecture lectureToUpdate = lectureRepository.findById(id).orElseThrow(
      () -> new LectureNotFoundException("Method update: Lecture not found")
    );
    if (entry.getTitle() != null && !entry.getTitle().equals("")) {
      lectureToUpdate.setTitle(entry.getTitle());
    } else {
      throw new LectureInvalidTitleException("Method update: Title field can't be invalid");
    }

    return lectureRepository.save(lectureToUpdate);
  }

  /**
   * Deletes the Lecture entry with the given id from the db.
   * Throws an exception if no entry with that id can be found.
   * @param id - the id of the entry to be deleted.
   * @throws LectureNotFoundException when the Lecture entry has not been found.
   */
  @Override
  public void delete(String id) throws LectureNotFoundException {
    if (lectureRepository.findById(id).isPresent()) {
      lectureRepository.deleteById(id);
    } else {
      throw new LectureNotFoundException("Method delete: Lecture not found");
    }
  }
}
