import javax.swing.*;
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
        marketPlace.initGui(this, "Stats", storeStatsPanel,500, 500);
        this.sellerItemTableModel = new SellerItemTableModel();
        this.store = store;
        this.marketPlace = marketPlace;
        this.storeStatsHeader.setText(store.getName() + " Stats");

        ArrayList<ShoppingItem> storeItems = marketPlace.getSoldItems();
        for (ShoppingItem shoppingItem: storeItems) {
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
}
