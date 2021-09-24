package org.scoalaonline.api.service;

import lombok.RequiredArgsConstructor;
import org.scoalaonline.api.exception.role.RoleInvalidNameException;
import org.scoalaonline.api.exception.role.RoleNotFoundException;
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains the Role related logic needed for the API
 */
@Service
@RequiredArgsConstructor
public class RoleService implements ServiceInterface<Role>{
  private final RoleRepository roleRepository;

  /**
   * Retrieves a list of all Role entries found in the DB
   * @return the list of Role entries
   */
  @Override
  public List<Role> getAll() {
    return roleRepository.findAll();
  }

  /**
   * Retrieves one Role entry with the given id from the DB
   * or throws an error if no entry with that id is found.
   * @param id - id of the Role entry
   * @return the Role entry
   * @throws RoleNotFoundException
   */
  @Override
  public Role getOneById(String id) throws RoleNotFoundException {
    return roleRepository.findById(id).orElseThrow(
      () -> new RoleNotFoundException("Method getOneById: Role not found")
    );
  }

  /**
   * Adds a Role entry in the DB based on the received object.
   * @param entry - the Role entry.
   * @throws RoleInvalidNameException
   * @return the Role object that has been saved in the DB
   */
  @Override
  public Role add(Role entry) throws RoleInvalidNameException {
    Role roleToSave = new Role();
    if(entry.getName() != null && !entry.getName().equals(""))
      roleToSave.setName(entry.getName());
    else
      throw new RoleInvalidNameException("Method add: Name field can't be null.");

    return roleRepository.save(roleToSave);
  }

  /**
   * Updates the Role entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found.
   * @param id - the id of the entry to update
   * @param entry the Role entry.
   * @return the Role object saved in the DB
   * @throws RoleNotFoundException
   * @throws RoleInvalidNameException
   */
  @Override
  public Role update(String id, Role entry) throws RoleNotFoundException, RoleInvalidNameException {
    Role roleToUpdate = roleRepository.findById(id).orElseThrow(
      () -> new RoleNotFoundException("Method update: Role not found")
    );
    if(entry.getName() != null && !entry.getName().equals("")) {
      roleToUpdate.setName(entry.getName());
    } else {
      throw new RoleInvalidNameException("Method update: Name Field Can't Be Null");
    }
    return roleRepository.save(roleToUpdate);
  }

  /**
   * Deletes the Role entry with the given id or throws an exception if no
   * entry with that id can be found
   * @param id - id of the Role entry
   * @throws RoleNotFoundException
   */
  @Override
  public void delete(String id) throws RoleNotFoundException {
    if(roleRepository.findById(id).isPresent())
      roleRepository.deleteById(id);
    else
      throw new RoleNotFoundException("Method delete: Role Not Found");
  }
}
