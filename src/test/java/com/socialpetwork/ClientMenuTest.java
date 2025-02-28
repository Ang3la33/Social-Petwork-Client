package com.socialpetwork;

import com.socialpetwork.domain.CommentDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.cli.ClientMenu;
import com.socialpetwork.http.client.CommentClient;
import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.http.client.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ClientMenuTest {

    @Mock
    private UserClient userClient;

    @Mock
    private PostClient postClient;

    @Mock
    private CommentClient commentClient;

    @InjectMocks
    private ClientMenu clientMenu;

    private final UserDTO mockUser = new UserDTO(1L, "John Doe", "1990-01-01", "john@example.com", "johndoe", "password");
    private final PostDTO mockPost = new PostDTO(1L, "Test Post Content", mockUser, null);
    private final CommentDTO mockComment = new CommentDTO(1L, "Test Comment", mockUser, mockPost);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ClientMenu.loggedInUserId = 1L;
        ClientMenu.loggedInUsername = "johndoe";
        ClientMenu.loggedInUser = mockUser;
    }


    @Test
    public void testCreatePostSuccess() {
        when(postClient.createPost(any(PostDTO.class), eq(1L))).thenReturn(mockPost);

        PostDTO post = new PostDTO(null, "Test Post Content", mockUser, null);
        PostDTO createdPost = postClient.createPost(post, 1L);

        assertNotNull(createdPost);
        assertEquals("Test Post Content", createdPost.getContent());
        assertEquals(1L, createdPost.getUser().getId());
    }

    @Test
    public void testViewPosts() {
        when(postClient.getAllPosts()).thenReturn(List.of(mockPost));

        List<PostDTO> posts = postClient.getAllPosts();

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        assertEquals("Test Post Content", posts.get(0).getContent());
    }


    @Test
    public void testViewComments() throws Exception {
        when(commentClient.getCommentsByPostId(1L)).thenReturn(List.of(mockComment));

        List<CommentDTO> comments = commentClient.getCommentsByPostId(1L);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals("Test Comment", comments.get(0).getContent());
    }

    @Test
    public void testInvalidPostId() {
        when(postClient.getAllPosts()).thenReturn(List.of(mockPost));

        List<PostDTO> posts = postClient.getAllPosts();

        PostDTO selectedPost = posts.stream()
                .filter(post -> post.getId().equals(999L))
                .findFirst()
                .orElse(null);

        assertNull(selectedPost, "Post ID 999 should not exist.");
    }
}

