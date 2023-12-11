import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ClothingMarketPlace {

    private static final String CLOTHING_MARKET_PLACE_FILE = "ClothingMarketPlaceFile.json";
    private ArrayList<Seller> sellerList = new ArrayList<>();
    private ArrayList<Customer> customerList = new ArrayList<>();
    private int productCount;
    private int storeCount;

    private boolean client;

    // constructor
    public ClothingMarketPlace() {
        this.productCount = 1;
        this.storeCount = 1;
    }

    // getters and setters
    public int getProductCount() {
        return productCount;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public ArrayList<Seller> getSellerList() {
        return sellerList;
    }

    public ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean isClient() {
        return client;
    }

    public void tickProductCount() {
        this.productCount++;
    }

    public void tickStoreCount() {
        this.storeCount++;
    }

    public void addUser(User u) {
        if (u instanceof Customer) {
            if (!customerList.contains(u)) {
                customerList.add((Customer) u);
            }
        } else {
            if (!sellerList.contains(u)) {
                sellerList.add((Seller) u);
            }
        }
    }

    public ArrayList<ShoppingItem> getShoppingItems() {
        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();

        for (Seller seller : sellerList) {
            for (Store store : seller.getStores()) {
                for (Product product : store.getProducts()) {
                    shoppingItems.add(new ShoppingItem(product, seller.getEmail(), store.getName()));
                }
            }
        }

        return shoppingItems;
    }

    public ArrayList<ShoppingItem> getShoppingItems(Store store) {
        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
        for (Product product : store.getProducts()) {
            shoppingItems.add(new ShoppingItem(product, store.getSellerEmail(), store.getName()));
        }
        return shoppingItems;
    }

    public ArrayList<ShoppingItem> getSoldItems() {
        ArrayList<ShoppingItem> soldItems = new ArrayList<>();
        for (Customer c : getCustomerList()) {
            soldItems.addAll(c.getOrders());
        }
        return soldItems;
    }

    public ArrayList<ShoppingItem> getSoldItems(Store store) {
        ArrayList<ShoppingItem> soldItems = getSoldItems();
        for (ShoppingItem item : soldItems) {
            if (item.getStoreName().equals(store.getSellerEmail()) && item.getStoreName().equals(store.getName())) {
                soldItems.add(item);
            }
        }
        return soldItems;
    }

    public ArrayList<Store> getStores() {
        ArrayList<Store> stores = new ArrayList<>();

        for (Seller seller : sellerList) {
            stores.addAll(seller.getStores());
        }
        return stores;
    }

    public static ClothingMarketPlace loadMarketPlace() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ClothingMarketPlace mp = null;

        System.out.println("Loading market place...");
        try {
            FileReader reader = new FileReader(CLOTHING_MARKET_PLACE_FILE);
            mp = gson.fromJson(reader, ClothingMarketPlace.class);
            reader.close();
            System.out.println("Loading complete");
        } catch (IOException e) {
            System.out.println("market place file not found");
        }
        if (mp == null){
            System.out.println("Creating new market place");
            mp = new ClothingMarketPlace();
        }
        return mp;
    }

    public static ClothingMarketPlace loadMarketPlaceFromServer() {
        Request request = new Request(Operation.LOAD_MARKET_PLACE);
        Response response = sendRequest(request);
        Gson gson = new GsonBuilder().create();
        ClothingMarketPlace mp = gson.fromJson(response.getPayload(), ClothingMarketPlace.class);
        mp.setClient(true);
        return mp;
    }

    public void saveMarketPlace() {
        if (isClient()) {
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(CLOTHING_MARKET_PLACE_FILE);
            gson.toJson(this, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listShoppingItem(ArrayList<ShoppingItem> shoppingItems) {
        if (shoppingItems.size() == 0) {
            System.out.println("There are no shopping items!");
            return;
        }
        System.out.println("Shopping Item: ");
        for (ShoppingItem s: shoppingItems) {
            System.out.println(s);
        }
    }

    public void listStores(ArrayList<Store> stores) {
        if (stores.size() == 0) {
            System.out.println("There are no stores!");
            return;
        }

        System.out.println("Stores: ");
        for (Store s: stores) {
            System.out.println(s);
        }
    }

    public void sortByName(ArrayList<ShoppingItem> shoppingItems, boolean ascending) {
        Collections.sort(shoppingItems, new Comparator<ShoppingItem>() {
            public int compare(ShoppingItem o1, ShoppingItem o2)
            {
                if (ascending)
                    return o1.getProduct().getName().compareTo(o2.getProduct().getName());
                else
                    return o2.getProduct().getName().compareTo(o1.getProduct().getName());
            }
        });
    }

    public void sortByPrice(ArrayList<ShoppingItem> shoppingItems, boolean ascending) {
        Collections.sort(shoppingItems, new Comparator<ShoppingItem>() {
            public int compare(ShoppingItem o1, ShoppingItem o2)
            {
                Double d1 = o1.getProduct().getPrice();
                Double d2 = o2.getProduct().getPrice();
                if (ascending)
                    return d1.compareTo(d2);
                else
                    return d2.compareTo(d1);
            }
        });
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--cli")) {
            fireCommandLine();
        } else if (args.length > 0 && args[0].equals("--no_client")) {
            fireGui(false);
        } else {
            fireGui(true);
        }
    }

    public static void fireGui(boolean client) {
        ClothingMarketPlace mp;

        if (!client) {
            mp = loadMarketPlace();
        } else {
            mp = loadMarketPlaceFromServer();
        }

        while (true) {
            LoginForm loginForm = new LoginForm(null, mp);
            //loginForm.pack();
            loginForm.setVisible(true);

            User logInUser = loginForm.getLoginUser();
            if (logInUser == null) {
                mp.saveMarketPlace();
                return;
            }
            mp.addUser(logInUser);
            if (logInUser instanceof Customer) {
                Customer newCustomer = (Customer) logInUser;
                newCustomer.actionsGui(mp);

            } else {
                Seller newSeller = (Seller) logInUser;
                newSeller.actionsGui(mp);
            }
            mp.saveMarketPlace();
        }
    }

    public static void fireCommandLine() {
        ClothingMarketPlace mp = loadMarketPlace();
        while (true) {
            ArrayList<User> allUsers = new ArrayList<>();
            allUsers.addAll(mp.getCustomerList());
            allUsers.addAll(mp.getSellerList());
            User logInUser = User.authentication(allUsers);

            if (logInUser == null) {
                mp.saveMarketPlace();
                System.out.println("Thanks for using the Clothing Market Place\nGoodbye!");
                return;
            }

            mp.addUser(logInUser);
            if (logInUser instanceof Customer) {
                Customer newCustomer = (Customer) logInUser;
                System.out.println("Welcome Customer " + newCustomer.getEmail());
                newCustomer.actionsCommandLine(mp);
            } else {
                Seller newSeller = (Seller) logInUser;
                System.out.println("Welcome Seller " + newSeller.getEmail());
                newSeller.actionsCommandLine(mp);
            }

            mp.saveMarketPlace();
        }
    }

    public void initGui(JDialog dialog, String title, JPanel panel, int width, int height) {
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
    }

    public static Response sendRequest(Request request) {
        try {
            Gson gson = new GsonBuilder().create();
            Socket socket = new Socket(getServerHost(false), MarketPlaceServer.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(gson.toJson(request));
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) objectInputStream.readObject();
            return gson.fromJson(message, Response.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServerHost(boolean local) throws UnknownHostException {
        if (local) {
            InetAddress host = InetAddress.getLocalHost();
            return host.getHostName();
        } else {
            String hostName = InetAddress.getLocalHost().getHostName();
            InetAddress addr = InetAddress.getByName(hostName);
            return addr.getHostAddress();
        }
    }
}