package com.socialpetwork.http.cli;

import com.socialpetwork.domain.CommentDTO;
import com.socialpetwork.domain.PostDTO;
import com.socialpetwork.domain.UserDTO;
import com.socialpetwork.http.client.CommentClient;
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
    private static final CommentClient commentClient = new CommentClient();

    public static Long loggedInUserId = null;
    public static String loggedInUsername = null;
    public static UserDTO loggedInUser = null;

    public static void start() {
        System.out.println("🐾 Welcome to Social Petwork! 🐾");
        mainMenu();
    }

    // Main Menu (Before Login)
    public static void mainMenu() {
        while (loggedInUserId == null) {
            System.out.println("\n🐾 Welcome to The Social Petwork! 🐾");
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

    // Exit Application
    private static void exitApplication() {
        System.out.println("🐾 Goodbye! See you soon!");
        System.exit(0);
    }

    // Register a new user
    public static void register() {
        scanner.nextLine();
        System.out.print("👤 Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("📅 Enter birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        System.out.print("📧 Enter email: ");
        String email = scanner.nextLine();
        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();
        System.out.print("🔑 Enter password: ");
        String password = scanner.nextLine();


        // Create UserDTO
        UserDTO newUser = new UserDTO(null, name, birthday, email, username, password);

        // Register user
        boolean success = userClient.register(newUser);
        if (success) {
            System.out.println("✅ Registration successful! Please log in.");
        } else {
            System.out.println("❌ Registration failed. Try again.");
        }
    }


    // 🔐 Login
    public static void login() {
        scanner.nextLine();
        System.out.print("👤 Enter username: ");
        String username = scanner.nextLine();
        System.out.print("🔑 Enter password: ");
        String password = scanner.nextLine();

        loggedInUserId = userClient.login(username,password);
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

    // Browsing Users
    public static void browseUsers() {
        System.out.println("\n👥 " + loggedInUsername + " is Browsing Users");

        List<UserDTO> users = userClient.fetchUsers(); // Fetch and store users

        if (users == null || users.isEmpty()) {
            System.out.println("🚫 No users found.");
            return;
        }

        System.out.println("📋 Available Users:");
        for (UserDTO user : users) {
            System.out.println("👤 ID: " + user.getId() + " | Username: " + user.getUsername() + " | Name: " + user.getName());
        }

        System.out.print("\n1️⃣ Follow a User\n2️⃣ Unfollow a User\n3️⃣ Back to Dashboard\nSelect an option: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> followUser();
            case 2 -> unfollowUser();
            case 3 -> userDashboard();
            default -> System.out.println("❌ Invalid option. Try again.");
        }
    }
    // Method to validate user ID input
    private static Long getValidUserIdInput() {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void followUser() {
        scanner.nextLine(); // Clear the input buffer
        System.out.print("👤 Enter user ID to follow: ");
        try {
            Long userId = Long.parseLong(scanner.nextLine().trim());
            if (userId != null && userId > 0) {
                UserDTO user = userClient.getUserDetails(userId);
                if (user != null) {
                    String response = followClient.followUser(loggedInUserId, userId);
                    if (response.contains("Successfully followed")) {
                        System.out.println("✅ Successfully following " + user.getUsername() + ".");
                    } else {
                        System.out.println(response);
                    }
                } else {
                    System.out.println("❌ User not found. Please try again.");
                }
            } else {
                System.out.println("❌ Invalid user ID. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid numeric user ID.");
        }
    }

    private static void unfollowUser() {
        scanner.nextLine();
        System.out.print("👤 Enter user ID to unfollow: ");
        try {
            Long userId = Long.parseLong(scanner.nextLine().trim());
            if (userId != null && userId > 0) {
                System.out.println(followClient.unfollowUser(loggedInUserId, userId));
            } else {
                System.out.println("❌ Invalid user ID. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid numeric user ID.");
        }
    }

    // My Profile
    private static void myProfile() {
        System.out.println("\n📜 " + loggedInUsername + "'s Profile");
        System.out.println("👤 Name: " + loggedInUser.getName());
        System.out.println("📅 Birthday: " + loggedInUser.getBirthday());
        System.out.println("📧 Email: " + loggedInUser.getEmail());

        System.out.println("\n1️⃣ Back to Dashboard");
        System.out.print("Select an option: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> userDashboard();
            default -> System.out.println("❌ Invalid option. Try again.");
        }
    }

    // Posts Menu
    public static void postsMenu() {
        while (true) {
            System.out.println("\n📝 Posts Menu");
            System.out.println("1️⃣ Create a Post");
            System.out.println("2️⃣ View Posts");
            System.out.println("3️⃣ Back to Dashboard");
            System.out.print("Select an option: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> createPost();
                case 2 -> viewPosts(postClient.getAllPosts());
                case 3 -> { return; }
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    public static void createPost() {
        scanner.nextLine();
        System.out.print("📝 Enter your post content: ");
        String content = scanner.nextLine();

        // 🛠️ Validate if the loggedInUser is not null
        if (loggedInUser == null) {
            System.out.println("❌ Error: No logged-in user found.");
            return;
        }

        PostDTO post = new PostDTO(null, content, loggedInUser, null);
        System.out.println("🛠️ Creating post with user ID: " + loggedInUserId); // Debug line
        System.out.println("🛠️ Post Content: " + content); // Debug line

        PostDTO createdPost = postClient.createPost(post, loggedInUserId);

        if (createdPost != null) System.out.println("✅ Post created successfully!");
        else System.out.println("❌ Failed to create post.");
    }

    public static void viewPosts(List<PostDTO> posts) {
        if (posts == null || posts.isEmpty()) {
            System.out.println("🚫 No posts available.");
            return;
        }

        System.out.println("\n📂 Viewing " + posts.size() + " posts:");
        for (PostDTO post : posts) {
            System.out.println("\n🆔 Post ID: " + post.getId());
            System.out.println("📄 Post by: " + (post.getUser() != null ? post.getUser().getUsername() : "Unknown"));
            System.out.println("📝 " + post.getContent());
            System.out.println("────────────────────────");
        }

        System.out.println("\nOptions:");
        System.out.println("1️⃣ Select a Post by ID");
        System.out.println("2️⃣ Back to Post Menu");
        System.out.print("Select an option: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> selectPostForComment(posts);
            case 2 -> { return; }
            default -> {
                System.out.println("❌ Invalid option. Try again.");
                viewPosts(posts); // Recursively call to handle invalid input
            }
        }
    }

    // 🆔 Select a post by ID to view or comment
    private static void selectPostForComment(List<PostDTO> posts) {
        while (true) {
            scanner.nextLine(); // Clear the buffer to avoid skipping input
            System.out.print("🆔 Enter Post ID: ");
            String input = scanner.nextLine().trim();

            try {
                Long postId = Long.parseLong(input);

                // Validate Post ID exists in the posts list
                PostDTO selectedPost = posts.stream()
                        .filter(post -> post.getId().equals(postId))
                        .findFirst()
                        .orElse(null);

                if (selectedPost != null) {
                    System.out.println("\n📄 Post by: " + (selectedPost.getUser() != null ? selectedPost.getUser().getUsername() : "Unknown"));
                    System.out.println("📝 " + selectedPost.getContent());

                    System.out.println("\n1️⃣ View Comments");
                    System.out.println("2️⃣ Add a Comment");
                    System.out.println("3️⃣ Back to Post Menu");
                    System.out.print("Select an option: ");

                    int choice = getUserChoice();
                    switch (choice) {
                        case 1 -> viewComments(selectedPost);
                        case 2 -> addComment(selectedPost);
                        case 3 -> { return; }
                        default -> System.out.println("❌ Invalid option. Try again.");
                    }
                    break;
                } else {
                    System.out.println("❌ Invalid Post ID. Please enter a valid ID from the list above.");
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid Post ID. Please enter a numeric value.");
            }
        }
    }


    public static void viewComments(PostDTO post) {
        try {
            List<CommentDTO> comments = commentClient.getCommentsByPostId(Long.valueOf(post.getId().toString()));
            if (comments.isEmpty()) {
                System.out.println("🚫 No comments on this post.");
            } else {
                System.out.println("\n💬 Comments:");
                for (CommentDTO comment : comments) {
                    System.out.println("- " + comment.getUser().getUsername() + ": " + comment.getContent());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving comments: " + e.getMessage());
        }
    }

    public static void addComment(PostDTO post) {
        scanner.nextLine();
        System.out.print("💬 Enter your comment: ");
        String content = scanner.nextLine();
        try {
            CommentDTO newComment = commentClient.createComment(content, loggedInUser, post);
            if (newComment != null) System.out.println("✅ Comment added!");
            else System.out.println("❌ Failed to add comment.");
        } catch (Exception e) {
            System.out.println("❌ Error adding comment: " + e.getMessage());
        }
    }

    public static void logout() {
        System.out.println("🔒 Logging out...");
        loggedInUserId = null;
        loggedInUsername = null;
        loggedInUser = null;
    }

    public static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

}
