// Import statements
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom; // Enchantment 1: Import SecureRandom for better password generation
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Main class
public class Library {
    // GUI components
    private JFrame frame;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JTextArea passwordHistoryArea;

    // File path for password history
    private static final String PASSWORD_HISTORY_FILE = "password_history.txt";

    // List to hold password history
    private List<String> passwordHistory = new ArrayList<>();

    // Constructor to initialize the GUI
    public Library() {
        createGUI(); // Initialize GUI components
        loadPasswordHistory(); // Load password history from file
    }

    // Method to create the graphical user interface
    private void createGUI() {
        frame = new JFrame("Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Label for password input
        JLabel passwordLabel = new JLabel("Enter password:");
        frame.add(passwordLabel);

        // Password input field
        passwordField = new JPasswordField(20);
        frame.add(passwordField);

        // Button to generate password
        JButton generateButton = new JButton("Generate Password");
        generateButton.addActionListener(new GeneratePasswordListener());
        frame.add(generateButton);

        // Button to view password history
        JButton passwordHistoryButton = new JButton("Password History");
        passwordHistoryButton.addActionListener(new PasswordHistoryListener());
        frame.add(passwordHistoryButton);

        // Label to display messages
        messageLabel = new JLabel("");
        frame.add(messageLabel);

        // Button to clear password history
        JButton clearHistoryButton = new JButton("Clear Password History");
        clearHistoryButton.addActionListener(new ClearPasswordHistoryListener());
        frame.add(clearHistoryButton);

        // Button to save password
        JButton savePasswordButton = new JButton("Save Password");
        savePasswordButton.addActionListener(new SavePasswordListener());
        frame.add(savePasswordButton);

        frame.pack();
        frame.setVisible(true);
    }

    // Method to load password history from file
    private void loadPasswordHistory() {
        try (Stream<String> lines = Files.lines(Paths.get(PASSWORD_HISTORY_FILE))) {
            passwordHistory = lines.collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading password history: " + e.getMessage());
        }
    }


    // Method to save password history to file
    private void savePasswordHistory() {
        try {
            Files.write(Paths.get(PASSWORD_HISTORY_FILE), passwordHistory);
        } catch (IOException e) {
            System.err.println("Error saving password history: " + e.getMessage());
        }
    }

    // Method to clear password history
    private void clearPasswordHistory() {
        passwordHistory.clear();
        savePasswordHistory();
    }

    // ActionListener for generating passwords
    private class GeneratePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String generatedPassword = generatePassword();
            passwordField.setText(generatedPassword);
            addToPasswordHistory(generatedPassword);
            messageLabel.setText("Generated password: " + generatedPassword);
        }
    }

    // ActionListener for viewing password history
    private class PasswordHistoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            passwordHistoryArea = new JTextArea(10, 20);
            passwordHistoryArea.setText(String.join("\n", passwordHistory));
            JScrollPane scrollPane = new JScrollPane(passwordHistoryArea);
            JOptionPane.showMessageDialog(frame, scrollPane, "Password History", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // ActionListener for clearing password history
    private class ClearPasswordHistoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to clear the password history?", "Clear Password History", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                clearPasswordHistory();
                passwordHistoryArea.setText("");
                messageLabel.setText("Password history cleared.");
            }
        }
    }

    // ActionListener for saving passwords
    private class SavePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String passwordToSave = new String(passwordField.getPassword());
            if (!passwordToSave.isEmpty()) {
                addToPasswordHistory(passwordToSave);
                messageLabel.setText("Password saved: " + passwordToSave);
            } else {
                messageLabel.setText("Please enter a password to save.");
            }
        }
    }

    // Method to add a password to the history
    private void addToPasswordHistory(String password) {
        passwordHistory.add(password);
        savePasswordHistory();
    }

    // Method to generate a random password using SecureRandom
    private String generatePassword() {
        SecureRandom secureRandom = new SecureRandom(); // Enchantment 2: Create a SecureRandom instance for better password generation

        // Define character sets
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+[]{};:'\",.<>/?`~";

        // Combine character sets
        String all = lowercase + uppercase + digits + special;

        // Initialize password builder
        StringBuilder password = new StringBuilder();

        // Add random characters from each character set to the password
        password.append(lowercase.charAt(secureRandom.nextInt(lowercase.length())));
        password.append(uppercase.charAt(secureRandom.nextInt(uppercase.length())));
        password.append(digits.charAt(secureRandom.nextInt(digits.length())));
        password.append(special.charAt(secureRandom.nextInt(special.length())));

        // Add 8 random characters from the combined character set to the password
        for (int i = 0; i < 8; i++) {
            password.append(all.charAt(secureRandom.nextInt(all.length())));
        }

        // Return the generated password
        return password.toString();
    }

    // Main method
    public static void main(String[] args) {
        new Library();
    }
}
