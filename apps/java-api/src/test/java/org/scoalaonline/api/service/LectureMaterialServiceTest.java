package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.LectureMaterialInvalidDocumentException;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.repository.LectureMaterialRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

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
  void getOneByIdTest() throws LectureMaterialNotFoundException {
    // when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new LectureMaterial("id", "Document.pdf")));

    // then
    LectureMaterial lectureMaterial = underTestService.getOneById("id");

    Assertions.assertEquals("Document.pdf", lectureMaterial.getDocument());
  }

  @Test
  void getOneByIdExceptionTest() {
    //when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(LectureMaterialNotFoundException.class)
      .hasMessageContaining("Method getOneById: Lecture Material not found");
  }

  @Test
  void addTest() throws LectureMaterialInvalidDocumentException {
    // given
    LectureMaterial lectureMaterial = new LectureMaterial("string_id", "Some_Document.pdf");

    // when
    underTestService.add(lectureMaterial);

    // then
    ArgumentCaptor<LectureMaterial> lectureMaterialArgumentCaptor =
      ArgumentCaptor.forClass(LectureMaterial.class);

    verify(lectureMaterialRepository).save(lectureMaterialArgumentCaptor.capture());

    LectureMaterial capturedLectureMaterial = lectureMaterialArgumentCaptor.getValue();
    assertThat(capturedLectureMaterial.getDocument()).isEqualTo(lectureMaterial.getDocument());
  }
  // Other way of add exception

//    @Test
//    public void savedCustomer_Success() {
//        LectureMaterial lectureMaterial = new LectureMaterial("id","Document.pdf");
//
//        // providing knowledge
//        when(lectureMaterialRepository.save(any(LectureMaterial.class))).thenReturn(lectureMaterial);
//
//        LectureMaterial savedLectureMaterial = lectureMaterialRepository.save(lectureMaterial);
//        assertThat(savedLectureMaterial.getDocument()).isNotNull();
//    }

  @Test
  void addLectureMaterialInvalidDataExceptionTest() {
    // given
    LectureMaterial lectureMaterialEmpty = new LectureMaterial("id", "");
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    //when
    //then
    assertThatThrownBy(() -> underTestService.add(lectureMaterialEmpty))
      .isInstanceOf(LectureMaterialInvalidDocumentException.class)
      .hasMessageContaining("Method add: Document field can't be null.");

    verify(lectureMaterialRepository, never()).save(any());

    LectureMaterial lectureMaterialNull = new LectureMaterial("id", null);
    assertThatThrownBy(() -> underTestService.add(lectureMaterialNull))
      .isInstanceOf(LectureMaterialInvalidDocumentException.class)
      .hasMessageContaining("Method add: Document field can't be null.");

    verify(lectureMaterialRepository, never()).save(any());
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
