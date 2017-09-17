package com.app.creaseart.models;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelCoupans {

    private String CouponValue;

    private String CouponId;

    private String CouponExpiryDate;

    private String Coupon;

    public String getCouponValue ()
    {
        return CouponValue;
    }

    public void setCouponValue (String CouponValue)
    {
        this.CouponValue = CouponValue;
    }

    public String getCouponId ()
    {
        return CouponId;
    }

    public void setCouponId (String CouponId)
    {
        this.CouponId = CouponId;
    }

    public String getCouponExpiryDate ()
    {
        return CouponExpiryDate;
    }

    public void setCouponExpiryDate (String CouponExpiryDate)
    {
        this.CouponExpiryDate = CouponExpiryDate;
    }

    public String getCoupon ()
    {
        return Coupon;
    }

    public void setCoupon (String Coupon)
    {
        this.Coupon = Coupon;
    }

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 0;

}
