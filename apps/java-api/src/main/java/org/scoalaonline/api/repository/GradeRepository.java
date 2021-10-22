package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Grade;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GradeRepository extends Neo4jRepository<Grade, String> {

}
