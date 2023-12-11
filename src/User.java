/**
 * A program that has getters and setters of users and their accounts.
 * <p>
 * Purdue University -- CS18000 -- Fall 2023  -- Project 04
 *
 * @author Joshua Pang
 * Lab Section 25
 * @version January 10, 2021
 */

import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private String email;
    private String password;

    private boolean customer;

    public User(String email, String password) {
        setEmail(email);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCustomer() { return customer; }

    public void setCustomer(boolean customer) {
        this.customer = customer;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User))
            return false;
        User u = (User) o;
        return u.getEmail().toLowerCase().equals(this.email);
    }

    public static User logIn(Scanner scanner, ArrayList<User> users) {
        System.out.println("Please enter your email: ");
        String email = scanner.next();
        scanner.nextLine();

        System.out.println("Please enter your password: ");
        String password = scanner.next();
        scanner.nextLine();
        return logInWithInputs(email, password, users);

    }

    public static User logInWithInputs(String email, String password, ArrayList<User> users) {

        int index = users.indexOf(new User(email, null));
        if (index != -1) {
            User loginUser = users.get(index);
            if (password.equals(loginUser.getPassword())) {
                System.out.println("You have successfully logged in!");
                return loginUser;
            }
        }

        System.out.println("Incorrect email or password. Please try again.");
        return null;
    }

    public static User signUp(Scanner scanner, ArrayList<User> users) {
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

        System.out.println("Enter [C] if you are a Customer or [S] if you are a Seller: ");
        String userRole = scanner.nextLine();

        boolean customer;
        if (userRole.equals("C")) {
            customer = true;
        }  else if (userRole.equals("S")) {
            customer = false;
        }
        else {
            // if user chooses an option that is not accepted
            System.out.println("Invalid role, please try again.");
            return null;
        }
        return signupWithInputs(email, password, customer, users);
    }

    public static User signupWithInputs(String email, String password, boolean customer, ArrayList<User> users) {
        if (users.contains(new User(email, null))) {
            System.out.println("Email is taken, please enter a different email.");
            return null;
        }

        if (customer) {
            Customer newCustomer = new Customer(email, password);
            users.add(newCustomer);
            System.out.println("You have selected customer!");
            System.out.println("Account successfully added!\n");
            return newCustomer;
        } else {
            Seller newSeller = new Seller(email, password);
            System.out.println("You have selected seller!");
            System.out.println("Account successfully added!\n");
            users.add(newSeller);
            return newSeller;
        }
    }

    public static User authentication(ArrayList<User> users) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    1. Log In
                    2. Sign Up
                    3. Exit
                    Enter your choice below:
                    """); // Prompt user to either log in or sign up
            try {
                int authenticationType = scanner.nextInt();
                scanner.nextLine();

                switch(authenticationType) {
                    case 1 -> {                    // If user chooses to log in
                        User user = logIn(scanner, users);
                        if (user != null)
                            return user;
                    }
                    case 2 -> {                   // If user chooses to sign up
                        User user = signUp(scanner, users);
                        if (user != null)
                            return user;
                        break;
                    }
                    case 3 -> {
                        return null;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine();
            }
        }
    }
}
