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
 * Represents the Lecture node from the graph database.
 * It contains a title property.
 */
@Node("Lecture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Lecture name is required.")
  @Size(max = 250, message = "Lecture name cannot be longer than 250 characters.")
  @Property("title")
  private String title;
}
