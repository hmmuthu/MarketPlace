import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellerForm extends JDialog {
    private JPanel sellerPanel;
    private JTable storesTable;
    private JButton newStoreButton;
    private JButton signOffButton;
    private JButton passwordButton;
    private JButton productsButton;
    private JButton statsButton;
    private JButton importButton;
    private JButton exportButton;
    private Seller seller;
    private StoreTableModel storeTableModel;
    private ClothingMarketPlace marketPlace;

    public SellerForm(JFrame parent, Seller seller, ClothingMarketPlace marketPlace) {
        super(parent);
        setupLayout();
        marketPlace.initGui(this, "Seller", sellerPanel, 500, 500);
        this.storeTableModel = new StoreTableModel();
        this.marketPlace = marketPlace;
        this.seller = seller;

        for (Store s : seller.getStores()) {
            this.storeTableModel.addElement(s);
        }
        this.storesTable.setModel(this.storeTableModel);
        this.storesTable.getColumnModel().getColumn(0).setPreferredWidth(400);

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
        this.importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importAction();
            }
        });
        this.exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportAction();
            }
        });
        this.newStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newStoreAction();
            }
        });
        this.productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productsAction();
            }
        });
        this.statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statsAction();
            }
        });
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public StoreTableModel getStoreTableModel() {
        return storeTableModel;
    }

    public void setStoreTableModel(StoreTableModel storeTableModel) {
        this.storeTableModel = storeTableModel;
    }

    public ClothingMarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(ClothingMarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    private void changePasswordAction() {
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(null, pf, "Enter new password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok == JOptionPane.OK_OPTION) {
            seller.setPassword(new String(pf.getPassword()));
        }
    }

    private void importAction() {
        String fileName = JOptionPane.showInputDialog(null, "Enter file name to import", "import", JOptionPane.INFORMATION_MESSAGE);
        if (fileName == null) {
            return;
        }

        if (!seller.importProduct(marketPlace, fileName)) {
            JOptionPane.showMessageDialog(sellerPanel,
                    "import failed (incorrect format or file not found)",
                    "Import failed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(sellerPanel,
                    "Products successfully imported",
                    "Import success",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportAction() {
        String fileName = JOptionPane.showInputDialog(null, "Enter file name to export", "export", JOptionPane.INFORMATION_MESSAGE);
        if (fileName == null) {
            return;
        }

        if (!seller.exportProduct(fileName)) {
            JOptionPane.showMessageDialog(sellerPanel,
                    "cannot export the products",
                    "Export failed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(sellerPanel,
                    "Products successfully exported",
                    "Export success",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newStoreAction() {
        String storeName = JOptionPane.showInputDialog(null, "Enter your store name", "new store", JOptionPane.INFORMATION_MESSAGE);
        if (storeName == null) {
            return;
        }
        Store store = seller.createStore(storeName);
        if (store == null) {
            JOptionPane.showMessageDialog(sellerPanel,
                    "Store name already exists. Please try a different name",
                    "new store failed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            store.setId(marketPlace.getStoreCount());
            marketPlace.tickStoreCount();
            storeTableModel.addElement(store);
        }
    }

    private void productsAction() {
        if (storesTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(sellerPanel,
                    "one store need to be selected",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Store store = storeTableModel.getStores().get(storesTable.getSelectedRow());
        SellerProductForm sellerProductForm = new SellerProductForm(null, store, marketPlace);
        sellerProductForm.pack();
        sellerProductForm.setVisible(true);
    }

    private void statsAction() {
        if (storesTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(sellerPanel,
                    "one store need to be selected",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Store store = storeTableModel.getStores().get(storesTable.getSelectedRow());
        StoreStatsForm storeStats = new StoreStatsForm(null, store, marketPlace);
        storeStats.pack();
        storeStats.setVisible(true);
    }
    private void setupLayout() {
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        sellerPanel = new JPanel();
        sellerPanel.setLayout(new GridBagLayout());

        final JPanel headerPanel = new JPanel();
        sellerPanel.add(headerPanel);

        final JLabel headerLabel= new JLabel();
        headerLabel.setFont(defaultFont);
        headerLabel.setText("Stores");
        headerPanel.add(headerLabel);

        final JPanel tablePanel = new JPanel();
        sellerPanel.add(tablePanel);
        storesTable = new JTable();
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(storesTable);
        tablePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(15, 1));
        signOffButton = new JButton("Sign off");
        passwordButton = new JButton("Password");
        importButton = new JButton("Import");
        exportButton = new JButton("Export");
        newStoreButton = new JButton("New Store");
        productsButton = new JButton("Products");
        statsButton = new JButton("Stats");

        buttonPanel.add(new JPanel());
        buttonPanel.add(signOffButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(passwordButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(importButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(exportButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(newStoreButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(productsButton);
        buttonPanel.add(new JPanel());
        buttonPanel.add(statsButton);
        buttonPanel.add(new JPanel());

        sellerPanel.add(buttonPanel);
    }

}
