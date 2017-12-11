package com.example.android.iorder.model;

import java.io.Serializable;

/**
 * Created by anhtu on 11/9/2017.
 */

public class Drink implements Serializable{
    // mã loại thức uống
    private int drinkTypeID;

    // mã thức uống
    private int drinkID;
    // tên thức uống
    private String drinkName;
    // url icon thức uống
    private String icon;
    // giá bán thật sự
    private double unitPrice;

    public Drink(int drinkTypeID, int drinkID, String drinkName, String icon, double unitPrice) {
        this.drinkTypeID = drinkTypeID;
        this.drinkID = drinkID;
        this.drinkName = drinkName;
        this.icon = icon;
        this.unitPrice = unitPrice;
    }

    public int getDrinkTypeID() {
        return drinkTypeID;
    }

    public void setDrinkTypeID(int drinkTypeID) {
        this.drinkTypeID = drinkTypeID;
    }

    public int getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(int drinkID) {
        this.drinkID = drinkID;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
