package com.socialpetwork.domain;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PostServiceClient {

    private static final String POST_API_URL = "http://localhost:8080/post";
    private static final String GET_API_URL = "http://localhost:8080/posts";

    public static void createPost() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter post: ");
            String post = scanner.nextLine();

            // JSON payload - sends string to backend API when creating post
            String jsonInputString = "{\"post\":\"" + post + "\"}";

            // HTTP connection
            URL url = new URL(POST_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Send JSON input
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Post created");
            } else {
                System.out.println("Error creating post, Error code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating post: " + e.getMessage());
        }
    }

    public static void getPosts() throws IOException {
        try {
            URL url = new URL(GET_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                System.out.println(response.toString());
            } else {
                System.out.println("Error getting posts, Error code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
