package lk.crewx.pos.tm;

import javafx.scene.control.Button;

import java.time.LocalDate;

public class OrderTM {
    private String id;
    private LocalDate date;
    private double price;
    private double toPaid;
    private Button btn;

    public OrderTM(String id, LocalDate date, double price, Button btn, double toPaid) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.btn = btn;
        this.setToPaid(toPaid);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public double getToPaid() {
        return toPaid;
    }

    public void setToPaid(double toPaid) {
        this.toPaid = toPaid;
    }
}