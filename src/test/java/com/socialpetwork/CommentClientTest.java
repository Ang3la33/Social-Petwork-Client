package com.socialpetwork;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CommentClientTest {

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private CommentClient commentClient;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UserDTO testUser;
    private PostDTO testPost;
    private CommentDTO testComment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserDTO(1L, "TestUser", "08-16-1990", "testUser@example.com", "testUser", "https://example.com/profile.png");
        testPost = new PostDTO(1L, "Test Post", testUser, LocalDateTime.now());
        testComment = new CommentDTO(1L, "Test Comment", testUser, testPost);
    }

    @Test
    public void testGetComments() throws Exception {
        String responseJSON = mapper.writeValueAsString(testComment);
        when(httpClient.post(any(String.class), any(String.class))).thenReturn(new HttpResponse(201, responseJSON));

        CommentDTO result = commentClient.createComment("Test Comment",testUser,testPost);
        assertNotNull(result);
        assertEquals(testComment.getId(), result.getId());
    }



}







