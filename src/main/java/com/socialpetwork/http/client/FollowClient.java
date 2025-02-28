package com.socialpetwork.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialpetwork.domain.FollowDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.util.HttpClient;
import com.socialpetwork.util.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FollowClient {

    private static final String BASE_URL = "http://localhost:8080/users"; // Updated base URL to match FollowerController
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    /**
     * Get an HTTP connection (used for mocking in tests)
     */
    public HttpURLConnection getHttpConnection(String urlString) throws Exception {
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }

    // Follow a user
    public String followUser(Long followerId, Long followedUserId) {
        HttpURLConnection conn = null;
        try {
            String requestUrl = BASE_URL + "/" + followerId + "/follow/" + followedUserId;
            conn = getHttpConnection(requestUrl);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = objectMapper.writeValueAsString(
                    new FollowDTO(null, followerId, followedUserId, LocalDateTime.now())
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInputString.getBytes("utf-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "✅ Successfully followed user with ID: " + followedUserId;
            } else {
                return "❌ Failed to follow user. Server responded with code: " + responseCode;
            }

        } catch (Exception e) {
            return "❌ Error following user: " + e.getMessage();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    // Unfollow a user
    public String unfollowUser(Long followerId, Long followedUserId) {
        HttpURLConnection conn = null;
        try {
            String requestUrl = BASE_URL + "/" + followerId + "/unfollow/" + followedUserId;
            conn = getHttpConnection(requestUrl);
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "✅ Successfully unfollowed user";
            } else {
                return "❌ Failed to unfollow user. Server responded with code: " + responseCode;
            }

        } catch (Exception e) {
            return "❌ Error unfollowing user: " + e.getMessage();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

}

