package com.socialpetwork;

import static org.junit.jupiter.api.Assertions.*;

import com.socialpetwork.http.cli.ClientMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ClientMenuTest {
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testDisplayMenu() {
        ClientMenu.displayMenu();
        String output = outContent.toString();
        assertTrue(output.contains("🐾 Social Petwork - Main Menu 🐾"));
        assertTrue(output.contains("1️⃣ View All Users"));
        assertTrue(output.contains("2️⃣ View All Posts"));
        assertTrue(output.contains("3️⃣ Create a Post"));
        assertTrue(output.contains("4️⃣ Follow a User"));
        assertTrue(output.contains("5️⃣ View Followers"));
        assertTrue(output.contains("6️⃣ Exit"));
    }

    @Test
    void testValidInput() {
        // Simulate a valid input "1" followed by a newline.
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int choice = ClientMenu.getUserChoice();
        assertEquals(1, choice);
    }

    @Test
    void testInvalidInputThenValidInput() {
        String input = "abc\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int choice;
        choice = ClientMenu.getUserChoice();
        String output = outContent.toString();
        assertTrue(output.contains("❌ Incorrect input! Please enter a number."));
        assertEquals(2, choice);
    }

}


