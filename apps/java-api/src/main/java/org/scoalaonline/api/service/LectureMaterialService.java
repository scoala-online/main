package org.scoalaonline.api.service;

import org.scoalaonline.api.exception.LectureMaterialInvalidDataException;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.repository.LectureMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Contains the Lecture Material related logic needed for the API
 */
@Service
public class LectureMaterialService implements ServiceInterface<LectureMaterial>{

  @Autowired
  LectureMaterialRepository lectureMaterialRepository;
  /**
   * Retrieves a list of all Lecture Material entries found in the DB
   * @return the list of Lecture Material entries
   */
  @Override
  public List<LectureMaterial> getAll() {
    return lectureMaterialRepository.findAll();
  }

  /**
   * Retrieves one Lecture Material entry with the given id from the DB
   * or throws an error if no entry with that id is found.
   * @param id - id of the Lecture Material entry
   * @return the Lecture Material entry
   * @throws LectureMaterialNotFoundException
   */
  @Override
  public Optional<LectureMaterial> getOneById(String id) throws LectureMaterialNotFoundException {
    if(lectureMaterialRepository.findById(id).isEmpty())
      throw new LectureMaterialNotFoundException("Method getOneById: Lecture Material Not Found");
    return lectureMaterialRepository.findById(id);
  }
  /**
   * Adds a Lecture Material entry in the DB based on the received object.
   * @param entry
   * @throws LectureMaterialInvalidDataException
   * @return the Lecture Material object that has been saved in the DB
   */
  @Override
  public LectureMaterial add(LectureMaterial entry) throws LectureMaterialInvalidDataException{
    LectureMaterial lectureMaterialToSave = new LectureMaterial();
    if(entry.getDocument() != null)
      lectureMaterialToSave.setDocument(entry.getDocument());
    else
      throw new LectureMaterialInvalidDataException("Method post: Document field can't be null.");

    return lectureMaterialRepository.save(lectureMaterialToSave);
  }
  /**
   * Updates the Lecture Material entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found.
   * @param id - the id of the entry to update
   * @param object
   * @return the Lecture Material object saved in the DB
   * @throws LectureMaterialNotFoundException
   * @throws LectureMaterialInvalidDataException
   */
  @Override
  public LectureMaterial update(String id, LectureMaterial object) throws LectureMaterialNotFoundException, LectureMaterialInvalidDataException {
    LectureMaterial lectureMaterialToUpdate;
    if(lectureMaterialRepository.findById(id).isPresent())
       lectureMaterialToUpdate = lectureMaterialRepository.findById(id).get();
    else
      throw new LectureMaterialNotFoundException("Method update: Lecture Material Not Found");
    if(object.getDocument() != null) {
      object.setDocument(object.getDocument());
    } else {
      throw new LectureMaterialInvalidDataException("Method update: Document Field Can't Be Null");
    }
    return lectureMaterialRepository.save(lectureMaterialToUpdate);
  }

  /**
   * Deletes the Lecture Material entry with the given id or throws an exception if no
   * entry with that id can be found
   * @param id
   * @throws LectureMaterialNotFoundException
   */
  @Override
  public void delete(String id) throws LectureMaterialNotFoundException {
    if(lectureMaterialRepository.findById(id).isPresent())
      lectureMaterialRepository.deleteById(id);
    else
      throw new LectureMaterialNotFoundException("Method delete: Lecture Material Not Found");
  }
}