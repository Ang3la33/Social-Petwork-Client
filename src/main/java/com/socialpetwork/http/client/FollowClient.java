package com.socialpetwork.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialpetwork.domain.FollowDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FollowClient {

    private static final String BASE_URL = "http://localhost:8080/follow"; // Base URL for follow-related operations
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // ✅ Fix for LocalDateTime serialization

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
                return "✅ Successfully unfollowed user with ID: " + followedUserId;
            } else {
                return "❌ Failed to unfollow user. Server responded with code: " + responseCode;
            }

        } catch (Exception e) {
            return "❌ Error unfollowing user: " + e.getMessage();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    // Get followers of a user
    public List<FollowDTO> getFollowers(Long userId) {
        HttpURLConnection conn = null;
        try {
            String requestUrl = BASE_URL + "/" + userId + "/followers";
            conn = getHttpConnection(requestUrl);
            conn.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                // ✅ Parse JSON response correctly (Fix LocalDateTime issue)
                FollowDTO[] followersArray = objectMapper.readValue(response.toString(), FollowDTO[].class);
                List<FollowDTO> followers = Arrays.asList(followersArray);
                System.out.println("👥 Followers: " + followers);
                return followers;
            }

        } catch (Exception e) {
            System.out.println("❌ Error retrieving followers: " + e.getMessage());
            return List.of();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    // Get users followed by a user
    public List<FollowDTO> getFollowing(Long userId) {
        HttpURLConnection conn = null;
        try {
            String requestUrl = BASE_URL + "/" + userId + "/following";
            conn = getHttpConnection(requestUrl);
            conn.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                // ✅ Parse JSON response correctly (Fix LocalDateTime issue)
                FollowDTO[] followingArray = objectMapper.readValue(response.toString(), FollowDTO[].class);
                List<FollowDTO> following = Arrays.asList(followingArray);
                System.out.println("👤 Following: " + following);
                return following;
            }

        } catch (Exception e) {
            System.out.println("❌ Error retrieving following list: " + e.getMessage());
            return List.of();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
