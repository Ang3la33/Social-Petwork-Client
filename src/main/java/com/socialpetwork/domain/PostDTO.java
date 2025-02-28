package com.socialpetwork.domain;

import java.time.LocalDateTime;

public class PostDTO {

    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;

    public PostDTO(Object o, String newPost) {
        this.createdAt = LocalDateTime.now();
    }

    public PostDTO(Long id, long content, String user, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUserId(UserDTO user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostDTO [id=" + id + ", content=" + content + ", user=" + user + ", createdAt= " + createdAt + "]";
    }
}
