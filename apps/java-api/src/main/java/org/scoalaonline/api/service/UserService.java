package org.scoalaonline.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scoalaonline.api.DTO.Password;
import org.scoalaonline.api.DTO.RegisterForm;
import org.scoalaonline.api.DTO.Username;
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

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Contains the User related logic needed for the API
 */
@Service("userService")
@Slf4j
@RequiredArgsConstructor
public class UserService implements ServiceInterface<User>, UserDetailsService {
  private static final String DEFAULT_ROLE = "ROLE_STUDENT";
  private static final int TIME_TO_EXPIRATION = 30 * 60;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;

  /**
   * Retrieves User entry with the given username
   * Creates Spring Security UserDetails based on the User entry
   * @param username
   * @return UserDetails object
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(
      () -> {
        log.error("Username not found.");
        return new UsernameNotFoundException("Method loadUserByUsername: Username not found.");
      }
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
    UserInvalidRolesException,
    UserUsernameAlreadyUsedException,
    RoleNotFoundException {
    log.info("Adding user {}...", entry.getUsername());
    User userToSave = new User();

    if (entry.getName() != null && !entry.getName().equals("")) {
      userToSave.setName(entry.getName());
    } else {
      log.error("Name field can't be null.");
      throw new UserInvalidNameException("Method add: Name field can't be null.");
    }

    if (entry.getUsername() != null && !entry.getUsername().equals("") && entry.getUsername().matches("^(?=.{1,64}@)[A-Za-z0-9_!#$%&'*+-=?^_`{|}~\\/]+(\\.[A-Za-z0-9_=?^_`{|}~\\/-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})*$")){
      if (!userRepository.existsByUsername(entry.getUsername())) {
        userToSave.setUsername(entry.getUsername());
      } else {
        log.error("Username is already used.");
        throw new UserUsernameAlreadyUsedException("Method add: Username is already used.");
      }
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

    if (entry.getCreatedAt() != null) {
      userToSave.setCreatedAt(entry.getCreatedAt());
    } else {
      userToSave.setCreatedAt(LocalDateTime.now());
    }

    userToSave.setValidatedAt(entry.getValidatedAt());

    if (entry.getLastModifiedAt() != null) {
      userToSave.setLastModifiedAt(entry.getLastModifiedAt());
    } else {
      userToSave.setLastModifiedAt(LocalDateTime.now());
    }

    if (entry.getValidated() != null) {
      userToSave.setValidated(entry.getValidated());
    } else {
      userToSave.setValidated(false);
    }

    userToSave.setValidationCode(entry.getValidationCode());
    userToSave.setResetPasswordCode(entry.getResetPasswordCode());
    userToSave.setResetPasswordCodeExpiryDate(entry.getResetPasswordCodeExpiryDate());

    if (entry.getRoles() != null && !entry.getRoles().isEmpty()) {
      List<Role> rolesToSave = createRoleList(entry.getRoles());
      userToSave.setRoles(rolesToSave);
    } else {
      log.error("Roles field can't be null.");
      throw new UserInvalidRolesException("Method add: Roles field can't be null.");
    }

    return userRepository.save(userToSave);
  }

  /**
   * Adds a User entry in the DB based on the received RegisterForm.
   * Adds ROLE_STUDENT to the list of roles and sends a mail to the user with the created validation code.
   * @param entry - the User entry.
   * @throws UserInvalidNameException
   * @throws UserInvalidUsernameException
   * @throws UserUsernameAlreadyUsedException
   * @throws UserInvalidPasswordException
   * @throws RoleNotFoundException
   * @throws UnsupportedEncodingException
   * @throws MessagingException
   * @return the User object that has been saved in the DB
   */
  public User register(RegisterForm entry) throws UserInvalidNameException,
    UserInvalidUsernameException,
    UserUsernameAlreadyUsedException,
    UserInvalidPasswordException,
    RoleNotFoundException,
    UnsupportedEncodingException,
    MessagingException {
    log.info("Registering user {}...", entry.getUsername());
    User userToSave = new User();

    if(entry.getName() != null && !entry.getName().equals("")) {
      userToSave.setName(entry.getName());
    } else {
      log.error("Name field can't be null.");
      throw new UserInvalidNameException("Method register: Name field can't be null.");
    }

    if(entry.getUsername() != null && !entry.getUsername().equals("") && entry.getUsername().matches("^(?=.{1,64}@)[A-Za-z0-9_!#$%&'*+-=?^_`{|}~\\/]+(\\.[A-Za-z0-9_=?^_`{|}~\\/-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})*$")){
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
    userToSave = addRoleToUser(userToSave, DEFAULT_ROLE);

    userToSave.setCreatedAt(LocalDateTime.now());
    userToSave.setLastModifiedAt(LocalDateTime.now());

    String randomCode = UUID.randomUUID().toString();
    userToSave.setValidationCode(randomCode);
    userToSave.setValidated(false);

    User userSaved = userRepository.save(userToSave);
    log.info("The user was created");

    mailService.sendValidateAccountEmail(userToSave.getUsername(), userToSave.getName(), randomCode);
    log.info("Mail sent.");
    return userSaved;
  }

  /**
   * Sends a mail to the user with the provided username containing a new validation code.
   * Throws an error if no user with the given username is found or if the user was already validated.
   * Throws an error if there was a problem sending the mail.
   * @param username - username of the user.
   * @throws UnsupportedEncodingException
   * @throws MessagingException
   * @throws UserAlreadyValidatedException
   * @throws UserNotFoundException
   * @throws UserMissingUsernameException
   */
  public void resendValidationCode(Username username) throws
    UnsupportedEncodingException,
    MessagingException,
    UserAlreadyValidatedException,
    UserNotFoundException,
    UserMissingUsernameException {
    if(username.getUsername() == null || username.getUsername().equals("")) {
      log.error("Username is missing.");
      throw new UserMissingUsernameException("Method resendValidationCode: Username is missing.");
    }
    log.info("Resending validation code for user with username {} ...", username.getUsername());
    User user = userRepository.findByUsername(username.getUsername()).orElseThrow(
      () -> {
        log.error("User not found.");
        return new UserNotFoundException("Method resendValidationCode: User not found.");
      }
    );
    if (user.getValidated()) {
      log.error("User was already validated.");
      throw new UserAlreadyValidatedException("Method resendValidationCode: User was already validated.");
    }
    String randomCode = UUID.randomUUID().toString();
    user.setValidationCode(randomCode);

    userRepository.save(user);

    mailService.sendValidateAccountEmail(user.getUsername(), user.getName(), randomCode);
    log.info("Resending validation code is completed");
  }

  /**
   * Retrieves one User entry with the given validationCode from the DB and validates it
   * or throws an error if no entry with that validationCode is found.
   * @param validationCode - the code required for user validation.
   * @throws UserInvalidValidationCodeException
   */
  public void verifyValidation(String validationCode) throws UserInvalidValidationCodeException {
    log.info("Validating user with code {} ...", validationCode);
    User user = userRepository.findByValidationCode(validationCode).orElseThrow(
      () -> {
        log.error("Invalid validation code.");
        return new UserInvalidValidationCodeException("Method verifyValidation: Invalid validation code.");
      }
    );

    log.info("User was validated.");
    user.setValidatedAt(LocalDateTime.now());
    user.setValidationCode(null);
    user.setValidated(true);
    userRepository.save(user);
  }

  /**
   * Retrieves one User entry with the given username from the DB
   * or throws an error if no entry with that username is found.
   * Sends a mail containing the code for password reset, with an expiration date, created for the user.
   * @param username - username of the User entry.
   * @throws UnsupportedEncodingException
   * @throws MessagingException
   * @throws UserNotFoundException
   * @throws UserMissingUsernameException
   */
  public void requestResetPassword(Username username) throws UnsupportedEncodingException,
    MessagingException, UserNotFoundException, UserMissingUsernameException {
    if(username.getUsername() == null || username.getUsername().equals("")) {
      log.error("Username is missing.");
      throw new UserMissingUsernameException("Method resendValidationCode: Username is missing.");
    }
    log.info("Sending mail for password reset...");
    User user = userRepository.findByUsername(username.getUsername()).orElseThrow(
      () -> {
        log.error("User not found.");
        return new UserNotFoundException("Method requestPasswordReset: User not found.");
      }
    );

    String randomCode = UUID.randomUUID().toString();
    LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(TIME_TO_EXPIRATION);
    user.setResetPasswordCode(randomCode);
    user.setResetPasswordCodeExpiryDate(expiryDate);

    mailService.sendResetPasswordEmail(user.getUsername(), user.getName(), randomCode);
    log.info("Mail sent.");

    userRepository.save(user);
  }

  /**
   * Retrieves one User entry with the given resetPasswordCode from the DB ( resetPasswordCode is valid ).
   * Throws an error if no entry with that resetPasswordCode is found.
   * Throws an error if the resetPasswordCode is expired.
   * @param resetPasswordCode - the code required for password reset.
   * @throws UserInvalidResetPasswordCodeException
   * @throws UserResetPasswordCodeExpiredException
   */
  public void verifyResetPassword(String resetPasswordCode) throws UserInvalidResetPasswordCodeException, UserResetPasswordCodeExpiredException {
    log.info("Verifying resetPasswordCode {}", resetPasswordCode);
    User user = userRepository.findByResetPasswordCode(resetPasswordCode).orElseThrow(
      () -> {
        log.error("Invalid reset password code.");
        return new UserInvalidResetPasswordCodeException("Method resetPassword: Invalid reset password code.");
      }
    );

    if (user.getResetPasswordCodeExpiryDate().isBefore(LocalDateTime.now())) {
      log.error("The code for password reset has expired.");
      throw new UserResetPasswordCodeExpiredException("Method verifyResetPassword: The code for password reset has expired.");
    }

    log.info("Code is valid.");
  }

  /**
   * Retrieves one User entry with the given resetPasswordCode from the DB and changes its password with the given one
   * or throws an error if no entry with that resetPasswordCode is found.
   * Throws an error if the new password is invalid.
   * @param resetPasswordCode - the code required for password reset.
   * @param password - the new password.
   * @throws UserInvalidResetPasswordCodeException
   * @throws UserInvalidPasswordException
   */
  public void resetPassword(String resetPasswordCode, Password password) throws UserInvalidResetPasswordCodeException, UserInvalidPasswordException, UserResetPasswordCodeExpiredException {
    log.info("Changing password for user with resetPasswordCode {}", resetPasswordCode);
    User user = userRepository.findByResetPasswordCode(resetPasswordCode).orElseThrow(
      () -> {
        log.error("Invalid reset password code.");
        return new UserInvalidResetPasswordCodeException("Method resetPassword: Invalid reset password code.");
      }
    );

    if (user.getResetPasswordCodeExpiryDate().isBefore(LocalDateTime.now())) {
      log.error("The code for password reset has expired.");
      throw new UserResetPasswordCodeExpiredException("Method resetPassword: The code for password reset has expired.");
    }

    if(password.getPassword() != null && !password.getPassword().equals("") && password.getPassword().matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_]).{8,}")) {
      user.setPassword(passwordEncoder.encode(password.getPassword()));
    } else {
      log.error("Invalid password.");
      throw new UserInvalidPasswordException("Method resetPassword: Invalid password.");
    }

