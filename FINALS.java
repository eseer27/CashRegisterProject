import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<String> usernames = new ArrayList<>();
    static ArrayList<String> passwords = new ArrayList<>();
    static ArrayList<String> productNames = new ArrayList<>();
    static ArrayList<Double> productPrices = new ArrayList<>();
    static ArrayList<Integer> productQuantities = new ArrayList<>();
    static String loggedInUser = "";

    public static void main(String[] args) {
        productNames.add("SpamSilog");
        productPrices.add(40.00);
        productQuantities.add(10);
        productNames.add("Butt and Balls");
        productPrices.add(120.00);
        productQuantities.add(12);
        productNames.add("TapSilog");
        productPrices.add(80.00);
        productQuantities.add(13);
        productNames.add("HotSilog");
        productPrices.add(60.00);
        productQuantities.add(10);
        productNames.add("Tocilog");
        productPrices.add(70.00);
        productQuantities.add(10);

        boolean authenticated = false;
        while (!authenticated) {
            System.out.println("\nWelcome! Choose an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.print("Enter choice: ");
            int authChoice = safeIntInput();

            if (authChoice == 1) {
                userSignup();
            } else if (authChoice == 2) {
                userLogin();
                authenticated = true;
            } else {
                System.out.println("Invalid choice. Please select 1 or 2.");
            }
        }

        while (true) {
            System.out.println("\n=== TAPSI NI PAPS! ===");
            System.out.println("1. Admin Mode");
            System.out.println("2. Cashier Mode");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int mainChoice = safeIntInput();
            if (mainChoice == 1) {
                adminMode();
            } else if (mainChoice == 2) {
                cashierMode();
            } else if (mainChoice == 3) {
                System.out.println("\n=== THANK YOU FOR VISITING TAPSI NI PAPS! ===");
                return;
            } else {
                System.out.println("Oops! That’s not a valid choice. Please try again.");
            }
        }
    }

    static void userSignup() {
        System.out.println("=== USER SIGNUP ===");
        boolean valid = false;
        while (!valid) {
            System.out.print("Enter username (5-15 alphanumeric characters): ");
            String username = scanner.nextLine();
            if (username.matches("^[a-zA-Z0-9]{5,15}$")) {
                System.out.print("Enter password (8-20 characters, at least 1 uppercase, 1 number): ");
                String password = scanner.nextLine();
                if (password.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                    usernames.add(username);
                    passwords.add(password);
                    System.out.println("Signup successful!\n");
                } else {
                    System.out.println("Invalid password. Please ensure it contains at least one uppercase letter and one number.");
                }
            } else {
                System.out.println("Invalid username. It must be between 5-15 alphanumeric characters.");
            }
            break;
        }
    }

    static void userLogin() {
        System.out.println("=== USER LOGIN ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            String loginUser = scanner.nextLine();
            System.out.print("Password: ");
            String loginPass = scanner.nextLine();

            for (int i = 0; i < usernames.size(); i++) {
                if (usernames.get(i).equals(loginUser) && passwords.get(i).equals(loginPass)) {
                    loggedIn = true;
                    loggedInUser = loginUser;
                    break;
                }
            }

            if (loggedIn) {
                System.out.println("Login successful. Accessing Cash Register...\n");
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    static void adminMode() {
        System.out.println("\n1. Add Product");
        System.out.println("2. View Menu");
        System.out.println("3. Remove Product");
        System.out.print("Enter choice: ");

        int adminChoice = safeIntInput();
        if (adminChoice == 1) {
            System.out.print("Product name: ");
            String name = scanner.nextLine();
            System.out.print("Price: ");
            double price = safeDoubleInput();
            System.out.print("Quantity: ");
            int quantity = safeIntInput();
            productNames.add(name);
            productPrices.add(price);
            productQuantities.add(quantity);
            System.out.println(name + " added successfully.");
        } else if (adminChoice == 2) {
            displayMenu();
        } else if (adminChoice == 3) {
            displayMenu();
            System.out.print("Enter product number to remove: ");
            int removeIndex = safeIntInput() - 1;
            if (removeIndex >= 0 && removeIndex < productNames.size()) {
                String removedProduct = productNames.get(removeIndex);
                productNames.remove(removeIndex);
                productPrices.remove(removeIndex);
                productQuantities.remove(removeIndex);
                System.out.println(removedProduct + " removed successfully.");
            } else {
                System.out.println("Invalid product number.");
            }
        } else {
            System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    static void cashierMode() {
        if (productNames.isEmpty()) {
            System.out.println("No products available. Add products first.");
            return;
        }

        double totalAmount = 0.0;
        List<Integer> cartProducts = new ArrayList<>();
        List<Integer> cartQuantities = new ArrayList<>();

        displayMenu();

        boolean shopping = true;
        while (shopping) {
            System.out.print("\nEnter product number (0 to finish): ");
            int choice = safeIntInput();
            if (choice == 0) {
                shopping = false;
            } else if (choice >= 1 && choice <= productNames.size()) {
                int productIndex = choice - 1;
                System.out.print("Enter quantity: ");
                int qty = safeIntInput();

                if (qty > productQuantities.get(productIndex)) {
                    System.out.println("Not enough stock available. Current stock: " + productQuantities.get(productIndex));
                } else {
                    if (cartProducts.contains(productIndex)) {
                        int index = cartProducts.indexOf(productIndex);
                        int newQty = cartQuantities.get(index) + qty;
                        cartQuantities.set(index, newQty);
                    } else {
                        cartProducts.add(productIndex);
                        cartQuantities.add(qty);
                    }
                    productQuantities.set(productIndex, productQuantities.get(productIndex) - qty);
                    totalAmount += qty * productPrices.get(productIndex);
                }
            } else {
                System.out.println("Invalid product number. Try again.");
            }
        }

        if (cartProducts.isEmpty()) {
            System.out.println("No items selected. Returning to main menu.");
            return;
        }

        System.out.println("\n=== ORDER SUMMARY ===");
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < cartProducts.size(); i++) {
            int idx = cartProducts.get(i);
            int qty = cartQuantities.get(i);
            String itemName = productNames.get(idx);
            double itemPrice = productPrices.get(idx);
            double itemTotal = qty * itemPrice;
            System.out.println(itemName + " - Quantity: " + qty + " - Total: " + String.format("%.2f", itemTotal));
            summary.append(itemName).append(" - Quantity: ").append(qty).append(" - Price: ").append(itemPrice).append("\n");
        }
        System.out.println("-------------------------------");
        System.out.println("Total: " + String.format("%.2f", totalAmount));

        System.out.print("Payment method (cash/card): ");
        String payment = scanner.nextLine();

        if (payment.equalsIgnoreCase("cash")) {
            boolean paid = false;
            while (!paid) {
                System.out.print("Enter payment amount: ");
                double paymentAmount = safeDoubleInput();
                if (paymentAmount >= totalAmount) {
                    System.out.println("Change: " + String.format("%.2f", (paymentAmount - totalAmount)));
                    paid = true;
                } else {
                    System.out.println("Not enough payment. Please try again.");
                }
            }
        } else if (payment.equalsIgnoreCase("card")) {
            System.out.println("Card payment successful.");
        } else {
            System.out.println("Unknown payment method. Transaction canceled.");
            return;
        }

        logTransaction(summary.toString(), totalAmount);
    }

    static void logTransaction(String summary, double totalAmount) {
        try (FileWriter writer = new FileWriter("transactions.txt", true)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            writer.write("Date/Time: " + dtf.format(now) + "\n");
            writer.write("Cashier: " + loggedInUser + "\n");
            writer.write("Items Purchased:\n" + summary);
            writer.write("Total Amount: " + String.format("%.2f", totalAmount) + "\n");
            writer.write("-----------------------------\n");
        } catch (IOException e) {
            System.out.println("Error writing to transactions file: " + e.getMessage());
        }
    }

    static void displayMenu() {
        System.out.println("\n========= MENU =========");
        System.out.println("No.  Product Name           Price     Stock");
        for (int i = 0; i < productNames.size(); i++) {
            int number = i + 1;
            String name = productNames.get(i);
            double price = productPrices.get(i);
            int stock = productQuantities.get(i);
            System.out.println(number + ".  " + name + "                 " + String.format("%.2f", price) + "     " + stock);
        }
    }

    static int safeIntInput() {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return value;
    }

    static double safeDoubleInput() {
        boolean valid = false;
        double value = 0.0;
        while (!valid) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return value;
    }
}