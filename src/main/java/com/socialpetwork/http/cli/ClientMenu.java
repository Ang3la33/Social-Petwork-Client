package com.socialpetwork.http.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.domain.PostDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientMenu {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);
    private static Long loggedInUserId = null;
    private static String loggedInUsername = null;

    public static void main(String[] args) {
        while (true) {
            mainMenu();
        }
    }

    // 🏡 Main Menu (Before Login)
    private static void mainMenu() {
        while (loggedInUserId == null) {
            System.out.println("\n🐾 Welcome to Social Petwork! 🐾");
            System.out.println("1️⃣ Register");
            System.out.println("2️⃣ Login");
            System.out.println("3️⃣ Exit");
            System.out.print("Select an option: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> exitApplication();
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
        userDashboard(); // Redirect to dashboard after login
    }

    // 🔐 User Authentication
    private static void register() {
        scanner.nextLine();
        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();
        System.out.print("🔒 Enter password: ");
        String password = scanner.nextLine();

        String json = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("✅ Registration successful! Please log in.");
            } else {
                System.out.println("❌ Registration failed: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error registering: " + e.getMessage());
        }
    }

    private static void login() {
        scanner.nextLine();
        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();
        System.out.print("🔒 Enter password: ");
        String password = scanner.nextLine();

        String json = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                loggedInUserId = Long.parseLong(response.body());
                loggedInUsername = username; // Store username
                System.out.println("✅ Login successful! Welcome, " + username + "!");
            } else {
                System.out.println("❌ Login failed: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error logging in: " + e.getMessage());
        }
    }

    // 🏡 User Dashboard
    private static void userDashboard() {
        while (loggedInUserId != null) {
            System.out.println("\n🏡 Welcome, " + loggedInUsername + ", to Your Dashboard 🏡");
            System.out.println("1️⃣ Browse Users");
            System.out.println("2️⃣ My Profile");
            System.out.println("3️⃣ Posts");
            System.out.println("4️⃣ Logout");
            System.out.print("Select an option: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> browseUsers();
                case 2 -> myProfile();
                case 3 -> postsMenu();
                case 4 -> logout();
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    // 👥 Browsing Users
    private static void browseUsers() {
        System.out.println("\n👥 " + loggedInUsername + " is Browsing Users");
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

    // 📜 My Profile Menu
    private static void myProfile() {
        System.out.println("\n📜 " + loggedInUsername + "'s Profile");
        System.out.println("1️⃣ View My Followers");
        System.out.println("2️⃣ View Users I Follow");
        System.out.println("3️⃣ Back to Dashboard");
        System.out.print("Select an option: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> viewFollowers();
            case 2 -> viewFollowing();
            case 3 -> userDashboard();
            default -> System.out.println("❌ Invalid option. Try again.");
        }
    }

    // 📝 Posts Menu
    private static void postsMenu() {
        while (true) {
            System.out.println("\n📝 Posts Menu");
            System.out.println("1️⃣ Create a Post");
            System.out.println("2️⃣ View My Posts");
            System.out.println("3️⃣ View Posts from Users I Follow");
            System.out.println("4️⃣ View All Posts");
            System.out.println("5️⃣ Back to Dashboard");
            System.out.print("Select an option: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> createPost();
                case 2 -> viewPosts("my");
                case 3 -> viewPosts("following");
                case 4 -> viewPosts("all");
                case 5 -> { return; } // Exit posts menu
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void createPost() {
        scanner.nextLine();
        System.out.print("📝 Enter your post content: ");
        String content = scanner.nextLine();

        String json = "{\"content\":\"" + content + "\", \"userId\":" + loggedInUserId + "}";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/posts"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("✅ Post created successfully!");
            } else {
                System.out.println("❌ Failed to create post: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error creating post: " + e.getMessage());
        }
    }

    private static void logout() {
        System.out.println("🔒 Logging out...");
        loggedInUserId = null;
        loggedInUsername = null;
    }

    private static void exitApplication() {
        System.out.println("🐾 Goodbye! See you soon!");
        System.exit(0);
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
