package com.app.creaseart.models;

import org.json.JSONObject;

public class VolleyErrorModel {
    boolean isResponseNeedToParse=false;
    String strMessage="";
    JSONObject mJsonObject =new JSONObject();

    public JSONObject getmJsonObject() {
        return mJsonObject;
    }

    public void setmJsonObject(JSONObject mJsonObject) {
        this.mJsonObject = mJsonObject;
    }

    public boolean isResponseNeedToParse() {
        return isResponseNeedToParse;
    }

    public void setResponseNeedToParse(boolean responseNeedToParse) {
        isResponseNeedToParse = responseNeedToParse;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }



}
