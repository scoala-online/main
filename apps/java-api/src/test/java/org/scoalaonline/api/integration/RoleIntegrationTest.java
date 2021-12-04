package org.scoalaonline.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.scoalaonline.api.util.TestUtils.buildJsonBody;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.repository.RoleRepository;
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
public class RoleIntegrationTest {

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  void afterTests(){
    roleRepository.deleteById("ID1");
    roleRepository.deleteById("ID2");
    roleRepository.deleteById("ID3");
    roleRepository.deleteById("VALID_ID1");
    roleRepository.deleteById("VALID_ID2");
    roleRepository.deleteById("VALID_ID3");
    roleRepository.deleteById("INVALID_ID1");
    roleRepository.deleteById("VALID_ID");
  }

  private static Stream<Arguments> getAllCases() {

    // Create a role to add to database
    ArrayList<Role> arrayListNullCase = new ArrayList<>();
    ArrayList<Role> arrayListOneCase = new ArrayList<>();
    ArrayList<Role> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Role("ID1","ROLE_1"));

    arrayListManyCase.add(new Role("ID1","ROLE_1"));
    arrayListManyCase.add(new Role("ID2","ROLE_2"));
    arrayListManyCase.add(new Role("ID3","ROLE_3"));

    return Stream.of(
      Arguments.of(arrayListOneCase, HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of(arrayListOneCase, HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),
      Arguments.of(arrayListNullCase, HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(arrayListOneCase, HttpStatus.OK.value(),null,"ADMIN"),
      Arguments.of(arrayListManyCase, HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> getByIdCases() {
    ArrayList<Role> arrayListNullCase = new ArrayList<>();
    ArrayList<Role> arrayListOneCase = new ArrayList<>();
    ArrayList<Role> arrayListManyCase = new ArrayList<>();

    arrayListOneCase.add(new Role("VALID_ID1","ROLE_1"));

    arrayListManyCase.add(new Role("VALID_ID1","ROLE_1"));
    arrayListManyCase.add(new Role("VALID_ID2","ROLE_2"));
    arrayListManyCase.add(new Role("VALID_ID3","ROLE_3"));

    return Stream.of(
      Arguments.of(arrayListOneCase, "VALID_ID1", HttpStatus.FORBIDDEN.value(),"Forbidden", null,"STUDENT"),
      Arguments.of(arrayListOneCase, "VALID_ID1", HttpStatus.FORBIDDEN.value(),"Forbidden", null, "TEACHER"),
      Arguments.of(arrayListNullCase, "INVALID_ID1", HttpStatus.NOT_FOUND.value(), "GET: Role Not Found", null, "ADMIN"),
      Arguments.of(arrayListOneCase, "VALID_ID1",HttpStatus.OK.value(), null, arrayListOneCase.get(0), "ADMIN"),
      Arguments.of(arrayListManyCase, "VALID_ID2", HttpStatus.OK.value(), null, arrayListManyCase.get(1), "ADMIN")
    );
  }

  private static Stream<Arguments> addCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_ROLE", HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_ROLE", HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("", HttpStatus.BAD_REQUEST.value(),"POST: Role Invalid Name","ADMIN"),
      Arguments.of(null,  HttpStatus.BAD_REQUEST.value(),"POST: Role Invalid Name","ADMIN"),
      Arguments.of("NOT_NULL_NAME",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("ăâîșț",  HttpStatus.CREATED.value(),null,"ADMIN"),
      Arguments.of("!@#$%^&*()„”=+[]{};:'\"\\|,<>/?1234567890-_   ",  HttpStatus.CREATED.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> updateCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_ROLE","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_ROLE","VALID_ID", "VALID_ID",HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("INVALID_ID_ROLE","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"PATCH: Role Not Found","ADMIN"),
      Arguments.of("", "VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Role Invalid Name","ADMIN"),
      Arguments.of(null,"VALID_ID","VALID_ID",HttpStatus.BAD_REQUEST.value(),"PATCH: Role Invalid Name","ADMIN"),
      Arguments.of("NOT_NULL_NAME","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  private static Stream<Arguments> deleteByIdCases() {
    return Stream.of(
      Arguments.of("UNAUTHORIZED_ROLE","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","STUDENT"),
      Arguments.of("UNAUTHORIZED_ROLE","VALID_ID","VALID_ID" , HttpStatus.FORBIDDEN.value(),"Forbidden","TEACHER"),

      Arguments.of("ROLE_TO_BE_DELETED","VALID_ID", "INVALID_ID",HttpStatus.NOT_FOUND.value(),"DELETE: Role Not Found","ADMIN"),
      Arguments.of("ROLE_TO_BE_DELETED","VALID_ID","VALID_ID" , HttpStatus.OK.value(),null,"ADMIN")
    );
  }

  /**
   * Arranges the existence of a list of entries in the database.
   * Performs GET method on "/roles".
   * Asserts that the status is {@param status}.
   * Asserts that the content size is equal to the number of objects in the database.
   * Asserts that the JSON output contains added objects.
   * Otherwise asserts that the {@param errorMessage} is the expected one, and that the body's content is empty.
   *
   * IMPLEMENTATION DETAILS: after the addition of each list of cases, the function
   * goes through all the entries found in the returned JSON in REVERSE ORDER
   * and verifies if each of the added role appears in the JSON properly.
   * @throws Exception
   * @param input -> List of added roles;
   * @param status -> Expected status of POST function;
   * @param errorMessage -> Expected error message;
   * @param authority -> Role of the user trying to call POST function;
   */
  @DisplayName(value = "Get all 'Roles' test")
  @ParameterizedTest
  @MethodSource("getAllCases")
  public void getAllRolesTest(ArrayList<Role> input, Integer status, String errorMessage, String authority) throws Exception {

    // arrange
    roleRepository.saveAll(input);

    // when
    MockHttpServletResponse response = this.mockMvc.perform(
      get("/roles")
      .accept(MediaType.APPLICATION_JSON)
      .with(user(authority).roles(authority)))
      .andReturn().getResponse();

    // then
    // Goes through list of test entries
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      JSONArray parsedRoles = new JSONArray(response.getContentAsString());
      for (Role role : input) {

        Optional<Role> entity = roleRepository.findById(role.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.get().getName()).isEqualTo(role.getName());

        // TIME COMPLEXITY O(T) << O(N) where N is the number of entries in the database and T is the number
        // of entries added in the testing time, including entries that weren't added by the tests.

        // Goes through JSON list to check if the new added entries appear
        JSONObject parsedRole = null;
        for (int i = parsedRoles.length() - 1; i >= 0; i--) {
          parsedRole = new JSONObject(parsedRoles.get(i).toString());
          if (parsedRole.get("id").equals(role.getId())) {
            break;
          }
        }
        // Checks if the entry found in the previous for is the one added by the test
        // It fails if the entry is not added or found
        assertThat(parsedRole).isNotNull();
        assertThat(parsedRole.get("id")).isEqualTo(role.getId());
        assertThat(parsedRole.get("name")).isEqualTo(role.getName());
      }
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the existence of a Role object at a specified id.
   * Creates a JSON entry that will be expected to receive.
   * Performs GET method at "roles/{@param idParam}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input -> List of roles;
   * @param idParam -> The id of the wanted role;
   * @param status -> Expected status of GET function;
   * @param errorMessage -> Expected error message;
   * @param expectedRole -> The expected to find role;
   * @param authority -> Role of the user trying to call POST function;
   */
  @DisplayName(value = "Get 'Roles' by id test")
  @ParameterizedTest
  @MethodSource("getByIdCases")
  void getRoleByIdTest(ArrayList<Role> input, String idParam,
                          Integer status, String errorMessage, Role expectedRole, String authority) throws Exception {
    roleRepository.saveAll(input);
    // when
    MockHttpServletResponse response = this.mockMvc.perform(
      get("/roles/" + idParam + "/")
        .accept(MediaType.APPLICATION_JSON)
        .with(user(authority).roles(authority)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();

      JSONObject parsedRole = new JSONObject(response.getContentAsString());
      assertThat(parsedRole.get("id")).isEqualTo(expectedRole.getId());
      assertThat(parsedRole.get("name")).isEqualTo(expectedRole.getName());
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the creation of a Role object as JSON entry.
   * Performs POST at "role/" with the created JSON.
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param input ->  The name of the role;
   * @param status -> Expected status of POST function;
   * @param errorMessage -> Expected error message;
   * @param authority -> Role of the user trying to call POST function;
   */
  @DisplayName(value = "Add 'Roles' test")
  @ParameterizedTest
  @MethodSource("addCases")
  public void addRoleTest(String input, Integer status, String errorMessage, String authority) throws Exception {

    // arrange
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("name");
    List<Object> NamesArray = new ArrayList<>();
    NamesArray.add(input);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, NamesArray);

    // when
    MockHttpServletResponse response = this.mockMvc.perform(post("/roles")
      .contentType(MediaType.APPLICATION_JSON)
      .content(jsonObjectWriter.toString())
      .with(user(authority).roles(authority))).andDo(print())
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    String id = null;
    if(errorMessage == null) {
      JSONObject parsedRole = new JSONObject(response.getContentAsString());
      Optional<Role> entity = roleRepository.findById(parsedRole.get("id").toString());

      assertThat(response.getContentAsString()).isNotEmpty();
      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getName()).isEqualTo(input);
      id = parsedRole.get("id").toString();
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }

    if(id != null)
      roleRepository.deleteById(id);
  }

  /**
   * Arranges the creation of a Role object as JSON entry
   * and the existence of a Role object at specified id {@param expectedId}.
   * Performs PATCH method at "role/{@param wantedId}".
   * Asserts that the status is {@param status} and the object returned has the same
   * attribute values as the expected one. Otherwise, asserts that the {@param errorMessage}
   * is the expected one, and that the body's content is empty.
   * @throws Exception
   * @param name -> Name of role after update;
   * @param expectedId -> Id of Role that needs to be updated;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of PATCH method;
   * @param errorMessage -> Expected error message;
   * @param authority -> Role of the user trying to call PATCH function;
   */
  @DisplayName(value = "Update 'Roles' test")
  @ParameterizedTest
  @MethodSource("updateCases")
  void updateRoleTest(String name, String expectedId, String wantedId,
                         Integer status, String errorMessage, String authority) throws Exception {

    // arrange
    roleRepository.save(new Role(expectedId, name + ".updateMe"));

    // Json Generator
    List<String> FieldArray = new ArrayList<>();
    FieldArray.add("name");
    List<Object> NamesArray = new ArrayList<>();
    NamesArray.add(name);
    StringWriter jsonObjectWriter = buildJsonBody(FieldArray, NamesArray);


    //when
    MockHttpServletResponse response = this.mockMvc.perform(
      patch("/roles/" + wantedId + "/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonObjectWriter.toString())
        .with(user(authority).roles(authority)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);

    if(errorMessage == null) {
      assertThat(response.getContentAsString()).isNotEmpty();
      Optional<Role> entity = roleRepository.findById(wantedId);

      assertThat(entity.get()).isNotNull();
      assertThat(entity.get().getId()).isEqualTo(wantedId);
      assertThat(entity.get().getName()).isEqualTo(name);
    } else {
      assertThat(response.getContentAsString()).isEmpty();
    }
  }

  /**
   * Arranges the existence of entries in the database.
   * Performs DELETE method at "role/id0".
   * Asserts that the status is 200.
   * @throws Exception
   * @param name -> Name of role name to delete ;
   * @param expectedId -> Id of role that needs to be deleted;
   * @param wantedId -> Id given by request;
   * @param status -> Expected status of DELETE method;
   * @param errorMessage -> Expected error message;
   * @param authority -> Role of the user trying to call DELETE function;
   */
  @DisplayName(value = "Delete 'Roles' test")
  @ParameterizedTest
  @MethodSource("deleteByIdCases")
  void deleteRoleByIdNotFoundExceptionTest(String name, String expectedId, String wantedId,
                                              Integer status, String errorMessage, String authority) throws Exception {

    roleRepository.save(new Role(expectedId, name));
    // when
    MockHttpServletResponse response = mockMvc.perform(
      delete("/roles/" + wantedId + "/")
        .accept(MediaType.APPLICATION_JSON)
        .with(user(authority).roles(authority)))
      .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
    assertThat(response.getContentAsString()).isEmpty();

    if(errorMessage == null) {
      assertThat(roleRepository.findById(expectedId).isEmpty()).isTrue();
    }
  }

}
