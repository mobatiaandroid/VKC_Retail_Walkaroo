package com.mobatia.vkcretailer.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcretailer.adapter.NavDrawerListAdapter;
import com.mobatia.vkcretailer.adapter.SearchAdapter;
import com.mobatia.vkcretailer.appdialogs.AppExitDialog;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.fragments.ComplaintFragment;
import com.mobatia.vkcretailer.fragments.ContactUsFragment;
import com.mobatia.vkcretailer.fragments.CreditPaymentFragment;
import com.mobatia.vkcretailer.fragments.DealersRetailersAndDateFilter;
import com.mobatia.vkcretailer.fragments.FeedbackFragment;
import com.mobatia.vkcretailer.fragments.HomeFragment;
import com.mobatia.vkcretailer.fragments.LocationFragment;
import com.mobatia.vkcretailer.fragments.ProductListFragmentNew;
import com.mobatia.vkcretailer.fragments.RecentOrdersFragment;
import com.mobatia.vkcretailer.fragments.SalesHeadOrderList;
import com.mobatia.vkcretailer.fragments.SalesOrderFragment;
import com.mobatia.vkcretailer.fragments.SalesOrderStatusListFragment;
import com.mobatia.vkcretailer.fragments.SearchListFragment;
import com.mobatia.vkcretailer.fragments.SubDealerOrderList;
import com.mobatia.vkcretailer.fragments.SubdealerOrderStatusFragment;
import com.mobatia.vkcretailer.fragments.VKCDealersListView;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.model.NavDrawerItem;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.legacy.app.ActionBarDrawerToggle;

