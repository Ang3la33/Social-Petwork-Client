package com.socialpetwork;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socialpetwork.http.cli.ClientMenu;
import com.socialpetwork.http.client.UserClient;
import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.http.client.FollowClient;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

class ClientMenuTest {

    private ByteArrayOutputStream outContent;
    private ClientMenu clientMenu;

    @Mock
    private UserClient userClient;

    @Mock
    private PostClient postClient;

    @Mock
    private FollowClient followClient;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        userClient = mock(UserClient.class);
        postClient = mock(PostClient.class);
        followClient = mock(FollowClient.class);

        clientMenu = new ClientMenu();
    }

    @Test
    void testMainMenuDisplay() {
        String input = "3\n"; // Exit immediately after menu shows
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        clientMenu.mainMenu();
        String output = outContent.toString();

        assertTrue(output.contains("🐾 Welcome to Social Petwork! 🐾"));
        assertTrue(output.contains("1️⃣ Register"));
        assertTrue(output.contains("2️⃣ Login"));
        assertTrue(output.contains("3️⃣ Exit"));
    }

    @Test
    void testUserDashboardDisplay() {
        // Simulate logged-in user
        ClientMenu.loggedInUserId = 1L;
        ClientMenu.loggedInUsername = "testUser";

        String input = "4\n"; // Exit after menu shows
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        clientMenu.userDashboard();
        String output = outContent.toString();

        assertTrue(output.contains("🏡 Welcome, testUser, to Your Dashboard 🏡"));
        assertTrue(output.contains("1️⃣ Browse Users"));
        assertTrue(output.contains("2️⃣ My Profile"));
        assertTrue(output.contains("3️⃣ Posts"));
        assertTrue(output.contains("4️⃣ Logout"));
    }

    @Test
    void testValidInput() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int choice = ClientMenu.getUserChoice();
        assertEquals(1, choice);
    }

    @Test
    void testInvalidInputThenValidInput() {
        String input = "xyz\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int choice = ClientMenu.getUserChoice();
        String output = outContent.toString();

        assertTrue(output.contains("❌ Please enter a valid number."));
        assertEquals(2, choice);
    }

    @Test
    void testFetchUsers() {
        when(userClient.fetchUsers()).thenReturn(List.of(new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg")));

        clientMenu.browseUsers();
        String output = outContent.toString();

        assertTrue(output.contains("👥 Browsing Users"));
        assertTrue(output.contains("John Doe"));
    }

    @Test
    void testViewPosts() {
        when(postClient.getAllPosts()).thenReturn(List.of(new PostDTO(1L, "Hello world!", new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg"), null)));

        clientMenu.viewPosts("all");
        String output = outContent.toString();

        assertTrue(output.contains("📄 Post by: John Doe"));
        assertTrue(output.contains("📝 Hello world!"));
    }

    @Test
    void testLogout() {
        ClientMenu.loggedInUserId = 1L;
        ClientMenu.loggedInUsername = "testUser";

        clientMenu.logout();
        assertNull(ClientMenu.loggedInUserId);
        assertNull(ClientMenu.loggedInUsername);

        String output = outContent.toString();
        assertTrue(output.contains("🔒 Logging out..."));
    }
}
