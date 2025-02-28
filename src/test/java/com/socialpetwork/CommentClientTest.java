//package com.socialpetwork;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.socialpetwork.domain.CommentDTO;
//import com.socialpetwork.domain.PostDTO;
//import com.socialpetwork.domain.UserDTO;
//import com.socialpetwork.http.client.CommentClient;
//import com.socialpetwork.util.HttpClient;
//import com.socialpetwork.util.HttpResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//public class CommentClientTest {
//
//    @Mock
//    private HttpClient mockHttpClient;
//
//    @InjectMocks
//    private CommentClient commentClient;
//
//    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateComment_Success() throws Exception {
//        UserDTO user = new UserDTO(1L, "JohnDoe", "1990-01-01", "john@example.com", "johndoe", "password");
//        PostDTO post = new PostDTO(1L, 1L, "Test Post", LocalDateTime.now());
//        CommentDTO comment = new CommentDTO(1L, "Test Comment", user, post);
//
//        String commentJson = objectMapper.writeValueAsString(comment);
//        HttpResponse mockResponse = new HttpResponse(201, commentJson); // Use 201 for creation success
//
//        when(mockHttpClient.post(eq("http://localhost:8080/comments"), eq(commentJson)))
//                .thenReturn(mockResponse);
//
//        CommentDTO result = commentClient.createComment("Test Comment", user, post);
//
//        assertNotNull(result, "The result should not be null.");
//        assertEquals("Test Comment", result.getContent(), "The content should match the expected value.");
//        assertEquals(user.getId(), result.getUser().getId(), "User ID should match.");
//        assertEquals(post.getId(), result.getPost().getId(), "Post ID should match.");
//    }
//
//    @Test
//    public void testGetCommentById_Success() throws Exception {
//        CommentDTO comment = new CommentDTO(1L, "Test Comment", null, null);
//        String commentJson = objectMapper.writeValueAsString(comment);
//        HttpResponse mockResponse = new HttpResponse(200, commentJson);
//
//        when(mockHttpClient.get(anyString())).thenReturn(mockResponse);
//
//        CommentDTO result = commentClient.getCommentById(1L);
//
//        assertNotNull(result, "The result should not be null.");
//        assertEquals(1L, result.getId(), "Comment ID should match.");
//    }
//
//    @Test
//    public void testGetCommentsByPostId_Success() throws Exception {
//        CommentDTO comment = new CommentDTO(1L, "Test Comment", null, null);
//        List<CommentDTO> comments = Collections.singletonList(comment);
//        String commentsJson = objectMapper.writeValueAsString(comments);
//        HttpResponse mockResponse = new HttpResponse(200, commentsJson);
//
//        when(mockHttpClient.get(anyString())).thenReturn(mockResponse);
//
//        List<CommentDTO> result = commentClient.getCommentsByPostId(1L);
//
//        assertNotNull(result, "The result list should not be null.");
//        assertFalse(result.isEmpty(), "The result list should not be empty.");
//        assertEquals("Test Comment", result.get(0).getContent(), "Comment content should match.");
//    }
//
//    @Test
//    public void testUpdateComment_Success() throws Exception {
//        CommentDTO comment = new CommentDTO(1L, "Updated Comment", null, null);
//        String commentJson = objectMapper.writeValueAsString(comment);
//        HttpResponse mockResponse = new HttpResponse(200, commentJson);
//
//        when(mockHttpClient.put(anyString(), anyString())).thenReturn(mockResponse);
//
//        CommentDTO result = commentClient.updateComment(1L, "Updated Comment");
//
//        assertNotNull(result, "The result should not be null.");
//        assertEquals("Updated Comment", result.getContent(), "The content should be updated.");
//    }
//
//    @Test
//    public void testDeleteComment_Success() throws Exception {
//        HttpResponse mockResponse = new HttpResponse(204, "");
//
//        when(mockHttpClient.delete(anyString())).thenReturn(mockResponse);
//
//        boolean result = commentClient.deleteComment(1L);
//
//        assertTrue(result, "The delete operation should return true.");
//    }
//}
//
//
//
//
//
//
//
//
//
