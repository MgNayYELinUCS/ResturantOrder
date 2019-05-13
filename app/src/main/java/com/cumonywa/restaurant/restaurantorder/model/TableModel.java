package com.cumonywa.restaurant.restaurantorder.model;

public class TableModel {
    private String tableName;
    private int tableImage;
    private String waitername1;

    public TableModel(String tableName, int tableImage, String waitername) {
        this.tableName = tableName;
        this.tableImage = tableImage;
        this.waitername1 = waitername;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTableImage() {
        return tableImage;
    }

    public void setTableImage(int tableImage) {
        this.tableImage = tableImage;
    }

    public String getWaitername() {
        return waitername1;
    }

    public void setWaitername(String waitername) {
        this.waitername1 = waitername;
    }
}
