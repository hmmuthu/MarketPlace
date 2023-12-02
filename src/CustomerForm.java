import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CustomerForm extends JDialog {
    private JPanel customerPanel;
    private JTable productsTable;
    private JButton buyProductButton;
    private JButton signOffButton;
    private JButton passwordButton;
    private JButton sortByNameButton;
    private JButton sortByPriceButton;
    private JButton orderButton;
    private JButton searchButton;
    private JButton cartButton;
    private JButton listAllButton;

    private Customer customer;

    private CustomerItemTableModel customerItemTableModel;

    private ClothingMarketPlace marketPlace;

    private boolean sortByNameAscending = true;
    private boolean sortByPriceAscending = true;

    public CustomerForm(JFrame parent, Customer customer, ClothingMarketPlace marketPlace) {
        super(parent);
        setupLayout();
        marketPlace.initGui(this, "Customer", customerPanel, 500, 500);
        this.customerItemTableModel = new CustomerItemTableModel();
        this.customer = customer;
        this.marketPlace = marketPlace;

        ArrayList<ShoppingItem> storeItems = marketPlace.getShoppingItems();
        for (ShoppingItem shoppingItem : storeItems) {
            this.customerItemTableModel.addElement(shoppingItem);
        }
        this.productsTable.setModel(this.customerItemTableModel);
        this.productsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.productsTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        this.signOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordAction();
            }
        });
        this.searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchAction();
            }
        });
        this.sortByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByNameAction();
            }
        });
        this.sortByPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByPriceAction();
            }
        });
        this.buyProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyProductAction();
            }
        });
        this.cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartAction();
            }
        });
        this.listAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listAction();
            }
        });
        this.orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderAction();
            }
        });
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerItemTableModel getCustomerItemTableModel() {
        return customerItemTableModel;
    }

    public void setCustomerItemTableModel(CustomerItemTableModel customerItemTableModel) {
        this.customerItemTableModel = customerItemTableModel;
    }

    public ClothingMarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(ClothingMarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    public boolean isSortByNameAscending() {
        return sortByNameAscending;
    }

    public void setSortByNameAscending(boolean sortByNameAscending) {
        this.sortByNameAscending = sortByNameAscending;
    }

    public boolean isSortByPriceAscending() {
        return sortByPriceAscending;
    }

    public void setSortByPriceAscending(boolean sortByPriceAscending) {
        this.sortByPriceAscending = sortByPriceAscending;
    }

    private void changePasswordAction() {
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(null, pf, "Enter new password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            customer.setPassword(new String(pf.getPassword()));
        }
    }

    private void searchAction() {
        JComboBox searchType = new JComboBox();
        searchType.addItem("Name");
        searchType.addItem("Description");
        searchType.addItem("Store");
        JTextField searchField = new JTextField();
        Object[] message = {
                "Search By:", searchType,
                "Search:", searchField,
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Search product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ArrayList<ShoppingItem> searchedItems = null;
            ArrayList<ShoppingItem> items = marketPlace.getShoppingItems();
            switch (searchType.getSelectedIndex()) {
                case 0 -> searchedItems = customer.filterByName(searchField.getText(), items);
                case 1 -> searchedItems = customer.filterByDescription(searchField.getText(), items);
                case 2 -> searchedItems = customer.filterByStore(searchField.getText(), items);
            }

            if (searchedItems == null) {
                return;
            }

            this.customerItemTableModel = new CustomerItemTableModel();
            for (ShoppingItem shoppingItem : searchedItems) {
                customerItemTableModel.addElement(shoppingItem);
            }
            productsTable.setModel(customerItemTableModel);
            customerItemTableModel.fireTableDataChanged();
        }

    }

    private void sortByNameAction() {
        marketPlace.sortByName(customerItemTableModel.getShoppingItems(), sortByNameAscending);
        customerItemTableModel.fireTableDataChanged();
        sortByNameAscending = !sortByNameAscending;
    }

    private void sortByPriceAction() {
        marketPlace.sortByPrice(customerItemTableModel.getShoppingItems(), sortByPriceAscending);
        customerItemTableModel.fireTableDataChanged();
        sortByPriceAscending = !sortByPriceAscending;
    }

    private void buyProductAction() {
        if (productsTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(customerPanel,
                    "one product need to be selected",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        ShoppingItem selectedItem = customerItemTableModel.getShoppingItems().get(productsTable.getSelectedRow());
        String quanityString = JOptionPane.showInputDialog(
                null,
                String.format("Product Name: %s\nDescription: %s\nAvailable quantity: %d\nPrice: $%.02f\nEnter quantity to buy",
                        selectedItem.getProduct().getName(),
                        selectedItem.getProduct().getDescription(),
                        selectedItem.getProduct().getQuantity(),
                        selectedItem.getProduct().getPrice()),
                "Buy Product",
                JOptionPane.INFORMATION_MESSAGE);
        if (quanityString == null) {
            return;
        }
        int buyQuantity = 0;
        try {
            buyQuantity = Integer.parseInt(quanityString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(customerPanel,
                    "Quantity is invalid. Please try again",
                    "Buy product failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (buyQuantity > selectedItem.getProduct().getQuantity()) {
            JOptionPane.showMessageDialog(customerPanel,
                    "Quantity is too high. Please try again",
                    "Buy product failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Product cartProduct = new Product(
                    selectedItem.getProduct().getName(),
                    selectedItem.getProduct().getDescription(),
                    buyQuantity,
                    selectedItem.getProduct().getPrice(),
                    selectedItem.getProduct().getId());
            ShoppingItem cartItem = new ShoppingItem(cartProduct, selectedItem.getSellerName(), selectedItem.getStoreName());
            selectedItem.getProduct().removeQuantity(buyQuantity);
            customer.getCart().add(cartItem);
            JOptionPane.showMessageDialog(customerPanel,
                    "Successfully to cart",
                    "Buy product",
                    JOptionPane.INFORMATION_MESSAGE);
            customerItemTableModel.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cartAction() {
        CartForm cartForm = new CartForm(null, customer, marketPlace);
        cartForm.pack();
        cartForm.setVisible(true);
        //product could be removed from cart. so we need to refresh it
        customerItemTableModel.fireTableDataChanged();
    }

    private void listAction() {
        this.customerItemTableModel = new CustomerItemTableModel();
        for (ShoppingItem shoppingItem : marketPlace.getShoppingItems()) {
            customerItemTableModel.addElement(shoppingItem);
        }
        productsTable.setModel(customerItemTableModel);
        customerItemTableModel.fireTableDataChanged();
    }

    private void orderAction() {
        OrderForm orderForm = new OrderForm(null, customer, marketPlace);
        orderForm.pack();
        orderForm.setVisible(true);
    }

    private void setupLayout() {
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        customerPanel = new JPanel();
        customerPanel.setLayout(new GridBagLayout());

        final JPanel headerPanel = new JPanel();
        customerPanel.add(headerPanel);

        final JLabel headerLabel= new JLabel();
        headerLabel.setFont(defaultFont);
        headerPanel.add(headerLabel);

        final JPanel tablePanel = new JPanel();
        customerPanel.add(tablePanel);
        productsTable = new JTable();
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(productsTable);
        tablePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(19, 1));
        signOffButton = new JButton("Sign off");
        passwordButton = new JButton("Password");
        searchButton = new JButton("Search");
        listAllButton = new JButton("List All");
        orderButton = new JButton("Orders");
        buyProductButton = new JButton("Buy Product");
        sortByNameButton = new JButton("Sort By Name");
        sortByPriceButton = new JButton("Sort By Price");
        cartButton = new JButton("Cart");

        buttonPanel.add(new JPanel());
        buttonPanel.add(signOffButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(passwordButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(searchButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(listAllButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(orderButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(buyProductButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(sortByNameButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(sortByPriceButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(cartButton);
        buttonPanel.add(new JPanel());

        customerPanel.add(buttonPanel);
    }
}
