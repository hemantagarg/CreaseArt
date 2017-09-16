package com.app.creaseart.models;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelFamiyMember {

    private String Name;

    private String UserType;

    private String Email;

    private String IsActive;

    private String Mobile;

    private String IsLogin;

    private String ProfilePic;

    private String DeviceToken;

    private String memberId;



    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getUserType ()
    {
        return UserType;
    }

    public void setUserType (String UserType)
    {
        this.UserType = UserType;
    }

    public String getEmail ()
    {
        return Email;
    }

    public void setEmail (String Email)
    {
        this.Email = Email;
    }

    public String getIsActive ()
    {
        return IsActive;
    }

    public void setIsActive (String IsActive)
    {
        this.IsActive = IsActive;
    }

    public String getMobile ()
    {
        return Mobile;
    }

    public void setMobile (String Mobile)
    {
        this.Mobile = Mobile;
    }

    public String getIsLogin ()
    {
        return IsLogin;
    }

    public void setIsLogin (String IsLogin)
    {
        this.IsLogin = IsLogin;
    }

    public String getProfilePic ()
    {
        return ProfilePic;
    }

    public void setProfilePic (String ProfilePic)
    {
        this.ProfilePic = ProfilePic;
    }

    public String getDeviceToken ()
    {
        return DeviceToken;
    }

    public void setDeviceToken (String DeviceToken)
    {
        this.DeviceToken = DeviceToken;
    }

    public String getMemberId ()
    {
        return memberId;
    }

    public void setMemberId (String memberId)
    {
        this.memberId = memberId;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 0;

}
