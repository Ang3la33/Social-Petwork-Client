package com.socialpetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostDTO {

    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    // Default constructor
    public PostDTO() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for getAllPosts and createPost
    public PostDTO(Long id, Long userId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    // Simplified constructor for creating a new post without an ID
    public PostDTO(Long userId, String content) {
        this(null, userId, content, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PostDTO [id=" + id + ", userId=" + userId + ", content=" + content + ", createdAt=" + createdAt + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDTO)) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(id, postDTO.id) &&
                Objects.equals(userId, postDTO.userId) &&
                Objects.equals(content, postDTO.content) &&
                Objects.equals(createdAt, postDTO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, content, createdAt);
    }
}


