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

    @BeforeEach
    void setUp() {
        followClient = spy(new FollowClient());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // ✅ Fix LocalDateTime serialization issue
    }

    @Test
    void testFollowUser_Success() throws Exception {
        // Mock HttpURLConnection
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());

        // ✅ Mock output stream to prevent null errors
        OutputStream mockOutputStream = new ByteArrayOutputStream();
        when(mockConnection.getOutputStream()).thenReturn(mockOutputStream);

        // Mock response code
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        // Invoke method
        String result = followClient.followUser(1L, 2L);

        // Verify and assert
        assertEquals("✅ Successfully followed user with ID: 2", result);
        verify(mockConnection, times(1)).getResponseCode();
        verify(mockConnection, times(1)).getOutputStream(); // Ensure it was used
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

        assertEquals("✅ Successfully unfollowed user with ID: 2", result);
    }

    @Test
    void testUnfollowUser_Failure() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());

        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        String result = followClient.unfollowUser(1L, 2L);

        assertEquals("❌ Failed to unfollow user. Server responded with code: 400", result);
    }

    @Test
    void testGetFollowers() throws Exception {
        FollowDTO[] mockData = {new FollowDTO(1L, 2L, 1L, LocalDateTime.now())};

        String mockResponse = objectMapper.writeValueAsString(mockData);
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());

        InputStream mockInputStream = new ByteArrayInputStream(mockResponse.getBytes());
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        List<FollowDTO> followers = followClient.getFollowers(1L);

        assertNotNull(followers);
        assertEquals(1, followers.size());
        assertEquals(2L, followers.get(0).getFollowerId());
    }

    @Test
    void testGetFollowing() throws Exception {
        FollowDTO[] mockData = {new FollowDTO(1L, 1L, 2L, LocalDateTime.now())};

        String mockResponse = objectMapper.writeValueAsString(mockData); // ✅ Fix serialization

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        doReturn(mockConnection).when(followClient).getHttpConnection(anyString());

        InputStream mockInputStream = new ByteArrayInputStream(mockResponse.getBytes());
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        List<FollowDTO> following = followClient.getFollowing(1L);

        assertNotNull(following);
        assertEquals(1, following.size());
        assertEquals(2L, following.get(0).getFollowedUserId());
    }
}
