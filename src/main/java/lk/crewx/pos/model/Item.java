package lk.crewx.pos.model;

public class Item {
    private int id;
    private String name;
    private double sellingPrice;
    private int supplierID;
    private int category;
    private double qty;
    private double discount;

    public Item(int id, String name, double sellingPrice, int supplierID, int category, double qty, double discount) {
        this.id = id;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.supplierID = supplierID;
        this.category = category;
        this.qty = qty;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}