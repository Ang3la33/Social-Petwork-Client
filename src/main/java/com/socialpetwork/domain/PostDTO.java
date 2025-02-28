package com.socialpetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostDTO {
    private Long id;
    private UserDTO user;
    private String content;
    private LocalDateTime createdAt;

    public PostDTO() {
        this.createdAt = LocalDateTime.now();
    }

    public PostDTO(Long id, String content, UserDTO user, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.createdAt = (createdAt == null) ? LocalDateTime.now() : createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        return "PostDTO [id=" + id + ", user=" + user + ", content=" + content + ", createdAt=" + createdAt + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDTO)) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(id, postDTO.id) &&
                Objects.equals(user, postDTO.user) &&
                Objects.equals(content, postDTO.content) &&
                Objects.equals(createdAt, postDTO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, content, createdAt);
    }
}


