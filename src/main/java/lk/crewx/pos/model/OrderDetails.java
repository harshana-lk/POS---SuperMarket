package lk.crewx.pos.model;

public class OrderDetails {
    private double discount;
    private double subTotal;
    private double price;
    private double qty;
    private int oID;
    private int itemID;

    public OrderDetails(double price, double qty, int oID, int itemID, double discount, double subTotal) {
        this.setPrice(price);
        this.setQty(qty);
        this.setoID(oID);
        this.setItemID(itemID);
        this.discount = discount;
        this.subTotal = subTotal;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public int getoID() {
        return oID;
    }

    public void setoID(int oID) {
        this.oID = oID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}