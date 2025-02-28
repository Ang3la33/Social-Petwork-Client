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
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CommentClientTest {

    @Mock
    private HttpClient mockHttpClient;

    @InjectMocks
    private CommentClient commentClient;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UserDTO mockUser;
    private PostDTO mockPost;
    private CommentDTO mockComment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new UserDTO(1L, "John Doe", "1990-01-01", "john@example.com", "johndoe", "password");
        mockPost = new PostDTO(1L, "Test Post Content", mockUser, null);
        mockComment = new CommentDTO(1L, "Test Comment", mockUser, mockPost);
    }

    @Test
    public void testCreateComment_Success() throws Exception {
        // Arrange
        String commentJson = objectMapper.writeValueAsString(mockComment);
        HttpResponse mockResponse = new HttpResponse(201, commentJson); // Status 201 for Created

        when(mockHttpClient.post(eq("http://localhost:8080/comments"), anyString())).thenReturn(mockResponse);

        // Act
        CommentDTO result = commentClient.createComment(mockComment);

        // Assert
        assertNotNull(result, "Comment creation should return a non-null object.");
        assertEquals(mockComment.getId(), result.getId(), "The ID of the comment should match.");
        assertEquals(mockComment.getContent(), result.getContent(), "The content of the comment should match.");
        assertEquals(mockComment.getUser().getUsername(), result.getUser().getUsername(), "The username should match.");
    }


    @Test
    public void testGetCommentById_Success() throws Exception {
        // Arrange
        String commentJson = objectMapper.writeValueAsString(mockComment);
        HttpResponse mockResponse = new HttpResponse(200, commentJson);

        when(mockHttpClient.get(eq("http://localhost:8080/comments/1"))).thenReturn(mockResponse);

        // Act
        CommentDTO result = commentClient.getCommentById(1L);

        // Assert
        assertNotNull(result, "Comment retrieval by ID should return a non-null object.");
        assertEquals(1L, result.getId(), "The ID of the comment should match the expected value.");
        assertEquals("Test Comment", result.getContent(), "The content of the comment should match.");
        assertEquals(mockUser.getUsername(), result.getUser().getUsername(), "The username of the comment author should match.");
    }


    @Test
    public void testGetCommentsByPostId_Success() throws Exception {
        List<CommentDTO> comments = Collections.singletonList(mockComment);
        String commentsJson = objectMapper.writeValueAsString(comments);
        HttpResponse mockResponse = new HttpResponse(200, commentsJson);

        when(mockHttpClient.get(anyString())).thenReturn(mockResponse);

        List<CommentDTO> result = commentClient.getCommentsByPostId(1L);

        assertNotNull(result, "Fetching comments by Post ID should return a non-null list.");
        assertFalse(result.isEmpty(), "The result list should not be empty.");
        assertEquals("Test Comment", result.get(0).getContent(), "The comment content should match.");
    }

    @Test
    public void testUpdateComment_Success() throws Exception {
        mockComment.setContent("Updated Comment");
        String commentJson = objectMapper.writeValueAsString(mockComment);
        HttpResponse mockResponse = new HttpResponse(200, commentJson);

        when(mockHttpClient.put(anyString(), anyString())).thenReturn(mockResponse);

        CommentDTO result = commentClient.updateComment(1L, "Updated Comment");

        assertNotNull(result, "Updating a comment should return a non-null object.");
        assertEquals("Updated Comment", result.getContent(), "The updated content should match.");
    }

    @Test
    public void testDeleteComment_Success() throws Exception {
        HttpResponse mockResponse = new HttpResponse(204, "");

        when(mockHttpClient.delete(anyString())).thenReturn(mockResponse);

        boolean result = commentClient.deleteComment(1L);

        assertTrue(result, "The delete operation should return true on success.");
    }
}

