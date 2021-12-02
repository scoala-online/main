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
import org.scoalaonline.api.model.Lecture;
import org.scoalaonline.api.repository.LectureRepository;
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
public class LectureIntegrationTest {

  @Autowired
  private LectureRepository lectureRepository;
  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  void afterTests(){
    lectureRepository.deleteById("ID1");
    lectureRepository.deleteById("ID2");
    lectureRepository.deleteById("ID3");
    lectureRepository.deleteById("VALID_ID1");
    lectureRepository.deleteById("VALID_ID2");
    lectureRepository.deleteById("VALID_ID3");
    lectureRepository.deleteById("INVALID_ID1");
    lectureRepository.deleteById("VALID_ID");
  }

  private static Stream<Arguments> getAllCases() {

    // Create a lecture to add to database
    ArrayList<Lecture> arrayListNullCase = new ArrayList<>();
    ArrayList<Lecture> arrayListOneCase = new ArrayList<>();
    ArrayList<Lecture> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Lecture("ID1","EXAMPLE_TITLE_1"));

    arrayListManyCase.add(new Lecture("ID1","EXAMPLE_TITLE_1"));
    arrayListManyCase.add(new Lecture("ID2","EXAMPLE_TITLE_2"));
    arrayListManyCase.add(new Lecture("ID3","EXAMPLE_TITLE_3"));

