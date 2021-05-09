package com.db4neo.db4neo.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@NodeEntity
public class Person {


    @Id
    private String handleName;

    @Relationship(type = "FOLLOWED_BY", direction = INCOMING)
    private List<Person> followers;

    @Relationship(type = "FOLLOWS")
    private List<Person> follows;

    @Relationship(type = "CREATED_POST")
    private List<Post> posts;

    public Person() {
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public List<Person> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Person> followers) {
        this.followers = followers;
    }

    public List<Person> getFollows() {
        return follows;
    }

    public void setFollows(List<Person> follows) {
        this.follows = follows;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
