package org.scoalaonline.api.service;

import org.scoalaonline.api.exception.grade.GradeInvalidValueException;
import org.scoalaonline.api.exception.grade.GradeNotFoundException;
import org.scoalaonline.api.model.Grade;
import org.scoalaonline.api.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains the Grade related logic needed for the API
 */
@Service
public class GradeService implements ServiceInterface<Grade> {
  @Autowired
  GradeRepository gradeRepository;

  /**
   * Retrieves a list of all Grade entries found in the DB
   * @return the list of Grade entries
   */
  @Override
  public List<Grade> getAll() {
    return gradeRepository.findAll();
  }

  /**
   * Retrieves one Grade entry with the given id from the DB
   * or throws an error if no entry with that id is found.
   * @param id - id of the Grade entry
   * @return the Grade entry
   * @throws GradeNotFoundException
   */
  @Override
  public Grade getOneById(String id) throws GradeNotFoundException {
    return gradeRepository.findById(id).orElseThrow(
      () -> new GradeNotFoundException("Method getOneById: Grade not found")
    );
  }

  /**
   * Adds a Grade entry in the DB based on the received object.
   * @param entry
   * @throws GradeInvalidValueException
   * @return the Grade object that has been saved in the DB
   */
  @Override
  public Grade add(Grade entry) throws GradeInvalidValueException {
    Grade gradeToSave = new Grade();
    if(entry.getValue() > 0 && entry.getValue() < 13) {
      gradeToSave.setValue(entry.getValue());
    } else
      throw new GradeInvalidValueException("Method add: Value field has to be an integer between 0 and 13");

    return gradeRepository.save(gradeToSave);
  }
  /**
   * Updates the Grade entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found or if the edited value is invalid.
   * @param id - the id of the entry to update
   * @param entry
   * @return the Grade object saved in the DB
   * @throws GradeNotFoundException
   * @throws GradeInvalidValueException
   */
  @Override
  public Grade update(String id, Grade entry) throws GradeInvalidValueException, GradeNotFoundException {
    Grade gradeToUpdate = gradeRepository.findById(id).orElseThrow(
      () -> new GradeNotFoundException("Method update: Grade not found")
    );
    if(entry.getValue() > 0 && entry.getValue() < 13) {
      gradeToUpdate.setValue(entry.getValue());
    } else {
      throw new GradeInvalidValueException("Method update: Value field has to be an integer between 0 and 11 ");
    }
    return gradeRepository.save(gradeToUpdate);
  }

  /**
   * Deletes the Grade entry with the given id or throws an exception if no
   * entry with that id can be found
   * @param id
   * @throws GradeNotFoundException
   */
  @Override
  public void delete(String id) throws GradeNotFoundException {
    if(gradeRepository.findById(id).isPresent())
      gradeRepository.deleteById(id);
    else
      throw new GradeNotFoundException("Method delete: Grade not found");
  }
}
