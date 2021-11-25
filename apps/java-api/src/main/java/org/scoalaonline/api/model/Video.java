package org.scoalaonline.api.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Duration;
import java.util.Objects;

@Node("Video")
public class Video {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("videoURL")
    private String videoURL;

    @Property("videoTitle")
    private String videoTitle;

    @Property("videoLength")
    private Duration videoLength;

    @Property("teacherURL")
    private String teacherURL;

    @Property("teacherImageURL")
    private String teacherImageURL;

    @Property("transcript")
    private String transcript;

    @Property("summary")
    private String summary;

    @Property("thumbnail")
    private String thumbnail;

    //@Relationships

    //region Constructors
    public Video(){}

    public Video(String id){
      this.id = id;
    }

    public Video(String id, String videoURL, String videoTitle, Duration videoLength, String teacherURL, String teacherImageURL, String transcript, String summary, String thumbnail) {
      this.id = id;
      this.videoURL = videoURL;
      this.videoTitle = videoTitle;
      this.videoLength = videoLength;
      this.teacherURL = teacherURL;
      this.teacherImageURL = teacherImageURL;
      this.transcript = transcript;
      this.summary = summary;
      this.thumbnail = thumbnail;
    }
    //endregion

    //region Getters
    public String getId() {
      return id;
    }

    public String getVideoURL() {
      return videoURL;
    }

    public String getVideoTitle() {
      return videoTitle;
    }

    public Duration getVideoLength() {
      return videoLength;
    }

    public String getTeacherURL() {
      return teacherURL;
    }

    public String getTeacherImageURL() {
      return teacherImageURL;
    }

    public String getTranscript() {
      return transcript;
    }

    public String getSummary() {
      return summary;
    }

    public String getThumbnail() {
      return thumbnail;
    }
  //endregion

    //region Setters

    public void setId(String id) {
      this.id = id;
    }

    public void setVideoURL(String videoURL) {
      this.videoURL = videoURL;
    }

    public void setVideoTitle(String videoTitle) {
      this.videoTitle = videoTitle;
    }

    public void setVideoLength(Duration videoLength) {
      this.videoLength = videoLength;
    }

    public void setTeacherURL(String teacherURL) {
      this.teacherURL = teacherURL;
    }

    public void setTeacherImageURL(String teacherImageURL) {
      this.teacherImageURL = teacherImageURL;
    }

    public void setTranscript(String transcript) {
      this.transcript = transcript;
    }

    public void setSummary(String summary) {
      this.summary = summary;
    }

    public void setThumbnail(String thumbnail) {
      this.thumbnail = thumbnail;
    }

  //endregion

    //region Equals & Hashcode
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Video that = (Video) o;
      return Objects.equals(id, that.id) && Objects.equals(videoURL, that.videoURL) && Objects.equals( videoTitle, that.videoTitle ) && Objects.equals( videoLength, that.videoLength ) && Objects.equals( teacherURL, that.teacherURL ) && Objects.equals( teacherImageURL, that.teacherImageURL ) && Objects.equals( transcript, that.transcript ) && Objects.equals( summary, that.summary ) && Objects.equals( thumbnail, that.thumbnail );
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, videoURL, videoTitle, videoLength, teacherURL, teacherImageURL, transcript, summary, thumbnail);
    }
    //endregion
  }
