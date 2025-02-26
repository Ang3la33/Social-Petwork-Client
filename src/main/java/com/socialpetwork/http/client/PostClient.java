package com.socialpetwork.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;

import java.io.IOException;
import java.util.List;

public class PostClient {

    private final String BASE_URL = "http://localhost:8080/posts";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PostClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    public List<PostDTO> getAllPosts() {
        String url = BASE_URL;
        try {
            HttpResponse response = httpClient.get(url);

            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), new TypeReference<List<PostDTO>>() {});
            } else {
                System.out.println("Error- Status Code: " + response.getStatusCode());
                return List.of(); // returns empty list if error
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return List.of();
        }
    }

    public PostDTO getPostById(Long id) {
        String url = BASE_URL + "/" + id;
        try {
            HttpResponse response = httpClient.get(url);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), PostDTO.class);
            } else {
                System.out.println("Post Not Found. Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public PostDTO createPost(PostDTO postDTO, Long userId) {
        String url = BASE_URL + "?user_id=" + userId;
        try {
            String jsonPayload = objectMapper.writeValueAsString(postDTO);
            HttpResponse response = httpClient.post(url, jsonPayload);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), PostDTO.class);
            } else {
                System.out.println("Error - Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public PostDTO updatePost(Long id, Long userId, PostDTO postDTO) {
        String url = BASE_URL + "/" + id + "?user_id=" + userId;
        try {
            String requestBody = objectMapper.writeValueAsString(postDTO);
            HttpResponse response = httpClient.put(url, requestBody);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), PostDTO.class);
            } else {
                System.out.println("Error - Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public boolean deletePost(Long id) {
        String url = BASE_URL + "/" + id;
        try{
            HttpResponse response = httpClient.delete(url);
            if (response.getStatusCode() == 200) {
                System.out.println("Post Deleted Successfully");
                return true;
            } else {
                System.out.println("Error - Status Code: " + response.getStatusCode());
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
