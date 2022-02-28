package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.role.RoleInvalidNameException;
import org.scoalaonline.api.exception.role.RoleNotFoundException;
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @InjectMocks
  private RoleService underTestService;

  @Mock
  private RoleRepository roleRepository;

  /**
   * Executes the getAll() method from RoleService class.
   * Asserts that it correctly called the findAll() method
   * from the repository.
   */
  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(roleRepository).findAll();
  }

  /**
   * Arranges the existence of a custom Role object in database.
   * Executes the getOneById( @param ) method from RoleService class.
   * Asserts that it finds the same object arranged in the former step.
   * @throws RoleNotFoundException
   */
  @Test
  void getOneByIdTest() throws RoleNotFoundException {
    // when
    when(roleRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new Role("id", "Name.pdf")));

    // then
    Role role = underTestService.getOneById("id");

    Assertions.assertEquals("Name.pdf", role.getName());
  }

  /**
   * Arranges the absence of any Role object in database.
   * Executes the getOneById( @param ) method from RoleService class.
   * Asserts that it throws the RoleNotFoundException exception
   * and the related message.
   */
  @Test
  void getOneByIdExceptionTest() {
    //when
    when(roleRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(RoleNotFoundException.class)
      .hasMessageContaining("Method getOneById: Role not found");
  }

  /**
   * Arranges the creation of a custom Role object.
   * Executes the add( Role @param) method
   * from RoleService class.
   * Asserts that a Role object has been added to the database
   * and it has the same attribute values as the one created previously.
   * @throws RoleInvalidNameException
   */
  @Test
  void addTest() throws RoleInvalidNameException {
    // given
    Role role = new Role("string_id", "Some_Name.pdf");

    // when
    underTestService.add(role);

    // then
    ArgumentCaptor<Role> roleArgumentCaptor =
      ArgumentCaptor.forClass(Role.class);

    verify(roleRepository).save(roleArgumentCaptor.capture());

    Role capturedRole = roleArgumentCaptor.getValue();
    assertThat(capturedRole.getName()).isEqualTo(role.getName());
  }

  /**
   * Arranges the creation of two Role objects
   * with invalid attribute values.
   * Executes the add( Role @param ) method
   * from RoleService class.
   * Asserts that nothing was saved in the database and
   * it throws RoleInvalidNameException exception
   * with the related messages.
   */
  @Test
  void addRoleInvalidDataExceptionTest() {
    // If you have repo methods called in the add method
    // make sure they are set to true so the test can pass
    // Example:
    // given(studentRepository.method(@param)).willReturn(true);

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Role role = new Role("id", (String) exceptionCase);
      // when & then
      assertThatThrownBy(() -> underTestService.add(role))
        .isInstanceOf(RoleInvalidNameException.class)
        .hasMessageContaining("Method add: Name field can't be null.");

      verify(roleRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of two Role objects:
   * 1. The object already present in the database.
   * 2. The object that will update the former.
   * Arranges the existence of the first object in the database.
   * Executes the update( @param ,Role @param ) method
   * from RoleService class.
   * Asserts that a Role object has been saved to the database
   * and that it has the same attribute values as the one we want to update with.
   * @throws RoleNotFoundException
   * @throws RoleInvalidNameException
   */
  @Test
  void updateTest() throws RoleNotFoundException, RoleInvalidNameException {
    // given
    Role role = new Role("id","Name.pdf");
    Role updatedRole = new Role(anyString(), "Name.docs");

    // when
    when(roleRepository.findById(role.getId()))
      .thenReturn(Optional.of(role));

    // then
    underTestService.update(role.getId(), updatedRole);

    ArgumentCaptor<Role> roleArgumentCaptor =
      ArgumentCaptor.forClass(Role.class);

    verify(roleRepository).save(roleArgumentCaptor.capture());

    Role capturedRole = roleArgumentCaptor.getValue();
    assertThat(capturedRole.getName()).isEqualTo(role.getName());

    verify(roleRepository).findById(role.getId());

  }

  /**
   * Arranges the creation of a Role object we will try to
   * update with and makes sure the database has no entries to update.
   * Executes the update( @param , Role @param ) method
   * from RoleService class.
   * Asserts that the RoleNotFoundException exception
   * is thrown with the related messages.
   */
  @Test
  void updateRoleNotFoundExceptionTest() {
    // given
    Role role = new Role("ID","Name.pdf");

    // when
    // Role Not Found Exception
    when(roleRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.update(anyString(), role))
      .isInstanceOf(RoleNotFoundException.class)
      .hasMessageContaining("Method update: Role not found");

    verify(roleRepository, never()).save(any());
  }
  /**
   * Arranges the creation of a Role object we will try to
   * update with and makes sure the object has invalid attribute values.
   * Executes the update( @param , Role @param ) method
   * from RoleService class.
   * Asserts that the RoleInvalidNameException exception
   * is thrown with the related messages.
   */
  @Test
  void updateRoleInvalidDataExceptionTest() {
    // when
    when(roleRepository.findById(anyString()))
      .thenReturn(Optional.of(new Role("id","Name.pdf")));

    List<Object> exceptionCases = new ArrayList<Object>();
    exceptionCases.add("");
    exceptionCases.add(null);
    for(Object exceptionCase : exceptionCases){
      // given
      Role role = new Role("id", (String) exceptionCase);

      // when & then
      assertThatThrownBy(() -> underTestService.update("id",role))
        .isInstanceOf(RoleInvalidNameException.class)
        .hasMessageContaining("Method update: Name field can't be null.");

      verify(roleRepository, never()).save(any());
    }
  }

  /**
   * Arranges the creation of a Role object and
   * makes sure of its existence in the database.
   * Executes the delete( @param ) method from RoleService class.
   * Asserts that the deleteById( @param ) method from repository has been
   * executed successfully.
   * @throws RoleNotFoundException
   */
  @Test
  void delete() throws RoleNotFoundException {
    // given
    Role role = new Role("id","Name.pdf");

    // when
    when(roleRepository.findById(role.getId()))
      .thenReturn(Optional.of(role));

    // then
    underTestService.delete(role.getId());

    verify(roleRepository).deleteById(role.getId());
  }

  /**
   * Arranges the absence of any Role object in database.
   * Executes the delete( @param ) method from RoleService class.
   * Asserts that it throws the RoleNotFoundException exception
   * and the related message.
   */
  @Test
  void deleteException() {

    // when
    when(roleRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> underTestService.delete(anyString()))
      .isInstanceOf(RoleNotFoundException.class)
      .hasMessageContaining("Method delete: Role not found.");

    verify(roleRepository, never()).delete(any());
  }
}
