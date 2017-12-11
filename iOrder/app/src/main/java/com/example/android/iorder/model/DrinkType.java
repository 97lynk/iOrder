package com.example.android.iorder.model;

import java.io.Serializable;

/**
 * Created by anhtu on 11/9/2017.
 */

public class DrinkType implements Serializable {
    // mã loại thức uống
    private int drinkTypeID;
    // tên loại thức uống
    private String drinkTypeName;
    // url icon loại thức uống
    private String icon;

    public DrinkType(int drinkTypeID, String drinkTypeName, String icon) {
        this.drinkTypeID = drinkTypeID;
        this.drinkTypeName = drinkTypeName;
        this.icon = icon;
    }

    public int getDrinkTypeID() {
        return drinkTypeID;
    }

    public void setDrinkTypeID(int drinkTypeID) {
        this.drinkTypeID = drinkTypeID;
    }

    public String getDrinkTypeName() {
        return drinkTypeName;
    }

    public void setDrinkTypeName(String drinkTypeName) {
        this.drinkTypeName = drinkTypeName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
