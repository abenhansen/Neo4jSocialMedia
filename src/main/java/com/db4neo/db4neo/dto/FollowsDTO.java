package com.db4neo.db4neo.dto;

public class FollowsDTO {

    public String follower;

    public String target;

    public FollowsDTO(){

    }

    public FollowsDTO(String follower, String target) {
        this.follower = follower;
        this.target = target;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
