package com.app.creaseart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.aynctask.CommonAsyncTaskAquery;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.iclasses.InternalStorageContentProvider;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.utils.AppUtils;
import com.app.creaseart.utils.CircleTransform;
import com.app.creaseart.utils.GPSTracker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import eu.janmuller.android.simplecropimage.CropImage;

public class UserProfileFragment extends BaseFragment implements ApiResponse {


    public static UserProfileFragment userProfileFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = UserProfileFragment.class.getSimpleName();
    private EditText user_name, edtmobilenumber, edtEmailId, edtAddress;
    private ImageView image_user;
    private Button btn_updateprofile;
    String latitude = "0.0", longitude = "0.0";
    String path = "", selectedPath1 = "";
    private File mFileTemp, selectedFilePath, seletedImageUser;
    Spinner spinner_state, spinner_city, spinner_locality, spinner_zone;
    private Bitmap bitmap = null;
    private Bundle b;
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private View view_bg;
    ArrayAdapter<String> adapterState;
    ArrayList<String> listState = new ArrayList<>();
    ArrayList<String> listStateID = new ArrayList<>();
    ArrayAdapter<String> adapterCity;
    ArrayList<String> listCity = new ArrayList<>();
    ArrayList<String> listCityID = new ArrayList<>();
    ArrayAdapter<String> adapterLocality;
    ArrayList<String> listLocality = new ArrayList<>();
    ArrayList<String> listLocalityID = new ArrayList<>();
    ArrayAdapter<String> adapterZone;
    ArrayList<String> listZone = new ArrayList<>();
    ArrayList<String> listZoneID = new ArrayList<>();
    private TextView text_code;
    private String selectedCity, selectedState, selectedZone, selectedLocality;

    public static UserProfileFragment getInstance() {
        if (userProfileFragment == null)
            userProfileFragment = new UserProfileFragment();
        return userProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_profile_fragment, container, false);
        mActivity = getActivity();
        userProfileFragment = this;
        initViews();

        String states = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(states)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(mActivity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        manageHeaderView();
        setListener();
        //   getProfile();
        getStateList();

        return view;
    }

