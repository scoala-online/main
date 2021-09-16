package org.scoalaonline.api.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.service.LectureMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
  void getLectureMaterialById() throws Exception {
    //given
    given(lectureMaterialService.getOneById("id0"))
      .willReturn(lectureMaterialList.get(0));

    //Json Generator
    // !! TO DO MAKE A FUNCTION FOR IT !!
    // !! FOR NO REPETITIVE CODE !!
    JsonFactory factory = new JsonFactory();
    StringWriter jsonObjectWriter = new StringWriter();
    JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

    generator.useDefaultPrettyPrinter();
    generator.useDefaultPrettyPrinter();
    generator.writeStartObject();
    generator.writeFieldName("id");
    generator.writeString("id0");
    generator.writeFieldName("document");
    generator.writeString("Document_1.pdf");
    generator.writeEndObject();
    generator.close();

    //when & then
    this.mockMvc.perform( get("/lecture-materials/id0")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }
  @Test
  void addLectureMaterialTest() throws Exception {
    //given
    given(lectureMaterialService.getOneById("id3"))
      .willThrow(LectureMaterialNotFoundException.class);

    //Json Generator
    // !! TO DO MAKE A FUNCTION FOR IT !!
    // !! FOR NO REPETITIVE CODE !!
    JsonFactory factory = new JsonFactory();
    StringWriter jsonObjectWriter = new StringWriter();
    JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

    generator.useDefaultPrettyPrinter();
    generator.useDefaultPrettyPrinter();
    generator.writeStartObject();
    generator.writeFieldName("id");
    generator.writeString("id3");
    generator.writeFieldName("document");
    generator.writeString("Document_3.pdf");
    generator.writeEndObject();
    generator.close();


    //when & then
    this.mockMvc.perform(
      post("/lecture-materials")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();

//        assertThat(lectureMaterialService.getOneById("id3").getDocument()
//                .equals("Document_3.pdf"))
//                .isTrue();
  }

  @Test
  @Disabled
  void updateLectureMaterial() {
  }

  @Test
  @Disabled
  void deleteLectureMaterial() {
  }
}
