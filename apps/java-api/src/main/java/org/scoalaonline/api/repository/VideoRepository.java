package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Video;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface VideoRepository extends Neo4jRepository<Video, String> {

}
