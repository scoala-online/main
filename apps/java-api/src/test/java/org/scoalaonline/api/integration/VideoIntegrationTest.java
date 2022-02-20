package org.scoalaonline.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.scoalaonline.api.util.TestUtils.buildJsonBody;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.scoalaonline.api.model.Video;
import org.scoalaonline.api.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideoIntegrationTest {

  @Autowired
  private VideoRepository videoRepository;
  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  void afterTests(){
    videoRepository.deleteById("ID1");
    videoRepository.deleteById("ID2");
    videoRepository.deleteById("ID3");
    videoRepository.deleteById("VALID_ID1");
    videoRepository.deleteById("VALID_ID2");
    videoRepository.deleteById("VALID_ID3");
    videoRepository.deleteById("INVALID_ID1");
    videoRepository.deleteById("VALID_ID");
  }

  private static Stream<Arguments> getAllCases() {

    // Create a video to add to database
    ArrayList<Video> arrayListNullCase = new ArrayList<>();
    ArrayList<Video> arrayListOneCase = new ArrayList<>();
    ArrayList<Video> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Video("VALID_ID", "videoURL", "videoTitle", Duration.ofMillis(1),
      "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail"));

    arrayListManyCase.add(new Video("VALID_ID1", "videoURL1", "videoTitle1", Duration.ofMillis(1),
      "teacherURL1", "teacherImageURL1", "transcript1", "summary1", "thumbnail1"));
    arrayListManyCase.add(new Video("VALID_ID2", "videoURL2", "videoTitle2", Duration.ofMillis(2),
      "teacherURL2", "teacherImageURL2", "transcript2", "summary2", "thumbnail2"));
    arrayListManyCase.add(new Video("VALID_ID3", "videoURL3", "videoTitle3", Duration.ofMillis(3),
      "teacherURL3", "teacherImageURL3", "transcript3", "summary3", "thumbnail3"));

    return Stream.of(
      Arguments.of(arrayListNullCase),
      Arguments.of(arrayListOneCase),
      Arguments.of(arrayListManyCase)
    );
  }

  private static Stream<Arguments> getByIdCases() {
    ArrayList<Video> arrayListNullCase = new ArrayList<>();
    ArrayList<Video> arrayListOneCase = new ArrayList<>();
    ArrayList<Video> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Video("VALID_ID", "videoURL", "videoTitle", Duration.ofMillis(1),
      "teacherURL", "teacherImageURL", "transcript", "summary", "thumbnail"));

    arrayListManyCase.add(new Video("VALID_ID1", "videoURL1", "videoTitle1", Duration.ofMillis(1),
      "teacherURL1", "teacherImageURL1", "transcript1", "summary1", "thumbnail1"));
    arrayListManyCase.add(new Video("VALID_ID2", "videoURL2", "videoTitle2", Duration.ofMillis(2),
      "teacherURL2", "teacherImageURL2", "transcript2", "summary2", "thumbnail2"));
    arrayListManyCase.add(new Video("VALID_ID3", "videoURL3", "videoTitle3", Duration.ofMillis(3),
      "teacherURL3", "teacherImageURL3", "transcript3", "summary3", "thumbnail3"));

    return Stream.of(
      Arguments.of(arrayListNullCase,"INVALID_ID1", HttpStatus.NOT_FOUND.value(), "GET: Video not found.", null),
      Arguments.of(arrayListOneCase,"VALID_ID",HttpStatus.OK.value(), null, arrayListOneCase.get(0)),
      Arguments.of(arrayListManyCase,"VALID_ID2", HttpStatus.OK.value(), null, arrayListManyCase.get(1))
    );
  }

  private static Stream<Arguments> addCases() {
    return Stream.of(
      Arguments.of("UNAUTHORISED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORISED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid URL.","ADMIN"),
      Arguments.of(null, "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid URL.","ADMIN"),
      Arguments.of("videoURL0", "", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid title.","ADMIN"),
      Arguments.of("videoURL0", null, Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid title.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(0), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(-1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(-30), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", null, "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofDays(2), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid teacher URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), null, "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid teacher URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "", "transcript0",
        "summary0", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid teacher image URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", null, "transcript0",
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid teacher image URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "",
        "summary0", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid transcript.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", null,
        "summary0", "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid transcript.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "", "thumbnail0", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid summary.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        null, "thumbnail0",  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid summary.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "", HttpStatus.BAD_REQUEST.value(),"POST: Video invalid thumbnail.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", null,  HttpStatus.BAD_REQUEST.value(),"POST: Video invalid thumbnail.","ADMIN"),

      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("videoURL0ăâîșț", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("videoURL0!@#$%^&*()„”=+[]{};:'\"\\|,<>/?1234567890-_   ", "videoTitle0", Duration.ofMillis(1),
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0",  HttpStatus.CREATED.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> updateCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("UNAUTHORIZED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"PATCH: Video not found.","ADMIN"),

      Arguments.of("", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid URL.","ADMIN"),
      Arguments.of(null, "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid URL.","ADMIN"),
      Arguments.of("videoURL0", "", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid title.","ADMIN"),
      Arguments.of("videoURL0", null, Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid title.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(0), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(-1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(-30), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", null, "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofDays(2), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid length.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid teacher URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), null, "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid teacher URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid teacher image URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", null, "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid teacher image URL.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid transcript.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", null,
        "summary0", "thumbnail0", "VALID_ID","VALID_ID",  HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid transcript.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid summary.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        null, "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid summary.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "", "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid thumbnail.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", null, "VALID_ID","VALID_ID", HttpStatus.BAD_REQUEST.value(),"PATCH: Video invalid thumbnail.","ADMIN"),

      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of("videoURL0ăâîșț", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of("videoURL0!@#$%^&*()„”=+[]{};:'\"\\|,<>/?1234567890-_   ", "videoTitle0", Duration.ofMillis(1),
        "teacherURL0", "teacherImageURL0", "transcript0", "summary0", "thumbnail0", "VALID_ID","VALID_ID", HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> deleteByIdCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"DELETE: Video not found.","ADMIN"),
      Arguments.of("videoURL0", "videoTitle0", Duration.ofMillis(1), "teacherURL0", "teacherImageURL0", "transcript0",
        "summary0", "thumbnail0","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  /**
   * Arranges the existence of a list of entries in the database.
   * Performs GET method on "/videos"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * Asserts that the JSON output contains added objects
   *
   * IMPLEMENTATION DETAILS: after the addition of each list of cases, the function
   * goes through all the entries found in the returned JSON in REVERSE ORDER
   * and verifies if each of the added video appears in the JSON properly.
   * @throws Exception
   * @param input -> List of added videos
   */
  @DisplayName(value = "Get all 'Videos' test")
  @ParameterizedTest
  @MethodSource("getAllCases")
  public void getAllVideosTest(ArrayList<Video> input) throws Exception {

    // arrange
    videoRepository.saveAll(input);

    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/videos")).andDo(print())
      .andExpect(status().isOk()).andReturn().getResponse();

    JSONArray parsedVideos = new JSONArray(response.getContentAsString()) ;

    // then
    // Goes through list of test entries
    for (Video video: input) {

      Optional<Video> entity = videoRepository.findById(video.getId());
      assertThat(entity).isNotNull();
      assertThat(entity.get().getVideoURL()).isEqualTo(video.getVideoURL());
      assertThat(entity.get().getVideoTitle()).isEqualTo(video.getVideoTitle());
      assertThat(entity.get().getVideoLength()).isEqualTo(video.getVideoLength());
      assertThat(entity.get().getTeacherURL()).isEqualTo(video.getTeacherURL());
      assertThat(entity.get().getTeacherImageURL()).isEqualTo(video.getTeacherImageURL());
      assertThat(entity.get().getTranscript()).isEqualTo(video.getTranscript());
      assertThat(entity.get().getSummary()).isEqualTo(video.getSummary());
      assertThat(entity.get().getThumbnail()).isEqualTo(video.getThumbnail());


      // TIME COMPLEXITY O(T) << O(N) where N is the number of entries in the database and T is the number
      // of entries added in the testing time, including entries that weren't added by the tests.

      // Goes through JSON list to check if the new added entries appear
      JSONObject parsedVideo = null;
      for(int i = parsedVideos.length() - 1; i >= 0; i --) {
        parsedVideo = new JSONObject(parsedVideos.get(i).toString());
        if(parsedVideo.get("id").equals(video.getId())){
          break;
        }
      }
      // Checks if the entry found in the previous for is the one added by the test
      // It fails if the entry is not added or found
      assertThat(parsedVideo).isNotNull();
      assertThat(parsedVideo.get("id")).isEqualTo(video.getId());
      assertThat(parsedVideo.get("videoURL")).isEqualTo(video.getVideoURL());
      assertThat(parsedVideo.get("videoTitle")).isEqualTo(video.getVideoTitle());
      assertThat(parsedVideo.get("videoLength")).isEqualTo(video.getVideoLength().toString());
      assertThat(parsedVideo.get("teacherURL")).isEqualTo(video.getTeacherURL());
      assertThat(parsedVideo.get("teacherImageURL")).isEqualTo(video.getTeacherImageURL());
      assertThat(parsedVideo.get("transcript")).isEqualTo(video.getTranscript());
      assertThat(parsedVideo.get("summary")).isEqualTo(video.getSummary());
      assertThat(parsedVideo.get("thumbnail")).isEqualTo(video.getThumbnail());
    }
  }

  /**
   * Arranges the existence of a Video object at a specified id.
   * Creates a JSON entry that will be expected to receive.
   * Performs GET method at "videos/{@param idParam}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise, asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> List of videos;
   * @param idParam -> The id of the wanted video;
   * @param status -> Expected status of GET function;
   * @param errorMessage -> Expected error message;
   * @param expectedVideo -> The expected to find video;
   */
  @DisplayName(value = "Get 'Videos' by id test")
  @ParameterizedTest
  @MethodSource("getByIdCases")
  void getVideoByIdTest(ArrayList<Video> input, String idParam,
                          Integer status, String errorMessage, Video expectedVideo) throws Exception {
    videoRepository.saveAll(input);
    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/videos/" + idParam + "/")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();

      JSONObject parsedVideo = new JSONObject(response.getContentAsString());
      assertThat(parsedVideo.get("id")).isEqualTo(expectedVideo.getId());
      assertThat(parsedVideo.get("videoURL")).isEqualTo(expectedVideo.getVideoURL());
      assertThat(parsedVideo.get("videoTitle")).isEqualTo(expectedVideo.getVideoTitle());
      assertThat(parsedVideo.get("videoLength")).isEqualTo(expectedVideo.getVideoLength().toString());
      assertThat(parsedVideo.get("teacherURL")).isEqualTo(expectedVideo.getTeacherURL());
      assertThat(parsedVideo.get("teacherImageURL")).isEqualTo(expectedVideo.getTeacherImageURL());
      assertThat(parsedVideo.get("transcript")).isEqualTo(expectedVideo.getTranscript());
      assertThat(parsedVideo.get("summary")).isEqualTo(expectedVideo.getSummary());
      assertThat(parsedVideo.get("thumbnail")).isEqualTo(expectedVideo.getThumbnail());
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }
  /**
   * Arranges the creation a Video object as JSON entry.
   * Performs POST at "video/" with the created JSON.
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise, asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @param videoURL -> Name of new video URL;
   * @param videoTitle -> Name of new video title;
   * @param videoLength -> Name of new video length;
   * @param teacherURL -> Name of new teacher URL;
   * @param teacherImageURL -> Name of new teacher image URL;
   * @param transcript -> Name of new transcript;
   * @param summary -> Name of new summary;
   * @param thumbnail -> Name of new thumbnail;
   * @param status -> Expected status of POST function;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call POST function;
   * @throws Exception
   */
  @DisplayName(value = "Add 'Videos' test")
  @ParameterizedTest
  @MethodSource("addCases")
  public void addVideoTest(String videoURL, String videoTitle, Duration videoLength, String teacherURL,
                           String teacherImageURL, String transcript, String summary, String thumbnail, Integer status,
                           String errorMessage, String role) throws Exception {

    // arrange
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
    ValuesArray.add(videoURL);
    ValuesArray.add(videoTitle);
    ValuesArray.add(videoLength);
    ValuesArray.add(teacherURL);
    ValuesArray.add(teacherImageURL);
    ValuesArray.add(transcript);
    ValuesArray.add(summary);
    ValuesArray.add(thumbnail);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    // when
    MockHttpServletResponse response = this.mockMvc.perform(post("/videos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString())
        .with(user(role).roles(role))).andDo(print())
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    String id = null;
    if(errorMessage == null) {
      JSONObject parsedVideo = new JSONObject(response.getContentAsString());
      Optional<Video> entity = videoRepository.findById(parsedVideo.get("id").toString());

      assertThat(response.getContentAsString()).isNotEmpty();
      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getVideoURL()).isEqualTo(videoURL);
      assertThat(entity.get().getVideoTitle()).isEqualTo(videoTitle);
      assertThat(entity.get().getVideoLength()).isEqualTo(videoLength);
      assertThat(entity.get().getTeacherURL()).isEqualTo(teacherURL);
      assertThat(entity.get().getTeacherImageURL()).isEqualTo(teacherImageURL);
      assertThat(entity.get().getTranscript()).isEqualTo(transcript);
      assertThat(entity.get().getSummary()).isEqualTo(summary);
      assertThat(entity.get().getThumbnail()).isEqualTo(thumbnail);

      id = parsedVideo.get("id").toString();
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }

    if(id != null)
      videoRepository.deleteById(id);
  }

  /**
   * Arranges the creation a Video object as JSON entry
   * and the existence of a Video object at specified id {@param existentId}.
   * Performs PATCH method at "video/{@param existentId}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise, asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @param videoURL -> Name of new video URL;
   * @param videoTitle -> Name of new video title;
   * @param videoLength -> Name of new video length;
   * @param teacherURL -> Name of new teacher URL;
   * @param teacherImageURL -> Name of new teacher image URL;
   * @param transcript -> Name of new transcript;
   * @param summary -> Name of new summary;
   * @param thumbnail -> Name of new thumbnail;
   * @param expectedId -> Id of Video that needs to be updated;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of PATCH method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call PATCH function;
   * @throws Exception
   */
  @DisplayName(value = "Update 'Videos' test")
  @ParameterizedTest
  @MethodSource("updateCases")
  void updateVideoTest(String videoURL, String videoTitle, Duration videoLength, String teacherURL,
                       String teacherImageURL, String transcript, String summary, String thumbnail, String expectedId, String wantedId,
                         Integer status, String errorMessage,String role) throws Exception {

    // arrange
    videoRepository.save(new Video(expectedId, videoURL+ "0", videoTitle + "0", Duration.ofMillis(2),
      teacherURL + "0", teacherImageURL + "0", transcript + "0", summary + "0", thumbnail + "0"));

    // Json Generator
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
    ValuesArray.add(videoURL);
    ValuesArray.add(videoTitle);
    ValuesArray.add(videoLength);
    ValuesArray.add(teacherURL);
    ValuesArray.add(teacherImageURL);
    ValuesArray.add(transcript);
    ValuesArray.add(summary);
    ValuesArray.add(thumbnail);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when
    MockHttpServletResponse response = this.mockMvc.perform(
        patch("/videos/" + wantedId + "/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString())
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();
      Optional<Video> entity = videoRepository.findById(wantedId);

      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getId()).isEqualTo(wantedId);
      assertThat(entity.get().getVideoURL()).isEqualTo(videoURL);
      assertThat(entity.get().getVideoTitle()).isEqualTo(videoTitle);
      assertThat(entity.get().getVideoLength()).isEqualTo(videoLength);
      assertThat(entity.get().getTeacherURL()).isEqualTo(teacherURL);
      assertThat(entity.get().getTeacherImageURL()).isEqualTo(teacherImageURL);
      assertThat(entity.get().getTranscript()).isEqualTo(transcript);
      assertThat(entity.get().getSummary()).isEqualTo(summary);
      assertThat(entity.get().getThumbnail()).isEqualTo(thumbnail);
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "video/id0".
   * Asserts that the status is 200.
   * @param videoURL -> Name of new video URL;
   * @param videoTitle -> Name of new video title;
   * @param videoLength -> Name of new video length;
   * @param teacherURL -> Name of new teacher URL;
   * @param teacherImageURL -> Name of new teacher image URL;
   * @param transcript -> Name of new transcript;
   * @param summary -> Name of new summary;
   * @param thumbnail -> Name of new thumbnail;
   * @param expectedId -> Id of video that needs to be deleted;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of DELETE method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call DELETE function;
   * @throws Exception
   */
  @DisplayName(value = "Delete 'Videos' test")
  @ParameterizedTest
  @MethodSource("deleteByIdCases")
  void deleteVideoByIdNotFoundExceptionTest(String videoURL, String videoTitle, Duration videoLength, String teacherURL,
                                            String teacherImageURL, String transcript, String summary, String thumbnail, String expectedId, String wantedId,
                                              Integer status, String errorMessage, String role) throws Exception {

    videoRepository.save(new Video(expectedId, videoURL, videoTitle, videoLength, teacherURL, teacherImageURL, transcript, summary, thumbnail));
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/videos/" + wantedId + "/")
          .accept(MediaType.APPLICATION_JSON)
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
    assertThat(response.getContentAsString()).isEmpty();

    if(errorMessage == null) {
      assertThat(videoRepository.findById(expectedId).isEmpty()).isTrue();
    }
  }

}
