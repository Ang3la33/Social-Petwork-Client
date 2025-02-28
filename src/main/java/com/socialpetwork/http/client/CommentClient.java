package com.socialpetwork.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialpetwork.domain.CommentDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;

import java.util.Collections;
import java.util.List;

public class CommentClient {

    private static final String BASE_URL = "http://localhost:8080/comments";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final HttpClient httpClient = new HttpClient();

    public CommentDTO createComment(String content, UserDTO user, PostDTO post) throws Exception {
        CommentDTO comment = new CommentDTO(null, content, user, post);
        String jsonString = objectMapper.writeValueAsString(comment);
        HttpResponse httpResponse = httpClient.post(BASE_URL, jsonString);

        if (httpResponse.getStatusCode() == 200) {
            return objectMapper.readValue(httpResponse.getBody(), CommentDTO.class);
        }
        return null;
    }

    public CommentDTO getCommentById(Long commentId) throws Exception {
        HttpResponse httpResponse = httpClient.get(BASE_URL + "/" + commentId);
        if (httpResponse.getStatusCode() == 200) {
            return objectMapper.readValue(httpResponse.getBody(), CommentDTO.class);
        }
        return null;
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) throws Exception {
        HttpResponse httpResponse = httpClient.get(BASE_URL + "/post/" + postId);
        if (httpResponse.getStatusCode() == 200) {
            return objectMapper.readValue(httpResponse.getBody(), new TypeReference<List<CommentDTO>>() {});
        }
        return Collections.emptyList();
    }

    public CommentDTO updateComment(Long id, String newContent) throws Exception {
        CommentDTO comment = new CommentDTO();
        comment.setId(id);
        comment.setContent(newContent);
        String jsonString = objectMapper.writeValueAsString(comment);

        HttpResponse httpResponse = httpClient.put(BASE_URL + "/" + id, jsonString);

        if (httpResponse.getStatusCode() == 200) {
            return objectMapper.readValue(httpResponse.getBody(), CommentDTO.class);
        }
        return null;
    }

    public boolean deleteComment(Long id) throws Exception {
        HttpResponse httpResponse = httpClient.delete(BASE_URL + "/" + id);
        return httpResponse.getStatusCode() == 204;
    }

}

