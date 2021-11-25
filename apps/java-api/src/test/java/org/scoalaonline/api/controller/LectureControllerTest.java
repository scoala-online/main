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
import org.scoalaonline.api.exception.lecture.LectureInvalidTitleException;
import org.scoalaonline.api.exception.lecture.LectureNotFoundException;
import org.scoalaonline.api.model.Lecture;
import org.scoalaonline.api.service.LectureService;
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
class LectureControllerTest {
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private LectureService lectureService;

  private List<Lecture> lectureList;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    this.lectureList = new ArrayList<>();
    this.lectureList.add(new Lecture("id0", "Title_1"));
    this.lectureList.add(new Lecture("id1", "Title_2"));
    this.lectureList.add(new Lecture("id2", "Title_3."));
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs GET method on "/lectures"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * @throws Exception
   */
  @DisplayName(value = "Test getting all lectures.")
  @Test
  void getAllLecturesTest() throws Exception{
    given(lectureService.getAll()).willReturn(lectureList);

    this.mockMvc.perform(get("/lectures")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
      .andReturn();
  }

  /**
   * Arranges the existence of a Lecture object at a specified id.
   * Creates a JSON entry that will be expected to be received.
   * Performs GET method at "lectures/id0".
   * Asserts that the status is 200 and the object returned has the same
   * attribute values as the expected one.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a lecture by id.")
  @Test
  void getLectureByIdTest() throws Exception {
    //given
    given(lectureService.getOneById("id0"))
      .willReturn(lectureList.get(0));

    //Json Generator

    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("id");
    FieldArray.add("title");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("id0");
    ValuesArray.add("Title_1");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform( get("/lectures/id0")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }

  /**
   * Arranges the absence of a Lecture object with the given id ("id3").
   * Performs GET method at "lecture/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */

  @DisplayName(value = "Test getting a lecture by id and expect 'Lecture not found.' exception.")
  @Test
  void getLectureByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureService.getOneById("id3"))
      .willThrow(new LectureNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
        get("/lectures/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the creation a Lecture object as JSON entry.
   * Performs POST at "lecture/" with the created JSON.
   * Asserts that the given status is 201.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a lecture.")
  @Test
  void addLectureTest() throws Exception {
    //given

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("title");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Title_3");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform(
        post("/lectures")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();
  }

  /**
   * Arranges the creation of Lecture objects
   * with invalid attribute values.
   * Performs POST method at "lecture/".
   * Asserts that the given status is 400 and that the
   * database remains empty.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a lecture and expect 'Invalid title.' exception.")
  @Test
  void addLectureInvalidTitleExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(lectureService.add(new Lecture(null,(String) exceptionCase)))
        .willThrow(LectureInvalidTitleException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("title");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          post("/lectures").contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Lecture object as JSON entry
   * and the existence of a Lecture object at specified id ("id0").
   * Performs PATCH method at "lecture/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a lecture.")
  @Test
  void updateLectureTest() throws Exception {
    // given
    given(lectureService.getAll()).willReturn(lectureList);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("title");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Title_3");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when & then
    this.mockMvc.perform(
        patch("/lectures/id0")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andExpect(status().isOk())
      .andReturn();
  }

  /**
   * Arranges the creation of Lecture objects
   * with invalid attribute values.
   * Performs PATCH method at "lecture/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a lecture and expect 'Invalid title.' exception.")
  @Test
  void updateLectureInvalidTitleExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(lectureService.getAll())
        .willReturn(lectureList);
      given(lectureService.update("id0",new Lecture(null, (String) exceptionCase)))
        .willThrow(LectureInvalidTitleException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("title");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
          patch("/lectures/id0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the absence of a Lecture object with the given id ("id3").
   * Performs PATCH method at "lecture/id3".
   * Asserts that the given status is 404 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a lecture and expecting 'Lecture not found.' exception.")
  @Test
  void updateLectureByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureService.update("id3",new Lecture(null, "Title")))
      .willThrow(LectureNotFoundException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("title");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Title");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when
    MockHttpServletResponse response = mockMvc.perform(
        patch("/lectures/id3")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();


    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "lecture/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a lecture.")
  @Test
  void deleteLectureTest() throws Exception {
    // given
    given(lectureService.getAll()).willReturn(lectureList);

    // when & then
    this.mockMvc.perform(
        delete("/lectures/id0")
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

  }

  /**
   * Arranges the absence of a Lecture object with the given id ("id3").
   * Performs DELETE method at "lectures/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a lecture and expect 'Lecture not found.' exception.")
  @Test
  void deleteLectureByIdNotFoundExceptionTest() throws Exception {
    // given
    doThrow(LectureNotFoundException.class).when(lectureService).delete("id3");
    // when
    MockHttpServletResponse response = mockMvc.perform(
        delete("/lectures/id3")
          .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }
}
