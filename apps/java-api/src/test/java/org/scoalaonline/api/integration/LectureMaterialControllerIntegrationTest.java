package org.scoalaonline.api.integration;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoalaonline.api.controller.LectureMaterialController;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.repository.LectureMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.scoalaonline.api.util.TestUtils.buildJsonBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithMockUser(roles={"ADMIN"})
@WebAppConfiguration
public class LectureMaterialControllerIntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private LectureMaterialRepository lectureMaterialRepository;

  private  LectureMaterial lectureMaterialToSave;

  private MockMvc mockMvc;
  @BeforeEach
  public void beforeTestSetup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    // Create a lecture material to add to database
    lectureMaterialToSave = new LectureMaterial();
    lectureMaterialToSave.setDocument("EXAMPLE_DOCUMENT.txt");
    lectureMaterialRepository.save(lectureMaterialToSave);
  }
  @AfterEach
  public void afterTestSetup() {
    lectureMaterialRepository.delete(lectureMaterialToSave);
  }

  @Test
  public void givenWac_whenServletContext_thenItProvidesGreetController() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
  }

  @Test
  public void getAllLectureMaterialsTest() throws Exception {
    this.mockMvc.perform(get("/lecture-materials")).andDo(print())
      .andExpect(status().isOk());
    Optional<LectureMaterial> entity = lectureMaterialRepository.findById(lectureMaterialToSave.getId());
    assertThat(entity.get().getDocument()).isEqualTo("EXAMPLE_DOCUMENT.txt");
  }

  @Test
  public void addLectureMaterialTest() throws Exception {
    LectureMaterial entity = new LectureMaterial();

    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("document");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("EXAMPLE_POST_DOCUMENT.pdf");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    this.mockMvc.perform(post("/lecture-materials")
      .contentType(MediaType.APPLICATION_JSON)
      .content(jsonObjectWriter.toString())).andDo(print())
      .andExpect(status().isCreated());

    List<LectureMaterial> entities = lectureMaterialRepository.findAll();

    for (LectureMaterial lectureMaterialToFind: entities) {
      if(lectureMaterialToFind.getDocument().equals("EXAMPLE_POST_DOCUMENT.pdf")) {
        entity = lectureMaterialToFind;
        break;
      }
    }

    assertThat(entity.getDocument()).isEqualTo("EXAMPLE_POST_DOCUMENT.pdf");
    lectureMaterialRepository.delete(entity);
  }
}
