package com.yberry.dinehawaii.Bussiness.model;


public class CustomizationModel {

    private String ItemId;
    private String ItemName;

    public CustomizationModel(String itemId, String itemName) {
        ItemId = itemId;
        ItemName = itemName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemId() {

        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }
}
