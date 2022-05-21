package org.scoalaonline.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the 'LectureMaterial' node from the graph database.
 * It contains a document property.
 */
@Node("LectureMaterial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureMaterial {
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Lecture Material document URL is required.")
  @Property("document")
  private String document;

  @NotBlank(message = "Lecture Material title is required.")
  @Size(max = 255, message = "Lecture Material title cannot be longer than 255 characters.")
  @Property("title")
  private String title;
}
