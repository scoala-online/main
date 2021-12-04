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
import org.scoalaonline.api.model.Grade;
import org.scoalaonline.api.repository.GradeRepository;
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
public class GradeIntegrationTest {

  @Autowired
  private GradeRepository gradeRepository;
  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  void afterTests(){
    gradeRepository.deleteById("ID1");
    gradeRepository.deleteById("ID2");
    gradeRepository.deleteById("ID3");
    gradeRepository.deleteById("VALID_ID1");
    gradeRepository.deleteById("VALID_ID2");
    gradeRepository.deleteById("VALID_ID3");
    gradeRepository.deleteById("INVALID_ID1");
    gradeRepository.deleteById("VALID_ID");
  }

  private static Stream<Arguments> getAllCases() {

    // Create a grade to add to database
    ArrayList<Grade> arrayListNullCase = new ArrayList<>();
    ArrayList<Grade> arrayListOneCase = new ArrayList<>();
    ArrayList<Grade> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Grade("ID1",1));

    arrayListManyCase.add(new Grade("ID1",1));
    arrayListManyCase.add(new Grade("ID2",2));
    arrayListManyCase.add(new Grade("ID3",3));

    return Stream.of(
      Arguments.of(arrayListNullCase),
      Arguments.of(arrayListOneCase),
      Arguments.of(arrayListManyCase)
    );
  }

  private static Stream<Arguments> getByIdCases() {
    ArrayList<Grade> arrayListNullCase = new ArrayList<>();
    ArrayList<Grade> arrayListOneCase = new ArrayList<>();
    ArrayList<Grade> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Grade("VALID_ID1",1));

    arrayListManyCase.add(new Grade("VALID_ID1",1));
    arrayListManyCase.add(new Grade("VALID_ID2",2));
    arrayListManyCase.add(new Grade("VALID_ID3",3));

    return Stream.of(
      Arguments.of(arrayListNullCase,"INVALID_ID1", HttpStatus.NOT_FOUND.value(), "GET: Grade not found.", null),
      Arguments.of(arrayListOneCase,"VALID_ID1",HttpStatus.OK.value(), null, arrayListOneCase.get(0)),
      Arguments.of(arrayListManyCase,"VALID_ID2", HttpStatus.OK.value(), null, arrayListManyCase.get(1))
    );
  }

  private static Stream<Arguments> addCases() {
    return Stream.of(
      Arguments.of(1, HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of(1, HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of(-1, HttpStatus.BAD_REQUEST.value(),"POST: Grade invalid value.","ADMIN"),
      Arguments.of(14,  HttpStatus.BAD_REQUEST.value(),"POST: Grade invalid value.","ADMIN"),
      Arguments.of(Integer.MAX_VALUE, HttpStatus.BAD_REQUEST.value(),"POST: Grade invalid value.","ADMIN"),
      Arguments.of(Integer.MIN_VALUE, HttpStatus.BAD_REQUEST.value(),"POST: Grade invalid value.","ADMIN"),

      Arguments.of(1,  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of(13,  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of(0,  HttpStatus.CREATED.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> updateCases() {
    return Stream.of(
      Arguments.of(1,"VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of(1,"VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of(1,"VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"PATCH: Grade not found.","ADMIN"),

      Arguments.of(-1, "VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Grade invalid value.","ADMIN"),
      Arguments.of(14,"VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Grade invalid value.","ADMIN"),
      Arguments.of(Integer.MAX_VALUE,"VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Grade invalid value.","ADMIN"),
      Arguments.of(Integer.MIN_VALUE,"VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Grade invalid value.","ADMIN"),

      Arguments.of(1,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(0,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(13,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> deleteByIdCases() {
    return Stream.of(
      Arguments.of(1,"VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of(1,"VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of(1,"VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"DELETE: Grade not found.","ADMIN"),

      Arguments.of(1,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(0,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(13,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(-1,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(14,"VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
      );
  }

  /**
   * Arranges the existence of a list of entries in the database.
   * Performs GET method on "/grades"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * Asserts that the JSON output contains added objects
   *
   * IMPLEMENTATION DETAILS: after the addition of each list of cases, the function
   * goes through all the entries found in the returned JSON in REVERSE ORDER
   * and verifies if each of the added grade appears in the JSON properly.
   * @throws Exception
   * @param input -> List of added grades
   */
  @DisplayName(value = "Get all 'Grades' test")
  @ParameterizedTest
  @MethodSource("getAllCases")
  public void getAllGradesTest(ArrayList<Grade> input) throws Exception {

    // arrange
    gradeRepository.saveAll(input);

    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/grades")).andDo(print())
      .andExpect(status().isOk()).andReturn().getResponse();

    JSONArray parsedGrades = new JSONArray(response.getContentAsString()) ;

    // then
    // Goes through list of test entries
    for (Grade grade: input) {

      Optional<Grade> entity = gradeRepository.findById(grade.getId());
      assertThat(entity).isNotNull();
      assertThat(entity.get().getValue()).isEqualTo(grade.getValue());

      // TIME COMPLEXITY O(T) << O(N) where N is the number of entries in the database and T is the number
      // of entries added in the testing time, including entries that weren't added by the tests.

      // Goes through JSON list to check if the new added entries appear
      JSONObject parsedGrade = null;
      for(int i = parsedGrades.length() - 1; i >= 0; i --) {
        parsedGrade = new JSONObject(parsedGrades.get(i).toString());
        if(parsedGrade.get("id").equals(grade.getId())){
          break;
        }
      }
      // Checks if the entry found in the previous for is the one added by the test
      // It fails if the entry is not added or found
      assertThat(parsedGrade).isNotNull();
      assertThat(parsedGrade.get("id")).isEqualTo(grade.getId());
      assertThat(parsedGrade.get("value")).isEqualTo(grade.getValue());
    }
  }

  /**
   * Arranges the existence of a Grade object at a specified id.
   * Creates a JSON entry that will be expected to receive.
   * Performs GET method at "grades/{@param idParam}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> List of grades;
   * @param idParam -> The id of the wanted grade;
   * @param status -> Expected status of GET function;
   * @param errorMessage -> Expected error message;
   * @param expectedGrade -> The expected to find grade;
   */
  @DisplayName(value = "Get 'Grades' by id test")
  @ParameterizedTest
  @MethodSource("getByIdCases")
  void getGradeByIdTest(ArrayList<Grade> input, String idParam,
                                  Integer status, String errorMessage, Grade expectedGrade) throws Exception {
    gradeRepository.saveAll(input);
    // when
    MockHttpServletResponse response = this.mockMvc
      .perform(get("/grades/" + idParam + "/")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();

      JSONObject parsedGrade = new JSONObject(response.getContentAsString());
      assertThat(parsedGrade.get("id")).isEqualTo(expectedGrade.getId());
      assertThat(parsedGrade.get("value")).isEqualTo(expectedGrade.getValue());
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Grade object as JSON entry.
   * Performs POST at "grade/" with the created JSON.
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> Name of new value;
   * @param status -> Expected status of POST function;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call POST function;
   */
  @DisplayName(value = "Add 'Grades' test")
  @ParameterizedTest
  @MethodSource("addCases")
  public void addGradeTest(Integer input, Integer status, String errorMessage, String role) throws Exception {

    // arrange
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add(input);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    // when
    MockHttpServletResponse response = this.mockMvc.perform(post("/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString())
        .with(user(role).roles(role))).andDo(print())
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    String id = null;
    if(errorMessage == null) {
      JSONObject parsedGrade = new JSONObject(response.getContentAsString());
      Optional<Grade> entity = gradeRepository.findById(parsedGrade.get("id").toString());

      assertThat(response.getContentAsString()).isNotEmpty();
      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getValue()).isEqualTo(input);
      id = parsedGrade.get("id").toString();
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }

    if(id != null)
      gradeRepository.deleteById(id);
  }

  /**
   * Arranges the creation a Grade object as JSON entry
   * and the existence of a Grade object at specified id {@param existentId}.
   * Performs PATCH method at "grade/{@param existentId}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param value -> Name of grade value after update;
   * @param expectedId -> Id of grade that needs to be updated;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of PATCH method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call PATCH function;
   */
  @DisplayName(value = "Update 'Grades' test")
  @ParameterizedTest
  @MethodSource("updateCases")
  void updateGradeTest(Integer value, String expectedId, String wantedId,
                                 Integer status, String errorMessage,String role) throws Exception {

    // arrange
    gradeRepository.save(new Grade(expectedId, value + 2));

    // Json Generator
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add(value);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when
    MockHttpServletResponse response = this.mockMvc.perform(
        patch("/grades/" + wantedId + "/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString())
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();
      Optional<Grade> entity = gradeRepository.findById(wantedId);

      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getId()).isEqualTo(wantedId);
      assertThat(entity.get().getValue()).isEqualTo(value);
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "grade/id0".
   * Asserts that the status is 200.
   * @throws Exception
   * @param value -> Name of grade value to delete ;
   * @param expectedId -> Id of grade that needs to be deleted;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of DELETE method;
   * @param errorMessage -> Expected error message;
   * @param role -> Role of the user trying to call DELETE function;
   */
  @DisplayName(value = "Delete 'Grades' test")
  @ParameterizedTest
  @MethodSource("deleteByIdCases")
  void deleteGradeByIdNotFoundExceptionTest(Integer value, String expectedId, String wantedId,
                                                      Integer status, String errorMessage, String role) throws Exception {

    gradeRepository.save(new Grade(expectedId, value));
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/grades/" + wantedId + "/")
          .accept(MediaType.APPLICATION_JSON)
          .with(user(role).roles(role)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
    assertThat(response.getContentAsString()).isEmpty();

    if(errorMessage == null) {
      assertThat(gradeRepository.findById(expectedId).isEmpty()).isTrue();
    }
  }

}
