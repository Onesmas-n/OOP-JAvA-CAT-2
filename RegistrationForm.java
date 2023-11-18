/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author USER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationForm {
    private JFrame frame;
    private JTextField nameField;
    private JTextField mobileField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup genderGroup;
    private JComboBox<Integer> dayDropdown;
    private JComboBox<String> monthDropdown;
    private JComboBox<Integer> yearDropdown;
    private JTextField addressField;
    private JCheckBox termsCheckbox;
    private JButton submitButton;
    private JButton resetButton;
    private JTextArea displayArea;

    // JDBC Database Connection Details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/register";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public RegistrationForm() {
        frame = new JFrame("Registration Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Mobile:"));
        mobileField = new JTextField();
        panel.add(mobileField);

        panel.add(new JLabel("Gender:"));
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        panel.add(genderPanel);

        panel.add(new JLabel("DOB:"));
        // Day Dropdown
        dayDropdown = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayDropdown.addItem(i);
        }
        panel.add(dayDropdown);

        // Month Dropdown
        monthDropdown = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"});
        panel.add(monthDropdown);

        // Year Dropdown
        yearDropdown = new JComboBox<>();
        for (int i = 1900; i <= 2023; i++) {
            yearDropdown.addItem(i);
        }
        panel.add(yearDropdown);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Accept Terms And Conditions:"));
        termsCheckbox = new JCheckBox();
        panel.add(termsCheckbox);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitButtonClicked();
            }
        });
        panel.add(submitButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetButtonClicked();
            }
        });
        panel.add(resetButton);

        displayArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(displayArea);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void submitButtonClicked() {
        // Retrieve data from fields
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        int day = (int) dayDropdown.getSelectedItem();
        String month = (String) monthDropdown.getSelectedItem();
        int year = (int) yearDropdown.getSelectedItem();
        String dob = day + " " + month + " " + year;
        String address = addressField.getText();
        boolean termsAccepted = termsCheckbox.isSelected();

        // Display data on the right side control
        displayArea.append("Name: " + name + "\nMobile: " + mobile + "\nGender: " + gender +
                "\nDOB: " + dob + "\nAddress: " + address + "\nTerms Accepted: " + termsAccepted + "\n\n");

        // Insert data into the database
        insertUserData(name, mobile, gender, dob, address, termsAccepted);

        // Clear input fields after submission
        nameField.setText("");
        mobileField.setText("");
        genderGroup.clearSelection();
        dayDropdown.setSelectedIndex(0);
        monthDropdown.setSelectedIndex(0);
        yearDropdown.setSelectedIndex(0);
        addressField.setText("");
        termsCheckbox.setSelected(false);
    }

    private void resetButtonClicked() {
        // Clear all input fields and display area
        nameField.setText("");
        mobileField.setText("");
        genderGroup.clearSelection();
        dayDropdown.setSelectedIndex(0);
        monthDropdown.setSelectedIndex(0);
        yearDropdown.setSelectedIndex(0);
        addressField.setText("");
        termsCheckbox.setSelected(false);
        displayArea.setText("");
    }

    private void insertUserData(String name, String mobile, String gender, String dob, String address, boolean termsAccepted) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO user_data (name, mobile, gender, dob, address, terms_accepted) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, mobile);
                preparedStatement.setString(3, gender);
                preparedStatement.setString(4, dob);
                preparedStatement.setString(5, address);
                preparedStatement.setBoolean(6, termsAccepted);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistrationForm();
            }
        });
    }
}
