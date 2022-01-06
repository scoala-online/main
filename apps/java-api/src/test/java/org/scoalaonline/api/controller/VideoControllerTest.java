package org.scoalaonline.api.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoalaonline.api.exception.video.*;
import org.scoalaonline.api.model.Video;
import org.scoalaonline.api.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.scoalaonline.api.util.TestUtils.buildJsonBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
class VideoControllerTest {
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private VideoService videoService;

  private List<Video> videoList;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    this.videoList = new ArrayList<>();
    this.videoList.add( new Video("id0", "videoURL0", "videoTitle0", Duration.ofMillis(1),
      "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0"));
    this.videoList.add( new Video("id1", "videoURL1", "videoTitle1", Duration.ofMillis(2),
      "teacherURL1", "teacherImageURL1", "transcript1", "summary1", "thumbnail1"));
    this.videoList.add( new Video("id2", "videoURL2", "videoTitle2", Duration.ofMillis(3),
      "teacherURL2", "teacherImageURL2", "transcript2", "summary2", "thumbnail2"));
  }

  //region getVideo tests
  /**
   * Arranges the existence of entries in the database.
   * Performs GET method on "/videos"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * @throws Exception
   */
  @DisplayName(value = "Test getting all videos.")
  @Test
  void getAllVideosTest() throws Exception{
    given(videoService.getAll()).willReturn(videoList);

    this.mockMvc.perform(get("/videos")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
      .andReturn();
  }

  /**
   * Arranges the existence of a Video object at a specified id.
   * Creates a JSON entry that will be expected to be received.
   * Performs GET method at "videos/id0".
   * Asserts that the status is 200 and the object returned has the same
   * attribute values as the expected one.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a video by id.")
  @Test
  void getVideoByIdTest() throws Exception {
    // given
    given(videoService.getOneById("id0")).willReturn(videoList.get(0));

    // JSON Generator

    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("id");
    FieldArray.add("videoURL");
    FieldArray.add("videoTitle");
    FieldArray.add("videoLength");
    FieldArray.add("teacherURL");
    FieldArray.add("teacherImageURL");
    FieldArray.add("transcript");
    FieldArray.add("summary");
    FieldArray.add("thumbnail");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add("id0");
    ValuesArray.add("videoURL0");
    ValuesArray.add("videoTitle0");
    ValuesArray.add("PT0.001S");
    ValuesArray.add("teacherURL0");
    ValuesArray.add("teacherImageURL0");
    ValuesArray.add("transcript0");
    ValuesArray.add("summary0");
    ValuesArray.add("thumbnail0");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    // when & then
    this.mockMvc.perform(get("/videos/id0")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }

  /**
   * Arranges the absence of a Video object with the given id ("id3")
   * Performs GET method at "videos/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a video by id and expect 'Video not found' exception.")
  @Test
  void getVideoByIdNotFoundExceptionTest() throws Exception {
    //given
    given(videoService.getOneById("id3"))
      .willThrow(new VideoNotFoundException());

    //when
    MockHttpServletResponse response = mockMvc.perform(
        get("/videos/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    //then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString().isEmpty());
  }
  //endregion
  //region addVideo tests
  /**
   * Arranges the creation a Video object as JSON entry.
   * Performs POST at "videos/" with the created JSON.
   * Asserts that the given status is 201.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a video.")
  @Test
  void addVideoTest() throws Exception {
    //given

    //Json Generator
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("videoURL");
    FieldArray.add("videoTitle");
    FieldArray.add("videoLength");
    FieldArray.add("teacherURL");
    FieldArray.add("teacherImageURL");
    FieldArray.add("transcript");
    FieldArray.add("summary");
    FieldArray.add("thumbnail");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add("videoURL_0");
    ValuesArray.add("videoTitle_0");
    ValuesArray.add(Duration.ofMillis(4));
    ValuesArray.add("teacherURL_0");
    ValuesArray.add("teacherImageURL_0");
    ValuesArray.add("transcript_0");
    ValuesArray.add("summary_0");
    ValuesArray.add("thumbnail_0");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform(
        post("/videos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();
  }
  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs POST method at "videos/".
   * Asserts that the given status is 400 and that the
   * database remains empty.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a video and expect 'Invalid URL' exception.")
  @Test
  void addVideoInvalidURLExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, (String) exceptionCase, "videoTitle0", Duration.ofMillis(1),
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0")))
        .willThrow(VideoInvalidURLException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      ValuesArray.add("videoTitle0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL0");
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add("transcript0");
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  @DisplayName(value = "Test adding a video and expect 'Invalid title' exception.")
  @Test
  void addVideoInvalidTitleExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", (String) exceptionCase, Duration.ofMillis(1),
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0")))
        .willThrow(VideoInvalidTitleException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL0");
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add("transcript0");
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }
  @DisplayName(value = "Test adding a video and expect 'Invalid duration' exception.")
  @Test
  // FIXME: The test doesn't work on Duration.ofMillis(Integer.MIN_VALUE) or anything of this sort
  // Example:     exceptionCases.add(Duration.ofMillis(Integer.MIN_VALUE + 2147483647));
  void addVideoInvalidLengthExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(Duration.ofMillis(0));
    exceptionCases.add(null);
    exceptionCases.add(Duration.ofMillis(-1));
    exceptionCases.add(Duration.ofMillis(-30));
    exceptionCases.add(Duration.ofDays(2));
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", "videoTitle0", (Duration) exceptionCase,
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0")))
        .willThrow(VideoInvalidLengthException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add("videoTitle0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("teacherURL0");
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add("transcript0");
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  @DisplayName(value = "Test adding a video and expect 'Invalid teacher URL' exception.")
  @Test
  void addVideoInvalidTeacherURLExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", "videoTitle0", Duration.ofMillis(1),
        (String) exceptionCase, "teacherImageURL0", "transcript0", "summary0", "thumbnail0")))
        .willThrow(VideoInvalidTeacherURLException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add("videoTitle0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add(exceptionCase);
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add("transcript0");
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  @DisplayName(value = "Test adding a video and expect 'Invalid teacher image URL' exception.")
  @Test
  void addVideoInvalidTeacherImageURLExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", "videoTitle0", Duration.ofMillis(1),"teacherURL0",
        (String) exceptionCase, "transcript0", "summary0", "thumbnail0")))
        .willThrow(VideoInvalidTeacherImageURLException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add("videoTitle0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("transcript0");
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  @DisplayName(value = "Test adding a video and expect 'Invalid summary' exception.")
  @Test
  void addVideoInvalidSummaryExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", "videoTitle0", Duration.ofMillis(1),"teacherURL0","teacherImageURL0",
        (String) exceptionCase, "summary0", "thumbnail0")))
        .willThrow(VideoInvalidURLException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add("videoTitle0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL0");
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  @DisplayName(value = "Test adding a video and expect 'Invalid transcript' exception.")
  @Test
  void addVideoInvalidTranscriptExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", "videoTitle0", Duration.ofMillis(1),"teacherURL0",
        "teacherImageURL0", (String) exceptionCase, "summary0", "thumbnail0")))
        .willThrow(VideoInvalidTranscriptException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add("videoTitle0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL0");
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("summary0");
      ValuesArray.add("thumbnail0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  @DisplayName(value = "Test adding a video and expect 'Invalid thumbnail' exception.")
  @Test
  void addVideoInvalidThumbnailExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(videoService.add(new Video(null, "videoURL0", "videoTitle0", Duration.ofMillis(1),
        "teacherURL0","teacherImageURL0", "transcript0", "summary0",
        (String) exceptionCase))).willThrow(VideoInvalidThumbnailException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL0");
      ValuesArray.add("videoTitle0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL0");
      ValuesArray.add("teacherImageURL0");
      ValuesArray.add("transcript0");
      ValuesArray.add("summary0");
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/videos").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }
  //endregion
  //region updateVideo tests
  /**
   * Arranges the creation a Video object as JSON entry
   * and the existence of a Video object at specified id ("id0").
   * Performs PATCH method at "video/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video.")
  @Test
  void updateVideoTest() throws Exception {
    // given
    given(videoService.getAll()).willReturn(videoList);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("videoURL");
    FieldArray.add("videoTitle");
    FieldArray.add("videoLength");
    FieldArray.add("teacherURL");
    FieldArray.add("teacherImageURL");
    FieldArray.add("transcript");
    FieldArray.add("summary");
    FieldArray.add("thumbnail");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("videoURL_0");
    ValuesArray.add("videoTitle_0");
    ValuesArray.add(Duration.ofMillis(1));
    ValuesArray.add("teacherURL_0");
    ValuesArray.add("teacherImageURL_0");
    ValuesArray.add("transcript_0");
    ValuesArray.add("summary_0");
    ValuesArray.add("thumbnail_0");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when & then
    this.mockMvc.perform(
        patch("/videos/id0")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isOk())
      .andReturn();
  }

  /**
   * Arranges the absence of a Video object with the given id ("id3").
   * Performs PATCH method at "video/id3".
   * Asserts that the given status is 404 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expecting 'Video not found' exception.")
  @Test
  void updateVideoByIdNotFoundExceptionTest() throws Exception {
    // given
    given(videoService.update("id3",new Video(null, "videoURL_0", "videoTitle_0", Duration.ofMillis(1),"teacherURL_0",
      "teacherImageURL_0", "transcript_0", "summary_0", "thumbnail_0")))
      .willThrow(VideoNotFoundException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("videoURL");
    FieldArray.add("videoTitle");
    FieldArray.add("videoLength");
    FieldArray.add("teacherURL");
    FieldArray.add("teacherImageURL");
    FieldArray.add("transcript");
    FieldArray.add("summary");
    FieldArray.add("thumbnail");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("videoURL_0");
    ValuesArray.add("videoTitle_0");
    ValuesArray.add(Duration.ofMillis(1));
    ValuesArray.add("teacherURL_0");
    ValuesArray.add("teacherImageURL_0");
    ValuesArray.add("transcript_0");
    ValuesArray.add("summary_0");
    ValuesArray.add("thumbnail_0");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when
    MockHttpServletResponse response = mockMvc.perform(
        patch("/videos/id3")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();


    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid URL' exception.")
  @Test
  void updateVideoInvalidURLExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null, (String) exceptionCase, "videoTitle_0", Duration.ofMillis(1),"teacherURL_0",
        "teacherImageURL_0", "transcript_0", "summary_0", "thumbnail_0")))
        .willThrow(VideoInvalidURLException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL_0");
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add("transcript_0");
      ValuesArray.add("summary_0");
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid Title' exception.")
  @Test
  void updateVideoInvalidTitleExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", (String) exceptionCase, Duration.ofMillis(1),"teacherURL_0",
        "teacherImageURL_0", "transcript_0", "summary_0", "thumbnail_0")))
        .willThrow(VideoInvalidTitleException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL_0");
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add("transcript_0");
      ValuesArray.add("summary_0");
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid length' exception.")
  @Test
  void updateVideoInvalidLengthExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(Duration.ofMillis(0));
    exceptionCases.add(null);
    exceptionCases.add(Duration.ofMillis(-1));
    exceptionCases.add(Duration.ofMillis(-30));
    exceptionCases.add(Duration.ofDays(2));
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", "videoTitle_0", (Duration) exceptionCase
        , "teacherURL_0", "teacherImageURL_0", "transcript_0", "summary_0"
        , "thumbnail_0")))
        .willThrow(VideoInvalidLengthException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("teacherURL_0");
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add("transcript_0");
      ValuesArray.add("summary_0");
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid teacher URL' exception.")
  @Test
  void updateVideoInvalidTeacherURLExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", "videoTitle_0",
        Duration.ofMillis(1),(String) exceptionCase, "teacherImageURL_0", "transcript_0",
        "summary_0", "thumbnail_0")))
        .willThrow(VideoInvalidTeacherURLException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add(exceptionCase);
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add("transcript_0");
      ValuesArray.add("summary_0");
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid teacher image URL' exception.")
  @Test
  void updateVideoInvalidTeacherImageURLExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", "videoTitle_0",
        Duration.ofMillis(1),"teacherURL_0", (String) exceptionCase, "transcript_0",
        "summary_0", "thumbnail_0")))
        .willThrow(VideoInvalidTeacherURLException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL_0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("transcript_0");
      ValuesArray.add("summary_0");
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid transcript' exception.")
  @Test
  void updateVideoInvalidTranscriptExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", "videoTitle_0",
        Duration.ofMillis(1),"teacherURL_0", "teacherImageURL_0", (String) exceptionCase,
        "summary_0", "thumbnail_0")))
        .willThrow(VideoInvalidTranscriptException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL_0");
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("summary_0");
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid summary' exception.")
  @Test
  void updateVideoInvalidSummaryExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", "videoTitle_0",
        Duration.ofMillis(1),"teacherURL_0", "teacherImageURL_0", "transcript_0",
        (String) exceptionCase, "thumbnail_0")))
        .willThrow(VideoInvalidSummaryException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL_0");
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add("transcript_0");
      ValuesArray.add(exceptionCase);
      ValuesArray.add("thumbnail_0");
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of Video objects
   * with invalid attribute values.
   * Performs PATCH method at "video/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a video and expect 'Invalid thumbnail' exception.")
  @Test
  void updateVideoInvalidThumbnailExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(videoService.getAll())
        .willReturn(videoList);
      given(videoService.update("id0",new Video(null,"videoURL_0", "videoTitle_0",
        Duration.ofMillis(1),"teacherURL_0", "teacherImageURL_0", "transcript_0",
        "summary_0", (String) exceptionCase)))
        .willThrow(VideoInvalidTeacherURLException.class);

      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("videoURL");
      FieldArray.add("videoTitle");
      FieldArray.add("videoLength");
      FieldArray.add("teacherURL");
      FieldArray.add("teacherImageURL");
      FieldArray.add("transcript");
      FieldArray.add("summary");
      FieldArray.add("thumbnail");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add("videoURL_0");
      ValuesArray.add("videoTitle_0");
      ValuesArray.add(Duration.ofMillis(1));
      ValuesArray.add("teacherURL_0");
      ValuesArray.add("teacherImageURL_0");
      ValuesArray.add("transcript_0");
      ValuesArray.add("summary_0");
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/videos/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }
  //endregion
  //region deleteVideo tests
  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "video/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a video.")
  @Test
  void deleteVideoTest() throws Exception {
    // given
    given(videoService.getAll()).willReturn(videoList);

    // when & then
    this.mockMvc.perform(
        delete("/videos/id0")
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

  }

  /**
   * Arranges the absence of a Video object with the given id ("id3").
   * Performs DELETE method at "videos/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a video and expect 'Video not found' exception.")
  @Test
  void deleteVideoByIdNotFoundExceptionTest() throws Exception {
    // given
    doThrow(VideoNotFoundException.class).when(videoService).delete("id3");
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/videos/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }
  //endregion

}
