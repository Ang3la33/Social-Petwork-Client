package com.socialpetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.client.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock
    private HttpURLConnection mockConnection;

    private UserClient userClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userClient = spy(new UserClient());
    }

    // ✅ Test Registration Success
    @Test
    void testRegisterUser_Success() throws Exception {
        UserDTO newUser = new UserDTO(null, "John Doe", "1990-01-01", "johndoe@example.com", "johndoe","123");

        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("POST"), anyString());
        when(mockConnection.getResponseCode()).thenReturn(201);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream("User registered successfully".getBytes()));

        boolean result = userClient.register(newUser);

        assertTrue(result, "User registration should be successful");
    }

    // ❌ Test Registration Failure
    @Test
    void testRegisterUser_Failure() throws Exception {
        UserDTO newUser = new UserDTO(null, "John Doe", "1990-01-01", "johndoe@example.com", "johndoe","123");

        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("POST"), anyString());
        when(mockConnection.getResponseCode()).thenReturn(400);
        when(mockConnection.getErrorStream()).thenReturn(new ByteArrayInputStream("Registration failed".getBytes()));

        boolean result = userClient.register(newUser);

        assertFalse(result, "User registration should fail");
    }

    // ✅ Test Login Success
    @Test
    void testLoginUser_Success() throws Exception {
        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("POST"), anyString());
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream("1".getBytes()));

        Long userId = userClient.login("johndoe");

        assertNotNull(userId, "User ID should not be null on successful login");
        assertEquals(1L, userId, "User ID should match expected value");
    }

    // ❌ Test Login Failure
    @Test
    void testLoginUser_Failure() throws Exception {
        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("POST"), anyString());
        when(mockConnection.getResponseCode()).thenReturn(401);
        when(mockConnection.getErrorStream()).thenReturn(new ByteArrayInputStream("Login failed".getBytes()));

        Long userId = userClient.login("johndoe");

        assertNull(userId, "User ID should be null for incorrect credentials");
    }

    // ✅ Test Fetching User Details Success
    @Test
    void testGetUserDetails_Success() throws Exception {
        UserDTO mockUser = new UserDTO(1L, "John Doe", "1990-01-01", "johndoe@example.com", "johndoe","123");
        String jsonResponse = objectMapper.writeValueAsString(mockUser);

        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("GET"), isNull());
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes()));

        UserDTO user = userClient.getUserDetails(1L);

        assertNotNull(user, "User should not be null");
        assertEquals("johndoe", user.getUsername(), "Usernames should match");
    }

    // ❌ Test Fetching User Details Failure (User Not Found)
    @Test
    void testGetUserDetails_Failure() throws Exception {
        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("GET"), isNull());
        when(mockConnection.getResponseCode()).thenReturn(404);
        when(mockConnection.getErrorStream()).thenReturn(new ByteArrayInputStream("User not found".getBytes()));

        UserDTO user = userClient.getUserDetails(999L);

        assertNull(user, "User should be null if not found");
    }

    // ✅ Test Fetching Users Success
    @Test
    void testFetchUsers_Success() throws Exception {
        String jsonResponse = "[{\"id\":1,\"name\":\"John Doe\",\"username\":\"johndoe\"}," +
                "{\"id\":2,\"name\":\"Jane Doe\",\"username\":\"janedoe\"}]";

        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("GET"), isNull());
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes()));

        List<UserDTO> users = userClient.fetchUsers();

        assertNotNull(users, "Users list should not be null");
        assertEquals(2, users.size(), "There should be two users in the list");
        assertEquals("johndoe", users.get(0).getUsername(), "First user should be 'johndoe'");
    }

    // ❌ Test Fetching Users Failure
    @Test
    void testFetchUsers_Failure() throws Exception {
        doReturn(mockConnection).when(userClient).createConnection(anyString(), eq("GET"), isNull());
        when(mockConnection.getResponseCode()).thenReturn(500);
        when(mockConnection.getErrorStream()).thenReturn(new ByteArrayInputStream("Internal Server Error".getBytes()));

        List<UserDTO> users = userClient.fetchUsers();

        assertNotNull(users, "Users list should not be null even on failure");
        assertTrue(users.isEmpty(), "Users list should be empty on failure");
    }
}
