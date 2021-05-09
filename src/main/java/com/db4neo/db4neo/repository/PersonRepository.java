package com.db4neo.db4neo.repository;

import com.db4neo.db4neo.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, String> {

    Person getPersonByHandleName(String handleName);

    //Not Implemented this is how we make custom queries
    @Query("MATCH (m:Movie)<-[r:ACTED_IN]-(a:Person) RETURN m,r,a LIMIT $props")
    Collection<Person> graph(@Param("props") int limit);
}
