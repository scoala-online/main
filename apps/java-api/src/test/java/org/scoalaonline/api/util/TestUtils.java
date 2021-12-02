package org.scoalaonline.api.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;
import java.util.List;

public class TestUtils {
  //static methods
  /* UTIL Method for building json
  * */
  public static StringWriter buildJsonBody(List<String> fields, List<Object> values) throws IOException {

    JsonFactory factory = new JsonFactory();
    StringWriter jsonObjectWriter = new StringWriter();
    JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

    generator.useDefaultPrettyPrinter();
    generator.writeStartObject();
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    generator.setCodec(mapper);

    for(int i = 0; i < fields.size(); i++) {
      generator.writeFieldName(fields.get(i));
      if (values.get(i) == null)
        generator.writeNull();
      else if(values.get(i).getClass().equals(String.class))
        generator.writeString((String) values.get(i));
      else if(values.get(i).getClass().equals(Double.class))
        generator.writeNumber((double) values.get(i));
      else if(values.get(i).getClass().equals(Duration.class))
        generator.writeObject((Duration)values.get(i));
      /* For more possible cases, add else if here
       With the required type
       Example:
       else if(value.get(i).getClass().equals(MyClass.class))
          generator.writeObject((MyObject) value.get(i));
      */
    }

    generator.writeEndObject();
    generator.close();

    return jsonObjectWriter;
  }
}
