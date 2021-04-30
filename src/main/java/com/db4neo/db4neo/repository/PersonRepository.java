package com.db4neo.db4neo.repository;

import com.db4neo.db4neo.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Person getPersonByName(String name);

}
