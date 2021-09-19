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
import org.springframework.beans.factory.annotation.Autowired;

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

  /**
   * Executes the getAll() method from LectureMaterialService class.
   * Asserts that it correctly called the findAll() method
   * from the repository.
   */
  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(lectureMaterialRepository).findAll();
  }

  /**
   * Arranges the existence of an custom LectureMaterial object in database.
   * Executes the getOneById( @param ) method from LectureMaterialService class.
   * Asserts that it finds the same object arranged in the former step.
   * @throws LectureMaterialNotFoundException
   */
  @Test
  void getOneByIdTest() throws LectureMaterialNotFoundException {
    // when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new LectureMaterial("id", "Document.pdf")));

    // then
    LectureMaterial lectureMaterial = underTestService.getOneById("id");

    Assertions.assertEquals("Document.pdf", lectureMaterial.getDocument());
  }

  /**
   * Arranges the absence of any LectureMaterial object in database.
   * Executes the getOneById( @param ) method from LectureMaterialService class.
   * Asserts that it throws the LectureMaterialNotFoundException exception
   * and the related message.
   */
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

  /**
   * Arranges the creation of a custom LectureMaterial object.
   * Executes the add( LectureMaterial @param) method
   * from LectureMaterialService class.
   * Asserts that a LectureMaterial object has been added to the database
   * and it has the same attribute values as the one created previously.
   * @throws LectureMaterialInvalidDocumentException
   */
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

  /**
   * Arranges the creation of two LectureMaterial object
   * with invalid attribute values.
   * Executes the add( LectureMaterial @param ) method
   * from LectureMaterialService class.
   * Asserts that nothing was saved in the database and
   * it throws LectureMaterialInvalidDocumentException exception
   * with the related messages.
   */
  @Test
  void addLectureMaterialInvalidDataExceptionTest() {
    // given
    LectureMaterial lectureMaterialEmpty = new LectureMaterial("id", "");
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    //TODO Resolve repetitive code here
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

  /**
   * Arranges the creation of two LectureMaterial objects:
   * 1. The object already present in the database.
   * 2. The object that will update the former.
   * Arranges the existence of the first object in the database.
   * Executes the update( @param ,LectureMaterial @param ) method
   * from LectureMaterialSevice class.
   * Asserts that a LectureMaterial object has been added to the database
   * and it has the same attribute values as the one we want to update with.
   * @throws LectureMaterialNotFoundException
   * @throws LectureMaterialInvalidDocumentException
   */
  @Test
  void updateTest() throws LectureMaterialNotFoundException, LectureMaterialInvalidDocumentException {
    // given
    LectureMaterial lectureMaterial = new LectureMaterial("id","Document.pdf");
    LectureMaterial updatedLectureMaterial = new LectureMaterial(anyString(), "Document.docs");

    // when
    when(lectureMaterialRepository.findById(lectureMaterial.getId()))
      .thenReturn(Optional.of(lectureMaterial));

    // then
    underTestService.update(lectureMaterial.getId(), updatedLectureMaterial);

    ArgumentCaptor<LectureMaterial> lectureMaterialArgumentCaptor =
      ArgumentCaptor.forClass(LectureMaterial.class);

    verify(lectureMaterialRepository).save(lectureMaterialArgumentCaptor.capture());

    LectureMaterial capturedLectureMaterial = lectureMaterialArgumentCaptor.getValue();
    assertThat(capturedLectureMaterial.getDocument()).isEqualTo(lectureMaterial.getDocument());

    verify(lectureMaterialRepository).findById(lectureMaterial.getId());

  }

  /**
   * Arranges the creation of an LectureMaterial object we will try to
   * update with and makes sure the database has no entries.
   * Executes the update( @param , LectureMaterial @param ) method
   * from LectureMaterialService class.
   * Asserts that the LectureMaterialNotFoundException exception
   * is thrown with the related messages.
   */
  @Test
  void updateLectureMaterialNotFoundExceptionTest() {
    // given
    LectureMaterial lectureMaterial = new LectureMaterial("ID","Document.pdf");

    // when
    // Lecture Material Not Found Exception
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.update(anyString(), lectureMaterial))
      .isInstanceOf(LectureMaterialNotFoundException.class)
      .hasMessageContaining("Method update: Lecture Material not found");

    verify(lectureMaterialRepository, never()).delete(any());
  }
  /**
   * Arranges the creation of an LectureMaterial object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , LectureMaterial @param ) method
   * from LectureMaterialService class.
   * Asserts that the LectureMaterialInvalidDocumentException exception
   * is thrown with the related messages.
   */
  @Test
  void updateLectureMaterialInvalidDataExceptionTest() {
    // when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(Optional.of(new LectureMaterial("id","Document.pdf")));

    //Lecture material invalid data exception
    // 1. Null
    // given
    LectureMaterial lectureMaterialNull = new LectureMaterial("id", null);

    // then
    assertThatThrownBy(() -> underTestService.update("id",lectureMaterialNull))
      .isInstanceOf(LectureMaterialInvalidDocumentException.class)
      .hasMessageContaining("Method update: Document Field Can't Be Null");

    verify(lectureMaterialRepository, never()).save(any());
    // 2.Empty string
    // given
    LectureMaterial lectureMaterialEmpty = new LectureMaterial("id", "");

    // then
    assertThatThrownBy(() -> underTestService.update("id",lectureMaterialEmpty))
      .isInstanceOf(LectureMaterialInvalidDocumentException.class)
      .hasMessageContaining("Method update: Document Field Can't Be Null");

    verify(lectureMaterialRepository, never()).save(any());
  }

  /**
   * Arranges the creation of an LectureMaterial object and
   * makes sure its existence in the database.
   * Executes the delete( @param ) method from LectureMaterialService class.
   * Asserts that the deleteById( @param ) method from repository has been
   * executed successfully.
   * @throws LectureMaterialNotFoundException
   */
  @Test
  void delete() throws LectureMaterialNotFoundException {
    // given
    LectureMaterial lectureMaterial = new LectureMaterial("id","Document.pdf");

    // when
    when(lectureMaterialRepository.findById(lectureMaterial.getId()))
      .thenReturn(Optional.of(lectureMaterial));

    // then
    underTestService.delete(lectureMaterial.getId());

    verify(lectureMaterialRepository).deleteById(lectureMaterial.getId());
  }

  /**
   * Arranges the absence of any LectureMaterial object in database.
   * Executes the delete( @param ) method from LectureMaterialService class.
   * Asserts that it throws the LectureMaterialNotFoundException exception
   * and the related message.
   */
  @Test
  void deleteException() {

    // when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.delete(anyString()))
      .isInstanceOf(LectureMaterialNotFoundException.class)
      .hasMessageContaining("Method delete: Lecture Material Not Found");

    verify(lectureMaterialRepository, never()).delete(any());
  }
}
