package com.example.android.iorder.util;

import com.example.android.iorder.model.*;

import java.util.ArrayList;

// lớp giữ hầu hết dữ liệu trong suốt quá trình ứng dụng chạy
// TODO Context của ứng dụng
public class MyContext {

    public static ArrayList<Item> items = new ArrayList<>();
    public static ArrayList<Drink> drinks = new ArrayList<>();
    public static ArrayList<DrinkType> drinkTypes = new ArrayList<>();
    public static ArrayList<Table> tables = new ArrayList<>();

    // method tìm các item đã order theo drinkID
    public static Item findItemByID(int DrinkID){
        for (Item i: items) {
            if(i.getDrink().getDrinkID() == DrinkID)
                return i;
        }
        return null;
    }
}
