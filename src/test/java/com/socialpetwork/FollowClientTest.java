package com.socialpetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialpetwork.domain.FollowDTO;
import com.socialpetwork.http.client.FollowClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowClientTest {

    private FollowClient followClient;
    private ObjectMapper objectMapper;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        followClient = spy(new FollowClient());
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        fixedDateTime = LocalDateTime.of(2024, 2, 1, 12, 0, 0); // Fix timestamp issues
    }

    @Test
    void testFollowUser_Success() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());

        OutputStream mockOutputStream = new ByteArrayOutputStream();
        when(mockConnection.getOutputStream()).thenReturn(mockOutputStream);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        String result = followClient.followUser(1L, 2L);

        assertEquals("✅ Successfully followed user with ID: 2", result);
        verify(mockConnection, times(1)).getResponseCode();
        verify(mockConnection, times(1)).getOutputStream();
    }

    @Test
    void testFollowUser_Failure() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());

        OutputStream mockOutputStream = new ByteArrayOutputStream();
        when(mockConnection.getOutputStream()).thenReturn(mockOutputStream);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        String result = followClient.followUser(1L, 2L);

        assertEquals("❌ Failed to follow user. Server responded with code: 400", result);
    }

    @Test
    void testUnfollowUser_Success() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        String result = followClient.unfollowUser(1L, 2L);

        assertEquals("✅ Successfully unfollowed user", result);
    }

    @Test
    void testUnfollowUser_Failure() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        String result = followClient.unfollowUser(1L, 2L);

        assertEquals("❌ Failed to unfollow user. Server responded with code: 400", result);
    }
}
