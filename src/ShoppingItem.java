/**
 * Project 04 --ShoppingItem
 *
 * This program models each Shopping Item
 *
 * @author Harini Muthu
 *
 * @version November 12, 2023
 *
 */

public class ShoppingItem {
    private Product product;
    private String sellerName;
    private String storeName;

    /**
     * new shopping item that takes all parameters
     * @param product
     * @param sellerName
     * @param storeName
     */
    public ShoppingItem(Product product, String sellerName, String storeName) {
        this.product = product;
        this.sellerName = sellerName;
        this.storeName = storeName;
    }

    /**
     * new shopping item that takes only product ID
     * @param productId
     */
    public ShoppingItem(int productId) {
        this.product = new Product(productId);
        sellerName = "";
        storeName = "";
    }

    /**
     * new shopping item that takes no parameters
     */
    public ShoppingItem() {
        this.product = new Product(0);
        sellerName = "";
        storeName = "";
    }

    // getters and setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ShoppingItem)) {
            return false;
        }

        ShoppingItem s = (ShoppingItem) o;
        if(s.getProduct().getId() == this.getProduct().getId()) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("%5d | %20s | %s", product.getId(), getStoreName(), product.toString());
    }
}