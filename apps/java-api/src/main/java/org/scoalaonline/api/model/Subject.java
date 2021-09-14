package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Objects;

/**
 * Represents the Subject node from the graph database.
 * It contains a Value property.
 */
@Node("Subject")
public class Subject {
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @Property("value")
  private String value;
  //TODO: add @Relationships

  //region Constructors
  public Subject(){}

  public Subject(String id) {
    this.id = id;
  }

  public Subject(String id, String value) {
    this.id = id;
    this.value = value;
  }
  //endregion

  //region Getters
  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }
  //endregion

  //region Setters
  public void setId(String id) {
    this.id = id;
  }

  public void setValue(String value) {
    this.value = value;
  }
  //endregion

  //region Equals & Hashcode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Subject subject = (Subject) o;
    return Objects.equals(getId(), subject.id) && Objects.equals(getValue(), subject.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getValue());
  }
  //endregion
}
