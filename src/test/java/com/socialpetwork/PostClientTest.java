package com.socialpetwork;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostClientTest {

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private PostClient postClient;

    private ObjectMapper objectMapper;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        fixedDateTime = LocalDateTime.of(2024, 2, 1, 12, 0, 0); // Use fixed timestamp
    }

    @Test
    public void testGetAllPosts_success() throws Exception {
        UserDTO exampleUser = new UserDTO(101L, "Example", "1990-01-01", "example@example.com", "example", "password");
        List<PostDTO> expectedPosts = Arrays.asList(
                new PostDTO(1L, "Post 1", exampleUser, fixedDateTime),
                new PostDTO(2L, "Post 2", exampleUser, fixedDateTime)
        );

        String jsonPayload = objectMapper.writeValueAsString(expectedPosts);
        HttpResponse mockResponse = new HttpResponse(200, jsonPayload);
        when(httpClient.get("http://localhost:8080/posts")).thenReturn(mockResponse);

        List<PostDTO> actualPosts = postClient.getAllPosts();

        assertNotNull(actualPosts);
        assertEquals(expectedPosts.size(), actualPosts.size()); // Compare size instead of objects directly
        assertEquals(expectedPosts.get(0).getContent(), actualPosts.get(0).getContent()); // Compare content
        verify(httpClient).get("http://localhost:8080/posts");
    }

    @Test
    public void testGetAllPosts_errorResponse() throws Exception {
        HttpResponse mockResponse = new HttpResponse(500, "Internal Server Error");
        when(httpClient.get("http://localhost:8080/posts")).thenReturn(mockResponse);

        List<PostDTO> actualPosts = postClient.getAllPosts();

        assertNotNull(actualPosts);
        assertTrue(actualPosts.isEmpty());
        verify(httpClient).get("http://localhost:8080/posts");
    }

    @Test
    public void testCreatePost_success() throws Exception {
        UserDTO exampleUser = new UserDTO(1L, "Example", "1990-01-01", "example@example.com", "example", "password");
        PostDTO newPost = new PostDTO(null, "New post", null, null);
        PostDTO createdPost = new PostDTO(3L, "New post", exampleUser, fixedDateTime);
        String jsonPayload = objectMapper.writeValueAsString(createdPost);

        String url = "http://localhost:8080/posts?userId=1"; // FIXED userId casing
        HttpResponse mockResponse = new HttpResponse(201, jsonPayload);
        when(httpClient.post(eq(url), any(String.class))).thenReturn(mockResponse);

        PostDTO result = postClient.createPost(newPost, 1L);

        assertNotNull(result);
        assertEquals(createdPost.getId(), result.getId()); // Compare ID instead of objects
        assertEquals(createdPost.getContent(), result.getContent()); // Compare content
        verify(httpClient).post(eq(url), any(String.class));
    }

    @Test
    public void testCreatePost_invalidResponse() throws Exception {
        PostDTO newPost = new PostDTO(null, "Invalid post", null, null);
        HttpResponse mockResponse = new HttpResponse(400, "Invalid Request");

        String url = "http://localhost:8080/posts?userId=1"; // FIXED userId casing
        when(httpClient.post(eq(url), any(String.class))).thenReturn(mockResponse);

        PostDTO result = postClient.createPost(newPost, 1L);

        assertNull(result);
        verify(httpClient).post(eq(url), any(String.class));
    }
}
