package lk.crewx.pos.model;

import java.time.LocalDate;

public class Orders {
    private int id;
    private LocalDate date;
    private double total;
    private double discount;
    private double subtotal;
    private boolean paymentStatus;
    private double toPaid;
    private String name;

    public Orders(int id, LocalDate date, double total, double discount, double subtotal, boolean paymentStatus, double toPaid, String name) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.discount = discount;
        this.subtotal = subtotal;
        this.paymentStatus = paymentStatus;
        this.toPaid = (toPaid);
        this.setName(name);
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", date=" + date +
                ", total=" + total +
                ", discount=" + discount +
                ", subtotal=" + subtotal +
                ", paymentStatus=" + paymentStatus +
                ", toPaid=" + toPaid +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getToPaid() {
        return toPaid;
    }

    public void setToPaid(double toPaid) {
        this.toPaid = toPaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
