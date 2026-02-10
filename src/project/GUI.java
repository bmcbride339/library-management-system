package project;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GUI extends JFrame {
    private UserLogin userLogin;
    private Manager manager;
    private User currentUser;

    private JTextField userIdField;
    private JTextField itemIdField;
    private JTextArea outputArea;

    private JButton loginButton;
    private JButton createUserButton;
    private JButton checkoutButton;
    private JButton returnButton;
    private JButton reserveButton;
    private JButton payFeeButton;
    private JButton logoutButton;
    private JButton searchButton;
    private JButton viewInfoButton;

    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton forceCheckoutButton;
    private JButton forceReturnButton;
    private JButton clearFeeButton;

    public GUI(UserLogin userLogin, Manager manager) {
        this.userLogin = userLogin;
        this.manager = manager;
        initialize();
    }

    public void initialize() {
        setTitle("McBride's Library System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        sortItemsFile();


        Font font = new Font("SansSerif", Font.PLAIN, 20);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JPanel adminPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        userIdField = new JTextField();
        itemIdField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        loginButton = new JButton("Login");
        createUserButton = new JButton("Create User");
        checkoutButton = new JButton("Checkout Item");
        returnButton = new JButton("Return Item");
        reserveButton = new JButton("Reserve Item");
        payFeeButton = new JButton("Pay Late Fees");
        logoutButton = new JButton("Logout");
        searchButton = new JButton("Search Items");
        viewInfoButton = new JButton("View My Info");

        addItemButton = new JButton("Add Item (Admin)");
        removeItemButton = new JButton("Remove Item (Admin)");
        forceCheckoutButton = new JButton("Force Checkout (Admin)");
        forceReturnButton = new JButton("Force Return (Admin)");
        clearFeeButton = new JButton("Clear Fee for User");
        addListeners();

        topPanel.add(new JLabel("User ID:"));
        topPanel.add(userIdField);
        topPanel.add(new JLabel("Item ID:"));
        topPanel.add(itemIdField);

        buttonPanel.add(loginButton);
        buttonPanel.add(createUserButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(payFeeButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(viewInfoButton);

        adminPanel.add(addItemButton);
        adminPanel.add(removeItemButton);
        adminPanel.add(forceCheckoutButton);
        adminPanel.add(forceReturnButton);
        adminPanel.add(clearFeeButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(adminPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        disableUserFunctions();
    }

    public void addListeners() {
        loginButton.addActionListener(e -> login());
        createUserButton.addActionListener(e -> createUser());
        checkoutButton.addActionListener(e -> checkoutItem());
        returnButton.addActionListener(e -> returnItem());
        reserveButton.addActionListener(e -> reserveItem());
        payFeeButton.addActionListener(e -> payLateFees());
        logoutButton.addActionListener(e -> logout());
        searchButton.addActionListener(e -> searchItems());
        viewInfoButton.addActionListener(e -> viewUserInfo());

        addItemButton.addActionListener(e -> addItem());
        removeItemButton.addActionListener(e -> removeItem());
        forceCheckoutButton.addActionListener(e -> forceCheckout());
        forceReturnButton.addActionListener(e -> forceReturn());
        clearFeeButton.addActionListener(e -> clearUserFee());
    }

    public void login() {
        String id = userIdField.getText().trim();
        if (userLogin.userExists(id)) {
            currentUser = userLogin.getUser(id);
            output("Logged in as: " + currentUser.getName() + (currentUser.isAdmin() ? " (Admin)" : ""));
            enableUserFunctions();
        } else {
            output("User not found.");
        }
    }

    public void createUser() {
        String id = JOptionPane.showInputDialog(this, "Enter a new User ID:");
        if (id == null || id.trim().isEmpty()) {
            output("User creation canceled (no ID entered).");
            return;
        }
        id = id.trim();

        
        if (userLogin.userExists(id)) {
            output("User ID already exists. Please try a different ID.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        if (name == null || name.trim().isEmpty()) {
            output("User creation canceled (no name entered).");
            return;
        }
        name = name.trim();

        boolean isAdmin = JOptionPane.showConfirmDialog(this, "Admin account?", "Admin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        currentUser = userLogin.createUser(id, name, isAdmin);
        output("User created: " + name + " (" + id + ")");
        enableUserFunctions();
    }


    public void checkoutItem() {
        if (!isLoggedIn()) return;
        
        String itemId = itemIdField.getText().trim().toUpperCase(); 

        Item item = manager.getItem(itemId);

        if (item != null) {
            if (item.getStatus().equalsIgnoreCase("AVAILABLE") || (currentUser.isAdmin() && item.getStatus().equalsIgnoreCase("RESERVED"))) {
                String dueDate = calculateDueDate(item);
                if (manager.checkOutItem(itemId, currentUser, dueDate)) {
                    output("Item checked out successfully. Due: " + dueDate);
                    userLogin.saveUser();
                } else {
                    output("Checkout failed.");
                }
            } else {
                output("Item not available.");
            }
        } else {
            output("Item not found.");
        }
    }

    public void returnItem() {
        if (!isLoggedIn()) return;
        String itemId = itemIdField.getText().trim();
        if (manager.returnItem(itemId, currentUser)) {
            output("Item returned.");
            userLogin.saveUser();
        } else {
            output("Return failed.");
        }
    }

    public void reserveItem() {
        if (!isLoggedIn()) return;
        String itemId = itemIdField.getText().trim();
        if (manager.reserveItem(itemId, currentUser)) {
            output("Item reserved.");
            userLogin.saveUser();
        } else {
            output("Reserve failed.");
        }
    }

    public void payLateFees() {
        if (!isLoggedIn()) return;
        if (currentUser.getLateFee() > 0) {
            currentUser.feeClear();
            userLogin.saveUser();
            output("Late fees cleared.");
        } else {
            output("No late fees due.");
        }
    }

    public void logout() {
        currentUser = null;
        output("Logged out.");
        disableUserFunctions();
    }

    public void searchItems() {
        String query = JOptionPane.showInputDialog(this, "Enter the book title to search:");

        if (query != null && !query.trim().isEmpty()) {
            output("Searching for title: " + query);
            ArrayList<String> itemsList = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("items.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    itemsList.add(line);
                }
            } catch (Exception e) {
                output("Error reading items file: " + e.getMessage());
                return;
            }

            int index = binarySearch(itemsList, query);
            if (index >= 0) {
                String result = itemsList.get(index);
                output("Item found: " + result);
            } else {
                output("Item not found.");
            }
        }
    }
    public int binarySearch(ArrayList<String> itemsList, String query) {
        int low = 0;
        int high = itemsList.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            String title = itemsList.get(mid).split("\\|")[3].trim();
            if (title.equalsIgnoreCase(query)) {
                return mid;
            } else if (title.compareToIgnoreCase(query) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1; 
    }
    public void sortItemsFile() {
        ArrayList<String> itemsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                itemsList.add(line);
            }
        } catch (Exception e) {
            output("Error reading items file: " + e.getMessage());
            return;
        }

        itemsList.sort((line1, line2) -> {
            String title1 = line1.split("\\|")[3].trim();
            String title2 = line2.split("\\|")[3].trim();
            return title1.compareToIgnoreCase(title2); 
        });

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("items.txt"))) {
            for (String item : itemsList) {
                writer.write(item);
                writer.newLine();
            }
        } catch (Exception e) {
            output("Error writing to items file: " + e.getMessage());
        }
    }

    public void viewUserInfo() {
        if (!isLoggedIn()) return;

        StringBuilder info = new StringBuilder();
        info.append("User ID: ").append(currentUser.getID()).append("\n");
        info.append("Name: ").append(currentUser.getName()).append("\n");
        info.append("Admin: ").append(currentUser.isAdmin() ? "Yes" : "No").append("\n\n");

        ArrayList<String> items = currentUser.getCheckedOut();
        if (items.isEmpty()) {
            info.append("No items checked out.");
        } else {
            info.append("Checked out items:\n");
            for (String id : items) {
                Item item = manager.getItem(id);
                if (item != null) {
                    info.append("- ").append(item.getTitle()).append(" (").append(item.getID()).append(")\n");
                }
            }
        }

        JOptionPane.showMessageDialog(this, info.toString(), "User Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addItem() {
        if (!isAdmin()) return;
        String[] types = {"Book", "Audiobook", "Magazine", "Newspaper"};
        String type = (String) JOptionPane.showInputDialog(this, "Select item type:", "Add Item", JOptionPane.PLAIN_MESSAGE, null, types, types[0]);

        if (type != null) {
            String id = JOptionPane.showInputDialog(this, "Enter item ID:");
            String title = JOptionPane.showInputDialog(this, "Enter item title:");
            String author = "NONE";
            if (type.equals("Book") || type.equals("Audiobook")) {
                author = JOptionPane.showInputDialog(this, "Enter author:");
            }
            Item item = null;

            if (type.equals("Book")) {
                item = new Book(id, "AVAILABLE", title, "NONE", author);
            }
            if (type.equals("Audiobook")) {
                String duration = JOptionPane.showInputDialog(this, "Enter duration (minutes):");
                item = new AudioBook( id, "AVAILABLE", title, "NONE", author, duration);
            }
            if (type.equals("Magazine")) {
                String issue = JOptionPane.showInputDialog(this, "Enter issue:");
                item = new Magazine(id, "AVAILABLE", title, "NONE", author, issue);
            }
            if (type.equals("Newspaper")) {
                String issue = JOptionPane.showInputDialog(this, "Enter issue:");
                String pubDate = JOptionPane.showInputDialog(this, "Enter publication date:");
                item = new Newspaper(id, "AVAILABLE", title, "NONE", pubDate, issue);
            }

            if (item != null) {
                manager.addItem(item);
                output(type + " added successfully.");
            }
        }
        sortItemsFile();
    }

    public void removeItem() {
        if (!isAdmin()) return;
        String id = itemIdField.getText().trim();
        if (manager.removeItem(id)) {
            output("Item removed.");
        } else {
            output("Failed to remove item.");
        }
        sortItemsFile();
    }

    public void forceCheckout() {
        if (!isAdmin()) return;
        String id = itemIdField.getText().trim();
        Item item = manager.getItem(id);
        if (item != null) {
            item.setStatus("CHECKED_OUT");
            item.setDueDate(calculateDueDate(item));
            manager.saveItem();
            output("Force checkout successful.");
        } else {
            output("Item not found.");
        }
    }

    public void forceReturn() {
        if (!isAdmin()) return;
        String id = itemIdField.getText().trim();
        Item item = manager.getItem(id);
        if (item != null) {
            item.setStatus("AVAILABLE");
            item.setDueDate("NONE");
            manager.saveItem();
            output("Force return successful.");
        } else {
            output("Item not found.");
        }
    }

    public String calculateDueDate(Item item) {
        LocalDate today = LocalDate.now();
        String type = item.getType();

        if (type.equalsIgnoreCase("BOOK")) {
            return today.plusDays(14).format(DateTimeFormatter.ISO_DATE);
        } else if (type.equalsIgnoreCase("AUDIOBOOK")) {
            return today.plusDays(21).format(DateTimeFormatter.ISO_DATE);
        } else if (type.equalsIgnoreCase("MAGAZINE")) {
            return today.plusDays(7).format(DateTimeFormatter.ISO_DATE);
        } else if (type.equalsIgnoreCase("NEWSPAPER")) {
            return today.plusDays(1).format(DateTimeFormatter.ISO_DATE);
        } else {
            return today.format(DateTimeFormatter.ISO_DATE);
        }
    }

    public boolean isLoggedIn() {
        if (currentUser == null) {
            output("Please login first.");
            return false;
        }
        return true;
    }

    public boolean isAdmin() {
        if (!isLoggedIn()) return false;
        if (!currentUser.isAdmin()) {
            output("Admin access required.");
            return false;
        }
        return true;
    }
    
    public void clearUserFee() {
        if (!isAdmin()) return;

        String userId = JOptionPane.showInputDialog(this, "Enter the User ID to clear fees:");
        if (userId == null || userId.trim().isEmpty()) {
            output("Operation canceled.");
            return;
        }

        userId = userId.trim();
        if (!userLogin.userExists(userId)) {
            output("User not found.");
            return;
        }

        User user = userLogin.getUser(userId);
        if (user.getLateFee() > 0) {
            user.feeClear();
            userLogin.saveUser();
            output("Cleared late fees for user: " + user.getName() + " (" + user.getID() + ")");
        } else {
            output("User has no late fees.");
        }
    }

    public void output(String message) {
        outputArea.append(message + "\n");
    }

    public void enableUserFunctions() {
        checkoutButton.setEnabled(true);
        returnButton.setEnabled(true);
        reserveButton.setEnabled(true);
        payFeeButton.setEnabled(true);
        logoutButton.setEnabled(true);
        searchButton.setEnabled(true);
        viewInfoButton.setEnabled(true);

        boolean admin = currentUser != null && currentUser.isAdmin();
        addItemButton.setEnabled(admin);
        removeItemButton.setEnabled(admin);
        forceCheckoutButton.setEnabled(admin);
        forceReturnButton.setEnabled(admin);
        clearFeeButton.setEnabled(admin);
    }

    public void disableUserFunctions() {
        checkoutButton.setEnabled(false);
        returnButton.setEnabled(false);
        reserveButton.setEnabled(false);
        payFeeButton.setEnabled(false);
        logoutButton.setEnabled(false);
        searchButton.setEnabled(false);
        viewInfoButton.setEnabled(false);

        addItemButton.setEnabled(false);
        removeItemButton.setEnabled(false);
        forceCheckoutButton.setEnabled(false);
        forceReturnButton.setEnabled(false);
        clearFeeButton.setEnabled(false);
    }
}

