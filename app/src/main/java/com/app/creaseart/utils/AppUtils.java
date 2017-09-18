package com.app.creaseart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 25-11-2015.
 */
public class AppUtils {


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {

            if (email.contains("..") || email.contains(".@")) {
                isValid = false;
            } else {
                isValid = true;
            }

        }
        return isValid;
    }

    public static Integer getBadgeCount(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getInt("count", 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static void setBadgeCount(Context context, int count) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("count", count);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void onKeyBoardDown(Context con) {
        try {
            InputMethodManager inputManager = (InputMethodManager) con
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isAcceptingText()) {
                if (inputManager.isActive()) {
                    inputManager.hideSoftInputFromWindow(((Activity) con).getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setUserId(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("user_id", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void setIsChatVisible(Context context, Boolean userId) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isVisible", userId);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void setChatUserId(Context context, String userId) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("chatuser_id", userId);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getChatUserId(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("chatuser_id", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static Boolean getIsChatVisible(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getBoolean("isVisible", false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static String getUserId(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("user_id", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setUserImage(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("user_image", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUserImage(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("user_image", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setUseremail(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("email", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUseremail(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("email", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setBusinessId(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("BusinessId", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getBusinessId(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("BusinessId", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static void setUserRole(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("userrole", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getTimeFromDateString(String dateString) {
        String formattedString = "";
        try {
            // String source = "2013-02-19T11:20:16.393Z";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;
            date = formatter.parse(dateString);
            SimpleDateFormat formatterNew = new SimpleDateFormat("hh:mm a");
            formattedString = formatterNew.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedString;
    }


    public static String getDateFromDateString(String dateString) {
        String formattedString = "";
        try {
            // String source = "2013-02-19T11:20:16.393Z";
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = null;
            date = formatter.parse(dateString);
            SimpleDateFormat formatterNew = new SimpleDateFormat("EEE, d MMM");
            formattedString = formatterNew.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedString;
    }

    public static String getUserRole(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("userrole", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setService(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("service", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getService(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("service", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static void setUserName(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("user_name", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUserName(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("user_name", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /*  Show Debug Message into Logcat  */
    public static void showLog(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    /*  Show Debug Error Message into Logcat  */
    public static void showErrorLog(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    /*  Show Debug Error Message into Logcat  */
    public static void showMessageLog(String TAG, String msg) {
        Log.v(TAG, msg);
    }


    public static void setUserMobile(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("Mobile", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUserMobile(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("Mobile", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setUserCode(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("code", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUserCode(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("code", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setUserVerificationCode(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("Verificationcode", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUserVerificationCode(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("Verificationcode", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setZoneId(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("zoneid", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getZoneId(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("zoneid", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static void setUniqueToken(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("token", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUniqueToken(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("token", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setGender(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("Gender", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static String getCompanyName(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("CompanyName", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setCompanyName(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("CompanyName", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void setCountryId(Context context, String userId) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Country", userId);
            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getCountryId(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("Country", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static String getCategories(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("selectedcategory", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setCategories(Context context, String image) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("selectedcategory", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getCategoriesId(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("selectedcategoryId", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static void setCategoriesId(Context context, String image) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("selectedcategoryId", image);

            editor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getGender(Context context) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString("Gender", "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getGcmRegistrationKey(Context context) {


        String ud_id = "";
        try {
            SharedPreferences sub_share = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
            ud_id = sub_share.getString(AppConstant.REGID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ud_id;


    }


}
