import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Project 04 --Seller
 *
 * This program models each seller and the actions they can perform
 *
 * @author Harini Muthu
 *
 * @version November 12, 2023
 *
 */
public class Seller extends User {

    private ArrayList<Store> stores = new ArrayList<Store>();
    private static final String INVALID_FORMAT = "StoreName, ProductName, Description, Quantity, Price";

    public Seller(String email, String password) {
        super(email, password);
    }

    public Seller(String email, String password, ArrayList<Store> stores) {
        super(email, password);
        this.stores = stores;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    /**
     * adds a store so the list of stores if the store list doesn't already contain the store. If it does, return null
     * @param store
     * @return
     */
    public Store addStore(Store store){
        if (!stores.contains(store)) {
            stores.add(store);
            return store;
        } else {
            return null;
        }
    }

    /**
     * creates a store
     * @param store
     * @return
     */
    public Store addStore(String store) {
        Store newStore = new Store(getEmail(), store);
        return addStore(newStore);
    }

    public Store getStore(String store)  {
        int index = stores.indexOf(new Store(getEmail(), store));
        if (index == -1)
            return null;
        return stores.get(index);
    }

    public Store getStore(int id) {
        int index = stores.indexOf(new Store(id));
        if (index == -1)
            return null;
        return stores.get(index);
    }

    /**
     * asks for store name to create store
     * @param scanner
     * @return
     */
    public Store createStore(Scanner scanner) {
        System.out.println("Enter your store name: ");
        String storeName = scanner.nextLine();
        return createStore(storeName);
    }

    public Store createStore(String storeName) {
        Store newStore = addStore(storeName);

        if (newStore == null) {
            System.out.println("Store name already exists. Please choose another store name");
            return null;
        }

        return newStore;
    }

    /**
     * opens a store
     * @param scanner
     * @return
     */
    public Store loginStore(Scanner scanner) {
        System.out.println("Enter your store ID: ");
        int storeID = scanner.nextInt();
        Store newStore = getStore(storeID);

        if (newStore == null) {
            System.out.println("Store id does not exists. Please choose another store id");
            return null;
        }

        return newStore;
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
     * viewing statistics for seller
     * @param scanner
     */

    public void viewStatistics(ClothingMarketPlace mp, Scanner scanner) {
        ArrayList<ShoppingItem> shoppingItems = getStatsForAStore(mp, scanner);
        if (shoppingItems.size() == 0)
            return;

        System.out.println("Enter the filename to save stats:");
        String fileName = scanner.nextLine();

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(fileName, false))) {
            Map<String, Integer> itemsPurchasedByCustomer = new HashMap<>();
            Map<String, Integer> salesByProduct = new HashMap<>();

            for (ShoppingItem item : shoppingItems) {
                Product product = item.getProduct();
                String productName = product.getName();
                String customerName = item.getSellerName();
                int quantity = product.getQuantity();

                itemsPurchasedByCustomer.put(customerName, itemsPurchasedByCustomer.getOrDefault(customerName, 0) + quantity);
                salesByProduct.put(productName, salesByProduct.getOrDefault(productName, 0) + quantity);
            }

            fileWriter.println("\nItems Purchased by Customer:");
            System.out.println("\nItems Purchased by Customer:");
            for (Map.Entry<String, Integer> entry : itemsPurchasedByCustomer.entrySet()) {
                String stats = "Customer: " + entry.getKey() + ", Quantity: " + entry.getValue();
                fileWriter.println(stats);
                System.out.println(stats);
            }

            fileWriter.println("\nSales by Product:");
            System.out.println("\nSales by Product:");
            for (Map.Entry<String, Integer> entry : salesByProduct.entrySet()) {
                String stats = "Product: " + entry.getKey() + ", Quantity: " + entry.getValue();
                fileWriter.println(stats);
                System.out.println(stats);
            }

            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * imports data from a file
     * @param scanner
     * @param mp
     */
    public void importProduct(ClothingMarketPlace mp, Scanner scanner) {
        System.out.println("Expected CSV format: " + INVALID_FORMAT);
        System.out.println("Enter import filename: ");
        String fileName = scanner.nextLine();
        importProduct(mp, fileName);
    }

    public boolean importProduct(ClothingMarketPlace mp, String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isEmpty())
                    continue;
                List<String> values = Arrays.asList(line.split(","));
                if (values.size() < 5)
                    throw new Exception(INVALID_FORMAT);
                String storeName = values.get(0).trim();
                String productName = values.get(1).trim();
                String description = values.get(2).trim();
                int quantity = Integer.parseInt(values.get(3).trim());
                double price = Double.parseDouble(values.get(4).trim());

                Store store = getStore(storeName);
                if (store == null) {
                    store = addStore(storeName);
                    store.setId(mp.getStoreCount());
                    mp.tickStoreCount();
                }

                Product p = store.getProduct(productName);
                if (p == null) {
                    p = new Product(productName, description, quantity, price, 0);
                    p.setId(mp.getProductCount());
                    mp.tickProductCount();
                    store.addProduct(p);
                } else {
                    p.setDescription(description);
                    p.setQuantity(quantity);
                    p.setPrice(price);
                }
            }
            fileScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * writes a file
     * @param scanner
     */
    public void exportProduct(Scanner scanner) {
        System.out.println("CSV format: " + INVALID_FORMAT);
        System.out.println("Enter export filename: ");
        String fileName = scanner.nextLine();
        exportProduct(fileName);
    }

    public boolean exportProduct(String fileName) {
        try {
            FileWriter f = new FileWriter(fileName);
            for (Store s : stores) {
                for (Product p : s.getProducts()) {
                    f.write(s.getName() + "," + p.getName() + "," + p.getDescription() + "," + p.getQuantity() + "," + p.getPrice());
                    f.write(System.lineSeparator());
                }
            }
            f.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<ShoppingItem> getStatsForAStore(ClothingMarketPlace mp, Scanner scanner) {
        for (Store s: stores) {
            System.out.println(s);
        }
        System.out.println("Enter store id: ");
        int storeId = scanner.nextInt();

        Store searchedStore = null;
        for (Store store : mp.getStores()) {
            if (store.getId() == storeId) {
                if (store.getSellerEmail().equals(getEmail())) {
                    searchedStore = store;
                }
                break;
            }
        }

        if (searchedStore == null) {
            System.out.println("Store id didn't match any entry");
            return new ArrayList<>();
        }

        return mp.getSoldItems(searchedStore);
    }

    public void printStats(ClothingMarketPlace mp, Scanner scanner) {
        ArrayList<ShoppingItem> shoppingItems = getStatsForAStore(mp, scanner);
        mp.listShoppingItem(shoppingItems);
    }


    /**
     * All Actions done by seller
     * @param mp
     */
    public void actionsCommandLine(ClothingMarketPlace mp) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // options
                System.out.print("""
                        Enter [1] to create a store.
                        Enter [2] to open a store.
                        Enter [3] to import file.
                        Enter [4] to export file.
                        Enter [5] to print store stats.
                        Enter [6] to change password.
                        Enter [7] to view statistics.
                        Enter [8] to back.
                        """);
                int sellerChoice = scanner.nextInt();
                scanner.nextLine();

                boolean goBack = false;
                Store store;

                switch (sellerChoice) {
                    case 1 -> {
                        mp.listStores(stores);
                        store = createStore(scanner);
                        if (store != null) {
                            store.setId(mp.getStoreCount());
                            mp.tickStoreCount();
                        }
                    }
                    case 2 -> {
                        mp.listStores(stores);
                        store = loginStore(scanner);
                        if (store != null) {
                            store.actions(mp);
                        }
                    }
                    case 3 -> importProduct(mp, scanner);
                    case 4 -> exportProduct(scanner);
                    case 5 -> printStats(mp, scanner);
                    case 6 -> changePassword(scanner);
                    case 7 -> viewStatistics(mp, scanner);
                    case 8 -> goBack = true;
                    default -> System.out.println("Invalid choice. Please try again.");
                }

                if (goBack) {
                    break;
                }

            } catch (Exception e) { // if seller enters a non integer-value
                System.out.println("Please input one of the above options");
                scanner.nextLine();
            }
        }
    }

    public void actionsGui(ClothingMarketPlace mp) {
        SellerForm sellerForm = new SellerForm(null, this, mp);
        sellerForm.pack();
        sellerForm.setVisible(true);
    }

}
