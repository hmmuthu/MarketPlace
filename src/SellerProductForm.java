import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SellerProductForm extends JDialog {
    private JPanel sellerProductPanel;
    private JTable productsTable;
    private JButton sortByNameButton;
    private JButton sortByPriceButton;
    private JButton newProductButton;
    private JButton modifyProductButton;
    private JButton deleteProductButton;

    private Store store;

    private SellerItemTableModel sellerItemTableModel;

    private ClothingMarketPlace marketPlace;

    private boolean sortByNameAscending = true;
    private boolean sortByPriceAscending = true;

    public SellerProductForm(JFrame parent, Store store, ClothingMarketPlace marketPlace) {
        super(parent);
        setupLayout();
        marketPlace.initGui(this, "Products", sellerProductPanel, 800, 500);
        this.marketPlace = marketPlace;
        this.store = store;
        this.sellerItemTableModel = new SellerItemTableModel();
        ArrayList<ShoppingItem> storeItems = marketPlace.getShoppingItems(store);
        for (ShoppingItem shoppingItem : storeItems) {
            this.sellerItemTableModel.addElement(shoppingItem);
        }
        this.productsTable.setModel(this.sellerItemTableModel);
        this.productsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        this.productsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
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
        this.newProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newProductAction();
            }
        });
        this.modifyProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyProductAction();
            }
        });
        this.deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProductAction();
            }
        });
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public SellerItemTableModel getSellerItemTableModel() {
        return sellerItemTableModel;
    }

    public void setSellerItemTableModel(SellerItemTableModel sellerItemTableModel) {
        this.sellerItemTableModel = sellerItemTableModel;
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

    private void sortByNameAction() {
        marketPlace.sortByName(sellerItemTableModel.getShoppingItems(), sortByNameAscending);
        sellerItemTableModel.fireTableDataChanged();
        sortByNameAscending = !sortByNameAscending;
    }

    private void sortByPriceAction() {
        marketPlace.sortByPrice(sellerItemTableModel.getShoppingItems(), sortByPriceAscending);
        sellerItemTableModel.fireTableDataChanged();
        sortByPriceAscending = !sortByPriceAscending;
    }

    private void newProductAction() {
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        Object[] message = {
                "Name:", nameField,
                "Description:", descriptionField,
                "Quantity:", quantityField,
                "Price:", priceField,
        };
        int option = JOptionPane.showConfirmDialog(null, message, "New product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String quantity = quantityField.getText();
            String price = priceField.getText();
            Product product;
            try {
                product = store.createProduct(name, description, Integer.parseInt(quantity), Double.parseDouble(price));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(sellerProductPanel,
                        "Quantity or Price value is invalid. Please try again",
                        "New product failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (product == null) {
                JOptionPane.showMessageDialog(sellerProductPanel,
                        "Product already exists or one of field is incorrect. Please try again",
                        "New product failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            product.setId(marketPlace.getProductCount());
            marketPlace.tickStoreCount();
            sellerItemTableModel.addElement(new ShoppingItem(product, store.getSellerEmail(), store.getName()));
            sellerItemTableModel.fireTableDataChanged();
        }
    }

    private void modifyProductAction() {
        if (productsTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(productsTable,
                    "one product need to be selected",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        ShoppingItem shoppingItem = sellerItemTableModel.getShoppingItems().get(productsTable.getSelectedRow());

        JTextField nameField = new JTextField(shoppingItem.getProduct().getName());
        JTextField descriptionField = new JTextField(shoppingItem.getProduct().getDescription());
        JTextField quantityField = new JTextField(Integer.toString(shoppingItem.getProduct().getQuantity()));
        JTextField priceField = new JTextField(Double.toString(shoppingItem.getProduct().getPrice()));
        Object[] message = {
                "Name:", nameField,
                "Description:", descriptionField,
                "Quantity:", quantityField,
                "Price:", priceField,
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Modify product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String quantity = quantityField.getText();
            String price = priceField.getText();
            Product product;
            try {
                shoppingItem.getProduct().setName(name);
                shoppingItem.getProduct().setDescription(description);
                shoppingItem.getProduct().setQuantity(Integer.parseInt(quantity));
                shoppingItem.getProduct().setPrice(Double.parseDouble(price));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(sellerProductPanel,
                        "One of the value is invalid. Please try again",
                        "Modify product failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            sellerItemTableModel.fireTableDataChanged();
        }
    }

    private void deleteProductAction() {
        if (productsTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(productsTable,
                    "one product need to be selected",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        ShoppingItem shoppingItem = sellerItemTableModel.getShoppingItems().get(productsTable.getSelectedRow());
        sellerItemTableModel.removeElement(shoppingItem);
        store.deleteProduct(shoppingItem.getProduct().getId());
        sellerItemTableModel.fireTableDataChanged();
    }

    private void setupLayout() {
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        sellerProductPanel = new JPanel();
        sellerProductPanel.setLayout(new GridBagLayout());

        final JPanel headerPanel = new JPanel();
        sellerProductPanel.add(headerPanel);

        final JLabel headerLabel= new JLabel();
        headerLabel.setFont(defaultFont);
        headerLabel.setText("Products");
        headerPanel.add(headerLabel);

        final JPanel tablePanel = new JPanel();
        sellerProductPanel.add(tablePanel);
        productsTable = new JTable();
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(productsTable);
        tablePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(11, 1));
        sortByNameButton = new JButton("Sort by Name");
        sortByPriceButton = new JButton("Sort by Price");
        newProductButton = new JButton("New Product");
        modifyProductButton = new JButton("Modify Product");
        deleteProductButton = new JButton("Delete Product");

        buttonPanel.add(new JPanel());
        buttonPanel.add(sortByNameButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(sortByPriceButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(newProductButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(modifyProductButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(new JPanel());

        sellerProductPanel.add(buttonPanel);
    }

}
