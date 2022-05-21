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
 * Represents the Chapter node from the graph database.
 * It contains a Title property.
 */
@Node("Chapter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chapter {
  
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Chapter title is required.")
  @Size(max = 255, message = "Chapter title cannot be longer than 255 characters.")
  @Property("title")
  private String title;
}
