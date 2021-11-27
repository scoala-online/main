package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.grade.GradeInvalidValueException;
import org.scoalaonline.api.exception.grade.GradeNotFoundException;
import org.scoalaonline.api.model.Grade;
import org.scoalaonline.api.repository.GradeRepository;

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
class GradeServiceTest {

  @InjectMocks
  private GradeService underTestService;

  @Mock
  private GradeRepository gradeRepository;

  /**
   * Executes the getAll() method from GradeService class.
   * Asserts that it correctly called the findAll() method
   * from the repository.
   */
  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(gradeRepository).findAll();
  }

  /**
   * Arranges the existence of a custom Grade object in database.
   * Executes the getOneById( @param ) method from GradeService class.
   * Asserts that it finds the same object arranged in the former step.
   * @throws GradeNotFoundException
   */
  @Test
  void getOneByIdTest() throws GradeNotFoundException {
    // when
    when(gradeRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new Grade("id", 0)));

    // then
    Grade grade = underTestService.getOneById("id");

    Assertions.assertEquals(0, grade.getValue());
  }

  /**
   * Arranges the absence of any Grade object in database.
   * Executes the getOneById( @param ) method from GradeService class.
   * Asserts that it throws the GradeNotFoundException exception
   * and the related message.
   */
  @Test
  void getOneByIdExceptionTest() {
    //when
    when(gradeRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(GradeNotFoundException.class)
      .hasMessageContaining("Method getOneById: Grade not found.");
  }

  /**
   * Arranges the creation of a custom Grade object.
   * Executes the add( Grade @param) method
   * from GradeService class.
   * Asserts that a Grade object has been added to the database
   * and it has the same attribute values as the one created previously.
   * @throws GradeInvalidValueException
   */
  @Test
  void addTest() throws GradeInvalidValueException {
    // given
    Grade grade = new Grade("string_id", 0);

    // when
    underTestService.add(grade);

    // then
    ArgumentCaptor<Grade> gradeArgumentCaptor =
      ArgumentCaptor.forClass(Grade.class);

    verify(gradeRepository).save(gradeArgumentCaptor.capture());

    Grade capturedGrade = gradeArgumentCaptor.getValue();
    assertThat(capturedGrade.getValue()).isEqualTo(grade.getValue());
  }

  /**
   * Arranges the creation of two Grade objects
   * with invalid attribute values.
   * Executes the add( Grade @param ) method
   * from GradeService class.
   * Asserts that nothing was saved in the database and
   * it throws GradeInvalidValueException exception
   * with the related messages.
   */
  @Test
  void addGradeInvalidDataExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(-1);
    exceptionCases.add(14);
    for(Object exceptionCase : exceptionCases){
      // given
      Grade grade = new Grade("id", (Integer) exceptionCase);
      // when & then
      assertThatThrownBy(() -> underTestService.add(grade))
        .isInstanceOf(GradeInvalidValueException.class)
        .hasMessageContaining("Method add: Value field has to be an integer between 0 and 13.");

      verify(gradeRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Grade objects:
   * 1. The object already present in the database.
   * 2. The object that will update the former.
   * Arranges the existence of the first object in the database.
   * Executes the update( @param ,Grade @param ) method
   * from GradeService class.
   * Asserts that a Grade object has been saved to the database
   * and that it has the same attribute values as the one we want to update with.
   * @throws GradeNotFoundException
   * @throws GradeInvalidValueException
   */
  @Test
  void updateTest() throws GradeNotFoundException, GradeInvalidValueException {
    // given
    Grade grade = new Grade("id",0);
    Grade updatedGrade = new Grade(anyString(), 1);

    // when
    when(gradeRepository.findById(grade.getId()))
      .thenReturn(Optional.of(grade));

    // then
    underTestService.update(grade.getId(), updatedGrade);

    ArgumentCaptor<Grade> gradeArgumentCaptor =
      ArgumentCaptor.forClass(Grade.class);

    verify(gradeRepository).save(gradeArgumentCaptor.capture());

    Grade capturedGrade = gradeArgumentCaptor.getValue();
    assertThat(capturedGrade.getValue()).isEqualTo(grade.getValue());

    verify(gradeRepository).findById(grade.getId());

  }

  /**
   * Arranges the creation of a Grade object we will try to
   * update with and makes sure the database has no entries to update.
   * Executes the update( @param , Grade @param ) method
   * from GradeService class.
   * Asserts that the GradeNotFoundException exception
   * is thrown with the related messages.
   */
  @Test
  void updateGradeNotFoundExceptionTest() {
    // given
    Grade grade = new Grade("ID",0);

    // when
    // Grade Not Found Exception
    when(gradeRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.update(anyString(), grade))
      .isInstanceOf(GradeNotFoundException.class)
      .hasMessageContaining("Method update: Grade not found.");

    verify(gradeRepository, never()).save(any());
  }
  /**
   * Arranges the creation of a Grade object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Grade @param ) method
   * from GradeService class.
   * Asserts that the GradeInvalidValueException exception
   * is thrown with the related messages.
   */
  @Test
  void updateGradeInvalidDataExceptionTest() {
    // when
    when(gradeRepository.findById(anyString()))
      .thenReturn(Optional.of(new Grade("id",0)));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(-1);
    exceptionCases.add(14);
    for(Object exceptionCase : exceptionCases){
      // given
      Grade grade = new Grade("id", (Integer) exceptionCase);

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",grade))
        .isInstanceOf(GradeInvalidValueException.class)
        .hasMessageContaining("Method update: Value field has to be an integer between 0 and 13.");

      verify(gradeRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Grade object and
   * makes sure of its existence in the database.
   * Executes the delete( @param ) method from GradeService class.
   * Asserts that the deleteById( @param ) method from repository has been
   * executed successfully.
   * @throws GradeNotFoundException
   */
  @Test
  void delete() throws GradeNotFoundException {
    // given
    Grade grade = new Grade("id",0);

    // when
    when(gradeRepository.findById(grade.getId()))
      .thenReturn(Optional.of(grade));

    // then
    underTestService.delete(grade.getId());

    verify(gradeRepository).deleteById(grade.getId());
  }

  /**
   * Arranges the absence of any Grade object in database.
   * Executes the delete( @param ) method from GradeService class.
   * Asserts that it throws the GradeNotFoundException exception
   * and the related message.
   */
  @Test
  void deleteException() {

    // when
    when(gradeRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.delete(anyString()))
      .isInstanceOf(GradeNotFoundException.class)
      .hasMessageContaining("Method delete: Grade not found.");

    verify(gradeRepository, never()).delete(any());
  }
}
