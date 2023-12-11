import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MarketPlaceServer {
    private ServerSocket server;
    public static int port = 9999;

    private ClothingMarketPlace marketPlace;

    public MarketPlaceServer() throws IOException {
        this.server = new ServerSocket(port);
        marketPlace = ClothingMarketPlace.loadMarketPlace();
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        MarketPlaceServer marketPlaceServer = new MarketPlaceServer();
        String hostName = InetAddress.getLocalHost().getHostName();
        InetAddress addr = InetAddress.getByName(hostName);
        System.out.println("Server running at " + addr.getHostAddress() + " port " + port);
        marketPlaceServer.run();
    }

    private void run() throws IOException, ClassNotFoundException {
        Gson gson = new GsonBuilder().create();
        Request req;

        while (true) {
            Socket socket = server.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            String message = (String)inputStream.readObject();
            req = gson.fromJson(message, Request.class);
            boolean terminate = processRequest(req, outputStream);
            inputStream.close();
            outputStream.close();
            socket.close();
            if (terminate) {
                break;
            }
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }

    private User getRequestUser(String email) {
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.addAll(this.marketPlace.getCustomerList());
        allUsers.addAll(this.marketPlace.getSellerList());
        int index = allUsers.indexOf(new User(email, null));

        if (index != -1) {
            return allUsers.get(index);
        }
        return null;
    }
    private void success_response(Gson gson, ObjectOutputStream outputStream) throws IOException {
        this.marketPlace.saveMarketPlace();
        Response response = new Response(true, "");
        outputStream.writeObject(gson.toJson(response));
    }

    private void success_response(Gson gson, String payload, ObjectOutputStream outputStream) throws IOException {
        this.marketPlace.saveMarketPlace();
        Response response = new Response(true, payload);
        outputStream.writeObject(gson.toJson(response));
    }

    private void failure_response(Gson gson, ObjectOutputStream outputStream, String error) throws IOException {
        Response response = new Response(false, "", error);
        outputStream.writeObject(gson.toJson(response));
    }
    private boolean processRequest(Request req, ObjectOutputStream outputStream) throws IOException {
        User user = null;
        Seller seller = null;
        Customer customer = null;
        if (req.getEmail() != null) {
            user = getRequestUser(req.getEmail());
            if (user instanceof Seller) {
                seller = (Seller) user;
            }
            if (user instanceof Customer) {
                customer = (Customer) user;
            }
        }

        Gson gson = new GsonBuilder().create();
        switch (req.getOperation()) {
            case TERMINATE -> {
                return true;
            }
            case TICK_STORE_COUNT -> {
                tickStoreCount(req, outputStream);
            }
            case TICK_PRODUCT_COUNT -> {
                tickProductCount(req, outputStream);
            }
            case LOAD_MARKET_PLACE -> {
                loadMarketPlace(outputStream);
            }
            case SIGNUP -> {
                signup(req, user, outputStream);
            }
            case CHANGE_PASSWORD -> {
                changePassword(req, user, outputStream);
            }
            case ADD_TO_CART -> {
                if (customer == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                addToCart(req, customer, outputStream);
            }
            case REMOVE_FROM_CART -> {
                if (customer == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                removeFromCart(req, customer, outputStream);
            }
            case CHECKOUT -> {
                if (customer == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                checkout(req, customer, outputStream);
            }
            case IMPORT_PRODUCT -> {
                if (seller == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                importProduct(req, seller, outputStream);
            }
            case NEW_STORE -> {
                if (seller == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                newStore(req, seller, outputStream);
            }

            case NEW_PRODUCT -> {
                if (seller == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                newProduct(req, seller, outputStream);
            }
            case MODIFY_PRODUCT -> {
                if (seller == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                modifyProduct(req, seller, outputStream);
            }
            case DELETE_PRODUCT -> {
                if (seller == null) {
                    failure_response(gson, outputStream,"user not found");
                    break;
                }
                deleteProduct(req, seller, outputStream);
            }
        }
        return false;
    }

    private void tickStoreCount(Request req, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        String payload = Integer.toString(this.marketPlace.getStoreCount());
        this.marketPlace.tickStoreCount();
        success_response(gson, payload, outputStream);
    }

    private void tickProductCount(Request req, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        String payload = Integer.toString(this.marketPlace.getProductCount());
        this.marketPlace.tickProductCount();
        success_response(gson, payload, outputStream);
    }

    private void loadMarketPlace(ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        success_response(gson, gson.toJson(this.marketPlace), outputStream);
    }
    private void signup(Request req, User user, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        if (user != null) {
            failure_response(gson, outputStream,"Email is taken, please enter a different email.");
            return;
        }

        User userFromPayload = gson.fromJson(req.getPayload(), User.class);
        if (userFromPayload.isCustomer()) {
            Customer newCustomer = new Customer(userFromPayload.getEmail(), userFromPayload.getPassword());
            this.marketPlace.getCustomerList().add(newCustomer);
        } else {
            Seller newSeller = new Seller(userFromPayload.getEmail(), userFromPayload.getPassword());
            this.marketPlace.getSellerList().add(newSeller);
        }
        success_response(gson, outputStream);
    }

    private void changePassword(Request req, User user, ObjectOutputStream outputStream) throws IOException {
        Response response;
        Gson gson = new GsonBuilder().create();
        User userFromPayload = gson.fromJson(req.getPayload(), User.class);

        if (user == null) {
            failure_response(gson, outputStream,"Cannot find the user.");
            return;
        }

        user.setPassword(userFromPayload.getPassword());
        success_response(gson, outputStream);
    }

    private void addToCart(Request req, Customer customer, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        ShoppingItem itemFromPayload = gson.fromJson(req.getPayload(), ShoppingItem.class);
        ArrayList<ShoppingItem> marketPlaceItems = marketPlace.getShoppingItems();
        int index = marketPlaceItems.indexOf(new ShoppingItem(itemFromPayload.getProduct().getId()));
        if (index == -1) {
            failure_response(gson, outputStream,"product not found");
            return;
        }

        ShoppingItem selectedItem = marketPlace.getShoppingItems().get(index);
        if (selectedItem.getProduct().getQuantity() < itemFromPayload.getProduct().getQuantity()) {
            failure_response(gson, outputStream, "quantity too high, please try again");
            return;
        }

        try {
            selectedItem.getProduct().removeQuantity(itemFromPayload.getProduct().getQuantity());
        } catch (Exception e) {
            failure_response(gson, outputStream, "exception when removing quantity");
            e.printStackTrace();
            return;
        }
        customer.getCart().add(itemFromPayload);
        success_response(gson, outputStream);
    }

    private void removeFromCart(Request req, Customer customer, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        ShoppingItem itemFromPayload = gson.fromJson(req.getPayload(), ShoppingItem.class);
        ArrayList<ShoppingItem> marketPlaceItems = marketPlace.getShoppingItems();
        int index = marketPlaceItems.indexOf(new ShoppingItem(itemFromPayload.getProduct().getId()));
        if (index == -1) {
            failure_response(gson, outputStream, "product not found");
            return;
        }
        customer.removeFromCart(itemFromPayload, marketPlaceItems);
        success_response(gson, outputStream);
    }

    private void checkout(Request req, Customer customer, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        customer.checkOut();
        success_response(gson, outputStream);
    }

    private void importProduct(Request req, Seller seller, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        Seller sellerFromPayload  = gson.fromJson(req.getPayload(), Seller.class);
        seller.setStores(sellerFromPayload.getStores());
        success_response(gson, outputStream);
    }

    private void newStore(Request req, Seller seller, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        Store store = gson.fromJson(req.getPayload(), Store.class);
        seller.addStore(store);
        success_response(gson, outputStream);
    }

    private void newProduct(Request req, Seller seller, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        ShoppingItem shoppingItem = gson.fromJson(req.getPayload(), ShoppingItem.class);
        int index = seller.getStores().indexOf(new Store(shoppingItem.getSellerName(), shoppingItem.getStoreName()));
        if (index == -1) {
            failure_response(gson, outputStream,"cannot find the store");
            return;
        }
        Store store = seller.getStores().get(index);
        try {
            store.addProduct(shoppingItem.getProduct());
        } catch (Exception e) {
            failure_response(gson, outputStream, "exception when adding product");
            e.printStackTrace();
            return;
        }
        success_response(gson, outputStream);
    }

    private void modifyProduct(Request req, Seller seller, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        ShoppingItem itemFromPayload = gson.fromJson(req.getPayload(), ShoppingItem.class);
        int index = seller.getStores().indexOf(new Store(itemFromPayload.getSellerName(), itemFromPayload.getStoreName()));
        if (index == -1) {
            failure_response(gson, outputStream,"cannot find the store");
            return;
        }
        Store store = seller.getStores().get(index);
        index = store.getProducts().indexOf(new Product(itemFromPayload.getProduct().getId()));
        if (index == -1) {
            failure_response(gson, outputStream,"cannot find the product");
            return;
        }
        Product product = store.getProducts().get(index);
        try {
            product.setName(itemFromPayload.getProduct().getName());
            product.setDescription(itemFromPayload.getProduct().getDescription());
            product.setQuantity(itemFromPayload.getProduct().getQuantity());
            product.setPrice(itemFromPayload.getProduct().getPrice());
        } catch (Exception e) {
            failure_response(gson, outputStream, "exception when modifying product");
            e.printStackTrace();
            return;
        }
        success_response(gson, outputStream);
    }

    private void deleteProduct(Request req, Seller seller, ObjectOutputStream outputStream) throws IOException {
        Gson gson = new GsonBuilder().create();
        ShoppingItem itemFromPayload = gson.fromJson(req.getPayload(), ShoppingItem.class);
        int index = seller.getStores().indexOf(new Store(itemFromPayload.getSellerName(), itemFromPayload.getStoreName()));
        if (index == -1) {
            failure_response(gson, outputStream,"cannot find the store");
            return;
        }
        Store store = seller.getStores().get(index);
        store.deleteProduct(itemFromPayload.getProduct().getId());
        success_response(gson, outputStream);
    }
}