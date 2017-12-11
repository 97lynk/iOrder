package com.example.android.iorder.model;

/**
 * Created by anhtu on 11/10/2017.
 */

public class Table {
    // mã bàn
    private int tableID;
    // tên bàn
    private String tableName;

    public Table(int tableID, String tableName) {
        this.tableID = tableID;
        this.tableName = tableName;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