    private void getStateList() {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            //  http://dev.stackmindz.com/creaseart/api/state.php
            String url = JsonApiHelper.BASEURL + JsonApiHelper.STATE;
            new CommonAsyncTaskHashmap(6, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void getCityList(String id) {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            //http://dev.stackmindz.com/creaseart/api/city.php?state_id=11
            String url = JsonApiHelper.BASEURL + JsonApiHelper.CITY + "state_id=" + id;
            new CommonAsyncTaskHashmap(3, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void getLocalityList(String id) {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/creaseart/api/locality.php?city_id=16
            String url = JsonApiHelper.BASEURL + JsonApiHelper.LOCALITY + "city_id=" + id;
            new CommonAsyncTaskHashmap(4, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private void getZoneList(String id) {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/creaseart/api/zone.php?locality_id=20
            String url = JsonApiHelper.BASEURL + JsonApiHelper.ZONE + "locality_id=" + id;
            new CommonAsyncTaskHashmap(5, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    private void getProfile() {

        // http://dev.stackmindz.com/creaseart/api/getProfile.php?user_id=1
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_PROFILE + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(2, mActivity, this).getqueryJsonbject(url, null, Request.Method.GET);
        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    private void setListener() {

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage1();
            }
        });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    selectedState = listStateID.get(position);
                    getCityList(listStateID.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    selectedCity = listCityID.get(position);
                    getLocalityList(listCityID.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_locality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    selectedLocality = listLocalityID.get(position);
                    getZoneList(listLocalityID.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    selectedZone = listZoneID.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidLoginDetails()) {
                    updateProfile();
                }
            }
        });
    }


    private void initViews() {
        btn_updateprofile = (Button) view.findViewById(R.id.btn_updateprofile);
        user_name = (EditText) view.findViewById(R.id.edtName);
        user_name.setText(AppUtils.getUserName(getContext()));
        edtmobilenumber = (EditText) view.findViewById(R.id.edtmobilenumber);
        text_code = (TextView) view.findViewById(R.id.text_code);
        text_code.setText(AppUtils.getUserVerificationCode(mActivity));
        edtmobilenumber.setText(AppUtils.getUserMobile(getContext()));
        edtEmailId = (EditText) view.findViewById(R.id.edtEmailId);
        spinner_city = (Spinner) view.findViewById(R.id.spinner_city);
        spinner_locality = (Spinner) view.findViewById(R.id.spinner_locality);
        spinner_state = (Spinner) view.findViewById(R.id.spinner_state);
        spinner_zone = (Spinner) view.findViewById(R.id.spinner_zone);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        edtEmailId.setText(AppUtils.getUseremail(getContext()));
        image_user = (ImageView) view.findViewById(R.id.image_user);
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation()) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        } else {
            showSettingsAlert();
        }


        listState.add("Select State");
        listStateID.add("-1");
        adapterState = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listState);
        spinner_state.setAdapter(adapterState);

        listCity.add("Select City");
        listCityID.add("-1");
        adapterCity = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listCity);
        spinner_city.setAdapter(adapterCity);
        listLocality.add("Select Locality");
        listLocalityID.add("-1");
        adapterLocality = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listLocality);
        spinner_locality.setAdapter(adapterLocality);
        listZone.add("Select Zone");
        listZoneID.add("-1");

        adapterZone = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listZone);
        spinner_zone.setAdapter(adapterZone);


    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mActivity.onBackPressed();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                mActivity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePicture();

                } else if (items[item].equals("Choose from Library")) {

                    openGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                    /*
                     * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
		        	 */
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(mActivity, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 0);
        intent.putExtra(CropImage.ASPECT_Y, 0);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;

            case REQUEST_CODE_GALLERY:
                try {
                    InputStream inputStream = mActivity.getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {
                    Log.e(TAG, "Error while creating temp file", e);
                }
                //  upload_image.setText("Image upload successfully");
                break;

            case REQUEST_CODE_CROP_IMAGE:
                try {
                    path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                selectedFilePath = new File(path);
                Log.e("filepath", "**" + selectedFilePath);

                seletedImageUser = selectedFilePath;
                Picasso.with(mActivity).load(seletedImageUser).placeholder(R.drawable.user).transform(new CircleTransform()).into(image_user);

                break;


        }
    }


    private void updateProfile() {
        if (AppUtils.isNetworkAvailable(mActivity)) {

            // http://dev.stackmindz.com/creaseart/api/update-profile.php?user_id=1&name=sadad&address=fsdfsd
            // &state=2&city=3&locality=1&zone=4&profile_image=

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("name", user_name.getText().toString());
            hm.put("address", edtAddress.getText().toString());
            hm.put("profile_image", seletedImageUser);
            hm.put("state", selectedState);
            hm.put("city", selectedCity);
            hm.put("locality", selectedLocality);
            hm.put("zone", selectedZone);
            hm.put("user_id", AppUtils.getUserId(mActivity));

            String url = JsonApiHelper.BASEURL + JsonApiHelper.UPDATE_PROFILE;
            new CommonAsyncTaskAquery(1, mActivity, this).getquery(url, hm);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.profile));
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setProgressLoader(false, false);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }


    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edtEmailId.getText().toString();
        String first_name = user_name.getText().toString();
        String mobileno = edtmobilenumber.getText().toString();
        String address = edtAddress.getText().toString();


        if (!first_name.equalsIgnoreCase("") && !emailaddress.equalsIgnoreCase("")
                && !mobileno.equalsIgnoreCase("") && !address.equalsIgnoreCase("") && !selectedState.equalsIgnoreCase("")
                && !selectedCity.equalsIgnoreCase("") && !selectedLocality.equalsIgnoreCase("") && !selectedZone.equalsIgnoreCase("")) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enter_name, Toast.LENGTH_SHORT).show();
            } else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enter_mb, Toast.LENGTH_SHORT).show();
            } else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enter_email, Toast.LENGTH_SHORT).show();
            } else if (address.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.enter_address, Toast.LENGTH_SHORT).show();
            } else if (selectedState.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.please_select_state, Toast.LENGTH_SHORT).show();
            } else if (selectedCity.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.please_select_city, Toast.LENGTH_SHORT).show();
            } else if (selectedLocality.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.please_select_locality, Toast.LENGTH_SHORT).show();
            } else if (selectedZone.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(mActivity, R.string.please_select_zone, Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }


    protected void setFragment(Fragment fragment) {

    }

    @Override
    public void onPostSuccess(int method, JSONObject jObject) {
        try {
            if (method == 1) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setZoneId(mActivity, data.getString("ZoneId"));
                    mActivity.onBackPressed();

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    if (!data.getString("ProfilePic").equalsIgnoreCase("")) {
                        Picasso.with(mActivity).load(data.getString("ProfilePic")).placeholder(R.drawable.user).transform(new CircleTransform()).into(image_user);
                    }
                    user_name.setText(data.getString("Name"));
                    edtmobilenumber.setText(data.getString("Mobile"));
                    edtEmailId.setText(data.getString("Email"));
                    edtmobilenumber.setEnabled(false);
                    edtEmailId.setEnabled(false);

                    edtAddress.setText(data.getString("Address"));
                    //   edtProfileDescription.setText(data.getString("Address"));

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 6) {
                listState.clear();
                listStateID.clear();
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("states");

                    listState.add("Select State");
                    listStateID.add("-1");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);
                        listState.add(jsonObject.getString("stateName"));
                        listStateID.add(jsonObject.getString("stateId"));
                    }
                    adapterState = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listState);
                    spinner_state.setAdapter(adapterState);

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 3) {
                listCity.clear();
                listCityID.clear();

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("cities");

                    listCity.add("Select City");
                    listCityID.add("-1");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);
                        listCity.add(jsonObject.getString("CityName"));
                        listCityID.add(jsonObject.getString("CityId"));
                    }

                    adapterCity = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listCity);
                    spinner_city.setAdapter(adapterCity);
                } else {
                    listCity.add("Select City");
                    listCityID.add("-1");
                    adapterCity.notifyDataSetChanged();
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 4) {
                listLocality.clear();
                listLocalityID.clear();
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("localities");

                    listLocality.add("Select Locality");
                    listLocalityID.add("-1");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);
                        listLocality.add(jsonObject.getString("LocalityName"));
                        listLocalityID.add(jsonObject.getString("LocalityId"));
                    }

                    adapterLocality = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listLocality);
                    spinner_locality.setAdapter(adapterLocality);
                } else {
                    listLocality.add("Select Locality");
                    listLocalityID.add("-1");
                    adapterLocality.notifyDataSetChanged();
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 5) {
                listZone.clear();
                listZoneID.clear();
                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("zones");

                    listZone.add("Select Zone");
                    listZoneID.add("-1");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);
                        listZone.add(jsonObject.getString("zoneName"));
                        listZoneID.add(jsonObject.getString("zoneId"));
                    }

                    adapterZone = new ArrayAdapter<String>(mActivity, R.layout.row_spinner, R.id.text_view, listZone);
                    spinner_zone.setAdapter(adapterZone);
                } else {
                    listZone.add("Select Zone");
                    listZoneID.add("-1");
                    adapterZone.notifyDataSetChanged();
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
