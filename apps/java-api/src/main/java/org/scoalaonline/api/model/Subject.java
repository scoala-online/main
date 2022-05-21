package org.scoalaonline.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the Subject node from the graph database.
 * It contains a Value property.
 */
@Node("Subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Subject name is required.")
  @Size(max = 20, message = "Subject cannot be longer than 30 characters.")
  @Property("value")
  private String value;

  @NotBlank(message = "Grade is required.")
  @Relationship(type = "HAS_GRADE", direction = Relationship.Direction.OUTGOING)
  private Grade grade;
}
