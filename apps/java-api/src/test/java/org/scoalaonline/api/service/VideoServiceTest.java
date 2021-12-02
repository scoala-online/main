package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.video.*;
import org.scoalaonline.api.model.Video;
import org.scoalaonline.api.repository.VideoRepository;

import java.time.Duration;
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
class VideoServiceTest {

  @InjectMocks
  private VideoService underTestService;

  @Mock
  private VideoRepository videoRepository;

  //region get tests
  /**
   * Executes the getAll() method from VideoService class.
   * Asserts that it correctly called the findAll() method
   * from the repository.
   */
  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(videoRepository).findAll();
  }

  /**
   * Arranges the existence of a custom Video object in database.
   * Executes the getOneById( @param ) method from VideoService class.
   * Asserts that it finds the same object arranged in the former step.
   * @throws VideoNotFoundException
   */
  @Test
  void getOneByIdTest() throws VideoNotFoundException {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    // then
    Video video = underTestService.getOneById("id");

    Assertions.assertEquals("videoURL", video.getVideoURL());
    Assertions.assertEquals("videoTitle", video.getVideoTitle());
    Assertions.assertEquals("PT0.001S", video.getVideoLength().toString());
    Assertions.assertEquals("teacherURL", video.getTeacherURL());
    Assertions.assertEquals("teacherImageURL", video.getTeacherImageURL());
    Assertions.assertEquals("transcript", video.getTranscript());
    Assertions.assertEquals("summary", video.getSummary());
    Assertions.assertEquals("thumbnail", video.getThumbnail());

  }

  /**
   * Arranges the absence of any Video object in database.
   * Executes the getOneById( @param ) method from VideoService class.
   * Asserts that it throws the VideoNotFoundException exception
   * and the related message.
   */
  @Test
  void getOneByIdExceptionTest() {
    //when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(VideoNotFoundException.class)
      .hasMessageContaining("Method getOneById: Video not found.");
  }
  //endregion
  //region add tests
  /**
   * Arranges the creation of a custom Video object.
   * Executes the add( Video @param) method
   * from VideoService class.
   * Asserts that a Video object has been added to the database
   * and it has the same attribute values as the one created previously.
   * @throws Exception
   */
  @Test
  void addTest() throws Exception {
    // given
    Video video = new Video("id0", "videoURL0", "videoTitle0", Duration.ofMillis(1),
      "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0");

    // when
    underTestService.add(video);

    // then
    ArgumentCaptor<Video> videoArgumentCaptor =
      ArgumentCaptor.forClass(Video.class);

    verify(videoRepository).save(videoArgumentCaptor.capture());

    Video capturedVideo = videoArgumentCaptor.getValue();
    assertThat(capturedVideo.getVideoURL()).isEqualTo(video.getVideoURL());
    assertThat(capturedVideo.getVideoTitle()).isEqualTo(video.getVideoTitle());
    assertThat(capturedVideo.getVideoLength()).isEqualTo(video.getVideoLength());
    assertThat(capturedVideo.getTeacherURL()).isEqualTo(video.getTeacherURL());
    assertThat(capturedVideo.getTeacherImageURL()).isEqualTo(video.getTeacherImageURL());
    assertThat(capturedVideo.getTranscript()).isEqualTo(video.getTranscript());
    assertThat(capturedVideo.getSummary()).isEqualTo(video.getSummary());
    assertThat(capturedVideo.getThumbnail()).isEqualTo(video.getThumbnail());
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidURL exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidURLExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0", (String) exceptionCase, "videoTitle0", Duration.ofMillis(1),
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidURLException.class)
        .hasMessageContaining("Method add: URL field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidTitle exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidTitleExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0", (String) exceptionCase, Duration.ofMillis(1),
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidTitleException.class)
        .hasMessageContaining("Method add: Title field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidLength exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidLengthExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(Duration.ofMillis(0));
    exceptionCases.add(null);
    exceptionCases.add(Duration.ofMillis(-1));
    exceptionCases.add(Duration.ofMillis(-30));
    exceptionCases.add(Duration.ofDays(2));
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0","videoTitle0",(Duration) exceptionCase,
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidLengthException.class)
        .hasMessageContaining("Method add: Length field has to be more than 0 and less than 1 day.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidTeacherURL exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidTeacherURLExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0","videoTitle0", Duration.ofMillis(1), (String) exceptionCase,
        "teacherImageURL0", "transcript0", "summary0", "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidTeacherURLException.class)
        .hasMessageContaining("Method add: TeacherURL field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidTeacherImageURL exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidTeacherImageURLExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0","videoTitle0", Duration.ofMillis(1),"teacherURL0",
        (String) exceptionCase, "transcript0", "summary0", "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidTeacherImageURLException.class)
        .hasMessageContaining("Method add: TeacherImageURL field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidTranscript exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidTranscriptExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0","videoTitle0", Duration.ofMillis(1),"teacherURL0",
        "teacherImageURL0", (String) exceptionCase, "summary0", "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidTranscriptException.class)
        .hasMessageContaining("Method add: Transcript field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidSummary exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidSummaryExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0","videoTitle0", Duration.ofMillis(1),"teacherURL0",
        "teacherImageURL0", "transcript0",(String) exceptionCase, "thumbnail0");
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidSummaryException.class)
        .hasMessageContaining("Method add: Summary field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Video objects
   * with invalid attribute values.
   * Executes the add( Video @param ) method
   * from VideoService class.
   * Asserts that nothing was saved in the database and
   * it throws VideoInvalidThumbnail exception
   * with the related messages.
   */
  @Test
  void addVideoInvalidThumbnailExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id0","videoURL0","videoTitle0", Duration.ofMillis(1),"teacherURL0",
        "teacherImageURL0", "transcript0", "summary0",(String) exceptionCase);
      // when & then
      assertThatThrownBy(() -> underTestService.add(video))
        .isInstanceOf(VideoInvalidThumbnailException.class)
        .hasMessageContaining("Method add: Thumbnail field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }
  //endregion
  // region update tests
  /**
   * Arranges the creation of two Video objects:
   * 1. The object already present in the database.
   * 2. The object that will update the former.
   * Arranges the existence of the first object in the database.
   * Executes the update( @param ,Video @param ) method
   * from VideoService class.
   * Asserts that a Video object has been saved to the database
   * and that it has the same attribute values as the one we want to update with.
   * @throws Exception
   */
  @Test
  void updateTest() throws Exception {
    // given
    Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
      "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail");
    Video updatedVideo = new Video(anyString(), "videoURL0", "videoTitle0", Duration.ofMillis(2),
      "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0");

    // when
    when(videoRepository.findById(video.getId()))
      .thenReturn(Optional.of(video));

    // then
    underTestService.update(video.getId(), updatedVideo);

    ArgumentCaptor<Video> videoArgumentCaptor =
      ArgumentCaptor.forClass(Video.class);

    verify(videoRepository).save(videoArgumentCaptor.capture());

    Video capturedVideo = videoArgumentCaptor.getValue();
    assertThat(capturedVideo.getVideoURL()).isEqualTo(video.getVideoURL());
    assertThat(capturedVideo.getVideoTitle()).isEqualTo(video.getVideoTitle());
    assertThat(capturedVideo.getVideoLength()).isEqualTo(video.getVideoLength());
    assertThat(capturedVideo.getTeacherURL()).isEqualTo(video.getTeacherURL());
    assertThat(capturedVideo.getTeacherImageURL()).isEqualTo(video.getTeacherImageURL());
    assertThat(capturedVideo.getTranscript()).isEqualTo(video.getTranscript());
    assertThat(capturedVideo.getSummary()).isEqualTo(video.getSummary());
    assertThat(capturedVideo.getThumbnail()).isEqualTo(video.getThumbnail());

    verify(videoRepository).findById(video.getId());
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the database has no entries to update.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoNotFoundException exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoNotFoundExceptionTest() {
    // given
    Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
      "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail");

    // when
    // Video Not Found Exception
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.update(anyString(), video))
      .isInstanceOf(VideoNotFoundException.class)
      .hasMessageContaining("Method update: Video not found.");

    verify(videoRepository, never()).save(any());
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidURL exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidURLExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", (String) exceptionCase, "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidURLException.class)
        .hasMessageContaining("Method update: URL field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidTitle exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidTileExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", (String) exceptionCase, Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidTitleException.class)
        .hasMessageContaining("Method update: Title field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidLength exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidLengthExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(Duration.ofMillis(0));
    exceptionCases.add(null);
    exceptionCases.add(Duration.ofMillis(-1));
    exceptionCases.add(Duration.ofMillis(-30));
    exceptionCases.add(Duration.ofDays(2));
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", "videoTitle", (Duration) exceptionCase,
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidLengthException.class)
        .hasMessageContaining("Method update: Length field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidTeacherURL exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidTeacherURLExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        (String) exceptionCase, "teacherImageURL", "transcript", "summary", "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidTeacherURLException.class)
        .hasMessageContaining("Method update: TeacherURL field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidTeacherImageURL exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidTeacherImageURLExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1), "teacherURL",
        (String) exceptionCase, "transcript", "summary", "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidTeacherImageURLException.class)
        .hasMessageContaining("Method update: TeacherImageURL field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidTranscript exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidTranscriptExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", (String) exceptionCase, "summary", "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidTranscriptException.class)
        .hasMessageContaining("Method update: Transcript field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidSummary exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidSummaryExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", (String) exceptionCase, "thumbnail");;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidSummaryException.class)
        .hasMessageContaining("Method update: Summary field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Video object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Video @param ) method
   * from VideoService class.
   * Asserts that the VideoInvalidThumbnail exception
   * is thrown with the related messages.
   */
  @Test
  void updateVideoInvalidThumbnailExceptionTest() {
    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.of(new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Video video = new Video("id", "videoURL", "videoTitle", Duration.ofMillis(1),
        "teacherURL", "teacherImageURL", "transcript", "summary", (String) exceptionCase);;

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",video))
        .isInstanceOf(VideoInvalidThumbnailException.class)
        .hasMessageContaining("Method update: Thumbnail field can't be null.");

      verify(videoRepository, never()).save(any());
    }
  }
  // endregion
  // region delete tests
  /**
   * Arranges the creation of a Video object and
   * makes sure of its existence in the database.
   * Executes the DELETE( @param ) method from VideoService class.
   * Asserts that the deleteById( @param ) method from repository has been
   * executed successfully.
   * @throws VideoNotFoundException
   */
  @Test
  void deleteTest() throws VideoNotFoundException {
    // given
    Video video = new Video("id0","videoURL0","videoTitle0", Duration.ofMillis(1),"teacherURL0",
      "teacherImageURL0", "transcript0", "summary0","thumbnail");

    // when
    when(videoRepository.findById(video.getId()))
      .thenReturn(Optional.of(video));

    // then
    underTestService.delete(video.getId());

    verify(videoRepository).deleteById(video.getId());
  }

  /**
   * Arranges the absence of any Video object in database.
   * Executes the delete( @param ) method from VideoService class.
   * Asserts that it throws the VideoNotFoundException exception
   * and the related message.
   */
  @Test
  void deleteVideoNotFoundExceptionTest() {

    // when
    when(videoRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.delete(anyString()))
      .isInstanceOf(VideoNotFoundException.class)
      .hasMessageContaining("Method delete: Video not found.");

    verify(videoRepository, never()).delete(any());
  }
  // endregion
}
