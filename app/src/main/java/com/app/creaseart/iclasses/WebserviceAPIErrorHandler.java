package com.app.creaseart.iclasses;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.creaseart.R;
import com.app.creaseart.models.VolleyErrorModel;
import com.app.creaseart.utils.AppUtils;

/****************************************************************
 * WebserviceAPIErrorHandler.java
  ****************************************************************/

public class WebserviceAPIErrorHandler {
    /**
     * Instance of This class
     */
    public static WebserviceAPIErrorHandler mErrorHandler;
    /**
     * Debugging TAG
     */
    private String TAG = WebserviceAPIErrorHandler.class.getSimpleName();

    private WebserviceAPIErrorHandler() {
    }

    /**
     * Get Instance of this class
     *
     * @return
     */
    public static WebserviceAPIErrorHandler getInstance() {
        if (mErrorHandler == null)
            mErrorHandler = new WebserviceAPIErrorHandler();
        return mErrorHandler;

    }

    /**
     * Volley Error Handler for only driver list as on error, user should be
     * navigated back to the map screen
     *
     * @param mError
     * @param mActivity
     */
    public VolleyErrorModel VolleyErrorHandlerReturningModel(VolleyError mError,
                                                             Context mActivity) {
        VolleyErrorModel mVolleyErrorModel = new VolleyErrorModel();
        String error_message = mActivity.getString(R.string.some_error_occurred);
        AppUtils.showErrorLog(TAG, "VolleyError :" + mError);
        if (mError instanceof NoConnectionError) {
            error_message = mActivity.getResources().getString(
                    R.string.no_internet_connection);
        } else if (mError instanceof TimeoutError) {
            error_message = mActivity.getResources().getString(
                    R.string.timeout);
        } else if (mError instanceof AuthFailureError) {
        } else if (mError instanceof ServerError) {
        } else if (mError instanceof NetworkError) {
            error_message = mActivity.getResources().getString(
                    R.string.alert_internet_connection);
        } else if (mError instanceof ParseError) {
        }
        //get response body and parse with appropriate encoding
/*
        if (mError.networkResponse.data != null) {
            try {
                String response = new String(mError.networkResponse.data, "UTF-8");
                if(!response.isEmpty()){
                    mVolleyErrorModel.setResponseNeedToParse(true);
                    mVolleyErrorModel.setmJsonObject(new JSONObject(response));
                }
            } catch (UnsupportedEncodingException e) {
                AppUtils.showErrorLog(TAG," Exception : "+e);
            }catch (JSONException e) {
                AppUtils.showErrorLog(TAG," Exception : "+e);

            }
        }
*/
        mVolleyErrorModel.setStrMessage(error_message);
        return mVolleyErrorModel;
    }

    /**
     * Volley Error Handler for only driver list as on error, user should be
     * navigated back to the map screen
     *
     * @param mError
     * @param mActivity
     */
    public String VolleyErrorHandlerReturningString(VolleyError mError,
                                                    Context mActivity) {
        String error_message = mActivity.getString(R.string.some_error_occurred);
        AppUtils.showErrorLog(TAG, "VolleyError :" + mError);
        if (mError instanceof NoConnectionError) {
            error_message = mActivity.getResources().getString(
                    R.string.no_internet_connection);
        } else if (mError instanceof TimeoutError) {
            error_message = mActivity.getResources().getString(
                    R.string.timeout);
        } else if (mError instanceof AuthFailureError) {
        } else if (mError instanceof ServerError) {
        } else if (mError instanceof NetworkError) {
            error_message = mActivity.getResources().getString(
                    R.string.alert_internet_connection);
        } else if (mError instanceof ParseError) {
        }
        return error_message;
    }

}
