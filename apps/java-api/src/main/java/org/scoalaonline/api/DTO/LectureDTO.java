package org.scoalaonline.api.DTO;

import java.util.List;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class LectureDTO {

    private String title;

    List<String> lectureMaterials;
}
