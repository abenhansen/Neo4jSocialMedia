package com.db4neo.db4neo.controllers;

import com.db4neo.db4neo.dto.CreatePostDTO;
import com.db4neo.db4neo.dto.FollowsDTO;
import com.db4neo.db4neo.dto.LikedPostDTO;
import com.db4neo.db4neo.model.Post;
import com.db4neo.db4neo.repository.PersonRepository;
import com.db4neo.db4neo.repository.PostRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/post")
public class PostController {


    private PostRepository postRepository;

    private final Driver driver;

    public PostController(PostRepository postRepository, Driver driver) {
        this.postRepository = postRepository;
        this.driver = driver;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<FollowsDTO> createPost(@RequestBody CreatePostDTO createPostDTO) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n:Person {handleName: '" + createPostDTO.getAuthor() + "'}) " +
                    "CREATE (p:Post {text: '" + createPostDTO.getText() + "', timeStamp: '" + LocalDateTime.now() + "'}) " +
                    "CREATE (n)-[: CREATED_POST]->(p)" +
                    "RETURN id(p) AS post_id");
            Record record = result.single();
            String id = record.get("post_id").toString();
            for (String taggedPerson : createPostDTO.getTaggedPeople()) {
                session.run("MATCH (" + taggedPerson + ":Person {handleName: '" + taggedPerson + "'}) " +
                        "MATCH (p:Post) " +
                        "WHERE ID(p) = " + id + " " +
                        "CREATE (" + taggedPerson + ")-[: TAGGED_IN]->(p) ");
            }
            return new ResponseEntity(createPostDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(createPostDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/like", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LikedPostDTO> likePost(@RequestBody LikedPostDTO likedPostDTO) {
        try (Session session = driver.session()) {
            session.run("MATCH (n:Person {handleName: '"+likedPostDTO.getLikedPerson()+"'})-[r:LIKED]->(p:Post) " +
                    "WHERE ID(p) = " + likedPostDTO.getPostId() + " " +
                    "DELETE r ");
            session.run("MATCH (p:Post) " +
                                "WHERE ID(p) = " + likedPostDTO.getPostId() + " "  +
                        "MATCH (n:Person {handleName: '" + likedPostDTO.getLikedPerson() + "'}) " +
                        "CREATE (n)-[: LIKED]->(p) ");
            return new ResponseEntity(likedPostDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(likedPostDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/unlike", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LikedPostDTO> unlikePost(@RequestBody LikedPostDTO likedPostDTO) {
        try (Session session = driver.session()) {
            session.run("MATCH (n:Person {handleName: '"+likedPostDTO.getLikedPerson()+"'})-[r:LIKED]->(p:Post) " +
                    "WHERE ID(p) = " + likedPostDTO.getPostId() + " " +
                    "DELETE r ");
            return new ResponseEntity(likedPostDTO, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(likedPostDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
