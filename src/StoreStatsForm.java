import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StoreStatsForm extends JDialog {
    private JPanel storeStatsPanel;
    private JTable productsTable;
    private JLabel storeStatsHeader;

    private Store store;

    private SellerItemTableModel sellerItemTableModel;

    private ClothingMarketPlace marketPlace;

    public StoreStatsForm(JFrame parent, Store store, ClothingMarketPlace marketPlace) {
        super(parent);
        setupLayout();
        marketPlace.initGui(this, "Stats", storeStatsPanel, 500, 500);
        this.sellerItemTableModel = new SellerItemTableModel();
        this.store = store;
        this.marketPlace = marketPlace;
        this.storeStatsHeader.setText(store.getName() + " Stats");

        ArrayList<ShoppingItem> storeItems = marketPlace.getSoldItems();
        for (ShoppingItem shoppingItem : storeItems) {
            this.sellerItemTableModel.addElement(shoppingItem);
        }
        this.productsTable.setModel(this.sellerItemTableModel);
        this.productsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.productsTable.getColumnModel().getColumn(1).setPreferredWidth(300);
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
    private void setupLayout() {
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        storeStatsPanel = new JPanel();
        storeStatsPanel.setLayout(new GridBagLayout());

        final JPanel headerPanel = new JPanel();
        storeStatsPanel.add(headerPanel);

        storeStatsHeader = new JLabel();
        storeStatsHeader.setFont(defaultFont);
        headerPanel.add(storeStatsHeader);

        final JPanel tablePanel = new JPanel();
        storeStatsPanel.add(tablePanel);
        productsTable = new JTable();
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(productsTable);
        tablePanel.add(scrollPane);
    }

}
