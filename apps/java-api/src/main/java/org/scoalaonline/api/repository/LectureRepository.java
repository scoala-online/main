package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Lecture;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LectureRepository extends Neo4jRepository<Lecture, String> {
}
