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
import org.scoalaonline.api.exception.role.RoleInvalidNameException;
import org.scoalaonline.api.exception.role.RoleNotFoundException;
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.service.RoleService;
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
class RoleControllerTest {
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private RoleService roleService;

  private List<Role> roleList;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    this.roleList = new ArrayList<>();
    this.roleList.add(new Role("id0", "ROLE_1"));
    this.roleList.add(new Role("id1", "ROLE_2"));
    this.roleList.add(new Role("id2", "ROLE_3"));
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs GET method on "/roles"
   * Asserts that returns 200 status and the
   * content size is equal to the number of objects in the database.
   * @throws Exception
   */
  @DisplayName(value = "Test getting all roles.")
  @Test
  void getAllRolesTest() throws Exception{
    given(roleService.getAll()).willReturn(roleList);

    this.mockMvc.perform(get("/roles")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
      .andReturn();
  }

  /**
   * Arranges the existence of a Role object at a specified id.
   * Creates a JSON entry that will be expected to be received.
   * Performs GET method at "roles/id0".
   * Asserts that the status is 200 and the object returned has the same
   * attribute values as the expected one.
   * @throws Exception
   */
  @DisplayName(value = "Test getting a role by id.")
  @Test
  void getRoleByIdTest() throws Exception {
    //given
    given(roleService.getOneById("id0"))
      .willReturn(roleList.get(0));

    //Json Generator

    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("id");
    FieldArray.add("name");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("id0");
    ValuesArray.add("ROLE_1");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform( get("/roles/id0")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonObjectWriter.toString()))
      .andReturn().getResponse();
  }

  /**
   * Arranges the absence of a Role object with the given id ("id3").
   * Performs GET method at "roles/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */

  @DisplayName(value = "Test getting a role by id and expect 'Role not found' exception.")
  @Test
  void getRoleByIdNotFoundExceptionTest() throws Exception {
    // given
    given(roleService.getOneById("id3"))
      .willThrow(new RoleNotFoundException());

    // when
    MockHttpServletResponse response = mockMvc.perform(
      get("/roles/id3")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the creation a Role object as JSON entry.
   * Performs POST at "roles/" with the created JSON.
   * Asserts that the given status is 201.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a role.")
  @Test
  void addRoleTest() throws Exception {
    //given

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("name");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("ROLE_3");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when & then
    this.mockMvc.perform(
      post("/roles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andExpect(status().isCreated())
      .andReturn();
  }

  /**
   * Arranges the creation of Role objects
   * with invalid attribute values.
   * Performs POST method at "roles/".
   * Asserts that the given status is 400 and that the
   * database remains empty.
   * @throws Exception
   */
  @DisplayName(value = "Test adding a role and expect 'Invalid Data' exception.")
  @Test
  void addRoleInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases) {
      // given
      given(roleService.add(new Role(null,(String) exceptionCase)))
        .willThrow(RoleInvalidNameException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("name");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
        post("/roles").contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation a Role object as JSON entry
   * and the existence of a Role object at specified id ("id0").
   * Performs PATCH method at "roles/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a role.")
  @Test
  void updateRoleTest() throws Exception {
    // given
    given(roleService.getAll()).willReturn(roleList);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("name");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("ROLE_3");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);


    //when & then
    this.mockMvc.perform(
      patch("/roles/id0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andExpect(status().isOk())
      .andReturn();
  }

  /**
   * Arranges the creation of Role objects
   * with invalid attribute values.
   * Performs PATCH method at "roles/id0".
   * Asserts that the given status is 400 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a role and expect 'Invalid Data' exception.")
  @Test
  void updateRoleInvalidDataExceptionTest() throws Exception {

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase: exceptionCases) {
      // given
      given(roleService.getAll())
        .willReturn(roleList);
      given(roleService.update("id0",new Role(null, (String) exceptionCase)))
        .willThrow(RoleInvalidNameException.class);

      //Json Generator
      List<String> FieldArray = new ArrayList<String>();
      FieldArray.add("name");
      List<Object> ValuesArray = new ArrayList<Object>();
      ValuesArray.add(exceptionCase);
      StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

      //when
      MockHttpServletResponse response = mockMvc.perform(
        patch("/roles/id0")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonObjectWriter.toString()))
        .andReturn().getResponse();

      //then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the absence of a Role object with the given id ("id3").
   * Performs PATCH method at "roles/id3".
   * Asserts that the given status is 404 and that the
   * response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test updating a role and expecting 'Role not found' exception.")
  @Test
  void updateRoleByIdNotFoundExceptionTest() throws Exception {
    // given
    given(roleService.update("id3",new Role(null, "Name")))
      .willThrow(RoleNotFoundException.class);

    //Json Generator
    List<String> FieldArray = new ArrayList<String>();
    FieldArray.add("name");
    List<Object> ValuesArray = new ArrayList<Object>();
    ValuesArray.add("Name");
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, ValuesArray);

    //when
    MockHttpServletResponse response = mockMvc.perform(
      patch("/roles/id3")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString()))
      .andReturn().getResponse();


    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "roles/id0".
   * Asserts that the status is 200.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a role.")
  @Test
  void deleteRoleTest() throws Exception {
    // given
    given(roleService.getAll()).willReturn(roleList);

    // when & then
    this.mockMvc.perform(
      delete("/roles/id0")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

  }

  /**
   * Arranges the absence of a Role object with the given id ("id3").
   * Performs DELETE method at "roles/id3".
   * Asserts that the status is 404 and the response is empty.
   * @throws Exception
   */
  @DisplayName(value = "Test deleting a role and expect 'Role not found' exception.")
  @Test
  void deleteRoleByIdNotFoundExceptionTest() throws Exception {
    // given
    doThrow(RoleNotFoundException.class).when(roleService).delete("id3");
    // when
    MockHttpServletResponse response = mockMvc.perform(
      delete("/roles/id3")
        .accept(MediaType.APPLICATION_JSON))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getContentAsString()).isEmpty();
  }
}
