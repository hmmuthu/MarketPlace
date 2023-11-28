import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project 04 --Store
 *
 * This program models each store and includes the actions that can be performed to one
 *
 * @author Harini Muthu
 *
 * @version November 12, 2023
 *
 */

public class Store {
    ArrayList<Product> products = new ArrayList<Product>();
    private String sellerEmail;
    private String name;
    private int id;

    /**
     * constructor with products
     * @param sellerEmail
     * @param name
     * @param products
     * @param id
     */
    public Store(String sellerEmail, String name, ArrayList<Product> products, int id) {
        setSellerEmail(sellerEmail);
        this.name = name;
        this.products = products;
        this.id = id;
    }

    /**
     * constructor without products
     * @param sellerEmail
     * @param name
     */
    public Store(String sellerEmail, String name) {
        setSellerEmail(sellerEmail);
        this.name = name;
    }

    /**
     * Constructor with just store ID
     * @param id
     */
    public Store(int id) {
        this.id = id;
        this.name = "";
        this.sellerEmail = "";
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail.toLowerCase();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addProduct(Product product) throws Exception {
        products.add(product);
    }

    // get product using product
    public Product getProduct(Product product) {
        int index = products.indexOf(product);
        if (index == -1)
            return null;
        return products.get(index);
    }

    // get product using id
    public Product getProduct(int id) {
        return getProduct(new Product(id));
    }

    // get product using productName
    public Product getProduct(String productName) {
        for (Product p : products) {
            if (p.getName().equals(productName)) {
                return p;
            }
        }
        return null;
    }

    /**
     * delete product
     * @param id
     */
    public void deleteProduct(int id)  {
        Product p = getProduct(id);
        if (p == null)
            return;
        products.remove(p);
    }

    @Override
    public String toString() {
        return String.format("%5d | %s", getId(), getName());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Store)) {
            return false;
        }
        Store s = (Store)o;

        if (s.getId() == id && id != 0) {
            return true;
        }
        return s.getName().equals(this.name) && s.getSellerEmail().equals(this.sellerEmail);
    }

    /**
     * verify that product exists
     * @param scanner
     * @return
     */
    public Product verifyProduct(Scanner scanner) {
        System.out.println("Enter your product id: ");
        int productId = Integer.parseInt(scanner.nextLine());
        Product p = getProduct(productId);
        if (p != null) {
            return p;
        } else {
            System.out.println("Product does not exist. Please choose another product id");
        }
        return null;
    }

    /**
     * delete product
     * @param scanner
     */
    public void deleteProduct(Scanner scanner) {
        Product product = verifyProduct(scanner);
        if (product != null) {
            deleteProduct(product.getId());
        }
    }

    /**
     * creates a product
     * @param scanner
     * @return
     */
    public Product createProduct(Scanner scanner) {
        System.out.println("Enter your product name: ");
        String productName = scanner.nextLine();

        System.out.println("Enter the product description: ");
        String description = scanner.nextLine();

        System.out.println("Enter quantity of the product: ");
        int quantity = scanner.nextInt();

        System.out.println("Enter price of the product: ");
        double price = scanner.nextDouble();
        return createProduct(productName, description, quantity, price);
    }

    public Product createProduct(String productName, String description, int quantity, double price) {
        Product p = getProduct(productName);
        if (p == null) { //make sure that the product name is unique
            try {
                p = new Product(productName, description, quantity, price, 0);

                //want to add the product to the store productStore, which is inside sellerStores
                addProduct(p);
                return p;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Product name already exists. Please choose another product name");
        }
        return null;
    }

    /**
     * All actions perforbale by Store
     * @param mp
     */
    public void actions(ClothingMarketPlace mp) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<ShoppingItem> storeItems = mp.getShoppingItems(this);
        while (true) {
            if (storeItems.size() == 0) {
                System.out.println("You have no products");
            } else {
                System.out.println("Your products are: ");
                for (ShoppingItem item: storeItems) {
                   System.out.println(item);
                }
            }

            System.out.print("""
                            Enter [1] to create a product.
                            Enter [2] to modify a product.
                            Enter [3] to delete a product.
                            Enter [4] to sort your product by name (ascending).
                            Enter [5] to sort your product by name (descending).
                            Enter [6] to sort your product by price (ascending).
                            Enter [7] to sort your product by price (descending).
                            Enter [8] to back
                            """);
            try {
                int sellerChoice = scanner.nextInt();
                scanner.nextLine();

                boolean goBack = false;
                switch (sellerChoice) {
                    case 1 -> {
                        Product product = createProduct(scanner);
                        if (product != null) {
                            product.setId(mp.getProductCount());
                            mp.tickProductCount();
                        }
                    }
                    case 2 -> {
                        Product product = verifyProduct(scanner);
                        if (product != null) {
                            product.actions();
                        }
                    }
                    case 3 -> deleteProduct(scanner);
                    case 4 -> mp.sortByName(storeItems, true);
                    case 5 -> mp.sortByName(storeItems, false);
                    case 6 -> mp.sortByPrice(storeItems, true);
                    case 7 -> mp.sortByPrice(storeItems, false);
                    case 8 -> goBack = true;
                    default -> System.out.println("Invalid choice, please try again.");
                }
                if (goBack) {
                    break;
                }
            } catch (Exception e) { // if seller chooses a non-integer value
                e.printStackTrace();
                scanner.nextLine();
            }
        }
    }
}
