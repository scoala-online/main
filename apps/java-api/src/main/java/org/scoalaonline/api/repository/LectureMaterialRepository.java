package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.LectureMaterial;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LectureMaterialRepository extends Neo4jRepository<LectureMaterial, String> {

}
