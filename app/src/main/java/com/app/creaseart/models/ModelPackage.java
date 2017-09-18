package com.app.creaseart.models;

import java.io.Serializable;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelPackage implements Serializable {
    private String bundleUnit;

    //private Item[] item;
    private String itemAttribute;
    private String itemName;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    private String discount;
    private boolean isSelected = false;

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }

    private String discountPrice;
    private String discountType;
    private String isDiscount;
    private String itemPrice;
    private String jsonArray;

    private String itemId;

    public String getItemAttribute() {
        return itemAttribute;
    }

    public void setItemAttribute(String itemAttribute) {
        this.itemAttribute = itemAttribute;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    private String bundlePrice;

    private String bundleId;

    private String bundleName;

    private String bundleColor;

    public String getBundleUnit() {
        return bundleUnit;
    }

    public void setBundleUnit(String bundleUnit) {
        this.bundleUnit = bundleUnit;
    }

   /* public Item[] getItem ()
    {
        return item;
    }

    public void setItem (Item[] item)
    {
        this.item = item;
    }*/

    public String getBundlePrice() {
        return bundlePrice;
    }

    public void setBundlePrice(String bundlePrice) {
        this.bundlePrice = bundlePrice;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getBundleColor() {
        return bundleColor;
    }

    public void setBundleColor(String bundleColor) {
        this.bundleColor = bundleColor;
    }

    private String packagePrice;

    private String packageName;

    //  private Bundle[] bundle;

    private String packageId;

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
/*
    public Bundle[] getBundle ()
    {
        return bundle;
    }

    public void setBundle (Bundle[] bundle)
    {
        this.bundle = bundle;
    }*/

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 0;

    public String getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(String jsonArray) {
        this.jsonArray = jsonArray;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
