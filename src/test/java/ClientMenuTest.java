import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;

public class ClientMenuTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    public void setUp() {
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    public void testMenuDisplay() {
        provideInput("1\n");
        ClientMenu menu = new ClientMenu();
        menu.displayMenu();
        String output = outputStreamCaptor.toString();

        assertTrue(output.contains("Select an option:"), "Menu should display prompt 'Select an option:'");
    }

    @Test
    public void testValidInputReturnsCorrectOption() {
        provideInput("2\n");
        ClientMenu menu = new ClientMenu();
        int choice = menu.getUserChoice();

        assertEquals(2, choice, "Correct input should return the chosen menu option");
    }

    @Test
    public void testInvalidInputDisplaysErrorAndRePrompts() {
        provideInput("invalid\n3\n");
        ClientMenu menu = new ClientMenu();
        int choice = menu.getUserChoice();
        String output = outputStreamCaptor.toString();

        assertTrue(output.contains("Invalid input"), "Should display error message for incorrect input");
        assertEquals(3, choice, "After incorrect input, valid input should be accepted");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4"})
    public void testEachMenuOption(String option) {
        provideInput(option + "\n");
        ClientMenu menu = new ClientMenu();
        int choice = menu.getUserChoice();

        assertEquals(Integer.parseInt(option), choice, "Menu option " + option + " should be returned correctly");
    }
}
