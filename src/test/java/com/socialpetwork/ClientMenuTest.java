package com.socialpetwork;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socialpetwork.domain.CommentDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.cli.ClientMenu;
import com.socialpetwork.http.client.CommentClient;
import com.socialpetwork.http.client.FollowClient;
import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.http.client.UserClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @Mock
    private CommentClient commentClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        userClient = mock(UserClient.class);
        postClient = mock(PostClient.class);
        followClient = mock(FollowClient.class);
        commentClient = mock(CommentClient.class);

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
    void testRegisterUser() {
        String input = "John Doe\n1990-01-01\njohn@example.com\njohndoe\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(userClient.register(any(UserDTO.class))).thenReturn(true);

        clientMenu.register();
        String output = outContent.toString();

        assertTrue(output.contains("✅ Registration successful! Please log in."));
    }

    @Test
    void testRegisterUserFailure() {
        String input = "John Doe\n1990-01-01\njohn@example.com\njohndoe\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(userClient.register(any(UserDTO.class))).thenReturn(false);

        clientMenu.register();
        String output = outContent.toString();

        assertTrue(output.contains("❌ Registration failed. Try again."));
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
        when(userClient.fetchUsers()).thenReturn(List.of(
                new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg")
        ));

        clientMenu.browseUsers();
        String output = outContent.toString();

        assertTrue(output.contains("👥 Browsing Users"));
        assertTrue(output.contains("John Doe"));
    }

    @Test
    void testViewPosts() {
        when(postClient.getAllPosts()).thenReturn(List.of(
                new PostDTO(1L, "Hello world!", new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg"), null)
        ));

        clientMenu.viewPosts(postClient.getAllPosts());
        String output = outContent.toString();

        assertTrue(output.contains("📄 Post by: John Doe"));
        assertTrue(output.contains("📝 Hello world!"));
    }

    @Test
    void testCreatePost() {
        String input = "Hello Petwork!\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(postClient.createPost(any(PostDTO.class), anyLong())).thenReturn(
                new PostDTO(1L, "Hello Petwork!", new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg"), null)
        );

        clientMenu.createPost();
        String output = outContent.toString();

        assertTrue(output.contains("✅ Post created successfully!"));
    }

    @Test
    void testAddComment() {
        String input = "Nice post!\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        PostDTO post = new PostDTO(1L, "Test Post", new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg"), null);
        CommentDTO comment = new CommentDTO(1L, "Nice post!", post.getUser(), post);

        try {
            when(commentClient.createComment(anyString(), any(UserDTO.class), any(PostDTO.class))).thenReturn(comment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        clientMenu.addComment(post);
        String output = outContent.toString();

        assertTrue(output.contains("✅ Comment added!"));
    }

    @Test
    void testViewComments() {
        PostDTO post = new PostDTO(1L, "Test Post", new UserDTO(1L, "John Doe", "1990-05-10", "john@example.com", "johndoe", "profile.jpg"), null);
        CommentDTO comment = new CommentDTO(1L, "Nice post!", post.getUser(), post);

        try {
            when(commentClient.getCommentsByPostId(Long.valueOf(anyString()))).thenReturn(List.of(comment));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        clientMenu.viewComments(post);
        String output = outContent.toString();

        assertTrue(output.contains("💬 Comments:"));
        assertTrue(output.contains("Nice post!"));
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