    return Stream.of(
      Arguments.of(arrayListNullCase),
      Arguments.of(arrayListOneCase),
      Arguments.of(arrayListManyCase)
    );
  }

  private static Stream<Arguments> getByIdCases() {
    ArrayList<Lecture> arrayListNullCase = new ArrayList<>();
    ArrayList<Lecture> arrayListOneCase = new ArrayList<>();
    ArrayList<Lecture> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Lecture("VALID_ID1","EXAMPLE_TITLE_1"));

    arrayListManyCase.add(new Lecture("VALID_ID1","EXAMPLE_TITLE_1"));
    arrayListManyCase.add(new Lecture("VALID_ID2","EXAMPLE_TITLE_2"));
    arrayListManyCase.add(new Lecture("VALID_ID3","EXAMPLE_TITLE_3"));

    return Stream.of(
      Arguments.of(arrayListNullCase,"INVALID_ID1", HttpStatus.NOT_FOUND.value(), "GET: Lecture not found.", null),
      Arguments.of(arrayListOneCase,"VALID_ID1",HttpStatus.OK.value(), null, arrayListOneCase.get(0)),
      Arguments.of(arrayListManyCase,"VALID_ID2", HttpStatus.OK.value(), null, arrayListManyCase.get(1))
    );
  }

  private static Stream<Arguments> addCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_TITLE", HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_TITLE", HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("", HttpStatus.BAD_REQUEST.value(),"POST: Lecture invalid title.","ADMIN"),
      Arguments.of(null,  HttpStatus.BAD_REQUEST.value(),"POST: Lecture invalid title.","ADMIN"),
      Arguments.of("NOT_NULL_TITLE",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("ăâîșț",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("!@#$%^&*()„”=+[]{};:'\"\\|,<>/?1234567890-_   ",  HttpStatus.CREATED.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> updateCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_TITLE","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_TITLE","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("INVALID_ID_TITLE","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"PATCH: Lecture not found.","ADMIN"),
      Arguments.of("", "VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Lecture invalid title.","ADMIN"),
      Arguments.of(null,"VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Lecture invalid title.","ADMIN"),
      Arguments.of("NOT_NULL_TITLE","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> deleteByIdCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_TITLE","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_TITLE","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("TITLE_TO_BE_DELETED","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"DELETE: Lecture not found.","ADMIN"),
      Arguments.of("TITLE_TO_BE_DELETED","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  /**
   * Arranges the existence of a list of entries in the database.
   * Performs GET method on "/lectures"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * Asserts that the JSON output contains added objects
   *
   * IMPLEMENTATION DETAILS: after the addition of each list of cases, the function
   * goes through all the entries found in the returned JSON in REVERSE ORDER
   * and verifies if each of the added lecture appears in the JSON properly.
   * @throws Exception
   * @param input -> List of added lectures
   */
  @DisplayName(value = "Get all 'Lectures' test")
  @ParameterizedTest
  @MethodSource("getAllCases")
  public void getAllLecturesTest(ArrayList<Lecture> input) throws Exception {

    // arrange
    lectureRepository.saveAll(input);

    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/lectures")).andDo(print())
      .andExpect(status().isOk()).andReturn().getResponse();

    JSONArray parsedLectures = new JSONArray(response.getContentAsString()) ;

    // then
    // Goes through list of test entries
    for (Lecture lecture: input) {

      Optional<Lecture> entity = lectureRepository.findById(lecture.getId());
      assertThat(entity).isNotNull();
      assertThat(entity.get().getTitle()).isEqualTo(lecture.getTitle());

      // TIME COMPLEXITY O(T) << O(N) where N is the number of entries in the database and T is the number
      // of entries added in the testing time, including entries that weren't added by the tests.

      // Goes through JSON list to check if the new added entries appear
      JSONObject parsedLecture = null;
      for(int i = parsedLectures.length() - 1; i >= 0; i --) {
        parsedLecture = new JSONObject(parsedLectures.get(i).toString());
        if(parsedLecture.get("id").equals(lecture.getId())){
          break;
        }
      }
      // Checks if the entry found in the previous for is the one added by the test
      // It fails if the entry is not added or found
      assertThat(parsedLecture).isNotNull();
      assertThat(parsedLecture.get("id")).isEqualTo(lecture.getId());
      assertThat(parsedLecture.get("title")).isEqualTo(lecture.getTitle());
    }
  }

  /**
   * Arranges the existence of a Lecture object at a specified id.
   * Creates a JSON entry that will be expected to receive.
   * Performs GET method at "lectures/{@param idParam}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> List of lectures;
   * @param idParam -> The id of the wanted lecture;
   * @param status -> Expected status of GET function;
   * @param errorMessage -> Expected error message;
   * @param expectedLecture -> The expected to find lecture;
   */
  @DisplayName(value = "Get 'Lectures' by id test")
  @ParameterizedTest
  @MethodSource("getByIdCases")
  void getLectureByIdTest(ArrayList<Lecture> input, String idParam,
                                  Integer status, String errorMessage, Lecture expectedLecture) throws Exception {
    lectureRepository.saveAll(input);
    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/lectures/" + idParam + "/")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();

      JSONObject parsedLecture = new JSONObject(response.getContentAsString());
      assertThat(parsedLecture.get("id")).isEqualTo(expectedLecture.getId());
      assertThat(parsedLecture.get("title")).isEqualTo(expectedLecture.getTitle());
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Lecture object as JSON entry.
   * Performs POST at "lecture/" with the created JSON.
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> Name of new title;
   * @param status -> Expected status of POST function;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call POST function;
   */
  @DisplayName(value = "Add 'Lectures' test")
  @ParameterizedTest
  @MethodSource("addCases")
  public void addLectureTest(String input, Integer status, String errorMessage, String role) throws Exception {

    // arrange
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("title");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add(input);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    // when
    MockHttpServletResponse response = this.mockMvc.perform(post("/lectures")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString())
        .with(user(role).roles(role))).andDo(print())
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    String id = null;
    if(errorMessage == null) {
      JSONObject parsedLecture = new JSONObject(response.getContentAsString());
      Optional<Lecture> entity = lectureRepository.findById(parsedLecture.get("id").toString());

      assertThat(response.getContentAsString()).isNotEmpty();
      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getTitle()).isEqualTo(input);
      id = parsedLecture.get("id").toString();
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }

    if(id != null)
      lectureRepository.deleteById(id);
  }

  /**
   * Arranges the creation a Lecture object as JSON entry
   * and the existence of a Lecture object at specified id {@param existentId}.
   * Performs PATCH method at "lecture/{@param existentId}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param title -> Name of lecture title after update;
   * @param expectedId -> Id of lecture that needs to be updated;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of PATCH method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call PATCH function;
   */
  @DisplayName(value = "Update 'Lectures' test")
  @ParameterizedTest
  @MethodSource("updateCases")
  void updateLectureTest(String title, String expectedId, String wantedId,
                                 Integer status, String errorMessage,String role) throws Exception {

    // arrange
    lectureRepository.save(new Lecture(expectedId, title + ".updateMe"));

    // Json Generator
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("title");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add(title);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when
    MockHttpServletResponse response = this.mockMvc.perform(
        patch("/lectures/" + wantedId + "/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString())
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();
      Optional<Lecture> entity = lectureRepository.findById(wantedId);

      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getId()).isEqualTo(wantedId);
      assertThat(entity.get().getTitle()).isEqualTo(title);
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "lecture/id0".
   * Asserts that the status is 200.
   * @throws Exception
   * @param title -> Name of lecture title to delete ;
   * @param expectedId -> Id of lecture that needs to be deleted;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of DELETE method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call DELETE function;
   */
  @DisplayName(value = "Delete 'Lectures' test")
  @ParameterizedTest
  @MethodSource("deleteByIdCases")
  void deleteLectureByIdNotFoundExceptionTest(String title, String expectedId, String wantedId,
                                                      Integer status, String errorMessage, String role) throws Exception {

    lectureRepository.save(new Lecture(expectedId, title));
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/lectures/" + wantedId + "/")
          .accept(MediaType.APPLICATION_JSON)
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
    assertThat(response.getContentAsString()).isEmpty();

    if(errorMessage == null) {
      assertThat(lectureRepository.findById(expectedId).isEmpty()).isTrue();
    }
  }

}
