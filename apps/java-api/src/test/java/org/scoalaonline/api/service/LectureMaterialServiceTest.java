package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.repository.LectureMaterialRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureMaterialServiceTest {

  @Mock
  private LectureMaterialRepository lectureMaterialRepository;
  private LectureMaterialService underTestService;

  @BeforeEach
  void setUp() {
    underTestService = new LectureMaterialService(lectureMaterialRepository);
  }


  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(lectureMaterialRepository).findAll();
  }

  @Test
  @Disabled
  void getOneByIdTest() throws LectureMaterialNotFoundException {
    // when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new LectureMaterial("id", "Document.pdf")));

    // then
    LectureMaterial lectureMaterial = underTestService.getOneById("id");

    Assertions.assertEquals("Document.pdf", lectureMaterial.getDocument());
  }

  @Test
  @Disabled
  void add() {
  }

  @Test
  @Disabled
  void update() {
  }

  @Test
  @Disabled
  void delete() {
  }
}
