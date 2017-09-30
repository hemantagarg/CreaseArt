package com.app.creaseart.models;

import java.util.ArrayList;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelCategory {

    private String Status;

    private String Quantity;

    private String Time;

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    private String Zone;

    private String Date;

    private String OrderNo;

    private String Address;

    private String OrderId;

    private String userImage;

    private String userMobile;

    private String userName;

    private String userEmail;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getQuantity ()
    {
        return Quantity;
    }

    public void setQuantity (String Quantity)
    {
        this.Quantity = Quantity;
    }

    public String getTime ()
    {
        return Time;
    }

    public void setTime (String Time)
    {
        this.Time = Time;
    }

    public String getDate ()
    {
        return Date;
    }

    public void setDate (String Date)
    {
        this.Date = Date;
    }

    public String getOrderNo ()
    {
        return OrderNo;
    }

    public void setOrderNo (String OrderNo)
    {
        this.OrderNo = OrderNo;
    }

    public String getAddress ()
    {
        return Address;
    }

    public void setAddress (String Address)
    {
        this.Address = Address;
    }

    public String getOrderId ()
    {
        return OrderId;
    }

    public void setOrderId (String OrderId)
    {
        this.OrderId = OrderId;
    }

    public String getUserImage ()
    {
        return userImage;
    }

    public void setUserImage (String userImage)
    {
        this.userImage = userImage;
    }

    public String getUserMobile ()
    {
        return userMobile;
    }

    public void setUserMobile (String userMobile)
    {
        this.userMobile = userMobile;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getUserEmail ()
    {
        return userEmail;
    }

    public void setUserEmail (String userEmail)
    {
        this.userEmail = userEmail;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 0;

}
