package org.scoalaonline.api.controller;

import lombok.RequiredArgsConstructor;
import org.scoalaonline.api.exception.role.RoleInvalidNameException;
import org.scoalaonline.api.exception.role.RoleNotFoundException;
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 GET:	"/roles"	retrieves all the entries
 GET:	"/roles/{id}"	retrieves the entry with the provided id
 POST: "/roles"	creates a new entry
 PATCH:	"/roles/{id}	edits the entry with the provided id
 DELETE:	"/roles/{id}	deletes the entry with the provided id
 */
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
  private final RoleService roleService;

  /**
   * Sends HTTP status Response Entity with all the Role entries.
   * @return a Response Entity with HTTP Status OK and a list of the Role entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<Role>> getAllRoles () {
    List<Role> roles = roleService.getAll();
    return new ResponseEntity<>(roles, HttpStatus.OK);
  }

  /**
   * Sends an HTTP Response Entity with a specific Role entry
   * Sends Status OK or Status Not Found if there is no entry with the provided id
   * @param id the id of the specific Role
   * @return the Response Entity with a Status Code and a Body
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<Role> getRoleById(@PathVariable("id") String id) {
    Role role;
    try
    {
      role = roleService.getOneById(id);
    } catch (RoleNotFoundException e)
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "GET: Role Not Found", e );
    }
    return new ResponseEntity<>(role, HttpStatus.OK);
  }

  /**
   * Sends HTTP status Response Entity with the Role entry that has been created.
   * Sends HTTP status Invalid value if the Role to be posted is invalid.
   * @param role the Role to be added in the db.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<Role> addRole (@RequestBody Role role) {
    Role savedRole;
    try {
      savedRole = roleService.add(role);
    } catch (RoleInvalidNameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Role Invalid Name", e );
    }
    return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
  }

  /**
   * Sends HTTP status Response Entity with the Role entry that has been updated.
   * Sends HTTP status Not Found if the Role cannot be found.
   * Sends HTTP status Invalid Value if the Role to be posted is invalid.
   * @param id the id of the Role to be updated.
   * @param role the Role to be updated.
   * @return the Response Entity with a Status Code and a body.
   */
  @PatchMapping( value = "/{id}" )
  public ResponseEntity<Role> updateRole( @PathVariable( "id" ) String id, @RequestBody Role role ) {
    Role updatedRole;
    try
    {
      updatedRole = roleService.update( id, role );
    } catch ( RoleNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "PATCH: Role Not Found", e );
    } catch ( RoleInvalidNameException e)
    {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: Role Invalid Name", e );
    }

    return new ResponseEntity<>( updatedRole, HttpStatus.OK );
  }

  /**
   * Deletes a role entry and sends a Response Entity
   * with the Status OK or Not Found if there is no entry with the provided id
   * @param id the id of the Role to be deleted.
   * @return a Response Entity with a Status
   */
  @DeleteMapping( value = "/{id}" )
  public ResponseEntity<HttpStatus> deleteRole( @PathVariable( "id" ) String id ) {
    try
    {
      roleService.delete( id );
    } catch ( RoleNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "DELETE: Role Not Found", e );
    }
    return new ResponseEntity<>( HttpStatus.OK );
  }

}
