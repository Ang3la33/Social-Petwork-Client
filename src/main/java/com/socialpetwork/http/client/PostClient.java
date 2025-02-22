package com.socialpetwork.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
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

    public PostClient() {
        httpClient = new HttpClient();
        objectMapper = new ObjectMapper();
    }

    public List<PostDTO> getAllPosts() {
        String url = BASE_URL;
        try {
            HttpResponse response = httpClient.get(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
