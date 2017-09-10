package com.app.creaseart.interfaces;

import org.json.JSONObject;

/**
 * Created by seocor1 on 11/7/2016.
 */
public interface ApiResponse {

    public void onPostSuccess(int method, JSONObject response);

    public void onPostFail(int method, String response);

}
