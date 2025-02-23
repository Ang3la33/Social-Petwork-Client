package com.socialpetwork.http.cli;

import sun.net.www.http.HttpClient;


import java.util.Scanner;
// Menus inside of menu - too many options

public class ClientMenu {
    private static final String baseURL = "https://localhost:8008"; //FILLER
    // Not sure how to do this at the moment
    // private static final HttpClient client = HttpClient.newHttpClient();


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
                    System.out.println("Exiting the program.");
                    System.exit(0);
                }
            }
        }
    }
    // Profile Menu
    private static void userManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n Profile Management ");
            System.out.println("-------------------------");
            System.out.println("1. Create Profile");
            System.out.println("2. View All Users ");
            System.out.println("3. Delete User");
            System.out.println("4. Go to Home");
            System.out.println("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createUser(scanner);
                case 2 -> viewAllUsers(scanner);
                case 3 -> deleteUser(scanner);
                case 4 -> {
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
                case 1 -> followUser(scanner);
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
                case 1 -> createPost(scanner);
                case 2 -> updatePost(scanner);
                case 3 -> deletePost(scanner);
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
