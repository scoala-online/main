package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Node("Video")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
  @Id
  @GeneratedValue(UUIDStringGenerator.class)
  private String id;

  @NotBlank(message = "Video URL is required.")
  @Property("videoURL")
  private String videoURL;

  @NotBlank(message = "Video title is required.")
  @Size(max = 250, message = "Video title cannot be longer than 250 characters.")
  @Property("videoTitle")
  private String videoTitle;

  @NotBlank(message = "Video length is required.")
  @Min(value = 0, message = "Video cannot be shorter than 0 milliseconds.")
  @Max(value = 86400000, message = "Video cannot be longer than 24 hours.")
  @Property("videoLength")
  private Duration videoLength;

  @NotBlank(message = "Teacher image URL is required.")
  @Property("teacherImageURL")
  private String teacherImageURL;

  @NotBlank(message = "Transcript is required.")
  @Property("transcript")
  private String transcript;

  @NotBlank(message = "Summary is required.")
  @Property("summary")
  private String summary;

  @NotBlank(message = "Thumbnail is required.")
  @Property("thumbnail")
  private String thumbnail;

  @NotBlank(message = "Lecture is required.")
  @Relationship(type = "HAS_VIDEO", direction = Relationship.Direction.INCOMING)
  private Lecture lecture;

  @NotBlank(message = "Teacher is required.")
  @Relationship(type = "HAS_TEACHER", direction = Relationship.Direction.OUTGOING)
  private User teacher;
}
