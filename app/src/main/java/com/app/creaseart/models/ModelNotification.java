package com.app.creaseart.models;

/**
 * Created by hemanta on 30-08-2017.
 */

public class ModelNotification {


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType;
    private String Message;

    private String DateTime;

    private String SenderId;

    private String SenderName;

    private String NotificationId;

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public String getDateTime ()
    {
        return DateTime;
    }

    public void setDateTime (String DateTime)
    {
        this.DateTime = DateTime;
    }

    public String getSenderId ()
    {
        return SenderId;
    }

    public void setSenderId (String SenderId)
    {
        this.SenderId = SenderId;
    }

    public String getSenderName ()
    {
        return SenderName;
    }

    public void setSenderName (String SenderName)
    {
        this.SenderName = SenderName;
    }

    public String getNotificationId ()
    {
        return NotificationId;
    }

    public void setNotificationId (String NotificationId)
    {
        this.NotificationId = NotificationId;
    }


}
