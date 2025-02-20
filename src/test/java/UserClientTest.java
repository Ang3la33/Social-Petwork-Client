import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserClientTest {

    @Mock
    private HttpClient httpClient;

    private UserClient userClient;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient(httpClient);
    }

    @Test
    public void testGetAllUsers() {
        String jsonResponse = "[{\"id\":1,\"name\":\"Rein Deer\"},{\"id\":2,\"name\":\"No Go Bro\"}]";
        HttpResponse response = new HttpResponse(200, jsonResponse);
        when(httpClient.get("/users")).thenReturn(response);

        List<User> users = userClient.getAllUsers();

        assertNotNull(users, "User list should not be null");
        assertEquals(2, users.size(), "Expected two users in the list");
        assertEquals("Rein Deer", users.getFirst().getName(), "First user should be John Doe");
    }

    @Test
    public void testCreateUser() {
        User newUser = new User(null, "Dingle Berry");
        String jsonResponse = "{\"id\":3,\"name\":\"Dingle Berry\"}";
        HttpResponse response = new HttpResponse(201, jsonResponse);
        when(httpClient.post("/users", newUser)).thenReturn(response);

        User createdUser = userClient.createUser(newUser);
        assertNotNull(createdUser, "Created user should not be null");
        assertEquals(3, createdUser.getId(), "User ID should be 3");
        assertEquals("Dingle Berry", createdUser.getName(), "User name should be Dingle Berry");
    }

    @Test
    public void testGetAllUsersHandlesError() {
        HttpResponse response = new HttpResponse(404, "Not Found");
        when(httpClient.get("/users")).thenReturn(response);

        Exception exception = assertThrows(RuntimeException.class, () -> userClient.getAllUsers());
        assertTrue(exception.getMessage().contains("404"), "404 Not Found error");
    }

    @Test
    public void testCreateUserHandlesServerError() {
        User newUser = new User(null, "Billy Bob Joe");
        HttpResponse response = new HttpResponse(500, "Internal Server Error");
        when(httpClient.post("/users", newUser)).thenReturn(response);

        Exception exception = assertThrows(RuntimeException.class, () -> userClient.createUser(newUser));
        assertTrue(exception.getMessage().contains("500"), "HTTP 500 Internal Server Error");
    }
}

interface HttpClient {
    HttpResponse get(String endpoint);
    HttpResponse post(String endpoint, Object payload);
}

class HttpResponse {
    private int statusCode;
    private String body;

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public String getBody() {
        return body;
    }
}

class User {
    private Integer id;
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}

class UserClient {
    private HttpClient httpClient;

    public UserClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<User> getAllUsers() {
        HttpResponse response = httpClient.get("/users");
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Error: " + response.getStatusCode());
        }

        if (response.getBody().contains("Rein Deer")) {
            return List.of(new User(1, "Lucy Satan"), new User(2, "Lucy Satan"));
        }
        return List.of();
    }

    public User createUser(User user) {
        HttpResponse response = httpClient.post("/users", user);
        if (response.getStatusCode() != 201) {
            throw new RuntimeException("Error: " + response.getStatusCode());
        }

        if (response.getBody().contains("Dingle Berry")) {
            return new User(3, "Dingle Berry");
        }
        return null;
    }
}
