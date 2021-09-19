package org.scoalaonline.api.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scoalaonline.api.exception.LectureMaterialInvalidDocumentException;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.service.LectureMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.scoalaonline.api.util.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@WebMvcTest(controllers = LectureMaterialController.class)
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

  @Test
  void getLectureMaterialByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureMaterialService.getOneById("id3"))
      .willThrow(new LectureMaterialNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
      get("/lecture-material/id3")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

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

  //TODO: MAKE IT IN A WHILE LOOP FOR "" CASE AND NULL CASE
  @Test
  void addLectureMaterialInvalidDataExceptionTest() throws Exception {
    // Empty String Case
    // given
    given(lectureMaterialService.add(new LectureMaterial(null,"")))
      .willThrow(LectureMaterialInvalidDocumentException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when
    MockHttpServletResponse response = mockMvc.perform(
      post("/lecture-materials")
        .contentType(MediaType.APPLICATION_JSON)
    .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();

    //then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response.getContentAsString()).isEmpty();


    // Null String Case
    // given
    given(lectureMaterialService.add(new LectureMaterial(null,null)))
      .willThrow(LectureMaterialInvalidDocumentException.class);

    //Json Generator
    List<String> FieldArrayNull = new ArrayList<String>();
    FieldArrayNull.add("document");
    List<Object> ValuesArrayNull = new ArrayList<Object>();
    ValuesArrayNull.add(null);
    StringWriter jsonObjectWriterNull = buildJsonBody(FieldArrayNull, ValuesArrayNull);


    //when
    MockHttpServletResponse response_null = mockMvc.perform(
      post("/lecture-materials")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriterNull.toString()))
      .andReturn().getResponse();

    //then
    assertThat(response_null.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response_null.getContentAsString()).isEmpty();
  }
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
  //TODO: MAKE IT IN A WHILE LOOP FOR "" CASE AND NULL CASE
  @Test
  void updateLectureMaterialInvalidDataExceptionTest() throws Exception {
    // Empty String Case
    // given
    given(lectureMaterialService.getAll())
      .willReturn(lectureMaterialList);
    given(lectureMaterialService.update("id0",new LectureMaterial(null,"")))
      .willThrow(LectureMaterialInvalidDocumentException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("");
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


    // Null String Case
    // given
    given(lectureMaterialService.getAll())
      .willReturn(lectureMaterialList);
    given(lectureMaterialService.update("id0",new LectureMaterial(null,null)))
      .willThrow(LectureMaterialInvalidDocumentException.class);

    //Json Generator
    List<String> FieldArrayNull = new ArrayList<String>();
    FieldArrayNull.add("document");
    List<Object> ValuesArrayNull = new ArrayList<Object>();
    ValuesArrayNull.add(null);
    StringWriter jsonObjectWriterNull = buildJsonBody(FieldArrayNull, ValuesArrayNull);


    //when
    MockHttpServletResponse response_null = mockMvc.perform(
      patch("/lecture-materials/id0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriterNull.toString()))
      .andReturn().getResponse();

    //then
    assertThat(response_null.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(response_null.getContentAsString()).isEmpty();
  }

  @Test
  void updateLectureMaterialByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureMaterialService.getOneById("id3"))
      .willThrow(new LectureMaterialNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
      patch("/lecture-material/id3")
    .contentType(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

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
  @Test
  void deleteLectureMaterialByIdNotFoundExceptionTest() throws Exception {
    // given
    given(lectureMaterialService.getOneById("id3"))
      .willThrow(new LectureMaterialNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
      delete("/lecture-material/id3")
        .contentType(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }


}
