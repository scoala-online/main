package org.scoalaonline.api.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.scoalaonline.api.util.TestUtils.buildJsonBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoalaonline.api.exception.grade.GradeInvalidValueException;
import org.scoalaonline.api.exception.grade.GradeNotFoundException;
import org.scoalaonline.api.model.Grade;
import org.scoalaonline.api.service.GradeService;
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

@AutoConfigureJsonTesters
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
class GradeControllerTest {
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private GradeService gradeService;

  private List<Grade> gradeList;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    this.gradeList = new ArrayList<>();
    this.gradeList.add(new Grade("id0", 0));
    this.gradeList.add(new Grade("id1", 1));
    this.gradeList.add(new Grade("id2", 2));
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs GET method on "/grades"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * @throws Exception
   */
  @DisplayName(value = "Test getting all grades.")
  @Test
  void getAllGradesTest() throws Exception{
    given(gradeService.getAll()).willReturn(gradeList);

    this.mockMvc.perform(get("/grades")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
      .andReturn();
  }

  /**
   * Arranges the existence of a Grade object at a specified id.
   * Creates a JSON entry that will be expected to be received.
   * Performs GET method at "grades/id0".
   * Asserts that the status is 200 and the object returned has the same
   * attribute values as the expected one.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a grade by id.")
  @Test
  void getGradeByIdTest() throws Exception {
    //given
    given(gradeService.getOneById("id0"))
      .willReturn(gradeList.get(0));

    //Json Generator

    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("id");
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("id0");
    ValuesArray.add(0);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform( get("/grades/id0")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }

  /**
   * Arranges the absence of a Grade object with the given id ("id3").
   * Performs GET method at "grade/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a grade by id and expect 'Grade not found' exception.")
  @Test
  void getGradeByIdNotFoundExceptionTest() throws Exception {
    // given
    given(gradeService.getOneById("id3"))
      .willThrow(new GradeNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
        get("/grades/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the creation a Grade object as JSON entry.
   * Performs POST at "grades/" with the created JSON.
   * Asserts that the given status is 201.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a grade.")
  @Test
  void addGradeTest() throws Exception {
    //given

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add(2);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform(
        post("/grades")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();
  }

  /**
   * Arranges the creation of Grade objects
   * with invalid attribute values.
   * Performs POST method at "grades/".
   * Asserts that the given status is 400 and that the
   * database remains empty.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a grade and expect 'Invalid Value' exception.")
  @Test
  void addGradeInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(14);
    exceptionCases.add(-1);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(gradeService.add(new Grade(null, (Integer) exceptionCase)))
        .willThrow(GradeInvalidValueException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("value");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/grades").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Grade object as JSON entry
   * and the existence of a Grade object at specified id ("id0").
   * Performs PATCH method at "grades/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a grade.")
  @Test
  void updateGradeTest() throws Exception {
    // given
    given(gradeService.getAll()).willReturn(gradeList);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add(2);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when & then
    this.mockMvc.perform(
        patch("/grades/id0")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isOk())
      .andReturn();
  }

  /**
   * Arranges the creation of Grade objects
   * with invalid attribute values.
   * Performs PATCH method at "grades/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a grade and expect 'Invalid value' exception.")
  @Test
  void updateGradeInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add(14);
    exceptionCases.add(-1);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(gradeService.getAll())
        .willReturn(gradeList);
      given(gradeService.update("id0",new Grade(null, (int) exceptionCase)))
        .willThrow(GradeInvalidValueException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("value");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/grades/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the absence of a Grade object with the given id ("id3").
   * Performs PATCH method at "grade/id3".
   * Asserts that the given status is 404 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a grade and expecting 'Grade not found' exception.")
  @Test
  void updateGradeByIdNotFoundExceptionTest() throws Exception {
    // given
    given(gradeService.update("id3",new Grade(null, 4)))
      .willThrow(GradeNotFoundException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add(4);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when
    MockHttpServletResponse response = mockMvc.perform(
        patch("/grades/id3")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();


    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "grade/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a grade.")
  @Test
  void deleteGradeTest() throws Exception {
    // given
    given(gradeService.getAll()).willReturn(gradeList);

    // when & then
    this.mockMvc.perform(
        delete("/grades/id0")
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

  }

  /**
   * Arranges the absence of a Grade object with the given id ("id3").
   * Performs DELETE method at "grades/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a grade and expect 'Grade not found' exception.")
  @Test
  void deleteGradeByIdNotFoundExceptionTest() throws Exception {
    // given
    doThrow(GradeNotFoundException.class).when(gradeService).delete("id3");
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/grades/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }
}
