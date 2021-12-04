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
import org.scoalaonline.api.model.Subject;
import org.scoalaonline.api.repository.SubjectRepository;
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
public class SubjectIntegrationTest {

  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  void afterTests(){
    subjectRepository.deleteById("ID1");
    subjectRepository.deleteById("ID2");
    subjectRepository.deleteById("ID3");
    subjectRepository.deleteById("VALID_ID1");
    subjectRepository.deleteById("VALID_ID2");
    subjectRepository.deleteById("VALID_ID3");
    subjectRepository.deleteById("INVALID_ID1");
    subjectRepository.deleteById("VALID_ID");
  }

  private static Stream<Arguments> getAllCases() {

    // Create a subject to add to database
    ArrayList<Subject> arrayListNullCase = new ArrayList<>();
    ArrayList<Subject> arrayListOneCase = new ArrayList<>();
    ArrayList<Subject> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Subject("ID1","VALUE_1"));

    arrayListManyCase.add(new Subject("ID1","VALUE_1"));
    arrayListManyCase.add(new Subject("ID2","VALUE_2"));
    arrayListManyCase.add(new Subject("ID3","VALUE_3"));

    return Stream.of(
      Arguments.of(arrayListNullCase),
      Arguments.of(arrayListOneCase),
      Arguments.of(arrayListManyCase)
    );
  }

  private static Stream<Arguments> getByIdCases() {
    ArrayList<Subject> arrayListNullCase = new ArrayList<>();
    ArrayList<Subject> arrayListOneCase = new ArrayList<>();
    ArrayList<Subject> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Subject("VALID_ID1","VALUE_1"));

    arrayListManyCase.add(new Subject("VALID_ID1","VALUE_1"));
    arrayListManyCase.add(new Subject("VALID_ID2","VALUE_2"));
    arrayListManyCase.add(new Subject("VALID_ID3","VALUE_3"));

    return Stream.of(
      Arguments.of(arrayListNullCase,"INVALID_ID1", HttpStatus.NOT_FOUND.value(), "GET: Subject Not Found", null),
      Arguments.of(arrayListOneCase,"VALID_ID1",HttpStatus.OK.value(), null, arrayListOneCase.get(0)),
      Arguments.of(arrayListManyCase,"VALID_ID2", HttpStatus.OK.value(), null, arrayListManyCase.get(1))
    );
  }

  private static Stream<Arguments> addCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_VALUE", HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_VALUE", HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("", HttpStatus.BAD_REQUEST.value(),"POST: Subject Invalid Value","ADMIN"),
      Arguments.of(null,  HttpStatus.BAD_REQUEST.value(),"POST: Subject Invalid Value","ADMIN"),
      Arguments.of("NOT_NULL_VALUE",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("ăâîșț",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("!@#$%^&*()„”=+[]{};:'\"\\|,<>/?1234567890-_   ",  HttpStatus.CREATED.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> updateCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_VALUE","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_VALUE","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("INVALID_ID_VALUE","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"PATCH: Subject Not Found","ADMIN"),
      Arguments.of("", "VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Subject Invalid Value","ADMIN"),
      Arguments.of(null,"VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Subject Invalid Value","ADMIN"),
      Arguments.of("NOT_NULL_VALUE","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> deleteByIdCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_VALUE","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_VALUE","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("VALUE_TO_BE_DELETED","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"DELETE: Subject Not Found","ADMIN"),
      Arguments.of("VALUE_TO_BE_DELETED","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  /**
   * Arranges the existence of a list of entries in the database.
   * Performs GET method on "/subjects"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * Asserts that the JSON output contains added objects
   *
   * IMPLEMENTATION DETAILS: after the addition of each list of cases, the function
   * goes through all the entries found in the returned JSON in REVERSE ORDER
   * and verifies if each of the added subject appears in the JSON properly.
   * @throws Exception
   * @param input -> List of added subjects
   */
  @DisplayName(value = "Get all 'Subjects' test")
  @ParameterizedTest
  @MethodSource("getAllCases")
  public void getAllSubjectsTest(ArrayList<Subject> input) throws Exception {

    // arrange
    subjectRepository.saveAll(input);

    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/subjects")).andDo(print())
      .andExpect(status().isOk()).andReturn().getResponse();

    JSONArray parsedSubjects = new JSONArray(response.getContentAsString()) ;

    // then
    // Goes through list of test entries
    for (Subject subject: input) {

      Optional<Subject> entity = subjectRepository.findById(subject.getId());
      assertThat(entity).isNotNull();
      assertThat(entity.get().getValue()).isEqualTo(subject.getValue());

      // TIME COMPLEXITY O(T) << O(N) where N is the number of entries in the database and T is the number
      // of entries added in the testing time, including entries that weren't added by the tests.

      // Goes through JSON list to check if the new added entries appear
      JSONObject parsedSubject = null;
      for(int i = parsedSubjects.length() - 1; i >= 0; i --) {
        parsedSubject = new JSONObject(parsedSubjects.get(i).toString());
        if(parsedSubject.get("id").equals(subject.getId())){
          break;
        }
      }
      // Checks if the entry found in the previous for is the one added by the test
      // It fails if the entry is not added or found
      assertThat(parsedSubject).isNotNull();
      assertThat(parsedSubject.get("id")).isEqualTo(subject.getId());
      assertThat(parsedSubject.get("value")).isEqualTo(subject.getValue());
    }
  }

  /**
   * Arranges the existence of a Subject object at a specified id.
   * Creates a JSON entry that will be expected to receive.
   * Performs GET method at "subjects/{@param idParam}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> List of subjects;
   * @param idParam -> The id of the wanted subject;
   * @param status -> Expected status of GET function;
   * @param errorMessage -> Expected error message;
   * @param expectedSubject -> The expected to find subject;
   */
  @DisplayName(value = "Get 'Subjects' by id test")
  @ParameterizedTest
  @MethodSource("getByIdCases")
  void getSubjectByIdTest(ArrayList<Subject> input, String idParam,
                                  Integer status, String errorMessage, Subject expectedSubject) throws Exception {
    subjectRepository.saveAll(input);
    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/subjects/" + idParam + "/")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();

      JSONObject parsedSubject = new JSONObject(response.getContentAsString());
      assertThat(parsedSubject.get("id")).isEqualTo(expectedSubject.getId());
      assertThat(parsedSubject.get("value")).isEqualTo(expectedSubject.getValue());
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Subject object as JSON entry.
   * Performs POST at "subject/" with the created JSON.
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> Name of new value;
   * @param status -> Expected status of POST function;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call POST function;
   */
  @DisplayName(value = "Add 'Subjects' test")
  @ParameterizedTest
  @MethodSource("addCases")
  public void addSubjectTest(String input, Integer status, String errorMessage, String role) throws Exception {

    // arrange
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add(input);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    // when
    MockHttpServletResponse response = this.mockMvc.perform(post("/subjects")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString())
        .with(user(role).roles(role))).andDo(print())
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    String id = null;
    if(errorMessage == null) {
      JSONObject parsedSubject = new JSONObject(response.getContentAsString());
      Optional<Subject> entity = subjectRepository.findById(parsedSubject.get("id").toString());

      assertThat(response.getContentAsString()).isNotEmpty();
      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getValue()).isEqualTo(input);
      id = parsedSubject.get("id").toString();
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }

    if(id != null)
      subjectRepository.deleteById(id);
  }

  /**
   * Arranges the creation a Subject object as JSON entry
   * and the existence of a Subject object at specified id {@param existentId}.
   * Performs PATCH method at "subject/{@param existentId}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise, asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param value -> Name of subject value after update;
   * @param expectedId -> Id of Subject that needs to be updated;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of PATCH method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call PATCH function;
   */
  @DisplayName(value = "Update 'Subjects' test")
  @ParameterizedTest
  @MethodSource("updateCases")
  void updateSubjectTest(String value, String expectedId, String wantedId,
                                 Integer status, String errorMessage,String role) throws Exception {

    // arrange
    subjectRepository.save(new Subject(expectedId, value + ".updateMe"));

    // Json Generator
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add(value);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when
    MockHttpServletResponse response = this.mockMvc.perform(
        patch("/subjects/" + wantedId + "/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString())
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();
      Optional<Subject> entity = subjectRepository.findById(wantedId);

      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getId()).isEqualTo(wantedId);
      assertThat(entity.get().getValue()).isEqualTo(value);
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "subject/id0".
   * Asserts that the status is 200.
   * @throws Exception
   * @param value -> Name of subject value to delete ;
   * @param expectedId -> Id of subject that needs to be deleted;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of DELETE method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call DELETE function;
   */
  @DisplayName(value = "Delete 'Subjects' test")
  @ParameterizedTest
  @MethodSource("deleteByIdCases")
  void deleteSubjectByIdNotFoundExceptionTest(String value, String expectedId, String wantedId,
                                                      Integer status, String errorMessage, String role) throws Exception {

    subjectRepository.save(new Subject(expectedId, value));
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/subjects/" + wantedId + "/")
          .accept(MediaType.APPLICATION_JSON)
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
    assertThat(response.getContentAsString()).isEmpty();

    if(errorMessage == null) {
      assertThat(subjectRepository.findById(expectedId).isEmpty()).isTrue();
    }
  }

}
