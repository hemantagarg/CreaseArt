package com.app.creaseart.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creaseart.R;
import com.app.creaseart.fragment.BaseFragment;
import com.app.creaseart.fragment.Fragment_ActivePackage;
import com.app.creaseart.fragment.Fragment_ChangePassword;
import com.app.creaseart.fragment.Fragment_FamilyAccuntLIst;
import com.app.creaseart.fragment.Fragment_Home;
import com.app.creaseart.fragment.Fragment_Notification;
import com.app.creaseart.fragment.UserBookingFragment;
import com.app.creaseart.fragment.UserProfileFragment;
import com.app.creaseart.interfaces.GlobalConstants;
import com.app.creaseart.utils.AppUtils;
import com.app.creaseart.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Stack;

public class Dashboard extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private View main_view;
    private static final String TAG = Dashboard.class.getSimpleName();
    private FrameLayout home_container;
    private AppBarLayout appBar;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
    };
    DrawerLayout drawer;
    private String mCurrentTab;
    private ProgressBar api_loading_request;
    /*
      * Fragment instance
      * */
    private static Dashboard mInstance;
    private TextView text_profile, myorders, familymember, text_logout, changepass, text_activePackage;
    public static volatile Fragment currentFragment;
    private HashMap<String, Stack<Fragment>> mStacks;
    private ImageView image_user, notification;

    /***********************************************
     * Function Name : getInstance
     * Description : This function will return the instance of this activity.
     *
     * @return
     */
    public static Dashboard getInstance() {
        if (mInstance == null)
            mInstance = new Dashboard();
        return mInstance;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void manageHeaderVisibitlity(boolean isVisible) {
        if (isVisible) {
            appBar.setVisibility(View.VISIBLE);
        } else {
            appBar.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
        mStacks = new HashMap<>();
        mStacks.put(GlobalConstants.TAB_HOME_BAR, new Stack<Fragment>());
        pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_Home(), true);
        setListener();
    }

    private void init() {
        context = Dashboard.this;
        mInstance = Dashboard.this;
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        main_view = findViewById(R.id.main_view);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        text_logout = (TextView) findViewById(R.id.text_logout);
        text_profile = (TextView) findViewById(R.id.text_profile);
        myorders = (TextView) findViewById(R.id.myorders);
        text_activePackage = (TextView) findViewById(R.id.text_activePackage);
        changepass = (TextView) findViewById(R.id.changepass);
        familymember = (TextView) findViewById(R.id.familymember);
        notification = (ImageView) findViewById(R.id.notification);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        api_loading_request = (ProgressBar) findViewById(R.id.api_loading_request);
        home_container = (FrameLayout) findViewById(R.id.home_container);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      /*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                main_view.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
                //below line used to remove shadow of drawer
                drawer.setScrimColor(Color.TRANSPARENT);
            }//this method helps you to aside menu drawer
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView text_username = (TextView) header.findViewById(R.id.text_name);
        TextView text_email = (TextView) header.findViewById(R.id.text_email);
        text_username.setText(AppUtils.getUserName(context));
        text_email.setText(AppUtils.getUseremail(context));
        image_user = (ImageView) header.findViewById(R.id.image_user);
        if (!AppUtils.getUserImage(context).equalsIgnoreCase("")) {
            Picasso.with(context).load(AppUtils.getUserImage(context)).placeholder(R.drawable.loginlogo).transform(new CircleTransform()).into(image_user);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mStacks.get(mCurrentTab).lastElement() instanceof Fragment_Home
                ) {
            manageHeaderVisibitlity(true);
        } else {
            manageHeaderVisibitlity(false);
        }

    }

    private void setWhiteColor() {
        text_profile.setBackgroundColor(getResources().getColor(R.color.white));
        text_logout.setBackgroundColor(getResources().getColor(R.color.white));
        myorders.setBackgroundColor(getResources().getColor(R.color.white));
        text_activePackage.setBackgroundColor(getResources().getColor(R.color.white));
        familymember.setBackgroundColor(getResources().getColor(R.color.white));
        changepass.setBackgroundColor(getResources().getColor(R.color.white));

        text_profile.setTextColor(getResources().getColor(R.color.textcolordark));
        changepass.setTextColor(getResources().getColor(R.color.textcolordark));
        familymember.setTextColor(getResources().getColor(R.color.textcolordark));
        myorders.setTextColor(getResources().getColor(R.color.textcolordark));
        text_activePackage.setTextColor(getResources().getColor(R.color.textcolordark));
        text_logout.setTextColor(getResources().getColor(R.color.textcolordark));
    }

    private void setListener() {

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_Notification(), true);
            }
        });
        text_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_logout.setTextColor(getResources().getColor(R.color.appcolor));
                text_logout.setBackgroundResource(R.drawable.text_bg);
                showLogoutBox();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        text_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_profile.setTextColor(getResources().getColor(R.color.appcolor));
                text_profile.setBackgroundResource(R.drawable.text_bg);
                pushFragments(GlobalConstants.TAB_HOME_BAR, new UserProfileFragment(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        familymember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                familymember.setTextColor(getResources().getColor(R.color.appcolor));
                familymember.setBackgroundResource(R.drawable.text_bg);
                pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_FamilyAccuntLIst(), true);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                changepass.setTextColor(getResources().getColor(R.color.appcolor));
                changepass.setBackgroundResource(R.drawable.text_bg);
                pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_ChangePassword(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                myorders.setTextColor(getResources().getColor(R.color.appcolor));
                myorders.setBackgroundResource(R.drawable.text_bg);
                pushFragments(GlobalConstants.TAB_HOME_BAR, new UserBookingFragment(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        text_activePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteColor();
                text_activePackage.setTextColor(getResources().getColor(R.color.appcolor));
                text_activePackage.setBackgroundResource(R.drawable.text_bg);
                pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_ActivePackage(), true);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


    }

    /*********************************************************************************
     * Function Name - activeFeedFragment
     * <p/>
     * Description - active the view of home tab manages the visibility of
     * five frames in this view
     ********************************************************************************/
    private void activeFeedFragment() {

        mCurrentTab = GlobalConstants.TAB_HOME_BAR;
        currentFragment = (BaseFragment) mStacks.get(mCurrentTab).lastElement();
        home_container.setVisibility(View.VISIBLE);
    }

    private void showLogoutBox() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Dashboard.this);

        alertDialog.setTitle("LOG OUT !");

        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AppUtils.setUserId(context, "");
                        AppUtils.setUseremail(context, "");
                        AppUtils.setUserName(context, "");
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }


    /*
         * To add fragment to a tab. tag -> Tab identifier fragment -> Fragment to
         * show, false when we switch tabs, or adding first fragment to a tab true
         * when we are pushing more fragment into navigation stack. shouldAdd ->
         * Should add to fragment navigation stack (mStacks.get(tag)). false when we
         * are switching tabs (except for the first time) true in all other cases.
         */
    public void pushFragments(String tag, Fragment fragment, boolean ShouldAdd) {
        if (fragment != null && currentFragment != fragment) {
            currentFragment = fragment;
            mCurrentTab = tag;
            if (ShouldAdd)
                mStacks.get(tag).add(fragment);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (tag.equals(GlobalConstants.TAB_HOME_BAR)) {
                ft.add(R.id.home_container, fragment);
                activeFeedFragment();
            }
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * This method is to set the get api loader
     */
    public void setProgressLoader(boolean loaderVisible) {
        if (loaderVisible) {
            api_loading_request.setVisibility(View.VISIBLE);

        } else {
            api_loading_request.setVisibility(View.GONE);
        }
    }

    /*********************************************************************************
     * Function Name - popFragments
     * <p/>
     * Description - this function is used to remove the top fragment of a
     * specific tab on back press
     ********************************************************************************/
    private void popFragments() {
        /*
         * // * Select the last fragment in current tab's stack.. which will be
		 * shown after the fragment transaction given below
		 */
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(
                mStacks.get(mCurrentTab).size() - 1);

        // Fragment fragment = getLastElement(mStacks.get(mCurrentTab));
        /* pop current fragment from stack.. */
        mStacks.get(mCurrentTab).remove(fragment);
        if (mStacks != null && mStacks.get(mCurrentTab) != null && !mStacks.get(mCurrentTab).isEmpty())
            currentFragment = mStacks.get(mCurrentTab).lastElement();
           /*
         * Remove the top fragment
		 */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        // ft.add(R.id.realtabcontent, fragment);
        ft.detach(fragment);
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        AppUtils.showLog(TAG, " ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() : " + ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (mStacks.get(mCurrentTab).size() > 0 &&
                    ((BaseFragment) mStacks.get(mCurrentTab).lastElement()).onBackPressed() == false) {
                AppUtils.showErrorLog(TAG, "onBackPressed");
            /*
             * top fragment in current tab doesn't handles back press, we can do
			 * our thing, which is
			 *
			 * if current tab has only one fragment in stack, ie first fragment
			 * is showing for this tab. finish the activity else pop to previous
			 * fragment in stack for the same tab
			 */
                if (mStacks.get(mCurrentTab).size() == 1) {
                    AppUtils.showLog(TAG, "mStacks.get(mCurrentTab).size() == 1");
                    super.onBackPressed();
                    finish();
                } else {
                    AppUtils.showLog(TAG,
                            "mStacks.get(" + mCurrentTab + ").size() not equal to 1 : "
                                    + mStacks.get(mCurrentTab).size());
                    popFragments();
                    if (mStacks.get(mCurrentTab).hashCode() != 0) {
                        // refresh screens
                        if (mStacks.get(mCurrentTab).size() > 0 && mStacks.get(mCurrentTab).lastElement() instanceof Fragment_Home) {
                            AppUtils.showLog(TAG, " Current Fragment is Feed Fragment");
                            //  refreshHomeFragment();
                        }

                        if (mStacks.get(mCurrentTab).lastElement() instanceof Fragment_Home) {
                            manageHeaderVisibitlity(true);
                        } else {
                            manageHeaderVisibitlity(false);
                        }
                        refreshFragments();
                    }
                }
            } else {
                // do nothing.. fragment already handled back button press.

/*
            if (mStacks.get(mCurrentTab).size() > 0 && (mStacks.get(mCurrentTab).lastElement() instanceof HealthFragment ||
                    mStacks.get(mCurrentTab).lastElement() instanceof NetworksFragment ||
                    mStacks.get(mCurrentTab).lastElement() instanceof DevicesFragment ||
                    mStacks.get(mCurrentTab).lastElement() instanceof NotificationsFragment)) {
                AppUtils.showLog(TAG, " Main Dashboards Screens displayed");
            }
*/

            }
        }
    }

    private void refreshFragments() {

        if (currentFragment instanceof Fragment_FamilyAccuntLIst)
            ((Fragment_FamilyAccuntLIst) currentFragment).onResume();

    }

}