public class DashboardFActivity extends AppCompatActivity implements
        VKCDbConstants {

    private DrawerLayout mDrawerLayout;
    ExpandableListView lvExp;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private String[] categoryIdList;
    public static String categoryId;
    public static int categorySelectionPosition = -1;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private static int mNavDrawerItemIndex = 0;
    private List<String> items;
    SearchView searchView;
    String userType;
    int mDealerCount, mRoleId;
    public String key = "";
    public static DashboardFActivity dashboardFActivity;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardFActivity = this;
        mContext = this;
        setContentView(R.layout.activity_dashboard_f);
        mTitle = mDrawerTitle = getTitle();
        AppController.tempmainCatArrayList.clear();
        AppController.tempsubCatArrayList.clear();
        AppController.tempsizeCatArrayList.clear();
        AppController.tempbrandCatArrayList.clear();
        AppController.temppriceCatArrayList.clear();
        AppController.tempcolorCatArrayList.clear();
        AppController.tempofferCatArrayList.clear();
        setIconTitle();
        Thread.setDefaultUncaughtExceptionHandler((UncaughtExceptionHandler) new AppController(
                this, DashboardFActivity.class));

        checkDatabase();
        // initArray();

        init(savedInstanceState);
        initProductDetails();
        //setActonBarOverflowButton();

    }

    private void setIconTitle() {
        navMenuTitles = getIntent().getExtras().getStringArray(
                "MAINCATEGORYNAMELIST");

        categoryIdList = getIntent().getExtras().getStringArray(
                "MAINCATEGORYIDLIST");

        userType = getIntent().getExtras().getString("USERTYPE");
        mDealerCount = getIntent().getExtras().getInt("DEALERCOUNT");
        mRoleId = getIntent().getExtras().getInt("ROLEID");
        // getting Navigation drawer icons from res
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

    }

    protected void checkDatabase() {
        DatabaseHelper myDbHelper = new DatabaseHelper(getApplicationContext(),
                DBNAME);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
            myDbHelper.close();
        } catch (SQLException sqle) {
            Log.v("", "Exception in thread");
            throw sqle;
        }
    }

    private void initArray() {

        //System.out.println("21012015:" + userType);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // Home
        navDrawerItems.add(new NavDrawerItem("Home", navMenuIcons
                .getResourceId(0, -1), "-1"));

        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
                    .getResourceId(i + 1, -1), categoryIdList[i]));
            // System.out.println("NAVMENu"+navMenuTitles.length);
        }
        navDrawerItems.add(new NavDrawerItem("Locate Us", R.drawable.brand,
                "-3"));
        navDrawerItems.add(new NavDrawerItem("Contact Us", R.drawable.location,
                "-5"));
        navMenuIcons.recycle();
    }

    @SuppressLint("NewApi")
    private void init(Bundle savedInstanceState) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        lvExp = (ExpandableListView) findViewById(R.id.lvExp);
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) lvExp
                .getLayoutParams();
        params.width = width;
        lvExp.setLayoutParams(params);
        adapter = new NavDrawerListAdapter(DashboardFActivity.this,
                navDrawerItems);

        /*HashMap<NavDrawerItem, ArrayList<NavDrawerItem>> hashMapNavDrawerItemChild = new HashMap<NavDrawerItem, ArrayList<NavDrawerItem>>();
        SortCategory sortCategory[] = VKCSplashActivity.sortCategoryGlobal
                .getSortCategorys();
        //System.out.println("13022015 SortCategory size:" + sortCategory.length);
        lvExp.setGroupIndicator(null);
        ArrayList<NavDrawerItem> itemsHome = new ArrayList<NavDrawerItem>();
        hashMapNavDrawerItemChild.put(navDrawerItems.get(0), itemsHome);

        for (int j = 0; j < sortCategory.length; j++) {

            ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
            ArrayList<CategoryModel> filterArrayList = sortCategory[j]
                    .getCategoryModels();
            Log.v("LOG", "HOMESET J :" + j);

            for (int i = 0; i < filterArrayList.size(); i++) {
                CategoryModel categoryModel = filterArrayList.get(i);
                NavDrawerItem drawerItem = new NavDrawerItem();
                drawerItem.setTitle(categoryModel.getName());
                drawerItem.setId(categoryModel.getId());
             //   Log.v("LOG", "HOMESET Name :" + categoryModel.getName());
                // drawerItem.setTitle("INNer loop");
                items.add(drawerItem);
            }
            hashMapNavDrawerItemChild.put(navDrawerItems.get(j + 1), items);
        }

        NavDrawerExpandableListAdapter expandableListAdapter = new NavDrawerExpandableListAdapter(
                this, navDrawerItems, hashMapNavDrawerItemChild,
                new OnExploreListener() {

                    @Override
                    public void onExpandGroup(int position) {
                        // TODO Auto-generated method stub
                        lvExp.expandGroup(position, true);
                    }

                    @Override
                    public void onCollapseGrope(int position) {
                        // TODO Auto-generated method stub
                        lvExp.collapseGroup(position);
                    }
                });
        expandableListAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemSelected(String id) {
                AppPrefenceManager.saveSubCategoryId(DashboardFActivity.this,
                        id + "");
                goToProductList(Integer.parseInt(id));
            }
        });
        lvExp.setAdapter(expandableListAdapter);
        lvExp.setBackgroundColor(Color.parseColor("#DDFFFFFF"));*/
        setActionBar();

       /* mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // Setting, Refresh and Rate App
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.closeDrawers();
*/
        beginFragmentTransaction();
        // set
        if (savedInstanceState == null) {
            if (AppPrefenceManager.getDealerCount(this).equals("0")
                    && AppPrefenceManager.getUserType(this).equals("7"))
            // && mRoleId == 7
            {
                displayView(-4);
            } else {
                displayView(-1);
            }

        }

    }

    private void goToProductList(int position) {
        // display view for selected item
        mDrawerLayout.closeDrawers();
        mNavDrawerItemIndex = position;

        AppPrefenceManager.saveListingOption(DashboardFActivity.this, "0");
        if (position == -1) {
            displayView(position);
        } else if (position == 1) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 2) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 3) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 4) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == 5) {
            categorySelectionPosition = position - 1;
            categoryId = categoryIdList[position - 1];
            displayView(position);
        } else if (position == -2) {
            AppPrefenceManager.saveListingOption(DashboardFActivity.this, "3");
            displayView(position);
        } else if (position == -4) {
            displayView(position);
        } else if (position == -3) {
            displayView(position);
        } else if (position == -5) {
            displayView(position);
        } else {// 09 /01/2015
            displayView(position);
            AppPrefenceManager.saveListingOption(DashboardFActivity.this, "5");
        }
    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private boolean exitFlag = true;

    private void setHomeToFront() {

        // Toast.makeText(dashboardFActivity, "setHomeToFront", 1000).show();
        beginFragmentTransaction();
        detachAllFragment();
        exitFlag = true;
        if (mHomeFragment == null) {
            mFragmentTransaction.add(R.id.frame_container, new HomeFragment(),
                    "Home");
        } else {

            /*
             * Bring to the front, if already exists in the fragmenttransaction
             */
            mFragmentTransaction.attach(mHomeFragment);
        }
        mFragmentTransaction.commit();
        setTitle(navMenuTitles[0]);
        mDrawerLayout.closeDrawer(lvExp);
    }

    private void showExirDialog(String str) {
        AppExitDialog appExitDialog = new AppExitDialog(
                DashboardFActivity.this, str);
        appExitDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        appExitDialog.setCancelable(true);
        appExitDialog.show();

    }

    @Override
    public void onBackPressed() {

        if (AppController.isCart) {
            displayView(11);
        } else if (AppController.isDealerList) {
            displayView(-1);
        } else {
            if (exitFlag) {
                showExirDialog("");
            } else {
                setHomeToFront();
            }
        }

    }

    private class SlideMenuClickListener implements
            ExpandableListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected item
            mNavDrawerItemIndex = position;
            AppPrefenceManager.saveListingOption(mContext, "0");
            if (position == 0) {
                displayView(position);
            } else if (position == 1) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 2) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 3) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 4) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == 5) {
                categorySelectionPosition = position - 1;
                categoryId = categoryIdList[position - 1];
                displayView(position);
            } else if (position == navMenuTitles.length + 1) {
                AppPrefenceManager.saveListingOption(DashboardFActivity.this,
                        "3");
                categorySelectionPosition = position - 2;
                categoryId = categoryIdList[position - 2];
                displayView(position);
            } else if (position == navMenuTitles.length + 2) {
                categorySelectionPosition = position - 3;
                categoryId = categoryIdList[position - 3];
                displayView(position);
            }
            if (position == navMenuTitles.length + 3) {
                categorySelectionPosition = position - 4;
                categoryId = categoryIdList[position - 4];
                displayView(position);
            }
        }
    }

    HomeFragment mHomeFragment;
    //ProductListFragment mProductFragment;
    ProductListFragmentNew mProductFragment;
    SearchListFragment mSearchFragment;
    LocationFragment mLocationFragment;
    VKCDealersListView mVkcDealersListView;
    ComplaintFragment mComplaintFragment;
    FeedbackFragment mFeedbackFragment;
    SalesOrderFragment mSalesOrderFragment;
    SubDealerOrderList mSubdealerOrderFragment;
    SubdealerOrderStatusFragment mPendingOrderFragment;
    DealersRetailersAndDateFilter dealersRetailersAndDateFilter;
    ContactUsFragment mContactUsFragment;
    CreditPaymentFragment creditPaymentStatusFragment;
    RecentOrdersFragment mRecentOrderFragment;
    SalesOrderStatusListFragment salesOrderStatusListFragment;
    SalesHeadOrderList msalesHeadOrderFragment;

    private void beginFragmentTransaction() {

        mFragmentManager = getSupportFragmentManager();

        mHomeFragment = (HomeFragment) mFragmentManager
                .findFragmentByTag("Home");
        mProductFragment = (ProductListFragmentNew) mFragmentManager
                .findFragmentByTag("Category");
        mSearchFragment = (SearchListFragment) mFragmentManager
                .findFragmentByTag("Search");

        mLocationFragment = (LocationFragment) mFragmentManager
                .findFragmentByTag("Locate Us");

        mVkcDealersListView = (VKCDealersListView) mFragmentManager
                .findFragmentByTag("VKC dealers");
        mContactUsFragment = (ContactUsFragment) mFragmentManager
                .findFragmentByTag("Contact Us");

        mComplaintFragment = (ComplaintFragment) mFragmentManager
                .findFragmentByTag("Complaint");
        mFeedbackFragment = (FeedbackFragment) mFragmentManager
                .findFragmentByTag("FeedBack");
        mSalesOrderFragment = (SalesOrderFragment) mFragmentManager
                .findFragmentByTag("Sales");
        mSubdealerOrderFragment = (SubDealerOrderList) mFragmentManager
                .findFragmentByTag("SubDealerOrderList");
        dealersRetailersAndDateFilter = (DealersRetailersAndDateFilter) mFragmentManager
                .findFragmentByTag("DealersRetailersAndDateFilter");

        creditPaymentStatusFragment = (CreditPaymentFragment) mFragmentManager
                .findFragmentByTag("CreditPaymentStatusFragment");

        salesOrderStatusListFragment = (SalesOrderStatusListFragment) mFragmentManager
                .findFragmentByTag("SalesOrder");
        mPendingOrderFragment = (SubdealerOrderStatusFragment) mFragmentManager
                .findFragmentByTag("PendingOrder");
        mRecentOrderFragment = (RecentOrdersFragment) mFragmentManager
                .findFragmentByTag("RecentOrder");
        msalesHeadOrderFragment = (SalesHeadOrderList) mFragmentManager
                .findFragmentByTag("SalesHead");
        mFragmentTransaction = mFragmentManager.beginTransaction();

    }

    private void detachAllFragment() {
        if (mHomeFragment != null) {
            mFragmentTransaction.detach(mHomeFragment);
        }
        if (mProductFragment != null) {
            mFragmentTransaction.detach(mProductFragment);
            AppPrefenceManager.saveProductListSortOption(
                    DashboardFActivity.this, "0");
        }
        if (mSearchFragment != null) {
            mFragmentTransaction.detach(mSearchFragment);
        }
        if (mLocationFragment != null) {
            mFragmentTransaction.detach(mLocationFragment);
        }
        if (mVkcDealersListView != null) {
            mFragmentTransaction.detach(mVkcDealersListView);
        }
        if (mComplaintFragment != null) {
            mFragmentTransaction.detach(mComplaintFragment);
        }
        if (mFeedbackFragment != null) {
            mFragmentTransaction.detach(mFeedbackFragment);
        }
        if (mSalesOrderFragment != null) {
            mFragmentTransaction.detach(mSalesOrderFragment);
        }
        if (mSalesOrderFragment != null) {
            mFragmentTransaction.detach(mSalesOrderFragment);
        }
        if (dealersRetailersAndDateFilter != null) {
            mFragmentTransaction.detach(dealersRetailersAndDateFilter);
        }
        if (creditPaymentStatusFragment != null) {
            mFragmentTransaction.detach(creditPaymentStatusFragment);
        }
        if (salesOrderStatusListFragment != null) {
            mFragmentTransaction.detach(salesOrderStatusListFragment);
        }
        if (mSubdealerOrderFragment != null) {
            mFragmentTransaction.detach(mSubdealerOrderFragment);
        }
        if (mPendingOrderFragment != null) {
            mFragmentTransaction.detach(mPendingOrderFragment);
        }
        if (mContactUsFragment != null) {
            mFragmentTransaction.detach(mContactUsFragment);
        }
        if (mRecentOrderFragment != null) {
            mFragmentTransaction.detach(mRecentOrderFragment);
        }
        if (msalesHeadOrderFragment != null) {
            mFragmentTransaction.detach(msalesHeadOrderFragment);
        }
    }

    public interface DisplayVIewListener {
        public void setDisplayViewListener(int i);
    }

    public void setDisplayView() {
        displayView(2);
    }

    public void goToSearchWithKey(String key) {

        this.key = key;
        displayView(-2);

    }

    public String[] getcategoryIdList() {
        Log.v("LOG", "22122014 getcategoryIdList in display view");
        return categoryIdList;
    }

    public void displayView(int position) {
        // update the main content with called Fragment
        beginFragmentTransaction();
        detachAllFragment();
        Fragment fragment = null;

        // Log.v("LOG", "24112014 position : " + position);
        System.out.println("position " + position);
        if (position == 0) {
            exitFlag = true;
        } else {
            exitFlag = false;
        }

        if (position == -1) {

            if (mHomeFragment == null) {
                // DisplayView displayView = new DisplayView();
                Log.d("Fragment Lifecycle", "Fragment nulll");
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", "VALUE");
                // bundle.putSerializable("OBJECT", displayView);

                homeFragment.setArguments(bundle);
                mFragmentTransaction.add(R.id.frame_container, homeFragment,
                        "Home");
                exitFlag = true;
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mHomeFragment);
            }

        } else if (position == -2) {
            if (mSearchFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new SearchListFragment(), "Search");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mSearchFragment);
            }
        } else if (position == -3) {
            if (mLocationFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new LocationFragment(), "Locate Us");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mLocationFragment);
            }
        } else if (position == -5) {

            if (mContactUsFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new ContactUsFragment(), "Contact Us");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mContactUsFragment);
            }
        } else if (position == -4) {

            if (mVkcDealersListView == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new VKCDealersListView(), "VKC dealers");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mVkcDealersListView);
            }
        } else if (position == R.id.feedback) {
            if (mFeedbackFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new FeedbackFragment(), "FeedBack");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mFeedbackFragment);
            }

        } /*else if (position == R.id.complaint) {
			if (mComplaintFragment == null) {
				Log.d("Fragment Lifecycle", "Fragment nulll");
				mFragmentTransaction.add(R.id.frame_container,
						new ComplaintFragment(), "Complaint");
			} else {

				
				 * Bring to the front, if already exists in the
				 * fragmenttransaction
				 

				Log.d("Fragment Lifecycle", "Fragment not  nulll");
				mFragmentTransaction.attach(mComplaintFragment);
			}
		}*/ else if (position == R.id.logout) {

            AppPrefenceManager.saveUserType(dashboardFActivity, "");
            AppPrefenceManager.saveUserId(dashboardFActivity, "");
            // AppPrefenceManager.saveUserName(dashboardFActivity, "");
            AppPrefenceManager.saveLoginStatusFlag(dashboardFActivity, "false");
            AppPrefenceManager.setDealerId(dashboardFActivity, "");
            AppPrefenceManager.setDealerIdName(dashboardFActivity, "");
            AppPrefenceManager.setRetailerId(dashboardFActivity, "");
            AppPrefenceManager.setRetailerName(dashboardFActivity, "");
            startActivity(new Intent(DashboardFActivity.this, LoginActivity.class));
            finish();

        } else if (position == R.id.home_menu) {
            if (mHomeFragment == null) {

                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", "VALUE");
                // bundle.putSerializable("OBJECT", displayView);

                homeFragment.setArguments(bundle);
                mFragmentTransaction.add(R.id.frame_container, homeFragment,
                        "Home");
            } else {
                mFragmentTransaction.attach(mHomeFragment);
            }
        } else if (position == R.id.create_new) {
            if (mSalesOrderFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderFragment(), "Sales"); // For Order submit
            } else {


                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mSalesOrderFragment);
            }

            // startActivity(new Intent(DashboardFActivity.this, CartActivity.class));
        } else if (position == R.id.salesHeadOrders) {
            if (msalesHeadOrderFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll");
                mFragmentTransaction.add(R.id.frame_container,
                        new SalesHeadOrderList(), "SalesHead"); // For Order
                // submit
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(msalesHeadOrderFragment);
            }
        } else if (position == R.id.credit) {

            if (creditPaymentStatusFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new CreditPaymentFragment(),
                        "CreditPaymentStatusFragment");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(creditPaymentStatusFragment);
            }

        } else if (position == R.id.salesorder) {

            if (salesOrderStatusListFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderStatusListFragment(), "SalesOrder");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(salesOrderStatusListFragment);
            }

        } else if (position == R.id.subdealerorder) {

            if (mSubdealerOrderFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SubDealerOrderList(), "SubDealerOrderList");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mSubdealerOrderFragment);
            }

        } else if (position == R.id.pendingorder) {

            if (mSubdealerOrderFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SubdealerOrderStatusFragment(), "PendingOrder");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mPendingOrderFragment);
            }

        } else if (position == R.id.salesmngmt) {

            if (salesOrderStatusListFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new SalesOrderStatusListFragment(), "SalesOrder");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(salesOrderStatusListFragment);
            }

        } else if (position == R.id.recentOrder) {

            if (mRecentOrderFragment == null) {

                mFragmentTransaction.add(R.id.frame_container,
                        new RecentOrdersFragment(), "RecentOrder");
            } else {

                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */

                Log.d("Fragment Lifecycle", "Fragment not  nulll");
                mFragmentTransaction.attach(mRecentOrderFragment);
            }

        } else if (position == 11) {
            if (mProductFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new ProductListFragmentNew(), "Category");

                Log.v("LOG", "22122014 displayView in IF :: " + position);
            } else {
                Log.v("LOG", "22122014 displayView in ELSE :: " + position);
                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */
                mFragmentTransaction.attach(mProductFragment);
            }
        } else {
            Log.v("LOG", "22122014 displayView :: " + position);
            if (mProductFragment == null) {
                mFragmentTransaction.add(R.id.frame_container,
                        new ProductListFragmentNew(), "Category");

                Log.v("LOG", "22122014 displayView in IF :: " + position);
            } else {
                Log.v("LOG", "22122014 displayView in ELSE :: " + position);
                /*
                 * Bring to the front, if already exists in the
                 * fragmenttransaction
                 */
                mFragmentTransaction.attach(mProductFragment);
            }

        }

        mFragmentTransaction.commit();
        // mDrawerList.setItemChecked(position, true);
        // mDrawerList.setSelection(position);
        if (position < navMenuTitles.length && position > 0) {
            setTitle(navMenuTitles[position]);
        }
        // mDrawerLayout.closeDrawer(mDrawerList);

    }

