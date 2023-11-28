import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CartForm extends JDialog {
    private JPanel cartPanel;
    private JTable productsTable;
    private JButton removeProductButton;
    private JButton checkoutButton;
    private JLabel total;

    private Customer customer;

    private CustomerItemTableModel customerItemTableModel;

    private ClothingMarketPlace marketPlace;

    public CartForm(JFrame parent, Customer customer, ClothingMarketPlace marketPlace) {
        super(parent);
        marketPlace.initGui(this, "Cart", cartPanel,500, 500);
        this.customerItemTableModel = new CustomerItemTableModel();
        this.customer = customer;
        this.marketPlace = marketPlace;

        ArrayList<ShoppingItem> storeItems = customer.getCart();
        for (ShoppingItem shoppingItem: storeItems) {
            this.customerItemTableModel.addElement(shoppingItem);
        }
        this.productsTable.setModel(this.customerItemTableModel);
        this.productsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.productsTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        this.total.setText('$' + String.format("%.2f", customer.getTotal()));
        this.checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutAction();
            }
        });
        this.removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProductAction();
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

    void checkoutAction() {
        if (customer.getCart().size() == 0) {
            JOptionPane.showMessageDialog(cartPanel,
                    "No products to checkout",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = customer.checkOut();
        JOptionPane.showMessageDialog(
                cartPanel,
                String.format("You have been charged $%.2f\nAll items in the cart has been bought", total),
                "Checkout",
                JOptionPane.INFORMATION_MESSAGE);
        this.customerItemTableModel = new CustomerItemTableModel();
        productsTable.setModel(customerItemTableModel);
        customerItemTableModel.fireTableDataChanged();
        this.total.setText("$0.00");
        dispose();
    }

    void removeProductAction() {
        if (productsTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(cartPanel,
                    "one product need to be selected",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        ShoppingItem selectedItem = customerItemTableModel.getShoppingItems().get(productsTable.getSelectedRow());
        customerItemTableModel.removeElement(selectedItem);
        customer.removeFromCart(selectedItem, marketPlace.getShoppingItems());
        total.setText('$' + String.format("%.2f", customer.getTotal()));
        customerItemTableModel.fireTableDataChanged();
    }
}