    log.info("Password reset.");
    user.setLastModifiedAt(LocalDateTime.now());
    user.setResetPasswordCode(null);
    userRepository.save(user);
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
    UserInvalidRolesException, RoleNotFoundException {
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

    userToUpdate.setCreatedAt(entry.getCreatedAt());
    userToUpdate.setValidatedAt(entry.getValidatedAt());

    if (entry.getLastModifiedAt() != null) {
      userToUpdate.setLastModifiedAt(entry.getLastModifiedAt());
    } else {
      userToUpdate.setLastModifiedAt(LocalDateTime.now());
    }

    userToUpdate.setValidated(entry.getValidated());
    userToUpdate.setValidationCode(entry.getValidationCode());
    userToUpdate.setResetPasswordCode(entry.getResetPasswordCode());
    userToUpdate.setResetPasswordCodeExpiryDate(entry.getResetPasswordCodeExpiryDate());

    if(entry.getRoles() != null && !entry.getRoles().isEmpty()) {
      List<Role> rolesToSave = createRoleList(entry.getRoles());
      userToUpdate.setRoles(rolesToSave);
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
      log.error("User not found in the database.");
      throw new UserNotFoundException("Method delete: User not found.");
    }
  }

  /**
   * Retrieves one Role entry with the given roleName from the DB
   * Adds the Role to User entry given by user
   * or throws an error if no entry with that roleName is found.
   * @param user - User entry
   * @param roleName - name of the Role entry
   * @return the modified user
   * @throws RoleNotFoundException
   */
  public User addRoleToUser(User user, String roleName) throws RoleNotFoundException {
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

    return user;
  }
  /**
   * Retrieves a list of Role entries based on the ids of the provided list of roles.
   * Throws an error when it reaches an id with no corresponding role in the DB.
   * @param roles - list of roles.
   * @return a new list of roles.
   * @throws RoleNotFoundException
   */
  public List<Role> createRoleList(List<Role> roles) throws RoleNotFoundException {
    ArrayList<Role> rolesToSave = new ArrayList<>();

    for (int i = 0, rolesSize = roles.size(); i < rolesSize; i++) {
      Role role = roles.get(i);
      Role roleToSave = roleRepository.findById(role.getId()).orElseThrow(
        () -> {
          log.error("Role not found.");
          return new RoleNotFoundException("Method createRoleList: Role not found.");
        }
      );
      rolesToSave.add(roleToSave);
    }

    return rolesToSave;
  }
}

