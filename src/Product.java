import java.util.Scanner;

/**
 * Project 04 --Product
 *
 * This program models each product and includes the actions that can be performed to it
 *
 * @author Harini Muthu
 *
 * @version November 12, 2023
 *
 */

public class Product {
    private String name;
    private String description;
    private int quantity;
    private double price;
    private int id;

    public Product(int id) {
        this.id = id;
    }

    public Product(String name, String description, int quantity, double price, int id) throws Exception {
        this.name = name;
        this.description = description;
        setQuantity(quantity);
        setPrice(price);
        this.id = id;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws Exception {
        if (quantity < 0) {
            throw new Exception("Quantity can't be negative");
        }
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws Exception {
        if (price <= 0) {
            throw new Exception("Price must be greater than 0");
        }
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Product))
            return false;
        Product p = (Product)o;
        return id == p.getId();
    }

    /**
     * removes a quantity
     * @param removeQuantity
     */
    public void removeQuantity(int removeQuantity) throws Exception {
        setQuantity(getQuantity() - removeQuantity);
    }

    @Override
    public String toString() {
        return String.format("%20s | %.2f | %5d", name, price, quantity);
    }

    /**
     * All actions performable by Store
     */
    public void actions() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Your product information is: ");
                System.out.println(this);

                System.out.print("""
                                Enter [1] to modify description.
                                Enter [2] to modify quantity.
                                Enter [3] to modify price.
                                Enter [4] to back.
                                """);
                int sellerChoice = scanner.nextInt();
                scanner.nextLine();

                boolean goBack = false;
                switch (sellerChoice) {
                    case 1 -> {
                        System.out.println("Enter the new description: ");
                        String newDescription = scanner.nextLine();
                        setDescription(newDescription); // sets description
                    }
                    case 2 -> {
                        System.out.println("Enter the new quantity: ");
                        int newQuantity = scanner.nextInt();
                        scanner.nextLine();
                        setQuantity(newQuantity);
                    }
                    case 3 -> {
                        System.out.println("Enter the new price: ");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine();
                        setPrice(newPrice);
                    }
                    case 4 -> goBack = true;
                    case 5 -> System.out.println("Invalid choice, please try again.");

                }
                if (goBack) {
                    break;
                }
            } catch (Exception e) { // if seller inputs a non-integer value
                System.out.println("Please input one of the above options.");
                scanner.nextLine();
            }
        }
    }
}