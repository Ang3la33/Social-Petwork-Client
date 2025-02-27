package com.socialpetwork.http.cli;

import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.client.FollowClient;
import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.http.client.UserClient;

import java.util.List;
import java.util.Scanner;

public class ClientMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserClient userClient = new UserClient();
    private static final PostClient postClient = new PostClient();
    private static final FollowClient followClient = new FollowClient();

    public static Long loggedInUserId = null;
    public static String loggedInUsername = null;
    private static UserDTO loggedInUser = null; // Store full user details

    public static void main(String[] args) {
        mainMenu();
    }

    // 🏡 Main Menu (Before Login)
    public static void mainMenu() {
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
        userDashboard();
    }

    // 🔐 Register a new user
    private static void register() {
        scanner.nextLine();
        System.out.print("👤 Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("📅 Enter birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        System.out.print("📧 Enter email: ");
        String email = scanner.nextLine();
        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();
        System.out.print("🖼️ Enter profile picture URL (or press Enter for default): ");
        String profileUrl = scanner.nextLine();

        if (profileUrl.isEmpty()) {
            profileUrl = "default.jpg"; // Default profile picture
        }

        // Create UserDTO
        UserDTO newUser = new UserDTO(null, name, birthday, email, username, profileUrl);

        // Register user
        boolean success = userClient.register(newUser);
        if (success) {
            System.out.println("✅ Registration successful! Please log in.");
        } else {
            System.out.println("❌ Registration failed. Try again.");
        }
    }


    // 🔐 Login
    private static void login() {
        scanner.nextLine();
        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();

        loggedInUserId = userClient.login(username);
        if (loggedInUserId != null) {
            loggedInUser = userClient.getUserDetails(loggedInUserId);
            loggedInUsername = loggedInUser.getUsername();
            System.out.println("✅ Login successful! Welcome, " + loggedInUsername + "!");
        } else {
            System.out.println("❌ Login failed. Try again.");
        }
    }

    // 🏡 User Dashboard
    public static void userDashboard() {
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
    public static void browseUsers() {
        System.out.println("\n👥 " + loggedInUsername + " is Browsing Users");
        userClient.fetchUsers();
        System.out.print("\n1️⃣ Follow a User\n2️⃣ Unfollow a User\n3️⃣ Back to Dashboard\nSelect an option: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> followUser();
            case 2 -> unfollowUser();
            case 3 -> userDashboard();
            default -> System.out.println("❌ Invalid option. Try again.");
        }
    }

    private static void followUser() {
        System.out.print("👤 Enter user ID to follow: ");
        Long userId = scanner.nextLong();
        System.out.println(followClient.followUser(loggedInUserId, userId));
    }

    private static void unfollowUser() {
        System.out.print("👤 Enter user ID to unfollow: ");
        Long userId = scanner.nextLong();
        System.out.println(followClient.unfollowUser(loggedInUserId, userId));
    }

    // 📜 My Profile
    private static void myProfile() {
        System.out.println("\n📜 " + loggedInUsername + "'s Profile");
        System.out.println("👤 Name: " + loggedInUser.getName());
        System.out.println("📅 Birthday: " + loggedInUser.getBirthday());
        System.out.println("📧 Email: " + loggedInUser.getEmail());
        System.out.println("🖼️ Profile Picture: " + loggedInUser.getProfileUrl());

        System.out.println("\n1️⃣ View My Followers");
        System.out.println("2️⃣ View Users I Follow");
        System.out.println("3️⃣ Back to Dashboard");
        System.out.print("Select an option: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> followClient.getFollowers(loggedInUserId);
            case 2 -> followClient.getFollowing(loggedInUserId);
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
                case 5 -> { return; }
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private static void createPost() {
        scanner.nextLine(); // Consume any leftover newline
        System.out.print("📝 Enter your post content: ");
        String content = scanner.nextLine();

        // ✅ Create a UserDTO object with the logged-in user ID
        UserDTO user = new UserDTO(loggedInUserId, null, null, null, loggedInUsername, null); // Only setting ID & username

        // ✅ Create PostDTO with user object
        PostDTO post = new PostDTO(null, content, user, null);

        // ✅ Send the post to the API
        PostDTO createdPost = postClient.createPost(post, loggedInUserId);

        if (createdPost != null) {
            System.out.println("✅ Post created successfully!");
        } else {
            System.out.println("❌ Failed to create post.");
        }
    }


    public static void viewPosts(String category) {
        List<PostDTO> posts = switch (category) {
            case "my" -> postClient.getAllPosts();
            case "following" -> postClient.getAllPosts();
            case "all" -> postClient.getAllPosts();
            default -> {
                System.out.println("❌ Invalid category!");
                yield List.of();
            }
        };

        if (posts.isEmpty()) {
            System.out.println("🚫 No posts available.");
            return;
        }

        for (PostDTO post : posts) {
            System.out.println("\n📄 Post by: " + (post.getUser() != null ? post.getUser().getId() : "Unknown"));
            System.out.println("📝 " + post.getContent());
        }
    }

    public static void logout() {
        System.out.println("🔒 Logging out...");
        loggedInUserId = null;
        loggedInUsername = null;
        loggedInUser = null;
    }

    private static void exitApplication() {
        System.out.println("🐾 Goodbye! See you soon!");
        System.exit(0);
    }

    public static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
