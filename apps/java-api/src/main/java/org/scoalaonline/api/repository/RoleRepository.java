package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface RoleRepository extends Neo4jRepository<Role, String> {
  Optional<Role> findByName(String name);
}
