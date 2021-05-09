package com.db4neo.db4neo.controllers;

import com.db4neo.db4neo.dto.FollowsDTO;
import com.db4neo.db4neo.model.Person;
import com.db4neo.db4neo.repository.PersonRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    private PersonRepository personRepository;

    private final Driver driver;


    public PersonController(PersonRepository personRepository, Driver driver) {
        this.personRepository = personRepository;
        this.driver = driver;
    }

    @GetMapping
    public Iterable<Person> findAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/{name}")
    public Person getPersonByName(@PathVariable String name) {
        return personRepository.getPersonByHandleName(name);
    }

    @PostMapping(path = "/follows", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> createRelationShipPerson(@RequestBody FollowsDTO followsDTO) {
        if (followsDTO.follower.equals(followsDTO.target))
            return new ResponseEntity<>(followsDTO, HttpStatus.BAD_REQUEST);
        try (Session session = driver.session()) {
            session.run("MATCH (n:Person {handleName: '" + followsDTO.follower + "'}) " +
                    "MATCH (m:Person {handleName: '" + followsDTO.target + "'}) " +
                    "DROP (n)-[: FOLLOWS]->(m) if exists " +
                    "DROP (n)<-[: FOLLOWED_BY]-(m) if exists " +
                    "CREATE (n)-[: FOLLOWS]->(m)" +
                    "CREATE (n)<-[: FOLLOWED_BY]-(m)");
            return new ResponseEntity(followsDTO, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(followsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PostMapping("/create")
    public void createPeople() {
        try (Session session = driver.session()) {
            session.run("MATCH (n) DETACH DELETE n");
            session.run("DROP CONSTRAINT uniqueName if exists");
            session.run("CREATE CONSTRAINT uniqueName on (n:Person) assert n.handleName is unique");
            session.run("WITH ['Amy','Bob','Cal','Dan','Eve', 'Svend', 'Erik', 'Susanne', 'Pernille', 'Per'] AS handleNames " +
                    "UNWIND handleNames AS handleName " +
                    "CREATE (:Person {handleName: handleName})");
            createRelationShipPerson(new FollowsDTO("Amy", "Bob"));
            createRelationShipPerson(new FollowsDTO("Amy", "Svend"));
            createRelationShipPerson(new FollowsDTO("Erik", "Bob"));
            createRelationShipPerson(new FollowsDTO("Susanne", "Dan"));
            createRelationShipPerson(new FollowsDTO("Per", "Bob"));
            createRelationShipPerson(new FollowsDTO("Dan", "Eve"));

        }
    }

}
