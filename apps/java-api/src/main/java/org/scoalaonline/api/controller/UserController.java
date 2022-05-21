package org.scoalaonline.api.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scoalaonline.api.DTO.Password;
import org.scoalaonline.api.DTO.RegisterForm;
import org.scoalaonline.api.DTO.Username;
import org.scoalaonline.api.exception.role.RoleNotFoundException;
import org.scoalaonline.api.exception.user.*;
import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.model.User;
import org.scoalaonline.api.service.JWTService;
import org.scoalaonline.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 GET:	"/users"	retrieves all the entries
 GET:	"/users/{id}"	retrieves the entry with the provided id
 GET:	"/users/username"	retrieves the entry with the provided username
 GET:	"/users/role/{roleName}"	retrieves all the entries with the provided role
 GET:	"/users/token/refresh"	offers new access token using the refresh token
 POST: "/users"	creates a new entry
 POST: "/users/register"	registers a new entry with the default role
 POST: "/users/verify/validation/{code}"	validates a user based on the given code
 POST: "users/request/validation"	requests a new validation code
 POST: "users/request/reset_password"	requests password reset
 POST: "/users/verify/reset_password/{code}"	checks if the code for password reset is valid
 POST: "/users/reset_password/{code}"	resets the password of an user based on the given code
 PATCH:	"/users/{id}	edits the entry with the provided id
 DELETE:	"/users/{id}	deletes the entry with the provided id
 */
