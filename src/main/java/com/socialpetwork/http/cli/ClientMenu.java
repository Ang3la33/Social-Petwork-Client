package com.socialpetwork.http.cli;

import com.socialpetwork.http.client.PostClient;
import com.socialpetwork.http.client.UserClient;
import sun.net.www.http.HttpClient;

// CHECK on all options


import java.util.Scanner;
// Menus inside of menu - too many options

public class ClientMenu {
    private static final String baseURL = "https://localhost:8008"; //FILLER
    // this may not work? trying set up
    private static final HttpClient client = HttpClient.newHttpClient(client);
    private static final PostClient postClient = new PostClient(client);
    private static final UserClient userClient = new UserClient(client);



    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("-------------------------");
            System.out.println("\n The Social-Petwork! ");
            System.out.println("-------------------------");
            System.out.println("What would you like to do today?");
            System.out.println(" ");
            System.out.println("1. Manage User");
            System.out.println("2. Find and Follow Friends ");
            System.out.println("3. Create and View Posts");
            System.out.println("4. Bark in the Comments");
            System.out.println("5. Exit ");
            System.out.println("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();


            switch(option){
                case 1 -> userManagementMenu(scanner);
                case 2 -> followersMenu(scanner);
                case 3 -> postMenu(scanner);
                case 4 -> commentsMenu(scanner);
                case 5 -> {
                    System.out.println("Exiting Social-Petwork.");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please select a number from 1 to 5.");
            }
        }
    }

    // Profile Menu
    private static void userManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n Profile Management ");
            System.out.println("-------------------------");
            System.out.println("1. Create Profile");
            System.out.println("2. Update User Information ");
            System.out.println("3. View All Users ");
            System.out.println("4. Delete User");
            System.out.println("5. Go to Home");
            System.out.println("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> userClient.createNewUser(scanner);
                case 2 -> userClient.viewAllUsers(scanner);
                case 3 -> userClient.deleteUser(scanner);
                case 4 -> userClient.deleteUser(scanner);
                case 5 -> {
                    return;
                }

            }


        }
    }
    private static void followersMenu(Scanner scanner){
        while (true) {
            System.out.println("\n Follower Menu ");
            System.out.println("-------------------------");
            System.out.println("1. Follow a User");
            System.out.println("2. Unfollow a User");
            System.out.println("3. View all Followers");
            System.out.println("4. Go to Home");
            System.out.println("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
              //  case 1 -> followClient.followUser(scanner);
                case 2 -> unfollowUser(scanner);
                case 3 -> getFollowers(scanner);
                case 4 -> {
                    return;
                }

            }


        }

    }
    private static void postMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n Post Menu ");
            System.out.println("-------------------------");
            System.out.println("1. Create Post");
            System.out.println("2. Update Post ");
            System.out.println("3. Delete Post");
            System.out.println("4. Go to Home");
            // not sure if I add find post by ID and user yet
            System.out.println("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> postClient.createPost(scanner);
                case 2 -> postClient.updatePost(scanner);
                case 3 -> postClient.deletePost(scanner);
                case 4 -> {
                    return;
                }

            }


        }
    }
    private static void commentsMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n Comments Menu ");
            System.out.println("-------------------------");
            System.out.println("1. Add a Comment");
            System.out.println("2. Update Comment");
            System.out.println("3. Delete Comment");
            System.out.println("4. Go to Home");
            System.out.println("Choose an option: ");
            // not sure if the find comment by commentID, userId and postId
            // same for view all comments
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createComment(scanner);
                case 2 -> updateComment(scanner);
                case 3 -> deleteComment(scanner);
                case 4 -> {
                    return;
                }

            }


        }
    }
}
