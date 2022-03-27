package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents the Lecture node from the graph database.
 * It contains a title property.
 */
@Node("Lecture")
@Data
@NoArgsConstructor
@AllArgsConstructor
// @Builder
public class Lecture {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @Property("title")
  private String title;
  
  @Relationship(type = "HAS_MATERIAL", direction = Relationship.Direction.OUTGOING)
  List<LectureMaterial> lectureMaterials;

  public Lecture(String id) {
    this.id = id;
  }

  public Lecture(String id, String title) {
    this.id = id;
    this.title = title;
  } 

  public List<LectureMaterial> getLectureMaterials() {
    return new ArrayList<>(lectureMaterials);
  }

  public void setLectureMaterials(List<LectureMaterial> lectureMaterials) {
    this.lectureMaterials = new ArrayList<>(lectureMaterials);
  }
}
