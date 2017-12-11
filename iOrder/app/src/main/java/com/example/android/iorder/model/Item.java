package com.example.android.iorder.model;

import java.io.Serializable;

/**
 * Created by ASUS on 16-Oct-16.
 */
public class Item implements Serializable {
    // mã hóa đơn
    private int billID;

    // tên loại thức uống
    private String drinkTypeName;

    // số lượng cần mua
    private int amount;
    // thành tiền: total = amount * unitPrice
    private double total;

    // thức uống trên item
    private Drink drink;

    public Item(Drink drink, String drinkTypeName, int amount, double total) {
        this.drink =drink;
        this.drinkTypeName = drinkTypeName;
        this.amount = amount;
        this.total = total;
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getDrinkTypeName() {
        return drinkTypeName;
    }

    public void setDrinkTypeName(String drinkTypeName) {
        this.drinkTypeName = drinkTypeName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
