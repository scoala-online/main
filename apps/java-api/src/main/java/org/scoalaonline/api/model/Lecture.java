package org.scoalaonline.api.model;

import org.scoalaonline.api.serializer.LectureMaterialSerializer;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents the Lecture node from the graph database.
 * It contains a title property.
 */
@Node("Lecture")
public class Lecture {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @Property("title")
  private String title;
  
  @Relationship(type = "HAS_MATERIAL", direction = Relationship.Direction.OUTGOING)
  @JsonSerialize(using = LectureMaterialSerializer.class)
  List<LectureMaterial> lectureMaterials ;

  //region Constructors
  public Lecture(){

  }

  public Lecture(String id) {
    this.id = id;
  }

  public Lecture(String id, String title) {
    this.id = id;
    this.title = title;
  }
  //endregion

  //region Getters
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<LectureMaterial> getLectureMaterials() {
    return new ArrayList<>(lectureMaterials);
  }
  //endregion Getters

  //region Setters
  public void setId(String id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setLectureMaterials(List<LectureMaterial> lectureMaterials) {
    this.lectureMaterials = new ArrayList<>(lectureMaterials);
  }
  //endregion Setters

  //region Equals & Hashcode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Lecture lecture = (Lecture) o;
    return Objects.equals(getId(), lecture.id) && Objects.equals(getTitle(), lecture.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getLectureMaterials());
  }
  //endregion
}
