package lk.crewx.pos.tm;

import javafx.scene.control.Button;

public class CartTm {

    private int id;
    private String name;
    private double unitPrice;
    private double qty;
    private double discount;
    private double subTotal;
    private Button btn;

    public CartTm(int id, String name, double unitPrice, double qty, double discount, double subTotal, Button btn) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.discount = discount;
        this.subTotal = subTotal;
        this.btn = btn;
        btn.setStyle("-fx-background-color: #ff9f43; ");


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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}

