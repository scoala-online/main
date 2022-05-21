package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Represents the 'Grade' node from the graph database.
 * It contains a value property.
 */
@Node("Grade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Grade name is required.")
  @Size(max = 20, message = "Grade name cannot be longer than 20 characters.")
  @Property("value")
  private String value;
}
