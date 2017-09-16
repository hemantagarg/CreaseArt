package com.app.creaseart.models;

import java.util.ArrayList;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelCategory {

    private String UserMobile;

    private String UserImage;

    private String PackageName;

    private String OrderId;

    private String UserName;

    private String UserEmail;

    public String getUserMobile ()
    {
        return UserMobile;
    }

    public void setUserMobile (String UserMobile)
    {
        this.UserMobile = UserMobile;
    }

    public String getUserImage ()
    {
        return UserImage;
    }

    public void setUserImage (String UserImage)
    {
        this.UserImage = UserImage;
    }

    public String getPackageName ()
    {
        return PackageName;
    }

    public void setPackageName (String PackageName)
    {
        this.PackageName = PackageName;
    }

    public String getOrderId ()
    {
        return OrderId;
    }

    public void setOrderId (String OrderId)
    {
        this.OrderId = OrderId;
    }

    public String getUserName ()
    {
        return UserName;
    }

    public void setUserName (String UserName)
    {
        this.UserName = UserName;
    }

    public String getUserEmail ()
    {
        return UserEmail;
    }

    public void setUserEmail (String UserEmail)
    {
        this.UserEmail = UserEmail;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 0;

}
