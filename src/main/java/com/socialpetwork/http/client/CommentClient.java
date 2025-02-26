package com.socialpetwork.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.domain.CommentDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class CommentClient {

    private static final String BASE_URL = "http://localhost:8080/comments";
    private final ObjectMapper mapper = new ObjectMapper();

    public CommentDTO createComment(String content, UserDTO user, PostDTO post) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        CommentDTO comment = new CommentDTO(null, content, user, post);
        String json = mapper.writeValueAsString(comment);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = json.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return mapper.readValue(br, CommentDTO.class);
            }
        }
        return null;
    }

    public CommentDTO getCommentById(Long id) throws Exception {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return mapper.readValue(br, CommentDTO.class);
            }
        }
        return null;
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) throws Exception {
        URL url = new URL(BASE_URL + "/" + postId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return mapper.readValue(br, List.class);
            }
        }
        return Collections.emptyList();
    }

    public CommentDTO updateComment(Long id, String newComment) throws Exception {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"content\": \"" + newComment + "\"}";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return mapper.readValue(br, CommentDTO.class);
            }
        }
        return null;
    }

    public boolean deleteComment(Long id) throws Exception {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_NO_CONTENT;

    }

}
