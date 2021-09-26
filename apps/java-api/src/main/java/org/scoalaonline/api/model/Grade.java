package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Objects;

/**
 * Represents the 'Grade' node from the graph database.
 * It contains a document property.
 */
@Node("Grade")
public class Grade {

  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @Property("value")
  private int value;

  //@Relationships

  //region Constructor
  public Grade(String id) {
    this.id = id;
  }

  public Grade(String id, int value) {
    this.id = id;
    this.value = value;
  }
  //endregion

  //region Getters
  public String getId() {
    return id;
  }

  public int getValue() {
    return value;
  }
  //endregion

  //region Setters
  public void setId(String id) {
    this.id = id;
  }

  public void setValue(int value) {
    this.value = value;
  }
  //endregion


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Grade)) return false;
    Grade grade = (Grade) o;
    return getValue() == grade.getValue() && Objects.equals(getId(), grade.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getValue());
  }
}
