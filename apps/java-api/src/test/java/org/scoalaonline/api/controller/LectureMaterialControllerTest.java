package org.scoalaonline.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoalaonline.api.exception.lectureMaterial.LectureMaterialInvalidDocumentException;
import org.scoalaonline.api.exception.lectureMaterial.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.service.LectureMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.scoalaonline.api.util.TestUtils.buildJsonBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(roles={"ADMIN"})
@ActiveProfiles("test")
class LectureMaterialControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LectureMaterialService lectureMaterialService;

  private List<LectureMaterial> lectureMaterialList;

  @BeforeEach
  void setUp() {
    this.lectureMaterialList = new ArrayList<>();
    this.lectureMaterialList.add(new LectureMaterial("id0", "Document_1.pdf"));
    this.lectureMaterialList.add(new LectureMaterial("id1", "Document_2.pdf"));
    this.lectureMaterialList.add(new LectureMaterial("id2", "Document_3.pdf"));
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs GET method on "/lecture-materials"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * @throws Exception
   */
  @DisplayName(value = "Test getting all lecture materials.")
  @Test
  void getAllLectureMaterialsTest() throws Exception{
    given(lectureMaterialService.getAll()).willReturn(lectureMaterialList);

    this.mockMvc.perform(get("/lecture-materials")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
      .andReturn();
  }

  /**
   * Arranges the existence of a LectureMaterial object at a specified id.
   * Creates a JSON entry that will be expected to receive.
   * Performs GET method at "lecture-materials/id0".
   * Asserts that the status is 200 and the object returned has the same
   * attribute values as the expected one.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a lecture material by id.")
  @Test
  void getLectureMaterialByIdTest() throws Exception {
    //given
    given(lectureMaterialService.getOneById("id0"))
      .willReturn(lectureMaterialList.get(0));

    //Json Generator

    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("id");
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("id0");
    ValuesArray.add("Document_1.pdf");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform( get("/lecture-materials/id0")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }

  /**
   * Arranges the absence of a LectureMaterial object with the given id ("id3").
   * Performs GET method at "lecture-material/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */

  @DisplayName(value = "Test getting a lecture material by id and expect 'Lecutre Material not found' exception.")
  @Test
  void getLectureMaterialByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureMaterialService.getOneById("id3"))
      .willThrow(new LectureMaterialNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
      get("/lecture-materials/id3")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the creation a LectureMaterial object as JSON entry.
   * Performs POST at "lecture-material/" with the created JSON.
   * Asserts that the given status is 201.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a lecture material.")
  @Test
  void addLectureMaterialTest() throws Exception {
    //given

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Document_3.pdf");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform(
      post("/lecture-materials")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();
  }

  /**
   * Arranges the creation of LectureMaterial objects
   * with invalid attribute values.
   * Performs POST method at "lecture-material/".
   * Asserts that the given status is 400 and that the
   * database remains empty.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a lecture material and expect 'Invalid Data' exception.")
  @Test
  void addLectureMaterialInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(lectureMaterialService.add(new LectureMaterial(null,(String) exceptionCase)))
        .willThrow(LectureMaterialInvalidDocumentException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("document");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
       post("/lecture-materials").contentType(MediaType.APPLICATION_JSON)
      .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the cration a LectureMaterial object as JSON entry
   * and the existence of a LectureMaterial object at specified id ("id0").
   * Performs PATCH method at "lecture-material/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a lecture material.")
  @Test
  void updateLectureMaterialTest() throws Exception {
    // given
    given(lectureMaterialService.getAll()).willReturn(lectureMaterialList);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Document_3.pdf");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when & then
    this.mockMvc.perform(
      patch("/lecture-materials/id0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andExpect(status().isOk())
      .andReturn();
  }
  /**
   * Arranges the creation of LectureMaterial objects
   * with invalid attribute values.
   * Performs PATCH method at "lecture-material/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a lecture material and expect 'Invalid Data' exception.")
  @Test
  void updateLectureMaterialInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(lectureMaterialService.getAll())
        .willReturn(lectureMaterialList);
      given(lectureMaterialService.update("id0",new LectureMaterial(null, (String) exceptionCase)))
        .willThrow(LectureMaterialInvalidDocumentException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("document");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
        patch("/lecture-materials/id0")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }
  /**
   * Arranges the absence of a LectureMaterial object with the given id ("id3").
   * Performs PATCH method at "lecture-material/id3".
   * Asserts that the given status is 404 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a lecture material and expecting 'Lecture Material not found' exception.")
  @Test
  void updateLectureMaterialByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureMaterialService.update("id3",new LectureMaterial(null, "Document")))
      .willThrow(LectureMaterialNotFoundException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Document");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when
    MockHttpServletResponse response = mockMvc.perform(
      patch("/lecture-materials/id3")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();


    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "lecture-material/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a lecture material.")
  @Test
  void deleteLectureMaterialTest() throws Exception {
    // given
    given(lectureMaterialService.getAll()).willReturn(lectureMaterialList);

    // when & then
    this.mockMvc.perform(
      delete("/lecture-materials/id0")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

  }
  /**
   * Arranges the absence of a LectureMaterial object with the given id ("id3").
   * Performs DELETE method at "lecture-materials/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a lecture material and expect 'Lecture Material not found' exception.")
  @Test
  void deleteLectureMaterialByIdNotFoundExceptionTest() throws Exception {
    // given
    doThrow(LectureMaterialNotFoundException.class).when(lectureMaterialService).delete("id3");
    // when
    MockHttpServletResponse response = mockMvc.perform(
      delete("/lecture-materials/id3")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }


}
