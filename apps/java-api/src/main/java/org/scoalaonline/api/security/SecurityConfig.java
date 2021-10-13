package org.scoalaonline.api.security;

import lombok.RequiredArgsConstructor;
import org.scoalaonline.api.filter.CustomAuthenticationFilter;
import org.scoalaonline.api.filter.CustomAuthorizationFilter;
import org.scoalaonline.api.service.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures access to API resources
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JWTService jwtService;

  /**
   * Sets a BCryptPasswordEncoder for encoding UserDetails password
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  /**
   * Adds filters for Authentication and Authorization processes
   * Configures access to API resources
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(),jwtService);
    customAuthenticationFilter.setFilterProcessesUrl("/users/login");
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests().antMatchers("/users/login/**", "/users/token/refresh/**").permitAll();

    http.authorizeRequests().antMatchers("/roles/**").hasAnyAuthority("ROLE_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/role/**").hasAnyAuthority("ROLE_ADMIN");
    // Custom authorization implemented in UserController.
    http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/username/**").permitAll();
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/register/**").permitAll();
    http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/{id}/**").access("@userSecurity.hasUserId(authentication,#id) or hasAnyAuthority(\"ROLE_ADMIN\")");
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/users/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority("ROLE_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/lecture-materials/**").permitAll();
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/lecture-materials/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/lecture-materials/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/lecture-materials/**").hasAnyAuthority("ROLE_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/lectures/**").permitAll();
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/lectures/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/lectures/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/lectures/**").hasAnyAuthority("ROLE_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/subjects/**").permitAll();
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/subjects/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/subjects/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/subjects/**").hasAnyAuthority("ROLE_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/grades/**").permitAll();
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/grades/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/grades/**").hasAnyAuthority("ROLE_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/grades/**").hasAnyAuthority("ROLE_ADMIN");

    http.authorizeRequests().antMatchers("/**").denyAll();

    http.authorizeRequests().anyRequest().authenticated();

    http.addFilter(customAuthenticationFilter);

    http.addFilterBefore(new CustomAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * Creates AuthenticationManagerBean
   * @return the Bean
   * @throws Exception
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
