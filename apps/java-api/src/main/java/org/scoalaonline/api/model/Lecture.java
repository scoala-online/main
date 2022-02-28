package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Objects;

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
  //TODO: add @Relationships

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
  //endregion Getters

  //region Setters
  public void setId(String id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
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
    return Objects.hash(getId(), getTitle());
  }
  //endregion
}
