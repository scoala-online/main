package org.scoalaonline.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.scoalaonline.api.model.Subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectListSerializer extends StdSerializer<List<Subject>> {
  public SubjectListSerializer() {
    this(null);
  }

  public SubjectListSerializer(Class<List<Subject>> t) {
    super(t);
  }

  @Override
  public void serialize(List<Subject> subjectList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    List<SerializedSubject> results = new ArrayList<>();
    for (Subject subject : subjectList) {
      SerializedSubject result = new SerializedSubject(subject.getId(), subject.getValue());
      results.add(result);
    }
    jsonGenerator.writeObject(results);
  }

  @AllArgsConstructor
  private class SerializedSubject {
    private String id;
    private String value;
  }
}