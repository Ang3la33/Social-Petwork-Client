package com.socialpetwork.http.client;

import com.socialpetwork.domain.FollowDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FollowClient {

    private static final String SERVER_URL = "http://localhost:8080"; // Update if needed

    // Follow a user
    public String followUser(Long followerId, Long followedUserId) {
        try {
            URL url = new URL(SERVER_URL + "/users/" + followerId + "/follow/" + followedUserId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = String.format("{\"followerId\":%d, \"followedUserId\":%d, \"followedAt\":\"%s\"}",
                    followerId, followedUserId, LocalDateTime.now());

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Successfully followed user with ID: " + followedUserId;
            } else {
                return "Failed to follow user. Server responded with code: " + responseCode;
            }

        } catch (Exception e) {
            return "Error following user: " + e.getMessage();
        }
    }

    // Unfollow a user
    public String unfollowUser(Long followerId, Long followedUserId) {
        try {
            URL url = new URL(SERVER_URL + "/users/" + followerId + "/unfollow/" + followedUserId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Successfully unfollowed user with ID: " + followedUserId;
            } else {
                return "Failed to unfollow user. Server responded with code: " + responseCode;
            }

        } catch (Exception e) {
            return "Error unfollowing user: " + e.getMessage();
        }
    }

    // Get followers of a user
    public List<FollowDTO> getFollowers(Long userId) {
        List<FollowDTO> followers = new ArrayList<>();
        try {
            URL url = new URL(SERVER_URL + "/users/" + userId + "/followers");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            System.out.println("Followers:");
            while ((line = in.readLine()) != null) {
                System.out.println(line); // In a real-world scenario, parse JSON here.
                // For now, we just print the raw response.
            }
            in.close();
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Error retrieving followers: " + e.getMessage());
        }
        return followers;
    }

    // Get users followed by a user
    public List<FollowDTO> getFollowing(Long userId) {
        List<FollowDTO> following = new ArrayList<>();
        try {
            URL url = new URL(SERVER_URL + "/users/" + userId + "/following");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            System.out.println("Following:");
            while ((line = in.readLine()) != null) {
                System.out.println(line); // In a real-world scenario, parse JSON here.
            }
            in.close();
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Error retrieving following list: " + e.getMessage());
        }
        return following;
    }
}
