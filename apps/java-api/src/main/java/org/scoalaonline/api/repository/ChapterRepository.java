package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Chapter;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ChapterRepository extends Neo4jRepository<Chapter, String> {

}