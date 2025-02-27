package com.socialpetwork.domain;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private PostDTO post;


    public CommentDTO(Long id, String content, UserDTO user, PostDTO post) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

}
