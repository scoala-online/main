package org.scoalaonline.api.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.scoalaonline.api.model.LectureMaterial;

public class LectureMaterialSerializer extends StdSerializer<List<LectureMaterial>> {

    class LectureMaterialId {
        private String id;
        private String document;

        public LectureMaterialId(){}

        public LectureMaterialId(String id){
            this.id = id;
        }

        public LectureMaterialId(String id, String document) {
            this.id = id;
            this.document = document;
        }

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
            LectureMaterialId that = (LectureMaterialId) o;
            return Objects.equals(id, that.id) && Objects.equals(document, that.document);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, document);
        }
    }

    public LectureMaterialSerializer(Class<List<LectureMaterial>> t) {
        super(t);
    }

    @Override
    public void serialize(List<LectureMaterial> lectureMaterials, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        List<LectureMaterialId> ids = new ArrayList<>();
        for (LectureMaterial lectureMaterial : lectureMaterials){
          LectureMaterialId id = new LectureMaterialId(lectureMaterial.getId(),lectureMaterial.getDocument());
          ids.add(id);
        }
        jsonGenerator.writeObject(ids);
    }
}
    
