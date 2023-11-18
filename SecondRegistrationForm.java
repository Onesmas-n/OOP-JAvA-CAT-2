/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.secondregistrationform;

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

public class SecondRegistrationForm {
    private JFrame frame;
    private JTextField idField;
    private JTextField nameField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup genderGroup;
    private JComboBox<Integer> dayDropdown;
    private JComboBox<String> monthDropdown;
    private JComboBox<Integer> yearDropdown;
    private JTextField addressField;
    private JTextField contactField;
    private JButton registerButton;
    private JButton exitButton;
    private JTabbedPane tabbedPane;
    private JTextArea displayArea;

    // JDBC Database Connection Details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/register";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public SecondRegistrationForm() {
        frame = new JFrame("Second Registration Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

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

        panel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        panel.add(contactField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButtonClicked();
            }
        });
        panel.add(registerButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(exitButton);

        tabbedPane = new JTabbedPane();
        displayArea = new JTextArea();
        tabbedPane.addTab("Registered Users", new JScrollPane(displayArea));

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.WEST);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void registerButtonClicked() {
        // Retrieve data from fields
        String id = idField.getText();
        String name = nameField.getText();
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        int day = (int) dayDropdown.getSelectedItem();
        String month = (String) monthDropdown.getSelectedItem();
        int year = (int) yearDropdown.getSelectedItem();
        String dob = day + " " + month + " " + year;
        String address = addressField.getText();
        String contact = contactField.getText();

        // Insert data into the database
        insertUserData(id, name, gender, dob, address, contact);

        // Display data on the tab
        displayArea.append("ID: " + id + "\nName: " + name + "\nGender: " + gender +
                "\nDOB: " + dob + "\nAddress: " + address + "\nContact: " + contact + "\n\n");

        // Clear input fields after registration
        idField.setText("");
        nameField.setText("");
        genderGroup.clearSelection();
        dayDropdown.setSelectedIndex(0);
        monthDropdown.setSelectedIndex(0);
        yearDropdown.setSelectedIndex(0);
        addressField.setText("");
        contactField.setText("");
    }

    private void insertUserData(String id, String name, String gender, String dob, String address, String contact) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO user_data (id, name, gender, dob, address, contact) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, gender);
                preparedStatement.setString(4, dob);
                preparedStatement.setString(5, address);
                preparedStatement.setString(6, contact);

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
                new SecondRegistrationForm();
            }
        });
    }
}
    
   