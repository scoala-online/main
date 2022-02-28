package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.lecture.LectureInvalidTitleException;
import org.scoalaonline.api.exception.lecture.LectureNotFoundException;
import org.scoalaonline.api.model.Lecture;
import org.scoalaonline.api.repository.LectureRepository;

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
class LectureServiceTest {

  @InjectMocks
  private LectureService underTestService;

  @Mock
  private LectureRepository lectureRepository;

  /**
   * Executes the getAll() method from LectureService class.
   * Asserts that it correctly called the findAll() method
   * from the repository.
   */
  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(lectureRepository).findAll();
  }

  /**
   * Arranges the existence of a custom Lecture object in database.
   * Executes the getOneById( @param ) method from LectureService class.
   * Asserts that it finds the same object arranged in the former step.
   * @throws LectureNotFoundException
   */
  @Test
  void getOneByIdTest() throws LectureNotFoundException {
    // when
    when(lectureRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new Lecture("id", "Title")));

    // then
    Lecture lecture = underTestService.getOneById("id");

    Assertions.assertEquals("Title", lecture.getTitle());
  }

  /**
   * Arranges the absence of any Lecture object in database.
   * Executes the getOneById( @param ) method from LectureService class.
   * Asserts that it throws the LectureNotFoundException exception
   * and the related message.
   */
  @Test
  void getOneByIdExceptionTest() {
    //when
    when(lectureRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(LectureNotFoundException.class)
      .hasMessageContaining("Method getOneById: Lecture not found.");
  }

  /**
   * Arranges the creation of a custom Lecture object.
   * Executes the add( Lecture @param) method
   * from LectureService class.
   * Asserts that a Lecture object has been added to the database
   * and it has the same attribute values as the one created previously.
   * @throws LectureInvalidTitleException
   */
  @Test
  void addTest() throws LectureInvalidTitleException {
    // given
    Lecture lecture = new Lecture("string_id", "Some_Title");

    // when
    underTestService.add(lecture);

    // then
    ArgumentCaptor<Lecture> lectureArgumentCaptor =
      ArgumentCaptor.forClass(Lecture.class);

    verify(lectureRepository).save(lectureArgumentCaptor.capture());

    Lecture capturedLecture = lectureArgumentCaptor.getValue();
    assertThat(capturedLecture.getTitle()).isEqualTo(lecture.getTitle());
  }

  /**
   * Arranges the creation of two Lecture objects
   * with invalid attribute values.
   * Executes the add( Lecture @param ) method
   * from LectureService class.
   * Asserts that nothing was saved in the database and
   * it throws LectureInvalidTitleException exception
   * with the related messages.
   */
  @Test
  void addLectureInvalidTitleExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Lecture lecture = new Lecture("id", (String) exceptionCase);
      // when & then
      assertThatThrownBy(() -> underTestService.add(lecture))
        .isInstanceOf(LectureInvalidTitleException.class)
        .hasMessageContaining("Method add: Title field can't be invalid.");

      verify(lectureRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Lecture objects:
   * 1. The object already present in the database.
   * 2. The object that will update the former.
   * Arranges the existence of the first object in the database.
   * Executes the update( @param ,Lecture @param ) method
   * from LectureService class.
   * Asserts that a Lecture object has been saved to the database
   * and that it has the same attribute values as the one we want to update with.
   * @throws LectureNotFoundException
   * @throws LectureInvalidTitleException
   */
  @Test
  void updateTest() throws LectureNotFoundException, LectureInvalidTitleException {
    // given
    Lecture lecture = new Lecture("id","Title");
    Lecture updatedLecture = new Lecture(anyString(), "Title.docs");

    // when
    when(lectureRepository.findById(lecture.getId()))
      .thenReturn(Optional.of(lecture));

    // then
    underTestService.update(lecture.getId(), updatedLecture);

    ArgumentCaptor<Lecture> lectureArgumentCaptor =
      ArgumentCaptor.forClass(Lecture.class);

    verify(lectureRepository).save(lectureArgumentCaptor.capture());

    Lecture capturedLecture = lectureArgumentCaptor.getValue();
    assertThat(capturedLecture.getTitle()).isEqualTo(lecture.getTitle());

    verify(lectureRepository).findById(lecture.getId());

  }

  /**
   * Arranges the creation of a Lecture object we will try to
   * update with and makes sure the database has no entries to update.
   * Executes the update( @param , Lecture @param ) method
   * from LectureService class.
   * Asserts that the LectureNotFoundException exception
   * is thrown with the related messages.
   */
  @Test
  void updateLectureNotFoundExceptionTest() {
    // given
    Lecture lecture = new Lecture("ID","Title");

    // when
    // Lecture Not Found Exception
    when(lectureRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.update(anyString(), lecture))
      .isInstanceOf(LectureNotFoundException.class)
      .hasMessageContaining("Method update: Lecture not found.");

    verify(lectureRepository, never()).save(any());
  }
  /**
   * Arranges the creation of a Lecture object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Lecture @param ) method
   * from LectureService class.
   * Asserts that the LectureInvalidTitleException exception
   * is thrown with the related messages.
   */
  @Test
  void updateLectureInvalidTitleExceptionTest() {
    // when
    when(lectureRepository.findById(anyString()))
      .thenReturn(Optional.of(new Lecture("id","Title")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Lecture lecture = new Lecture("id", (String) exceptionCase);

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",lecture))
        .isInstanceOf(LectureInvalidTitleException.class)
        .hasMessageContaining("Method update: Title field can't be invalid.");

      verify(lectureRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Lecture object and
   * makes sure of its existence in the database.
   * Executes the delete( @param ) method from LectureService class.
   * Asserts that the deleteById( @param ) method from repository has been
   * executed successfully.
   * @throws LectureNotFoundException
   */
  @Test
  void delete() throws LectureNotFoundException {
    // given
    Lecture lecture = new Lecture("id","Title");

    // when
    when(lectureRepository.findById(lecture.getId()))
      .thenReturn(Optional.of(lecture));

    // then
    underTestService.delete(lecture.getId());

    verify(lectureRepository).deleteById(lecture.getId());
  }

  /**
   * Arranges the absence of any Lecture object in database.
   * Executes the delete( @param ) method from LectureService class.
   * Asserts that it throws the LectureNotFoundException exception
   * and the related message.
   */
  @Test
  void deleteException() {

    // when
    when(lectureRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.delete(anyString()))
      .isInstanceOf(LectureNotFoundException.class)
      .hasMessageContaining("Method delete: Lecture not found");

    verify(lectureRepository, never()).delete(any());
  }
}
