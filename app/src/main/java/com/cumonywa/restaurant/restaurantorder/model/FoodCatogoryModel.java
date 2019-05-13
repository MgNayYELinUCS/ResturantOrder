package com.cumonywa.restaurant.restaurantorder.model;

public class FoodCatogoryModel {
    int foodId;
    String foodName;
    String tableName;
    String waiterName;

    public FoodCatogoryModel(int foodId, String foodName, String tableName, String waiterName) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.tableName = tableName;
        this.waiterName = waiterName;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }
}
