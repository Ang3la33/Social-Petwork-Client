package com.socialpetwork.domain;

import java.time.LocalDateTime;

public class FollowDTO {

    private Long id;               // Unique ID for the follow relationship
    private Long followerId;       // The user who is following
    private Long followedUserId;   // The user being followed
    private LocalDateTime followedAt; // Timestamp when the follow occurred

    // Default constructor
    public FollowDTO() {}

    // Parameterized constructor
    public FollowDTO(Long id, Long followerId, Long followedUserId, LocalDateTime followedAt) {
        this.id = id;
        this.followerId = followerId;
        this.followedUserId = followedUserId;
        this.followedAt = followedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Long followedUserId) {
        this.followedUserId = followedUserId;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }

    @Override
    public String toString() {
        return "FollowDTO [id=" + id +
                ", followerId=" + followerId +
                ", followedUserId=" + followedUserId +
                ", followedAt=" + followedAt + "]";
    }
}
