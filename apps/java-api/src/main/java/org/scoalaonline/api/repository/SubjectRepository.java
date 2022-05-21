package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Subject;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SubjectRepository extends Neo4jRepository<Subject, String> {

}
