import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CustomerItemTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Store", "Name", "Price", "Quantity"};
    private final ArrayList<ShoppingItem> shoppingItems;

    public CustomerItemTableModel() {
        this.shoppingItems = new ArrayList<>();
    }

    public ArrayList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void addElement(ShoppingItem item) {
        shoppingItems.add(item);
    }
    public void removeElement(ShoppingItem item) {
        shoppingItems.remove(item);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return shoppingItems.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0: return shoppingItems.get(rowIndex).getStoreName();
            case 1: return shoppingItems.get(rowIndex).getProduct().getName();
            case 2: return String.format("$%.02f", shoppingItems.get(rowIndex).getProduct().getPrice());
            case 3: return shoppingItems.get(rowIndex).getProduct().getQuantity();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column <= 3) {
            return columnNames[column];
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
