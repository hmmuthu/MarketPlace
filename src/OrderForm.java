import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrderForm extends JDialog {
    private JPanel orderPanel;
    private JTable productsTable;
    private JButton exportButton;

    private Customer customer;

    private CustomerItemTableModel customerItemTableModel;

    private ClothingMarketPlace marketPlace;

    public OrderForm(JFrame parent, Customer customer, ClothingMarketPlace marketPlace) {
        super(parent);
        setupLayout();
        marketPlace.initGui(this, "Orders", orderPanel, 500, 500);
        this.customerItemTableModel = new CustomerItemTableModel();
        this.customer = customer;
        this.marketPlace = marketPlace;

        ArrayList<ShoppingItem> storeItems = customer.getOrders();
        for (ShoppingItem shoppingItem : storeItems) {
            this.customerItemTableModel.addElement(shoppingItem);
        }
        this.productsTable.setModel(customerItemTableModel);
        this.productsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.productsTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        this.exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportAction();
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

    private void exportAction() {
        String fileName = JOptionPane.showInputDialog(null, "Enter file name to export", "export", JOptionPane.INFORMATION_MESSAGE);
        if (fileName == null) {
            return;
        }
        if (!this.customer.exportPurchaseHistory(fileName)) {
            JOptionPane.showMessageDialog(this.orderPanel,
                    "cannot export the purchase history",
                    "Export failed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(orderPanel,
                    "Purchase history successfully exported",
                    "Export success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setupLayout() {
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        orderPanel = new JPanel();
        orderPanel.setLayout(new GridBagLayout());

        final JPanel headerPanel = new JPanel();
        orderPanel.add(headerPanel);

        final JLabel headerLabel= new JLabel();
        headerLabel.setFont(defaultFont);
        headerLabel.setText("Orders");
        headerPanel.add(headerLabel);

        final JPanel tablePanel = new JPanel();
        orderPanel.add(tablePanel);
        productsTable = new JTable();
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(productsTable);
        tablePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));
        exportButton = new JButton("Export Purchase History");
        buttonPanel.add(exportButton);

        orderPanel.add(buttonPanel);
    }
}
