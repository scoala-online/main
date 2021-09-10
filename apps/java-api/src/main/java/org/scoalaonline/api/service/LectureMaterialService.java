package org.scoalaonline.api.service;

import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.repository.LectureMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LectureMaterialService implements ServiceInterface<LectureMaterial>{

  @Autowired
  LectureMaterialRepository lectureMaterialRepository;


  @Override
  public List<LectureMaterial> getAll() {
    return lectureMaterialRepository.findAll();
  }

  @Override
  public Optional<LectureMaterial> getOneById(String id) {
    return lectureMaterialRepository.findById(id);
  }

  @Override
  public LectureMaterial add(LectureMaterial entry) {
    LectureMaterial lectureMaterialToSave = new LectureMaterial();
    lectureMaterialToSave.setDocument(entry.getDocument());

    return lectureMaterialRepository.save(lectureMaterialToSave);
  }

  @Override
  public LectureMaterial update(String id, LectureMaterial object) {
    LectureMaterial lectureMaterialToUpdate = lectureMaterialRepository.findById(id).get();
    if(object.getDocument() != null) {
      object.setDocument(object.getDocument());
    }
    return lectureMaterialRepository.save(lectureMaterialToUpdate);
  }

  @Override
  public void delete(String id) {
    lectureMaterialRepository.deleteById(id);
  }
}
