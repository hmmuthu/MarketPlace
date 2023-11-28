import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StoreTableModel extends AbstractTableModel {

    private static final String[] columnNames = {"Name"};
    private final ArrayList<Store> stores;

    public StoreTableModel() {
        this.stores = new ArrayList<>();
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void addElement(Store s) {
        // Adds the element in the last position in the list
        stores.add(s);
        fireTableRowsInserted(stores.size()-1, stores.size()-1);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return stores.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0: return stores.get(rowIndex).getName();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return columnNames[column];
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
