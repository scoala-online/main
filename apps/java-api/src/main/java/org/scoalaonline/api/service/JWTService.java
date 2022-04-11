package org.scoalaonline.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Contains the logic for managing JWT
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService {
  private Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
  private int accessTimer = 15 * 60 * 1000;
  private int refreshTimer = 24 * 60 * 60 * 1000;

  /**
   * Generates an access token based on the username, requestURL and the list of roles, using the algorithm
   * @param username the username of the User entry
   * @param requestURL the URL of the request
   * @param roles the list of roles given to the User entry
   * @return the access token
   */
  public String getAccessToken(String username, String requestURL, List<String> roles) {
    return JWT.create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + accessTimer))
      .withIssuer(requestURL)
      .withClaim("roles", roles)
      .sign(algorithm);
  }

  /**
   * Generates an refresh token based on the username and the requestURL, using the algorithm
   * @param username the username of the User entry
   * @param requestURL the URL of the request
   * @return the refresh token
   */
  public String getRefreshToken(String username, String requestURL) {
    return JWT.create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + refreshTimer))
      .withIssuer(requestURL)
      .sign(algorithm);
  }

  /**
   * Generates a decoded JWT with information on the authenticated user
   * based on the given token, using the algorithm
   * @param token
   * @return a decoded JWT
   */
  public DecodedJWT decodeJWT(String token) {
    JWTVerifier verifier = JWT.require(algorithm).build();
    return verifier.verify(token);
  }

}
