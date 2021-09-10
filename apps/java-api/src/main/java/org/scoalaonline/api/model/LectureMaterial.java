package org.scoalaonline.api.model;


import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

@Node("LectureMaterial")
public class LectureMaterial {
  @Id
  @GeneratedValue
  private String id;

  @Property("document")
  private String document;

  //@Relationships

  //region Constructors
  public LectureMaterial(){}

  public LectureMaterial(String id){
    this.id = id;
  }

  public LectureMaterial(String id, String document) {
    this.id = id;
    this.document = document;
  }
  //endregion

  //region Getters
  public String getId() {
    return id;
  }

  public String getDocument() {
    return document;
  }
  //endregion

  //region Setters
  public void setDocument(String document) {
    this.document = document;
  }
  //endregion


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LectureMaterial that = (LectureMaterial) o;
    return Objects.equals(id, that.id) && Objects.equals(document, that.document);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, document);
  }
}
