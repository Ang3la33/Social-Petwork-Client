package com.socialpetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostClientTest {

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private PostClient postClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testGetAllPosts() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        List<PostDTO> mockPosts = List.of(
                new PostDTO(1L,"Post 1",1L,now),
                new PostDTO(2L,"Post 2",1L,now)
        );
        String jsonResponse = objectMapper.writeValueAsString(mockPosts);
        HttpResponse mockResponse = new HttpResponse(200, jsonResponse);

        when(httpClient.get("http://localhost:8080/posts")).thenReturn(mockResponse);

        List<PostDTO> posts = postClient.getAllPosts();

        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals("Post 1",posts.get(0).getContent());
        assertEquals(now,posts.get(0).getCreatedAt());
        assertEquals("Post 2",posts.get(1).getContent());
        assertEquals(now,posts.get(1).getCreatedAt());
    }

    @Test
    void testCreatePost() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        PostDTO newPost = new PostDTO(null,"New Post",1L,now);
        PostDTO createdPost = new PostDTO(1L,"New Post",1L,now);

        String requestBody = objectMapper.writeValueAsString(newPost);
        String responseBody = objectMapper.writeValueAsString(createdPost);

        HttpResponse mockResponse = new HttpResponse(200, responseBody);

        when(httpClient.post(eq("http://localhost:8080/posts?user_id=1"), eq(requestBody))).thenReturn(mockResponse);

        PostDTO result = postClient.createPost(newPost,1L);

        assertNotNull(result);
        assertEquals("New Post",result.getContent());
        assertEquals(now,result.getCreatedAt());
        assertEquals(1L,result.getId());
    }

    @Test
    void testUpdatePost() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        PostDTO updatedPost = new PostDTO(1L,"Updated Post",1L,now);

        String requestBody = objectMapper.writeValueAsString(updatedPost);
        String responseBody = objectMapper.writeValueAsString(updatedPost);

        HttpResponse mockResponse = new HttpResponse(200, responseBody);

        when(httpClient.put(eq("http://localhost:8080/posts/1?user_id=1"), anyString())).thenReturn(mockResponse);

        PostDTO result = postClient.updatePost(1L,1L, updatedPost);

        assertNotNull(result);
        assertEquals("Updated Post",result.getContent());
        assertEquals(now,result.getCreatedAt());
    }

    @Test
    void testDeletePost() throws IOException {
        Long postId = 1L;

        HttpResponse mockResponse = new HttpResponse(200, "");
        when(httpClient.delete(eq("http://localhost:8080/posts/"+postId))).thenReturn(mockResponse);

        boolean isSuccessful = postClient.deletePost(postId);
        assertEquals(true, isSuccessful);

        HttpResponse mockFailure = new HttpResponse(404, "");
        when(httpClient.delete(eq("http://localhost:8080/posts/"+postId))).thenReturn(mockFailure);
    }
}