@CrossOrigin
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
  private static final String DEFAULT_ROLE = "ROLE_STUDENT";
  private static final String ADMIN_ROLE = "ROLE_ADMIN";

  private final UserService userService;
  private final JWTService jwtService;

  /**
   * Sends HTTP status Response Entity with all the User entries.
   * @return a Response Entity with HTTP Status OK and a list of the User entries.
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<List<User>> getAllUsers () {
    List<User> users = userService.getAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with a specific User entry.
   * Sends HTTP status Not Found if there is no entry with the provided id.
   * @param id the id of the specific User.
   * @return the Response Entity with a Status Code and a body.
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
    User user;
    try
    {
      user = userService.getOneById(id);
    } catch (UserNotFoundException e)
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "GET: User Not Found", e );
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Sends HTTP Response Entity with a specific User entry.
   * Sends HTTP status Not Found if there is no entry with the provided username.
   * Sends HTTP status Forbidden if the currently authenticated user is not the same
   * as the one with the given username or does not have permission to see all User entries.
   * @param username - the username of the specific User.
   * @param authentication - information about the currently authenticated user.
   * @return the Response Entity with a Status Code and a body.
   */
  @GetMapping(value = "/username")
  public ResponseEntity<User> getUserByUsername(@RequestParam String username, Authentication authentication) {
    // Security Check
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.addAll(authentication.getAuthorities());
    if (!authentication.getName().trim().equals(username)
      && !authorities.contains(new SimpleGrantedAuthority(ADMIN_ROLE))) {
      throw new ResponseStatusException( FORBIDDEN, "GET: User invalid");
    }
    User user;
    try
    {
      user = userService.getOneByUsername(username);
    } catch (UserNotFoundException e)
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "GET: User Not Found", e );
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Sends HTTP status Response Entity with all the User entries
   * who have the Role with the provided roleName.
   * @return a Response Entity with Status Code OK and a list of the User entries.
   */
  @GetMapping(value = "/role/{roleName}")
  public ResponseEntity<List<User>> getAllUsersByRole(@PathVariable("roleName") String roleName) {
    List<User> users = userService.getAllByRole(roleName);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   * Offers a new access token for authorization based on the refresh_token
   * extracted from the Authorization field in the header of the request.
   * Sends Status OK, or Forbidden if the refresh-token is invalid
   * @param request
   * @param response
   * @throws IOException
   * @return a Response Entity with a Status and a body consisting of the two tokens
   */
  @GetMapping("/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String requestURL = request.getRequestURL().toString();
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = jwtService.decodeJWT(refresh_token);

        String username = decodedJWT.getSubject();
        User user = userService.getOneByUsername(username);
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String access_token = jwtService.getAccessToken(username, requestURL, roles);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
      } catch (Exception exception) {
        log.info("Error logging in: {}", exception.getMessage());
        response.setHeader("error", exception.getMessage());
        response.setStatus(FORBIDDEN.value());

        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }

    } else {
      throw new RuntimeException("Refresh token is missing");
    }
  }

  /**
   * Sends HTTP status Response Entity with the User entry that has been created.
   * Sends HTTP status Invalid Value if the User to be posted is invalid.
   * @param user the User to be added in the db.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = {"", "/"})
  public ResponseEntity<User> addUser (@RequestBody User user) {
    User savedUser;
    try {
      savedUser = userService.add(user);
    } catch (UserInvalidNameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Name", e );
    } catch (UserUsernameAlreadyUsedException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Username already used", e );
    } catch (UserInvalidUsernameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Username", e );
    } catch (UserInvalidPasswordException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Password", e );
    } catch (UserInvalidRolesException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Roles", e );
    } catch (RoleNotFoundException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Role not found", e );
    }

    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
  }

  /**
   * Registers a new User with the default role and sends a mail for validation.
   * Sends HTTP status Response Entity with the User entry that has been created.
   * Sends HTTP status Invalid value if the User to be posted is invalid,
   * or if there was a problem adding the default role,
   * Sends HTTP status SERVICE UNAVAILABLE if there was a problem sending the mail.
   * @param user the User to be added in the db provided by a RegisterForm.
   * @return the Response Entity with a Status Code and a body.
   */
  @PostMapping(value = "/register")
  public ResponseEntity<User> register (@RequestBody @Valid RegisterForm user) {
    User savedUser;
    try {
      savedUser = userService.register(user);
    } catch (UserInvalidNameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Name", e );
    } catch (UserUsernameAlreadyUsedException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Username already used", e );
    } catch (UserInvalidUsernameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Username", e );
    } catch (UserInvalidPasswordException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: User Invalid Password", e );
    } catch (RoleNotFoundException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Role not found", e );
    } catch (UnsupportedEncodingException e) {
      throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "POST: Messaging exception", e );
    } catch (MessagingException e) {
      throw new ResponseStatusException( HttpStatus.SERVICE_UNAVAILABLE, "POST: Messaging exception", e );
    }

    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
  }

  /**
   * Validates a User entry and sends a Response Entity
   * with the Status OK, or INTERNAL SERVER ERROR if the code is invalid.
   * @param code the code required for user validation.
   * @return a Response Entity with a Status.
   */
  @PostMapping( value = "/verify/validation/{code}" )
  public ResponseEntity<HttpStatus> verifyValidation(@PathVariable("code") String code){
    try {
      userService.verifyValidation(code);
    } catch ( UserInvalidValidationCodeException e ) {
      throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "Post: Invalid validation code", e );
    }

    return new ResponseEntity<>( HttpStatus.OK );
  }

  /**
   * Sends a mail to the user with the provided username containing a new validation code.
   * Sends a Response Entity with the Status OK, or Not Found if there is no entry with the provided username.
   * Sends a Response Entity with the Status CONFLICT if the user was already validated.
   * Sends HTTP status SERVICE UNAVAILABLE if there was a problem sending the mail.
   * @param username the username of the User who requires a password reset.
   * @return a Response Entity with a Status.
   */
  @PostMapping( value = "/request/validation" )
  public ResponseEntity<HttpStatus> requestValidation(@RequestBody @Valid Username username) {
    try {
      userService.resendValidationCode(username);
    } catch ( UserMissingUsernameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Username is missing.", e );
    } catch (UserNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST: User Not Found", e);
    } catch (UserAlreadyValidatedException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "POST: User was already validated.", e);
    }catch (UnsupportedEncodingException e) {
      throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "POST: Messaging exception", e );
    } catch (MessagingException e) {
      throw new ResponseStatusException( HttpStatus.SERVICE_UNAVAILABLE, "POST: Messaging exception", e );
    }

    return new ResponseEntity<>( HttpStatus.OK );
  }

  /**
   * Sends a mail for password reset to the user with the provided username.
   * Sends a Response Entity with the Status OK, or Not Found if there is no entry with the provided username.
   * Sends HTTP status SERVICE UNAVAILABLE if there was a problem sending the mail.
   * @param username the username of the User who requires a password reset.
   * @return a Response Entity with a Status.
   */
  @PostMapping( value = "/request/reset_password" )
  public ResponseEntity<HttpStatus> requestResetPassword(@RequestBody @Valid Username username) {
    try {
      userService.requestResetPassword(username);
    } catch ( UserMissingUsernameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "POST: Username is missing.", e );
    } catch ( UserNotFoundException e ) {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "POST: User Not Found", e );
    } catch (UnsupportedEncodingException e) {
    throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "POST: Messaging exception", e );
    } catch (MessagingException e) {
    throw new ResponseStatusException( HttpStatus.SERVICE_UNAVAILABLE, "POST: Messaging exception", e );
    }

    return new ResponseEntity<>( HttpStatus.OK );
  }

  /**
   * Checks if the code for the password reset is valid.
   * Sends a Response Entity with the Status OK, or INTERNAL SERVER ERROR if there is no entry with the provided code,
   * or Gone if the code is expired.
   * @param code the code required for password reset.
   * @return a Response Entity with a Status.
   */
  @PostMapping( value = "/verify/reset_password/{code}" )
  public ResponseEntity<HttpStatus> verifyResetPassword(@PathVariable("code") String code){
    try {
      userService.verifyResetPassword(code);
    } catch ( UserInvalidResetPasswordCodeException e ) {
      throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "Post: Invalid reset password code", e );
    } catch ( UserResetPasswordCodeExpiredException e ) {
      throw new ResponseStatusException( HttpStatus.GONE, "Post: The code for password reset has expired.", e );
    }

    return new ResponseEntity<>( HttpStatus.OK );
  }

  /**
   * Changes the password a user entry and sends a Response Entity
   * with the Status OK, or INTERNAL SERVER ERROR if there is no entry with the provided code,
   * or Gone if the code is expired.
   * Sends HTTP status Invalid Value if the new password is invalid.
   * @param code the code required for password reset.
   * @param password the new password.
   * @return a Response Entity with a Status.
   */
  @PatchMapping(value = ("/reset_password/{code}"))
  public ResponseEntity<HttpStatus> resetPassword (@PathVariable("code") String code, @RequestBody @Valid Password password) {
    try {
      userService.resetPassword(code, password);
    } catch ( UserInvalidResetPasswordCodeException e ) {
      throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "PATCH: Invalid reset password code", e );
    } catch ( UserResetPasswordCodeExpiredException e ) {
      throw new ResponseStatusException( HttpStatus.GONE, "PATCH: The code for password reset has expired.", e );
    } catch (UserInvalidPasswordException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: User Invalid Password", e );
    }

    return new ResponseEntity<>( HttpStatus.OK );
  }

  /**
   * Sends HTTP status Response Entity with the User entry that has been updated.
   * Sends HTTP status Not Found if the User cannot be found.
   * Sends HTTP status Invalid Value if the User to be posted is invalid.
   * @param id the id of the User to be updated.
   * @param user the User to be updated.
   * @return the Response Entity with a Status Code with a body.
   */
  @PatchMapping( value = "/{id}" )
  public ResponseEntity<User> updateUser( @PathVariable( "id" ) String id, @RequestBody User user ) {
    User updatedUser;
    try
    {
      updatedUser = userService.update( id, user );
    } catch ( UserNotFoundException e ) {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "PATCH: User Not Found", e );
    } catch ( UserInvalidNameException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: User Invalid Name", e );
    } catch (UserUsernameNotAllowedException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: Cannot change username", e );
    } catch (UserInvalidPasswordException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: User Invalid Password", e );
    } catch (UserInvalidRolesException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: User Invalid Roles", e );
    } catch (RoleNotFoundException e) {
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "PATCH: Role not found", e );
    }

    return new ResponseEntity<>( updatedUser, HttpStatus.OK );
  }

  /**
   * Deletes a user entry and sends a Response Entity
   * with the Status OK, or Not Found if there is no entry with the provided id
   * @param id the id of the User to be deleted.
   * @return a Response Entity with a Status
   */
  @DeleteMapping( value = "/{id}" )
  public ResponseEntity<HttpStatus> deleteUser( @PathVariable( "id" ) String id ) {
    try
    {
      userService.delete( id );
    } catch ( UserNotFoundException e )
    {
      throw new ResponseStatusException( HttpStatus.NOT_FOUND, "DELETE: User Not Found", e );
    }
    return new ResponseEntity<>( HttpStatus.OK );
  }

}
