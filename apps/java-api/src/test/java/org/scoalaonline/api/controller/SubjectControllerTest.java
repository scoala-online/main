package org.scoalaonline.api.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoalaonline.api.exception.subject.SubjectInvalidValueException;
import org.scoalaonline.api.exception.subject.SubjectNotFoundException;
import org.scoalaonline.api.model.Subject;
import org.scoalaonline.api.service.SubjectService;
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
class SubjectControllerTest {
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private SubjectService subjectService;

  private List<Subject> subjectList;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    this.subjectList = new ArrayList<>();
    this.subjectList.add(new Subject("id0", "value_0"));
    this.subjectList.add(new Subject("id1", "value_1"));
    this.subjectList.add(new Subject("id2", "value_2"));
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs GET method on "/subjects"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * @throws Exception
   */
  @DisplayName(value = "Test getting all subjects.")
  @Test
  void getAllSubjectsTest() throws Exception{
    given(subjectService.getAll()).willReturn(subjectList);

    this.mockMvc.perform(get("/subjects")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
      .andReturn();
  }

  /**
   * Arranges the existence of a Subject object at a specified id.
   * Creates a JSON entry that will be expected to be received.
   * Performs GET method at "subjects/id0".
   * Asserts that the status is 200 and the object returned has the same
   * attribute values as the expected one.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a subject by id.")
  @Test
  void getSubjectByIdTest() throws Exception {
    // given
    given(subjectService.getOneById("id0")).willReturn(subjectList.get(0));

    // JSON Generator

    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("id");
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<>();
    ValuesArray.add("id0");
    ValuesArray.add("value_0");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    // when & then
    this.mockMvc.perform(get("/subjects/id0")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }

  /**
   * Arranges the absence of a Subject object with the given id ("id3")
   * Performs GET method at "subjects/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a subject by id and expect 'Subject not found' exception.")
  @Test
  void getSubjectByIdNotFoundExceptionTest() throws Exception {
    //given
    given(subjectService.getOneById("id3"))
      .willThrow(new SubjectNotFoundException());

    //when
    MockHttpServletResponse response = mockMvc.perform(
      get("/subjects/id3")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    //then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString().isEmpty());
  }

  /**
   * Arranges the creation a Subject object as JSON entry.
   * Performs POST at "subjects/" with the created JSON.
   * Asserts that the given status is 201.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a subject.")
  @Test
  void addSubjectTest() throws Exception {
    //given

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("value_2");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform(
        post("/subjects")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();
  }

  /**
   * Arranges the creation of Subject objects
   * with invalid attribute values.
   * Performs POST method at "subjects/".
   * Asserts that the given status is 400 and that the
   * database remains empty.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a subject and expect 'Invalid Value' exception.")
  @Test
  void addSubjectInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(subjectService.add(new Subject(null,(String) exceptionCase)))
        .willThrow(SubjectInvalidValueException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("value");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/subjects").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Subject object as JSON entry
   * and the existence of a Subject object at specified id ("id0").
   * Performs PATCH method at "subject/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a subject.")
  @Test
  void updateSubjectTest() throws Exception {
    // given
    given(subjectService.getAll()).willReturn(subjectList);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("value_2");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when & then
    this.mockMvc.perform(
        patch("/subjects/id0")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isOk())
      .andReturn();
  }

  /**
   * Arranges the creation of Subject objects
   * with invalid attribute values.
   * Performs PATCH method at "subject/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a subject and expect 'Invalid Data' exception.")
  @Test
  void updateSubjectInvalidValueExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(subjectService.getAll())
        .willReturn(subjectList);
      given(subjectService.update("id0",new Subject(null, (String) exceptionCase)))
        .willThrow(SubjectInvalidValueException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("value");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/subjects/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the absence of a Subject object with the given id ("id3").
   * Performs PATCH method at "subject/id3".
   * Asserts that the given status is 404 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a subject and expecting 'Lecture Material not found' exception.")
  @Test
  void updateSubjectByIdNotFoundExceptionTest() throws Exception {
    // given
    given(subjectService.update("id3",new Subject(null, "value")))
      .willThrow(SubjectNotFoundException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("value");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("value");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when
    MockHttpServletResponse response = mockMvc.perform(
        patch("/subjects/id3")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();


    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "subject/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a subject.")
  @Test
  void deleteSubjectTest() throws Exception {
    // given
    given(subjectService.getAll()).willReturn(subjectList);

    // when & then
    this.mockMvc.perform(
        delete("/subjects/id0")
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

  }

  /**
   * Arranges the absence of a Subject object with the given id ("id3").
   * Performs DELETE method at "subjects/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a subject and expect 'Lecture Material not found' exception.")
  @Test
  void deleteSubjectByIdNotFoundExceptionTest() throws Exception {
    // given
    doThrow(SubjectNotFoundException.class).when(subjectService).delete("id3");
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/subjects/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

}
