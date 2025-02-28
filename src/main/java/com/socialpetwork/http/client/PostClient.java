package com.socialpetwork.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;

import java.io.IOException;
import java.util.List;

public class PostClient {

    private static final String BASE_URL = "http://localhost:8080/posts";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // ✅ Constructor for ClientMenu
    public PostClient() {
        this.httpClient = new HttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // ✅ Constructor for Testing (Mocking)
    public PostClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // 🔹 Fetch all posts
    public List<PostDTO> getAllPosts() {
        String url = BASE_URL;
        try {
            HttpResponse response = httpClient.get(url);

            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), new TypeReference<List<PostDTO>>() {});
            } else {
                System.out.println("❌ Error - Status Code: " + response.getStatusCode());
                return List.of();
            }
        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return List.of();
        }
    }

    // 🔹 Fetch only posts created by the logged-in user
    public List<PostDTO> getUserPosts(Long userId) {
        String url = BASE_URL + "/user/" + userId; // Endpoint: /posts/user/{userId}
        try {
            HttpResponse response = httpClient.get(url);

            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), new TypeReference<List<PostDTO>>() {});
            } else {
                System.out.println("❌ Error fetching user posts - Status Code: " + response.getStatusCode());
                return List.of();
            }
        } catch (IOException e) {
            System.out.println("❌ Error fetching user posts: " + e.getMessage());
            return List.of();
        }
    }

    // 🔹 Fetch a single post by ID
    public PostDTO getPostById(Long id) {
        String url = BASE_URL + "/" + id;
        try {
            HttpResponse response = httpClient.get(url);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), PostDTO.class);
            } else {
                System.out.println("❌ Post Not Found. Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return null;
        }
    }

    public PostDTO createPost(PostDTO postDTO, Long userId) {
        String url = BASE_URL + "?userId=" + userId;
        try {
            String jsonPayload = objectMapper.writeValueAsString(postDTO);
            System.out.println("📦 JSON Payload: " + jsonPayload);
            HttpResponse response = httpClient.post(url, jsonPayload);

            if (response.getStatusCode() == 201) {
                return objectMapper.readValue(response.getBody(), PostDTO.class);
            } else {
                System.out.println("❌ Error - Status Code: " + response.getStatusCode());
                System.out.println("❌ Error Response: " + response.getBody());
                return null;
            }
        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return null;
        }
    }


    // 🔹 Update a post
    public PostDTO updatePost(Long id, Long userId, PostDTO postDTO) {
        String url = BASE_URL + "/" + id + "?user_id=" + userId;
        try {
            String requestBody = objectMapper.writeValueAsString(postDTO);
            HttpResponse response = httpClient.put(url, requestBody);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), PostDTO.class);
            } else {
                System.out.println("❌ Error - Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return null;
        }
    }

    // 🔹 Delete a post
    public boolean deletePost(Long id) {
        String url = BASE_URL + "/" + id;
        try {
            HttpResponse response = httpClient.delete(url);
            if (response.getStatusCode() == 200) {
                System.out.println("✅ Post Deleted Successfully");
                return true;
            } else {
                System.out.println("❌ Error - Status Code: " + response.getStatusCode());
                return false;
            }
        } catch (IOException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return false;
        }
    }
}
