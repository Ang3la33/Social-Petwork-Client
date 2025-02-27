package com.socialpetwork.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.domain.UserDTO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UserClient {
    private static final String USER_API_URL = "http://localhost:8080/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 🔐 Register a new user (Saves to MySQL Database)
    public boolean register(UserDTO user) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(user);
            HttpURLConnection connection = createConnection(USER_API_URL + "/register", "POST", jsonPayload);

            int responseCode = connection.getResponseCode();
            String responseMessage = getResponseBody(connection, responseCode);

            if (responseCode == 201) {
                System.out.println("✅ Registration successful!");
                return true;
            } else {
                System.out.println("❌ Registration failed: " + responseMessage);
                return false;
            }
        } catch (IOException e) {
            System.out.println("❌ Error registering: " + e.getMessage());
            return false;
        }
    }

    // 🔑 Login User (Retrieves user ID from MySQL)
    public Long login(String username) {
        try {
            String jsonPayload = "{\"username\":\"" + username + "\"}";
            HttpURLConnection connection = createConnection(USER_API_URL + "/login", "POST", jsonPayload);

            int responseCode = connection.getResponseCode();
            String responseMessage = getResponseBody(connection, responseCode);

            if (responseCode == 200) {
                return Long.parseLong(responseMessage); // Server returns user ID if login is successful
            } else {
                System.out.println("❌ Login failed: " + responseMessage);
                return null;
            }
        } catch (IOException e) {
            System.out.println("❌ Error logging in: " + e.getMessage());
            return null;
        }
    }

    // 🛠 Fetch user details (From MySQL)
    public UserDTO getUserDetails(Long userId) {
        try {
            HttpURLConnection connection = createConnection(USER_API_URL + "/" + userId, "GET", null);

            int responseCode = connection.getResponseCode();
            String responseMessage = getResponseBody(connection, responseCode);

            if (responseCode == 200) {
                return objectMapper.readValue(responseMessage, UserDTO.class);
            } else {
                System.out.println("❌ Failed to fetch user details: " + responseMessage);
                return null;
            }
        } catch (IOException e) {
            System.out.println("❌ Error fetching user details: " + e.getMessage());
            return null;
        }
    }

    // 📄 Fetch All Users from Database
    public List<UserDTO> fetchUsers() {
        try {
            HttpURLConnection connection = createConnection(USER_API_URL, "GET", null);

            int responseCode = connection.getResponseCode();
            String responseMessage = getResponseBody(connection, responseCode);

            if (responseCode == 200) {
                return objectMapper.readValue(responseMessage, new TypeReference<List<UserDTO>>() {});
            } else {
                System.out.println("❌ Failed to fetch users: " + responseMessage);
                return List.of();
            }
        } catch (IOException e) {
            System.out.println("❌ Error fetching users: " + e.getMessage());
            return List.of();
        }
    }

    // 🔧 Utility: Create HTTP Connection
    public HttpURLConnection createConnection(String url, String method, String jsonPayload) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        // Only enable output for POST requests
        if ("POST".equalsIgnoreCase(method) && jsonPayload != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes("utf-8"));
            }
        }
        return connection;
    }

    // 🔧 Utility: Read Response Body (Handles Errors Correctly)
    private String getResponseBody(HttpURLConnection connection, int responseCode) throws IOException {
        InputStream inputStream = responseCode < 400 ? connection.getInputStream() : connection.getErrorStream();
        if (inputStream == null) return "No Response";

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}