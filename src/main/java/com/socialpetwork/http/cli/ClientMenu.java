package com.socialpetwork.http.cli;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ClientMenu {
    private static final String BASE_URL = "http://localhost:8080/api"; // Update if needed
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> viewAllPosts();
                case 3 -> createPost();
                case 4 -> followUser();
                case 5 -> viewFollowers();
                case 6 -> {
                    System.out.println("Exiting... 🐾");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid option. Please choose another option.");
            }
        }
    }

    public static void displayMenu() {
        System.out.println("\n🐾 Social Petwork - Main Menu 🐾");
        System.out.println("1️⃣ View All Users");
        System.out.println("2️⃣ View All Posts");
        System.out.println("3️⃣ Create a Post");
        System.out.println("4️⃣ Follow a User");
        System.out.println("5️⃣ View Followers");
        System.out.println("6️⃣ Exit");
        System.out.print("Select an option: ");
    }

    public static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Incorrect input! Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void viewAllUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("👤 Users: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error fetching users: " + e.getMessage());
        }
    }

    private static void viewAllPosts() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/posts"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("📸 Posts: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error fetching posts: " + e.getMessage());
        }
    }

    private static void createPost() {
        scanner.nextLine(); // Consume newline
        System.out.print("📝 Enter your post description here: ");
        String content = scanner.nextLine();

        String json = "{\"content\":\"" + content + "\", \"Billy Bob Joe\":{\"id\":1}}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/posts"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("✅ Post has been created: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error, cannot create post: " + e.getMessage());
        }
    }

    private static void followUser() {
        System.out.print("👤 Enter user ID to follow: ");
        int userId = scanner.nextInt();

        String json = "{\"followerId\":1, \"followeeId\":" + userId + "}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/follow"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("✅ Followed User: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error following user: " + e.getMessage());
        }
    }

    private static void viewFollowers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/1/followers"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("👥 Followers: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error fetching followers: " + e.getMessage());
        }
    }
}