/*	private void setActonBarOverflowButton() {
		// The content description used to locate the overflow button
		final String overflowDesc = getString(R.string.accessibility_overflow);
		// The top-level window
		final ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		// Wait a moment to ensure the overflow button can be located
		decor.postDelayed(new Runnable() {

			@Override
			public void run() {
				// The List that contains the matching views
				final ArrayList<View> outViews = new ArrayList<>();
				// Traverse the view-hierarchy and locate the overflow button
				decor.findViewsWithText(outViews, overflowDesc,
						View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
				// Guard against any errors
				if (outViews.isEmpty()) {
					return;
				}
				// Do something with the view
				final ImageButton overflow = (ImageButton) outViews.get(0);
				overflow.setImageResource(R.drawable.close);

			}

		}, 1000);
	}*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem userName = menu.findItem(R.id.userName);
        userName.setTitle(AppPrefenceManager.getUserName(this));
        Log.v("LOG", "06022015 " + " onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        Log.v("LOG", "06022015 " + " onOptionsItemSelected");
       /* if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        // Handle action bar actions click
        switch (item.getItemId()) {
		/*case R.id.complaint:

			// Toast.makeText(getBaseContext(),
			// "You selected complaint",1000).show();
			// mNavDrawerItemIndex=R.id.complaint;
			displayView(R.id.complaint);
			return true;*/
            case R.id.feedback:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                // mNavDrawerItemIndex=R.id.feedback;
                displayView(R.id.feedback);
                return true;
            case R.id.logout:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.logout;
                displayView(R.id.logout);
                return true;
            case R.id.create_new:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.logout;
                displayView(R.id.create_new);
                return true;
            case R.id.credit:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.credit;
                displayView(R.id.credit);
                return true;

            case R.id.salesorder:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.salesorder;
                displayView(R.id.salesorder);
                return true;
            case R.id.salesmngmt:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.salesmngmt;
                displayView(R.id.salesmngmt);
                return true;

            case R.id.subdealerorder:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.subdealerorder;
                displayView(R.id.subdealerorder);
                return true;
            case R.id.pendingorder:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.pendingorder;
                displayView(R.id.pendingorder);
                return true;
            case R.id.recentOrder:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.recentOrder;
                displayView(R.id.recentOrder);
                return true;
            case R.id.salesHeadOrders:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.salesHeadOrders;
                displayView(R.id.salesHeadOrders);
                return true;
            case R.id.home_menu:
                // Toast.makeText(getBaseContext(),
                // "You selected feedback",1000).show();
                mNavDrawerItemIndex = R.id.home_menu;
                displayView(R.id.home_menu);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        // return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v("LOG", "06022015 " + " onPrepareOptionsMenu");
        /*
         * For Hiding Options Menu
         */
        if (AppPrefenceManager.getUserType(dashboardFActivity).equals("4")) {
            // menu.findItem(R.id.salesorder).setTitle("SalesManagement");
            menu.findItem(R.id.salesmngmt).setVisible(false);
            menu.findItem(R.id.salesorder).setVisible(false);
            menu.findItem(R.id.subdealerorder).setVisible(false);
            menu.findItem(R.id.pendingorder).setVisible(false);
            menu.findItem(R.id.recentOrder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(true);
        } else if (AppPrefenceManager.getUserType(dashboardFActivity).equals(
                "6")) {
            // menu.findItem(R.id.salesorder).setTitle("SalesManagement");
            menu.findItem(R.id.recentOrder).setVisible(false);
            menu.findItem(R.id.subdealerorder).setVisible(true);
            menu.findItem(R.id.pendingorder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(false);
        } else if (AppPrefenceManager.getUserType(dashboardFActivity).equals(
                "7")) {
            menu.findItem(R.id.recentOrder).setVisible(true);
            menu.findItem(R.id.salesorder).setVisible(false);
            menu.findItem(R.id.pendingorder).setVisible(true);
            menu.findItem(R.id.subdealerorder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(false);
        } else {
            menu.findItem(R.id.subdealerorder).setVisible(false);
            menu.findItem(R.id.pendingorder).setVisible(false);
            menu.findItem(R.id.recentOrder).setVisible(false);
            menu.findItem(R.id.salesHeadOrders).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    // History
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadHistory(String query, boolean flagNoItem) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor

            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "VKC Sandals"};

            MatrixCursor cursor = new MatrixCursor(columns);
            if (!flagNoItem) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).toLowerCase(Locale.getDefault())
                            .contains(query.toLowerCase(Locale.getDefault()))) {
                        temp[0] = i;
                        temp[1] = items.get(i);
                        // replaced s with i as s not used anywhere.

                        cursor.addRow(temp);
                    }

                }
            }

            // SearchView

            searchView.setSuggestionsAdapter(new SearchAdapter(this, cursor,
                    items));
            searchView.setVerticalScrollBarEnabled(false);
        }
    }

    public void initProductDetails() {
        items = new ArrayList<String>();
        items.add("VKC sandals");
        items.add("VKC 2306 Sandals");
        items.add("VKC Pride 3108 Sandals");
        items.add("VKC 3311 Slippers");
        items.add("VKC 3306 Slippers");
        items.add("VKC 3385 Slippers");
        items.add("VKC 3390 Slippers");
    }
	
	/*@Override
	protected void onRestart() {
		AppController.tempmainCatArrayList.clear();
		AppController.tempsubCatArrayList.clear();
		AppController.tempsizeCatArrayList.clear();
		AppController.tempbrandCatArrayList.clear();
		AppController.temppriceCatArrayList.clear();
		AppController.tempcolorCatArrayList.clear();
		AppController.tempofferCatArrayList.clear();
		super.onRestart();
	}*/

}
