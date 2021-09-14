package org.scoalaonline.api.service;


import org.scoalaonline.api.exception.LectureMaterialInvalidDocumentException;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.exception.SubjectInvalidValueException;
import org.scoalaonline.api.exception.SubjectNotFoundException;
import org.scoalaonline.api.model.Subject;
import org.scoalaonline.api.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains the Subject related logic needed for the API.
 */
@Service
public class SubjectService implements ServiceInterface<Subject> {

  @Autowired
  SubjectRepository subjectRepository;

  /**
   * Retrieves a list of all Subject entries found in the DB.
   * @return the list of Subject entries.
   */
  @Override
  public List<Subject> getAll() {
    return subjectRepository.findAll();
  }

  /**
   * Retrieves one Subject entry with the given id from the DB.
   * Throws an exception if no entry with that id is found.
   * @param id - id of the Subject entry.
   * @return the Subject entry.
   * @throws SubjectNotFoundException when the Subject entry has not been found.
   */
  @Override
  public Subject getOneById(String id) throws SubjectNotFoundException {
    return subjectRepository.findById(id).orElseThrow(
      () -> new SubjectNotFoundException("Method getOneById: Subject not found")
    );
  }

  /**
   * Adds a Subject entry into the DB based on the received object.
   * Throws an exception if the value is invalid.
   * @param entry the Subject entry.
   * @return the Subject entry that has been saved into the db.
   * @throws SubjectInvalidValueException when the value entry is invalid.
   */
  @Override
  public Subject add(Subject entry) throws SubjectInvalidValueException {
    Subject subject = new Subject();
    if (entry.getValue() != null && !entry.getValue().equals(""))
      subject.setValue(entry.getValue());
    else
      throw new SubjectInvalidValueException("Method add: Value field can't be invalid");

    return subjectRepository.save(subject);
  }

  /**
   * Updates the Subject entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found.
   * Throws an exception if the Value is invalid.
   * @param id the id of the entry to be updated.
   * @param entry the Subject entry to be updated.
   * @return the updated Subject saved in the db.
   * @throws SubjectNotFoundException when the Subject entry has not been found.
   * @throws SubjectInvalidValueException when the Value entry is invalid.
   */
  @Override
  public Subject update(String id, Subject entry) throws SubjectNotFoundException, SubjectInvalidValueException {
    Subject subjectToUpdate = subjectRepository.findById(id).orElseThrow(
      () -> new SubjectNotFoundException("Method update: Subject not found")
    );
    if (entry.getValue() != null && !entry.getValue().equals("")) {
      subjectToUpdate.setValue(entry.getValue());
    } else {
      throw new SubjectInvalidValueException("Method update: Value field can't be invalid");
    }

    return subjectRepository.save(subjectToUpdate);
  }

  /**
   * Deletes the Subject entry with the given id from the db.
   * Throws an exception if no entry with that id can be found.
   * @param id the id of the entry to be deleted.
   * @throws SubjectNotFoundException when the Subject entry has not been found.
   */
  @Override
  public void delete(String id) throws SubjectNotFoundException {
    if (subjectRepository.findById(id).isPresent()) {
      subjectRepository.deleteById(id);
    } else {
      throw new SubjectNotFoundException("Method delete: Subject not Found");
    }
  }
}
