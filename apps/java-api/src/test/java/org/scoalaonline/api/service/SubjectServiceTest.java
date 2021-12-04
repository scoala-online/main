package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.subject.SubjectInvalidValueException;
import org.scoalaonline.api.exception.subject.SubjectNotFoundException;
import org.scoalaonline.api.model.Subject;
import org.scoalaonline.api.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

  @InjectMocks
  private SubjectService underTestService;

  @Mock
  private SubjectRepository subjectRepository;

  /**
   * Executes the getAll() method from SubjectService class.
   * Asserts that it correctly called the findAll() method
   * from the repository.
   */
  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(subjectRepository).findAll();
  }

  /**
   * Arranges the existence of a custom Subject object in database.
   * Executes the getOneById( @param ) method from SubjectService class.
   * Asserts that it finds the same object arranged in the former step.
   * @throws SubjectNotFoundException
   */
  @Test
  void getOneByIdTest() throws SubjectNotFoundException {
    // when
    when(subjectRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new Subject("id", "Value")));

    // then
    Subject subject = underTestService.getOneById("id");

    Assertions.assertEquals("Value", subject.getValue());
  }

  /**
   * Arranges the absence of any Subject object in database.
   * Executes the getOneById( @param ) method from SubjectService class.
   * Asserts that it throws the SubjectNotFoundException exception
   * and the related message.
   */
  @Test
  void getOneByIdExceptionTest() {
    //when
    when(subjectRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(SubjectNotFoundException.class)
      .hasMessageContaining("Method getOneById: Subject not found");
  }

  /**
   * Arranges the creation of a custom Subject object.
   * Executes the add( Subject @param) method
   * from SubjectService class.
   * Asserts that a Subject object has been added to the database
   * and it has the same attribute values as the one created previously.
   * @throws SubjectInvalidValueException
   */
  @Test
  void addTest() throws SubjectInvalidValueException {
    // given
    Subject subject = new Subject("string_id", "Some_Value");

    // when
    underTestService.add(subject);

    // then
    ArgumentCaptor<Subject> subjectArgumentCaptor =
      ArgumentCaptor.forClass(Subject.class);

    verify(subjectRepository).save(subjectArgumentCaptor.capture());

    Subject capturedSubject = subjectArgumentCaptor.getValue();
    assertThat(capturedSubject.getValue()).isEqualTo(subject.getValue());
  }

  /**
   * Arranges the creation of two Subject objects
   * with invalid attribute values.
   * Executes the add( Subject @param ) method
   * from SubjectService class.
   * Asserts that nothing was saved in the database and
   * it throws SubjectInvalidValueException exception
   * with the related messages.
   */
  @Test
  void addSubjectInvalidDataExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Subject subject = new Subject("id", (String) exceptionCase);
      // when & then
      assertThatThrownBy(() -> underTestService.add(subject))
        .isInstanceOf(SubjectInvalidValueException.class)
        .hasMessageContaining("Method add: Value field can't be invalid");

      verify(subjectRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Subject objects:
   * 1. The object already present in the database.
   * 2. The object that will update the former.
   * Arranges the existence of the first object in the database.
   * Executes the update( @param ,Subject @param ) method
   * from SubjectService class.
   * Asserts that a Subject object has been saved to the database
   * and that it has the same attribute values as the one we want to update with.
   * @throws SubjectNotFoundException
   * @throws SubjectInvalidValueException
   */
  @Test
  void updateTest() throws SubjectNotFoundException, SubjectInvalidValueException {
    // given
    Subject subject = new Subject("id","Value");
    Subject updatedSubject = new Subject(anyString(), "Value2");

    // when
    when(subjectRepository.findById(subject.getId()))
      .thenReturn(Optional.of(subject));

    // then
    underTestService.update(subject.getId(), updatedSubject);

    ArgumentCaptor<Subject> subjectArgumentCaptor =
      ArgumentCaptor.forClass(Subject.class);

    verify(subjectRepository).save(subjectArgumentCaptor.capture());

    Subject capturedSubject = subjectArgumentCaptor.getValue();
    assertThat(capturedSubject.getValue()).isEqualTo(subject.getValue());

    verify(subjectRepository).findById(subject.getId());

  }

  /**
   * Arranges the creation of a Subject object we will try to
   * update with and makes sure the database has no entries to update.
   * Executes the update( @param , Subject @param ) method
   * from SubjectService class.
   * Asserts that the SubjectNotFoundException exception
   * is thrown with the related messages.
   */
  @Test
  void updateSubjectNotFoundExceptionTest() {
    // given
    Subject subject = new Subject("ID","Value");

    // when
    // Subject Not Found Exception
    when(subjectRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.update(anyString(), subject))
      .isInstanceOf(SubjectNotFoundException.class)
      .hasMessageContaining("Method update: Subject not found");

    verify(subjectRepository, never()).save(any());
  }
  /**
   * Arranges the creation of a Subject object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Subject @param ) method
   * from SubjectService class.
   * Asserts that the SubjectInvalidValueException exception
   * is thrown with the related messages.
   */
  @Test
  void updateSubjectInvalidDataExceptionTest() {
    // when
    when(subjectRepository.findById(anyString()))
      .thenReturn(Optional.of(new Subject("id","Value")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Subject subject = new Subject("id", (String) exceptionCase);

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",subject))
        .isInstanceOf(SubjectInvalidValueException.class)
        .hasMessageContaining("Method update: Value field can't be invalid");

      verify(subjectRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Subject object and
   * makes sure of its existence in the database.
   * Executes the DELETE( @param ) method from SubjectService class.
   * Asserts that the deleteById( @param ) method from repository has been
   * executed successfully.
   * @throws SubjectNotFoundException
   */
  @Test
  void deleteTest() throws SubjectNotFoundException {
    // given
    Subject subject = new Subject("id","Value");

    // when
    when(subjectRepository.findById(subject.getId()))
      .thenReturn(Optional.of(subject));

    // then
    underTestService.delete(subject.getId());

    verify(subjectRepository).deleteById(subject.getId());
  }

  /**
   * Arranges the absence of any Subject object in database.
   * Executes the delete( @param ) method from SubjectService class.
   * Asserts that it throws the SubjectNotFoundException exception
   * and the related message.
   */
  @Test
  void deleteSubjectNotFoundExceptionTest() {

    // when
    when(subjectRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.delete(anyString()))
      .isInstanceOf(SubjectNotFoundException.class)
      .hasMessageContaining("Method delete: Subject not Found");

    verify(subjectRepository, never()).delete(any());
  }
}
