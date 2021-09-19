package org.scoalaonline.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.scoalaonline.api.service.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 *Creates a filter for the process of authentication
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  /**
   * Attempts to authenticate the user using the provided username and password from the request body
   * @param request
   * @param response
   * @return the result of the attempt
   * @throws AuthenticationException
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String username, password;

    try {
      Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
      username = requestMap.get("username");
      password = requestMap.get("password");
    } catch (IOException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }

    log.info("Username is {}", username);
    log.info("Password is {}", password);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
    return authenticationManager.authenticate(authenticationToken);
  }

  /**
   * Creates a response with Status Forbidden and an error message if the authentication is unsuccessful
   * @param request
   * @param response
   * @param failed
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    log.info(failed.getLocalizedMessage());
    response.setStatus(FORBIDDEN.value());

    Map<String, String> error = new HashMap<>();
    error.put("error_message", failed.getLocalizedMessage());
    response.setContentType(APPLICATION_JSON_VALUE);

    new ObjectMapper().writeValue(response.getOutputStream(), error);
  }

  /**
   * Creates a response with Status Ok and a body consisting of the generated
   * access and refresh tokens if the authentication is successful
   * @param request
   * @param response
   * @param chain
   * @param authentication
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    User user = (User)authentication.getPrincipal();
    String username = user.getUsername();
    String requestURL = request.getRequestURL().toString();
    List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    Map<String, String> tokens = new HashMap<>();

    tokens.put("access_token",jwtService.getAccessToken(username, requestURL, roles));
    tokens.put("refresh_token",jwtService.getRefreshToken(username, requestURL));
    response.setContentType(APPLICATION_JSON_VALUE);

    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
