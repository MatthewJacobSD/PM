import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private JFrame frame;
    private JPasswordField passwordField;
    private JButton generateButton;
    private JButton passwordHistoryButton;
    private JLabel messageLabel;
    private JTextArea passwordHistoryArea;

    private List<String> passwordHistory = new ArrayList<>();

    public Library() {
        createGUI();
        loadPasswordHistory();
    }

    private void createGUI() {
        frame = new JFrame("Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel passwordLabel = new JLabel("Enter password:");
        frame.add(passwordLabel);

        passwordField = new JPasswordField(20);
        frame.add(passwordField);

        generateButton = new JButton("Generate Password");
        generateButton.addActionListener(new GeneratePasswordListener());
        frame.add(generateButton);

        passwordHistoryButton = new JButton("Password History");
        passwordHistoryButton.addActionListener(new PasswordHistoryListener());
        frame.add(passwordHistoryButton);

        messageLabel = new JLabel("");
        frame.add(messageLabel);

        JButton clearHistoryButton = new JButton("Clear Password History");
        clearHistoryButton.addActionListener(new ClearPasswordHistoryListener());
        frame.add(clearHistoryButton);

        JButton savePasswordButton = new JButton("Save Password");
        savePasswordButton.addActionListener(new SavePasswordListener());
        frame.add(savePasswordButton);

        frame.pack();
        frame.setVisible(true);
    }

    private void loadPasswordHistory() {
        try {
            passwordHistory = Files.lines(Paths.get("password_history.txt")).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading password history: " + e.getMessage());
        }
    }

    private void savePasswordHistory() {
        try {
            Files.write(Paths.get("password_history.txt"), passwordHistory);
        } catch (IOException e) {
            System.err.println("Error saving password history: " + e.getMessage());
        }
    }

    private void clearPasswordHistory() {
        passwordHistory.clear();
        savePasswordHistory();
    }

    private class GeneratePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String generatedPassword = generatePassword();
            passwordField.setText(generatedPassword);
            passwordHistory.add(generatedPassword);
            savePasswordHistory();
            messageLabel.setText("Generated password: " + generatedPassword);
        }
    }

    private class PasswordHistoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            passwordHistoryArea = new JTextArea(10, 20);
            passwordHistoryArea.setText(String.join("\n", passwordHistory));
            JScrollPane scrollPane = new JScrollPane(passwordHistoryArea);
            JOptionPane.showMessageDialog(frame, scrollPane, "Password History", JOptionPane.PLAIN_MESSAGE);
        }
    }

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

    private class SavePasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String passwordToSave = new String(passwordField.getPassword());
            if (!passwordToSave.isEmpty()) {
                passwordHistory.add(passwordToSave);
                savePasswordHistory();
                messageLabel.setText("Password saved: " + passwordToSave);
            } else {
                messageLabel.setText("Please enter a password to save.");
            }
        }
    }

    private String generatePassword() {
        // Implement a more secure password generation algorithm here
        // For example, use a combination of uppercase, lowercase, digits, and special characters
        // with a minimum length of 12 characters

        // Define character sets
        String lowercase= "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+[]{};:'\",.<>/?`~";

        // Combine character sets
        String all = lowercase + uppercase + digits + special;

        // Initialize password builder
        StringBuilder password = new StringBuilder();

        // Add random characters from each character set to the password
        password.append(lowercase.charAt((int) (Math.random() * lowercase.length())));
        password.append(uppercase.charAt((int) (Math.random() * uppercase.length())));
        password.append(digits.charAt((int) (Math.random() * digits.length())));
        password.append(special.charAt((int) (Math.random() * special.length())));

        // Add 8 random characters from the combined character set to the password
        for (int i = 0; i < 8; i++) {
            password.append(all.charAt((int) (Math.random() * all.length())));
        }

        // Return the generated password
        return password.toString();
    }

    public static void main(String[] args) {
        new Library();
    }
}