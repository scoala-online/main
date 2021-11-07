package org.scoalaonline.api.repository;

import org.scoalaonline.api.model.Role;
import org.scoalaonline.api.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, String> {
  Boolean existsByUsername(String username);
  Optional<User> findByUsername(String username);
  Optional<User> findByValidationCode(String validationCode);
  Optional<User> findByResetPasswordCode(String resetPasswordCode);
  @Query("MATCH (a:User)-[r:HAS_ROLE]->(b:Role) WHERE b.name=$roleName return a")
  List<User> findAllByRolesContaining(@Param("roleName") String roleName);
}
