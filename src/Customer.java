import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Project 04 --Customer
 *
 * This program models each customer and the actions they can perform
 *
 * @author Harini Muthu
 *
 * @version November 12, 2023
 *
 */

public class Customer extends User {
    ArrayList<ShoppingItem> cart = new ArrayList<ShoppingItem>();
    ArrayList<ShoppingItem> orders = new ArrayList<ShoppingItem>();

    /**
     * Constructor with empty cart and no orders
     * @param email
     * @param password
     */
    public Customer(String email, String password) {
        super(email, password);
    }

    /**
     * Constructor with cart but no orders
     * @param email
     * @param password
     * @param cart
     */
    public Customer(String email, String password, ArrayList<ShoppingItem> cart) {
        super(email, password);
        this.cart = cart;
    }

    /**
     * Constructor with cart and orders
     * @param email
     * @param password
     * @param cart
     * @param orders
     */
    public Customer(String email, String password, ArrayList<ShoppingItem> cart, ArrayList<ShoppingItem> orders) {
        super(email, password);
        this.cart = cart;
        this.orders = orders;
    }

    // getters and setters
    public ArrayList<ShoppingItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<ShoppingItem> cart) {
        this.cart = cart;
    }

    public ArrayList<ShoppingItem> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<ShoppingItem> orders) {
        this.orders = orders;
    }

    /**
     * Asks Customer if they would like to add an item to a cart or sort the items shown from the itemList
     * @param marketPlaceItems
     * @param scanner
     * @throws Exception
     */
    public void addToCart(ClothingMarketPlace mp, ArrayList<ShoppingItem> marketPlaceItems, Scanner scanner) throws Exception {
        System.out.println("""
                        Enter [1] to add an item to your cart.
                        Enter [2] to sort all products by price (ascending).
                        Enter [3] to sort all products by name (ascending).
                        Enter [4] to sort all products by price (descending).
                        Enter [5] to sort all products by name (descending).
                        Enter [6] to go to a product page.
                        Enter [7] to go back.
                        """);
        int buyChoice = scanner.nextInt();
        boolean goBack = false;
        switch (buyChoice) {
            case 1 ->  {
                System.out.println("What is the product ID of the item you would like to buy: ");
                int buyItem = scanner.nextInt(); // buyItem = productID
                scanner.nextLine();
                int index = marketPlaceItems.indexOf(new ShoppingItem(buyItem)); // get index in marketPlaceItems
                if (index == -1) { // make sure selection is valid
                    System.out.println("This selection is invalid. Please try again");
                } else {
                    ShoppingItem item = marketPlaceItems.get(index);

                    System.out.println(item);
                    System.out.println("How many do you want to add to cart: "); // ask for quantity
                    int buyQuantity = scanner.nextInt();
                    scanner.nextLine();

                    if (buyQuantity < item.getProduct().getQuantity()) {
                        Product cartProduct = new Product(item.getProduct().getName(),
                                item.getProduct().getDescription(),
                                buyQuantity,
                                item.getProduct().getPrice(),
                                item.getProduct().getId());
                        ShoppingItem cartItem = new ShoppingItem(cartProduct, item.getSellerName(), item.getStoreName());
                        item.getProduct().removeQuantity(buyQuantity);
                        cart.add(cartItem);
                        goBack = true;
                    } else {
                        System.out.println("The quantity is too high");
                    }
                }
            }
            case 2 -> {
                mp.sortByPrice(marketPlaceItems, true);
                mp.listShoppingItem(marketPlaceItems);
            }
            case 3 -> {
                mp.sortByName(marketPlaceItems, true);
                mp.listShoppingItem(marketPlaceItems);
            }
            case 4 -> {
                mp.sortByPrice(marketPlaceItems, false);
                mp.listShoppingItem(marketPlaceItems);
            }
            case 5 -> {
                mp.sortByName(marketPlaceItems, false);
                mp.listShoppingItem(marketPlaceItems);
            }
            case 6 -> {
                System.out.println("What is the product id: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                int index = marketPlaceItems.indexOf(new ShoppingItem(id)); // get index in marketPlaceItems
                if (index == -1) {
                    System.out.println("Sorry, we do not have " + id + " at this marketplace");
                } else {
                    System.out.println(marketPlaceItems.get(index));
                    goBack = true;
                }
            }
            case 7 -> goBack = true;
            default-> System.out.println("Invalid choice, please try again");
        }
        if (goBack) {
            return;
        }
        addToCart(mp, marketPlaceItems, scanner);
    }


    /**
     * filters items by name
     * will show multiple items if there are multiple different stores that each have an item with the same name
     * otherwise will only show one item
     * @param scanner
     * @param marketPlaceItems
     * @return
     */
    public ArrayList<ShoppingItem> filterByName(Scanner scanner, ArrayList<ShoppingItem> marketPlaceItems) {
        System.out.println("Enter the product name to search: ");
        String productName = scanner.nextLine().toLowerCase();
        return filterByName(productName, marketPlaceItems);
    }

    public ArrayList<ShoppingItem> filterByName(String productName, ArrayList<ShoppingItem> marketPlaceItems) {
        ArrayList<ShoppingItem> searchedItems = new ArrayList<>();
        for (ShoppingItem item : marketPlaceItems) {
            if (item.getProduct().getName().toLowerCase().contains(productName)) {
                searchedItems.add(item);
            }
        }

        return searchedItems;
    }


    /**
     * searches if a word in the description is in the name of the item
     * @param scanner
     * @param marketPlaceItems
     * @return
     */
    public ArrayList<ShoppingItem> filterByDescription(Scanner scanner, ArrayList<ShoppingItem> marketPlaceItems) {
        System.out.println("Enter the product description to search: ");
        String productDescription = scanner.nextLine().toLowerCase();
        return filterByDescription(productDescription, marketPlaceItems);
    }

    public ArrayList<ShoppingItem> filterByDescription(String productDescription, ArrayList<ShoppingItem> marketPlaceItems) {
        ArrayList<ShoppingItem> searchedItems = new ArrayList<>();
        String desc = productDescription.toLowerCase();
        for (ShoppingItem item : marketPlaceItems) {
            if (item.getProduct().getDescription().toLowerCase().contains(desc)) {
                searchedItems.add(item);
            }
        }

        return searchedItems;
    }

    /**
     * returns all items from a specific store
     * @param scanner
     * @param marketPlaceItems
     * @return
     */
    public ArrayList<ShoppingItem> filterByStore(Scanner scanner, ArrayList<ShoppingItem> marketPlaceItems) {
        System.out.println("Enter the product store to search: ");
        String productStore = scanner.nextLine().toLowerCase();
        return filterByStore(productStore, marketPlaceItems);
    }

    public ArrayList<ShoppingItem> filterByStore(String productStore, ArrayList<ShoppingItem> marketPlaceItems) {
        ArrayList<ShoppingItem> searchedItems = new ArrayList<>();
        String store = productStore.toLowerCase();
        for (ShoppingItem item : marketPlaceItems) {
            if (item.getStoreName().toLowerCase().contains(store)) {
                searchedItems.add(item);
            }
        }

        return searchedItems;
    }

    /**
     * prints all items in the cart
     */
    public void printCart() {
        if (cart.size() == 0) {
            System.out.println("The Shopping Cart is empty");
        }
        for(ShoppingItem item : cart) {
            System.out.println(item);
        }
    }

    /**
     * viewing statistics for customer
     */

    public void viewStatistics(Scanner scanner) {
        System.out.println("Enter the filename to save stats:");
        String fileName = scanner.nextLine();

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(fileName, false))) {
            Map<String, Integer> salesByStore = new HashMap<>();
            Map<String, Integer> salesByProduct = new HashMap<>();

            for (ShoppingItem item : getOrders()) {
                String storeName = item.getStoreName();
                Product product = item.getProduct();
                String productName = product.getName();
                int quantity = product.getQuantity();

                salesByStore.put(storeName, salesByStore.getOrDefault(storeName, 0) + quantity);
                salesByProduct.put(productName, salesByProduct.getOrDefault(productName, 0) + quantity);
            }

            fileWriter.println("\nSales by Store:");
            System.out.println("\nSales by Store:");
            for (Map.Entry<String, Integer> entry : salesByStore.entrySet()) {
                String header = "Store: " + entry.getKey() + ", Quantity: " + entry.getValue();
                fileWriter.println(header);
                System.out.println(header);
            }

            fileWriter.println("\nSales by Product:");
            System.out.println("\nSales by Product:");
            for (Map.Entry<String, Integer> entry : salesByProduct.entrySet()) {
                String header = "Product: " + entry.getKey() + ", Quantity: " + entry.getValue();
                fileWriter.println(header);
                System.out.println(header);
            }

            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes items from the cart
     * @param scanner
     * @param marketPlaceItems
     * @throws Exception
     */
    public void removeFromCart(Scanner scanner, ArrayList<ShoppingItem> marketPlaceItems) throws Exception {
        if (cart.size() == 0) {
            System.out.println("No products in the cart to remove.");
            return;
        }

        System.out.println("Enter the product ID of the product you want to remove: ");
        int removeId = scanner.nextInt();
        scanner.nextLine();
        ShoppingItem removeShoppingItem = new ShoppingItem(removeId);
        int index = cart.indexOf(removeShoppingItem);

        if (index == -1) {
            System.out.println("This item is not in the cart");
            return;
        }

        removeShoppingItem = cart.get(index);
        removeFromCart(removeShoppingItem, marketPlaceItems);
    }

    public void removeFromCart(ShoppingItem removeShoppingItem, ArrayList<ShoppingItem> marketPlaceItems) {
        cart.remove(removeShoppingItem);
        int index = marketPlaceItems.indexOf(removeShoppingItem);
        if (index != -1) {
            ShoppingItem item = marketPlaceItems.get(index);
            try {
                item.getProduct().setQuantity(item.getProduct().getQuantity() + removeShoppingItem.getProduct().getQuantity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * prints the orders
     */
    public void printOrders() {
        if(orders.size() == 0) {
            System.out.println("You haven't made any orders");
        }
        for(ShoppingItem item : orders) {
            System.out.println(item);
        }
    }

    /**
     * changes user password
     * @param scanner
     */
    public void changePassword(Scanner scanner) {
        System.out.println("Enter new password: ");
        String newPassword = scanner.nextLine();
        setPassword(newPassword);
    }

    /**
     * exports all orders to a file (filename inputted by the user)
     * @param scanner
     */
    public void exportPurchaseHistory(Scanner scanner) {
        System.out.println("CSV Format: StoreName, Name, Description, Quantity, Price");
        System.out.println("Enter export filename: ");
        String fileName = scanner.nextLine();
        exportPurchaseHistory(fileName);
    }

    public boolean exportPurchaseHistory(String fileName) {
        try {
            FileWriter f = new FileWriter(fileName);
            for (ShoppingItem item: orders) {
                Product p = item.getProduct();
                f.write(item.getStoreName() + "," + p.getName() + "," + p.getDescription() +
                        "," + p.getQuantity() + "," + p.getPrice());
                f.write(System.lineSeparator());
            }
            f.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * buys all items in the cart
     */
    public double checkOut() {
        double total = getTotal();
        orders.addAll(cart);
        cart = new ArrayList<>();
        System.out.printf("You have been charged %.2f\n", total);
        System.out.println("All items from cart has been bought");
        return total;
    }

    public double getTotal() {
        double total = 0;
        for (ShoppingItem item: cart) {
            total += item.getProduct().getQuantity() * item.getProduct().getPrice();
        }
        return total;
    }

    /**
     * All actions performable by the Customer
     * @param cmp
     */
    public void actionsCommandLine(ClothingMarketPlace cmp){
        Scanner scanner = new Scanner(System.in);
        ArrayList<ShoppingItem> shoppingItems = cmp.getShoppingItems();
        ArrayList<Store> stores = cmp.getStores();

        while (true) {
            try {
                System.out.print("""
                        Enter [1] to list all products.
                        Enter [2] to search product by name.
                        Enter [3] to search product by description.
                        Enter [4] to search product by store.
                        Enter [5] to view all stores.
                        Enter [6] to view shopping cart.
                        Enter [7] to remove product from cart.
                        Enter [8] to look at purchase history.
                        Enter [9] to export purchase history.
                        Enter [10] to checkout.
                        Enter [11] to change password.
                        Enter [12] to view statistics.
                        Enter [13] to go back.
                        """);
                int customerChoice = scanner.nextInt();
                scanner.nextLine();

                boolean go_back = false;
                switch (customerChoice) {
                    case 1 -> {
                        cmp.listShoppingItem(shoppingItems);
                        addToCart(cmp, cmp.getShoppingItems(), scanner);
                    }
                    case 2 -> {
                        ArrayList<ShoppingItem> searchByNames = filterByName(scanner, cmp.getShoppingItems());
                        cmp.listShoppingItem(searchByNames);
                        addToCart(cmp, searchByNames, scanner);
                    }
                    case 3 -> {
                        ArrayList<ShoppingItem> searchByDescription = filterByDescription(scanner, cmp.getShoppingItems());
                        cmp.listShoppingItem(searchByDescription);
                        addToCart(cmp, searchByDescription, scanner);
                    }
                    case 4 -> {
                        ArrayList<ShoppingItem> searchByStore = filterByStore(scanner, cmp.getShoppingItems());
                        cmp.listShoppingItem(searchByStore);
                        addToCart(cmp, searchByStore, scanner);
                    }
                    case 5 -> cmp.listStores(stores);
                    case 6 -> printCart();
                    case 7 -> {
                        printCart();
                        removeFromCart(scanner, cmp.getShoppingItems());
                    }
                    case 8 -> printOrders();
                    case 9 -> exportPurchaseHistory(scanner);
                    case 10 -> {
                        printCart();
                        checkOut();
                    }
                    case 11 -> changePassword(scanner);
                    case 12 -> viewStatistics(scanner);
                    case 13 -> go_back = true;
                    default -> System.out.println("Invalid choice, please try again.");
                }

                if (go_back) {
                    break;
                }
            } catch (Exception e) { // if user inputs a non-integer value
                System.out.println("Please enter one of the above options");
                scanner.nextLine();
            }
        }
    }

    public void actionsGui(ClothingMarketPlace cmp) {
        CustomerForm customerForm = new CustomerForm(null, this, cmp);
        customerForm.pack();
        customerForm.setVisible(true);
    }

}