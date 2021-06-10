package com.pantrybuddy.activity;

public class UserProduct {
    private String productName;
    private String manufacturer;
    private String expiryDate;
    private int count;
    private String image;
    private String ingredients;
    private String servingSize;
    private boolean isAllergic;

    public UserProduct(String productName, String manufacturer, String expiryDate, int count, String image, String ingredients, String servingSize, boolean isAllergic ){
        this.productName = productName.trim();
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.count = count;
        this.image = image;
        this.ingredients = ingredients;
        this.servingSize = servingSize;
        this.isAllergic = isAllergic;
    }

    public int getCount() {
        return count;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getProductName() {
        return productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getImage(){return image;}

    public String getIngredients(){return ingredients;}

    public String getServingSize(){return servingSize;}

    public boolean isAllergic(){
        return isAllergic;
    }
}
