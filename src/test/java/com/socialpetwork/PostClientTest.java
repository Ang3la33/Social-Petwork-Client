package com.socialpetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostClientTest {

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private PostClient postClient;

    @BeforeEach
    void setUp() {
        // Setup code if necessary.
    }

    @Test
    public void testGetAllPosts() {
        List<Post> mockPosts = Arrays.asList(
                new Post(1, "First post content"),
                new Post(2, "Second post content")
        );
        when(httpClient.get("/posts")).thenReturn(mockPosts);
        List<Post> posts = postClient.getAllPosts();

        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals(new Post(1, "First post content"), posts.get(0));
        assertEquals(new Post(2, "Second post content"), posts.get(1));
        verify(httpClient).get("/posts");
    }

    @Test
    public void testCreatePost() {
        Post newPost = new Post(0, "New post content");
        Post createdPost = new Post(3, "New post content");
        when(httpClient.post(eq("/posts"), any(Post.class))).thenReturn(createdPost);
        Post result = postClient.createPost(newPost);

        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("New post content", result.getContent());
        verify(httpClient).post("/posts", newPost);
    }

    @Test
    public void testCreatePostInvalidResponse() {
        Post invalidPost = new Post(0, "");
        when(httpClient.post(eq("/posts"), any(Post.class)))
                .thenThrow(new HttpClientException("400 Bad Request"));

        HttpClientException exception = assertThrows(HttpClientException.class, () -> {
            postClient.createPost(invalidPost);
        });
        assertTrue(exception.getMessage().contains("400"));
        verify(httpClient).post("/posts", invalidPost);
    }
}

class Post {
    private int id;
    private String content;

    public Post(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() { return id; }
    public String getContent() { return content; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Post other))
            return false;
        return id == other.id && content.equals(other.content);
    }

    @Override
    public int hashCode() {
        return 31 * id + content.hashCode();
    }
}

interface HttpClient {
    List<Post> get(String url);
    Post post(String url, Post post);
}

class HttpClientException extends RuntimeException {
    public HttpClientException(String message) {
        super(message);
    }
}

class PostClient {
    private static final String POSTS_ENDPOINT = "/posts";
    private final HttpClient httpClient;

    public PostClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Post> getAllPosts() {
        return httpClient.get(POSTS_ENDPOINT);
    }

    public Post createPost(Post post) {
        return httpClient.post(POSTS_ENDPOINT, post);
    }
}


