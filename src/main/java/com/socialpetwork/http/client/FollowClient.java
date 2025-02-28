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

    // Get users followed by a user
    public List<FollowDTO> getFollowing(Long userId) {
        HttpURLConnection conn = null;
        try {
            String requestUrl = BASE_URL + "/" + userId + "/following";
            System.out.println("🛠️ Debug: Requesting following list from URL: " + requestUrl);
            conn = getHttpConnection(requestUrl);
            conn.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                System.out.println("🛠️ Debug: Raw response from server: " + response.toString());

                FollowDTO[] followingArray = objectMapper.readValue(response.toString(), FollowDTO[].class);

                // Debugging each fetched follow record
                for (FollowDTO follow : followingArray) {
                    System.out.println("🛠️ Debug: Follower ID: " + follow.getFollowerId());
                    System.out.println("🛠️ Debug: Followed User ID: " + follow.getFollowedUserId());
                }

                return Arrays.asList(followingArray);
            }

        } catch (Exception e) {
            System.out.println("❌ Error retrieving following list: " + e.getMessage());
            return List.of();
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

                FollowDTO[] followersArray = objectMapper.readValue(response.toString(), FollowDTO[].class);
                return Arrays.asList(followersArray);
            }

        } catch (Exception e) {
            System.out.println("❌ Error retrieving followers: " + e.getMessage());
            return List.of();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    // Get posts from followed users
    public List<PostDTO> getPostsFromFollowedUsers(Long loggedInUserId) {
        HttpClient httpClient = new HttpClient();
        String url = "http://localhost:8080/follows/posts/" + loggedInUserId;
        try {
            HttpResponse response = httpClient.get(url);
            if (response.getStatusCode() == 200) {
                return objectMapper.readValue(response.getBody(), new TypeReference<List<PostDTO>>() {});
            } else {
                System.out.println("❌ Error fetching followed users' posts - Status Code: " + response.getStatusCode());
                return List.of();
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            return List.of();
        }
    }

}

