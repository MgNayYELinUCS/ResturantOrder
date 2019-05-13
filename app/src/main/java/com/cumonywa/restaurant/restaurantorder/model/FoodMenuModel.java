package com.cumonywa.restaurant.restaurantorder.model;

public class FoodMenuModel {
    String food_name;
    int food_qty;
    int price;
    String waitername;
    private boolean isSelected;
    String description;

    public FoodMenuModel(String food_name, int food_qty, int price, String waitername, boolean isSelected, String description) {
        this.food_name = food_name;
        this.food_qty = food_qty;
        this.price = price;
        this.waitername = waitername;
        this.isSelected = isSelected;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWaitername() {
        return waitername;
    }

    public void setWaitername(String waitername) {
        this.waitername = waitername;
    }

    public FoodMenuModel(){}
    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_qty() {
        return food_qty;
    }

    public void setFood_qty(int food_qty) {
        this.food_qty = food_qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public FoodMenuModel(String food_name, int food_qty, int price, String waitername,Boolean isSelected) {
        this.food_name = food_name;
        this.food_qty = food_qty;
        this.price = price;
        this.waitername = waitername;
        this.isSelected=isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public FoodMenuModel(String food_name, int price, int food_qty) {

        this.food_name = food_name;
        this.food_qty = food_qty;
        this.price = price;
    }
}