//package com.socialpetwork;
//
//import com.socialpetwork.domain.FollowDTO;
//import com.socialpetwork.http.client.FollowClient;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FollowClientTest {
//
//    private FollowClient followClient;
//
//    @BeforeEach
//    void setUp() {
//        followClient = new FollowClient();
//    }
//
//    @Test
//    void testFollowUser_Success() throws Exception {
//        // Mock URL and HttpURLConnection
//        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
//        URL mockUrl = mock(URL.class);
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//
//        // Mock successful response
//        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
//
//        // Invoke method
//        String result = followClient.followUser(1L, 2L);
//
//        // Verify and assert
//        assertEquals("Successfully followed user with ID: 2", result);
//        verify(mockConnection, times(1)).getResponseCode();
//    }
//
//    @Test
//    void testFollowUser_Failure() throws Exception {
//        // Mock URL and HttpURLConnection
//        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
//        URL mockUrl = mock(URL.class);
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//
//        // Mock failure response
//        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
//
//        String result = followClient.followUser(1L, 2L);
//
//        assertEquals("Failed to follow user. Server responded with code: 400", result);
//    }
//
//    @Test
//    void testUnfollowUser_Success() throws Exception {
//        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
//        URL mockUrl = mock(URL.class);
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//
//        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
//
//        String result = followClient.unfollowUser(1L, 2L);
//
//        assertEquals("Successfully unfollowed user with ID: 2", result);
//    }
//
//    @Test
//    void testUnfollowUser_Failure() throws Exception {
//        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
//        URL mockUrl = mock(URL.class);
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//
//        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
//
//        String result = followClient.unfollowUser(1L, 2L);
//
//        assertEquals("Failed to unfollow user. Server responded with code: 400", result);
//    }
//
//    @Test
//    void testGetFollowers() throws Exception {
//        // Mock JSON response for followers
//        String mockResponse = "[{\"id\":1,\"followerId\":2,\"followedUserId\":1,\"followedAt\":\"2025-02-24T12:34:56\"}]";
//
//        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
//        URL mockUrl = mock(URL.class);
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//
//        InputStream mockInputStream = new ByteArrayInputStream(mockResponse.getBytes());
//        when(mockConnection.getInputStream()).thenReturn(mockInputStream);
//
//        List<FollowDTO> followers = followClient.getFollowers(1L);
//
//        assertNotNull(followers);
//        assertEquals(0, followers.size()); // Adjust if JSON parsing is added
//    }
//
//    @Test
//    void testGetFollowing() throws Exception {
//        // Mock JSON response for following
//        String mockResponse = "[{\"id\":1,\"followerId\":1,\"followedUserId\":2,\"followedAt\":\"2025-02-24T12:34:56\"}]";
//
//        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
//        URL mockUrl = mock(URL.class);
//        when(mockUrl.openConnection()).thenReturn(mockConnection);
//
//        InputStream mockInputStream = new ByteArrayInputStream(mockResponse.getBytes());
//        when(mockConnection.getInputStream()).thenReturn(mockInputStream);
//
//        List<FollowDTO> following = followClient.getFollowing(1L);
//
//        assertNotNull(following);
//        assertEquals(0, following.size()); // Adjust if JSON parsing is added
//    }
//}
