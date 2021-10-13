package org.scoalaonline.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

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

  @Relationship(type = "HAS_ROLE", direction = Relationship.Direction.OUTGOING)
  private List<Role> roles;

  public List<Role> getRoles() {
    return new ArrayList<>(roles);
  }

  public void setRoles(List<Role> roles) {
    this.roles = new ArrayList<>(roles);
  }
}
