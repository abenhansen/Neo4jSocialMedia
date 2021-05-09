package com.db4neo.db4neo.repository;

import com.db4neo.db4neo.model.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PostRepository extends Neo4jRepository<Post, Long> {


}
