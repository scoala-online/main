package org.scoalaonline.api.security;

import lombok.RequiredArgsConstructor;
import org.scoalaonline.api.model.User;
import org.scoalaonline.api.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Creates validation methods used to access certain resources from the API
 */
@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
  private final UserService userService;

  /**
   * Checks if the user with the specified id is the same as the currently authenticated user
   * @param authentication information about the currently authenticated user.
   * @param id - the id of the specific user
   * @return true or false based of the check
   */
  public boolean hasUserId(Authentication authentication, String id) {
    try {
      User user = userService.getOneById(id);
      return (authentication.getName().trim().equals(user.getUsername().trim()));
    } catch (Exception exception) {
      return false;
    }

  }
}
