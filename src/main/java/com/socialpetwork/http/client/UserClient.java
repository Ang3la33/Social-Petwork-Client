package com.socialpetwork.http.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserClient {
    private static final String USER_API_URL = "http://localhost:8080/users";
    public static void fetchUsers() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(USER_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder responseContent = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine).append("\n");
                }
                in.close();
                System.out.println("User List:");
                System.out.println(responseContent.toString());

            } else {
                System.err.println("Failed to fetch users. HTTP error code: " + responseCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine).append("\n");
                }
                errorReader.close();
                System.err.println("Error response: " + errorResponse.toString());
            }
        } catch (IOException e) {

            System.err.println("Error, can't connect to the server: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

