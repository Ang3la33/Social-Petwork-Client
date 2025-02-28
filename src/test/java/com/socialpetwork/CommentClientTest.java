package com.socialpetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialpetwork.domain.CommentDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.client.CommentClient;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CommentClientTest {

    private CommentClient commentClient;

    @Mock
    private HttpClient httpClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        httpClient = Mockito.mock(HttpClient.class);
        commentClient = new CommentClient();
    }

    @Test
    public void testCreateComment() throws Exception {
        CommentDTO mockComment = new CommentDTO(1L, "Test Content", new UserDTO(1L, "User"), new PostDTO(1L, "Test Post"));
        HttpResponse mockResponse = new HttpResponse(201, objectMapper.writeValueAsString(mockComment));
        when(httpClient.post(eq("http://localhost:8080/comments"), any())).thenReturn(mockResponse);

        CommentDTO result = commentClient.createComment("Test Content", new UserDTO(1L, "User"), new PostDTO(1L, "Test Post"));
        assertNotNull(result);
        assertEquals("Test Content", result.getContent());
    }

    @Test
    public void testGetCommentById() throws Exception {
        CommentDTO mockComment = new CommentDTO(1L, "Test Content", new UserDTO(1L, "User"), new PostDTO(1L, "Test Post"));
        HttpResponse mockResponse = new HttpResponse(200, objectMapper.writeValueAsString(mockComment));
        when(httpClient.get("http://localhost:8080/comments/1")).thenReturn(mockResponse);

        CommentDTO result = commentClient.getCommentById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetCommentsByPostId() throws Exception {
        CommentDTO mockComment = new CommentDTO(1L, "Test Content", new UserDTO(1L, "User"), new PostDTO(1L, "Test Post"));
        List<CommentDTO> mockComments = Collections.singletonList(mockComment);
        HttpResponse mockResponse = new HttpResponse(200, objectMapper.writeValueAsString(mockComments));
        when(httpClient.get("http://localhost:8080/comments/post/1")).thenReturn(mockResponse);

        List<CommentDTO> result = commentClient.getCommentsByPostId(1L);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateComment() throws Exception {
        CommentDTO mockComment = new CommentDTO(1L, "Updated Content", new UserDTO(1L, "User"), new PostDTO(1L, "Test Post"));
        HttpResponse mockResponse = new HttpResponse(200, objectMapper.writeValueAsString(mockComment));
        when(httpClient.get("http://localhost:8080/comments/1")).thenReturn(mockResponse);
        when(httpClient.put(eq("http://localhost:8080/comments/1"), any())).thenReturn(mockResponse);

        CommentDTO result = commentClient.updateComment(1L, "Updated Content");
        assertNotNull(result);
        assertEquals("Updated Content", result.getContent());
    }

    @Test
    public void testDeleteComment() throws Exception {
        HttpResponse mockResponse = new HttpResponse(204, "");
        when(httpClient.delete("http://localhost:8080/comments/1")).thenReturn(mockResponse);

        boolean result = commentClient.deleteComment(1L);
        assertTrue(result);
    }
}







