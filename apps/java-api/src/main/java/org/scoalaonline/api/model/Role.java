package org.scoalaonline.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * Represents the Role node from the graph database.
 * It contains a Name property.
 */
@Node("Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
  
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Role name is required.")
  @Size(max = 20, message = "Role name cannot be longer than 20 characters.")
  @Property("name")
  private String name;
}
