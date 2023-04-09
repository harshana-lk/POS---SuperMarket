package lk.crewx.pos.tm;

import javafx.scene.control.Button;

public class SupplierTM {
    private int id;
    private String name;
    private String address;
    private String telephone;
    private Button btn;

    public SupplierTM() {
    }

    public SupplierTM(int id, String name, String address, String telephone, Button btn) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.setBtn(btn);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
