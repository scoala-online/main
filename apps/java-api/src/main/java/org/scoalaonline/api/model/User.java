package org.scoalaonline.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *Represents the User node from the graph database.
 * It contains the following properties: Name, Username, Password and Roles.
 */
@Node("User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @Property("name")
  private String name;

  @Property("username")
  private String username;

  @Property("password")
  private String password;

  @Property("created_at")
  private LocalDateTime createdAt;

  @Property("validated_at")
  private LocalDateTime validatedAt;

  @Property("last_modified_at")
  private LocalDateTime lastModifiedAt;

  @Property("validated")
  private Boolean validated;

  @Property("validation_code")
  private String validationCode;

  @Property("reset_password_code")
  private String resetPasswordCode;

  @Property("reset_password_code_expiry_date")
  private LocalDateTime resetPasswordCodeExpiryDate;

  @Relationship(type = "HAS_ROLE", direction = Relationship.Direction.OUTGOING)
  private List<Role> roles;

  public List<Role> getRoles() {
    return new ArrayList<>(roles);
  }

  public void setRoles(List<Role> roles) {
    this.roles = new ArrayList<>(roles);
  }
}
