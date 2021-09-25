package org.scoalaonline.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scoalaonline.api.DTO.RegisterForm;
import org.scoalaonline.api.exception.role.RoleNotFoundException;
import org.scoalaonline.api.exception.user.*;
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.model.User;
import org.scoalaonline.api.repository.RoleRepository;
import org.scoalaonline.api.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains the User related logic needed for the API
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements ServiceInterface<User>, UserDetailsService {
  private static final String DEFAULT_ROLE = "ROLE_STUDENT";

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Retrieves User entry with the given username
   * Creates Spring Security UserDetails based on User entry
   * @param username
   * @return UserDetails object
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(
      () -> new UsernameNotFoundException("Method loadUserByUsername: User not found.")
    );

    log.info("User {} found in the database.", username);

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles().forEach(role -> {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    });

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
  }

  /**
   * Retrieves a list of all User entries found in the DB
   * @return the list of User entries
   */
  @Override
  public List<User> getAll() {
    log.info("Fetching all users...");
    return userRepository.findAll();
  }

  /**
   * Retrieves one User entry with the given id from the DB
   * or throws an error if no entry with that id is found.
   * @param id - id of the User entry
   * @return the User entry
   * @throws UserNotFoundException
   */
  @Override
  public User getOneById(String id) throws UserNotFoundException {
    log.info("Fetching user with id {}...", id);
    return userRepository.findById(id).orElseThrow(
      () -> {
        log.error("User not found.");
        return new UserNotFoundException("Method getOneById: User not found.");
      }
    );
  }

  /**
   * Retrieves one User entry with the given username from the DB
   * or throws an error if no entry with that username is found.
   * @param username - username of the User entry
   * @return the User entry
   * @throws UserNotFoundException
   */
  public User getOneByUsername(String username) throws UserNotFoundException{
    log.info("Fetching user {}...", username);
    return userRepository.findByUsername(username).orElseThrow(
      () -> {
        log.error("User not found.");
        return new UserNotFoundException("Method getOneByUsername: User not found.");
      }
    );
  }

  /**
   * Retrieves a list of all User entries found in the DB who have the Role given by roleName
   * @param roleName - name of the Role entry
   * @throws RoleNotFoundException
   * @return the list of User entries
   */
  public List<User> getAllByRole(String roleName) {
    log.info("Fetching all users with role {}...", roleName);
    return userRepository.findAllByRolesContaining(roleName);
  }

  /**
   * Adds a User entry in the DB based on the received object.
   * @param entry - the User entry.
   * @throws UserInvalidNameException
   * @throws UserInvalidUsernameException
   * @throws UserInvalidPasswordException
   * @throws UserInvalidRolesException
   * @return the User object that has been saved in the DB
   */
  @Override
  public User add(User entry) throws UserInvalidNameException,
    UserInvalidUsernameException,
    UserInvalidPasswordException,
    UserInvalidRolesException {
    log.info("Adding user {}...", entry.getUsername());
    User userToSave = new User();

    if (entry.getName() != null && !entry.getName().equals("")) {
      userToSave.setName(entry.getName());
    } else {
      log.error("Name field can't be null.");
      throw new UserInvalidNameException("Method add: Name field can't be null.");
    }

    if (entry.getUsername() != null && !entry.getUsername().equals("") && entry.getUsername().matches("^\\b[\\w.!#$%&’*+/=?^`{|}~-]+@[\\w-]+(?:.[\\w-]+)+\\b$")) {
      userToSave.setUsername(entry.getUsername());
    } else {
      log.error("Invalid username.");
      throw new UserInvalidUsernameException("Method add: Invalid username.");
    }

    if (entry.getPassword() != null && !entry.getPassword().equals("") && entry.getPassword().matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_]).{8,}")) {
      userToSave.setPassword(passwordEncoder.encode(entry.getPassword()));
    } else {
      log.error("Invalid password.");
      throw new UserInvalidPasswordException("Method add: Invalid password.");
    }

    if (entry.getRoles() != null && !entry.getRoles().isEmpty()) {
      userToSave.setRoles(entry.getRoles());
    } else {
      log.error("Roles field can't be null.");
      throw new UserInvalidRolesException("Method add: Roles field can't be null.");
    }

    return userRepository.save(userToSave);
  }

  /**
   * Adds a User entry in the DB based on the received RegisterForm.
   * Adds ROLE_STUDENT to the list of roles
   * @param entry - the User entry.
   * @throws UserInvalidNameException
   * @throws UserInvalidUsernameException
   * @throws UserUsernameAlreadyUsedException
   * @throws UserInvalidPasswordException
   * @throws RoleNotFoundException
   * @return the User object that has been saved in the DB
   */
  public User register(RegisterForm entry) throws UserInvalidNameException,
    UserInvalidUsernameException,
    UserUsernameAlreadyUsedException,
    UserInvalidPasswordException,
    RoleNotFoundException {
    log.info("Registering user {}...", entry.getUsername());
    User userToSave = new User();

    if(entry.getName() != null && !entry.getName().equals("")) {
      userToSave.setName(entry.getName());
    } else {
      log.error("Name field can't be null.");
      throw new UserInvalidNameException("Method register: Name field can't be null.");
    }

    if(entry.getUsername() != null && !entry.getUsername().equals("") && entry.getUsername().matches("^\\b[\\w.!#$%&’*+/=?^`{|}~-]+@[\\w-]+(?:.[\\w-]+)+\\b$")){
      if (!userRepository.existsByUsername(entry.getUsername())) {
        userToSave.setUsername(entry.getUsername());
      } else {
        log.error("Username is already used.");
        throw new UserUsernameAlreadyUsedException("Method register: Username is already used.");
      }
    } else {
      log.error("Invalid username.");
      throw new UserInvalidUsernameException("Method register: Invalid username.");
    }

    if(entry.getPassword() != null && !entry.getPassword().equals("") && entry.getPassword().matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_]).{8,}")) {
      userToSave.setPassword(passwordEncoder.encode(entry.getPassword()));
    } else {
      log.error("Invalid password.");
      throw new UserInvalidPasswordException("Method register: Invalid password.");
    }

    userToSave.setRoles(new ArrayList<>());

    addRoleToUser(userToSave, DEFAULT_ROLE);

    return userRepository.save(userToSave);
  }

  /**
   * Updates the User entry with the given id based on the received object.
   * Throws an exception if no entry with that id was found.
   * @param id - the id of the entry to update
   * @param entry - the User entry.
   * @throws UserNotFoundException
   * @throws UserInvalidNameException
   * @throws UserUsernameNotAllowedException
   * @throws UserInvalidPasswordException
   * @throws UserInvalidRolesException
   * @return the User object saved in the DB
   */
  @Override
  public User update(String id, User entry) throws UserNotFoundException,
    UserInvalidNameException,
    UserUsernameNotAllowedException,
    UserInvalidPasswordException,
    UserInvalidRolesException{
    User userToUpdate = userRepository.findById(id).orElseThrow(
      () -> {
        log.error("User not found.");
        return new UserNotFoundException("Method update: User not found.");
      }
    );

    log.info("Updating user {}...", userToUpdate.getUsername());
    if(entry.getName() != null && !entry.getName().equals("")) {
      userToUpdate.setName(entry.getName());
    } else {
      log.error("Name field can't be null.");
      throw new UserInvalidNameException("Method update: Name field can't be null.");
    }

    if(entry.getUsername() != null && !entry.getUsername().equals("") && !userToUpdate.getUsername().equals(entry.getUsername())) {
      log.error("Cannot change username.");
      throw new UserUsernameNotAllowedException("Method update: Cannot change username.");
    }

    if(entry.getPassword() != null && !entry.getPassword().equals("") && entry.getPassword().matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_]).{8,}")) {
      userToUpdate.setPassword(passwordEncoder.encode(entry.getPassword()));
    } else {
      log.error("Invalid password.");
      throw new UserInvalidPasswordException("Method update: Invalid password.");
    }

    if(entry.getRoles() != null && !entry.getRoles().isEmpty()) {
      userToUpdate.setRoles(entry.getRoles());
    } else {
      log.error("Roles field can't be null.");
      throw new UserInvalidRolesException("Method update: Roles field can't be null.");
    }

    return userRepository.save(userToUpdate);
  }

  /**
   * Deletes the User entry with the given id or throws an exception if no
   * entry with that id can be found
   * @param id - id of the User entry
   * @throws UserNotFoundException
   */
  @Override
  public void delete(String id) throws UserNotFoundException {
    if(userRepository.findById(id).isPresent()) {
      log.info("Deleting user with id {}...", id);
      userRepository.deleteById(id);
    } else {
      log.error("User not found in te database.");
      throw new UserNotFoundException("Method delete: User not found.");
    }
  }

  /**
   * Retrieves one Role entry with the given roleName from the DB
   * Adds the Role to User entry given by user
   * or throws an error if no entry with that roleName is found.
   * @param user - User entry
   * @param roleName - name of the Role entry
   * @throws RoleNotFoundException
   */
  public void addRoleToUser(User user, String roleName) throws RoleNotFoundException {
    Role role = roleRepository.findByName(roleName).orElseThrow(
      () -> {
        log.error("Role not found.");
        return new RoleNotFoundException("Method addRoleToUser: Role not found.");
      }
    );
    log.info("Adding role {} to user {}", role.getName(), user.getName());
    List<Role> newRoles = user.getRoles();
    newRoles.add(role);
    user.setRoles(newRoles);

    userRepository.save(user);
  }
}

