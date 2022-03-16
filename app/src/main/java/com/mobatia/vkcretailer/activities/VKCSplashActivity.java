package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobatia.vkcretailer.BuildConfig;
import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.appdialogs.PushNotificationDialog;
import com.mobatia.vkcretailer.appdialogs.PushNotificationDialog.DialogListener;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.controller.JSONParser;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.GoogleRegisterationService;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CategoryModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.PriceModel;
import com.mobatia.vkcretailer.model.SizeModel;
import com.mobatia.vkcretailer.model.SortCategory;

import android.Manifest;
import android.R.bool;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class VKCSplashActivity extends BaseActivity implements VKCUrlConstants,
        VKCJsonTagConstants {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ArrayList<BrandTypeModel> typeArrayList;
    ArrayList<SizeModel> sizeArrayList;
    ArrayList<ColorModel> colorArrayList;
    ArrayList<PriceModel> priceArrayList;
    ArrayList<CategoryModel> categoryArrayList;
    private Intent googleregserviceintent;
    private String deviceId = "";
    private Activity mActivity;
    public boolean mIsError = false;
    private Bundle bundle;
    private String type;
    private String message;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mActivity = this;
        init();

        AppController.tempmainCatArrayList.clear();
        AppController.tempsubCatArrayList.clear();
        AppController.tempsizeCatArrayList.clear();
        AppController.tempbrandCatArrayList.clear();
        AppController.temppriceCatArrayList.clear();
        AppController.tempcolorCatArrayList.clear();
        AppController.tempofferCatArrayList.clear();

        AppPrefenceManager.saveFilterDataCategory(mActivity, "");
        AppPrefenceManager.saveFilterDataSize(mActivity, "");
        AppPrefenceManager.saveFilterDataBrand(mActivity, "");
        AppPrefenceManager.saveFilterDataColor(mActivity, "");
        AppPrefenceManager.saveFilterDataPrice(mActivity, "");
        DashboardFActivity.categoryId = "";
        //startService();
        bundle = getIntent().getExtras();
        System.out.println("bundle " + "" + bundle);
        if (bundle != null) {
            type = bundle.getString("From");
            if (type != null) {
                if (bundle.getString("From").equalsIgnoreCase("GCM")) {
                    message = bundle.getString("Message");
                    showPushDialog(message);
                }
            } else {
                getSettingsApi();
            }
        } else {


            if (checkPermission()) {
                if (VKCUtils.checkInternetConnection(mActivity)) {
                    getSettingsApi();
                } else {
                    mIsError = true;
                    System.out.println("inside the error33");
                    VKCUtils.showtoast(mActivity, 0);
                }
            } else {
                VKCUtils.showtoast(mActivity, 59);
                requestPermission();
            }
        }
        AppPrefenceManager.saveIDsForOffer(mActivity, "");
    }

    /*******************************************************
     * Method name : showPushDialog Description : show Push notification message
     * in Dialog Parameters : message Return type : void Date : 29-May-2015
     * Author : Vandana Surendranath
     *****************************************************/
    private void showPushDialog(String message) {
        PushNotificationDialog pushDialog = new PushNotificationDialog(
                VKCSplashActivity.this);
        pushDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        pushDialog.setCancelable(true);
        pushDialog.init(message, new DialogListener() {

            @Override
            public void dismissDialog() {
                if (VKCUtils.checkInternetConnection(mActivity)) {
                    getSettingsApi();
                } else {
                    mIsError = true;
                    //System.out.println("inside the errror1");
                    VKCUtils.showtoast(mActivity, 0);
                }
            }
        });
        pushDialog.show();
    }

    @Override
    protected int getLayoutResourceId() {
        // TODO Auto-generated method stub
        return R.layout.activity_vkc_splash;
    }

    private void init() {
        ImageView splashImageView = (ImageView) findViewById(R.id.splashImageView);
        splashImageView.setScaleType(ScaleType.CENTER_CROP);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mActivity, READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean phoneAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (phoneAccepted) {

                        if (VKCUtils.checkInternetConnection(mActivity)) {
                            getSettingsApi();
                            // startService();
                        } else {
                            mIsError = true;
                            VKCUtils.showtoast(mActivity, 0);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{READ_PHONE_STATE, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                            PERMISSION_REQUEST_CODE);
                                }
                            } else {
                                requestPermission();
                            }
                        } else if (ActivityCompat.shouldShowRequestPermissionRationale(VKCSplashActivity.this,
                                Manifest.permission.READ_PHONE_STATE)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(VKCSplashActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                        PERMISSION_REQUEST_CODE);
                            }
                        } else {
                            Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                            startActivity(i);
                        }

                    }
                }


                break;
        }
    }

    public void startService() {
        deviceId = VKCUtils.getDeviceID(VKCSplashActivity.this);
		/*googleregserviceintent = new Intent(mActivity,
				GoogleRegisterationService.class);
		mActivity.startService(googleregserviceintent);*/
    }

    // VKCUtils.getSettingsApiCall(VKCSplashActivity.this);

    public void getSettingsApi() {
        //System.out.println("settings api");
        final VKCInternetManager manager = new VKCInternetManager(SETTINGS_URL);
        //Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());

        manager.getResponse(this, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                // TODO Auto-generated method stub
                // Log.v("LOG", "26122014 " + successResponse);
                intiArray();
                // successResponse
                // =VKCUtils.getrespnse(VKCSplashActivity.this,"settings");
                parseJSON(successResponse);
                // goTODashBoard();
                goToHome(mIsError);

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                //	Log.v("LOG", "04122014FAIL " + failureResponse);
                mIsError = true;
                goToHome(mIsError);
                // intiArray();
                // parseJSON(manager.getResponseCache());
                // goTODashBoard();
            }
        });

    }

    private void goToHome(boolean flag) {

        if (!flag) {
            loadSplash();
        } else {
            // CustomToast toast = new CustomToast(mActivity);
            // toast.show(0);
            //System.out.println("inside the errror2");
            VKCUtils.showtoast(mActivity, 0);
            finish();
        }
        // CustomToast toast = new CustomToast(mActivity);
        // toast.show(0);
        // finish();
    }

    private void intiArray() {
        typeArrayList = new ArrayList<BrandTypeModel>();
        categoryArrayList = new ArrayList<CategoryModel>();
        sizeArrayList = new ArrayList<SizeModel>();
        colorArrayList = new ArrayList<ColorModel>();
        priceArrayList = new ArrayList<PriceModel>();
    }

    private void parseJSON(String successResponse) {
        try {

            //System.out.println("28122014 " + successResponse);
            JSONObject jsonObject = new JSONObject(successResponse);
            JSONArray jsonArray = jsonObject
                    .getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
            JSONObject objResponse = jsonArray.getJSONObject(0);

            JSONArray categoryObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_CATEGORY);
            AppPrefenceManager.saveMainCategory(VKCSplashActivity.this,
                    categoryObjArray.toString());
            for (int i = 0; i < categoryObjArray.length(); i++) {
                JSONObject responseObj = categoryObjArray.getJSONObject(i);

                categoryArrayList.add(getCategoryObjectValues(responseObj));

            }

            JSONArray offerObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_OFFER);
            AppPrefenceManager.saveJsonOfferResponse(VKCSplashActivity.this,
                    offerObjArray);

            JSONArray typeObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_TYPE);
            AppPrefenceManager.saveJsonTypeResponse(VKCSplashActivity.this,
                    typeObjArray);
            for (int i = 0; i < typeObjArray.length(); i++) {
                JSONObject responseObj = typeObjArray.getJSONObject(i);

                typeArrayList.add(getTypeObjectValues(responseObj));

            }
            JSONArray sizeObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_SIZE);
            AppPrefenceManager.saveJsonSizeResponse(VKCSplashActivity.this,
                    sizeObjArray);
            for (int i = 0; i < sizeObjArray.length(); i++) {
                JSONObject responseObj = sizeObjArray.getJSONObject(i);

                sizeArrayList.add(getSizeObjectValues(responseObj));

            }
			/*for (int i = 0; i < sizeArrayList.size(); i++) {
				Log.v("05122014", "LOG " + sizeArrayList.get(i).getName());
			}
*/
            JSONArray colorObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_COLOR);
            AppPrefenceManager.saveJsonColorResponse(VKCSplashActivity.this,
                    colorObjArray);
            for (int i = 0; i < colorObjArray.length(); i++) {
                JSONObject responseObj = colorObjArray.getJSONObject(i);

                colorArrayList.add(getColorObjectValues(responseObj));

            }
			/*for (int i = 0; i < colorArrayList.size(); i++) {
				Log.v("05122014", "LOG " + colorArrayList.get(i).getColorcode());
			}*/
            JSONArray priceObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_PRICE);
            AppPrefenceManager.saveJsonPriceResponse(VKCSplashActivity.this,
                    priceObjArray);
            for (int i = 0; i < priceObjArray.length(); i++) {
                JSONObject responseObj = priceObjArray.getJSONObject(i);

                priceArrayList.add(getPriceObjectValues(responseObj));

            }
			/*for (int i = 0; i < priceArrayList.size(); i++) {
				Log.v("05122014", "LOG "
						+ priceArrayList.get(i).getFrom_range() + ","
						+ priceArrayList.get(i).getTo_range());
			}*/

            JSONArray bannerObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_ARRIVALS);
            AppPrefenceManager.saveNewArrivalBannerResponse(
                    VKCSplashActivity.this, bannerObjArray.toString());

            // JSONArray topSliderObjArray = objResponse
            // .getJSONArray("top_slider");
            // AppPrefenceManager.saveTopSliderResponse(VKCSplashActivity.this,
            // topSliderObjArray.toString());

            JSONArray bottomSliderObjArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_POPULAR);

            AppPrefenceManager.savePopularProductSliderResponse(
                    VKCSplashActivity.this, bottomSliderObjArray.toString());

            // brand banner
            JSONArray brandBannerArray = objResponse
                    .getJSONArray(JSON_TAG_SETTINGS_BRAND);

            AppPrefenceManager.saveBrandBannerResponse(VKCSplashActivity.this,
                    brandBannerArray.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mIsError = true;
        }
    }

    public CategoryModel getCategoryObjectValues(JSONObject object)
            throws JSONException {

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(object.getString(JSON_SETTINGS_CATEGORYID));
        categoryModel.setName(object.getString(JSON_SETTINGS_CATEGORYNAME));
        categoryModel.setParentId(object.getString(JSON_SETTINGS_PRODUCTID));

        return categoryModel;

    }

    public BrandTypeModel getTypeObjectValues(JSONObject object)
            throws JSONException {

        BrandTypeModel brandModel = new BrandTypeModel();
        brandModel.setId(object.getString(JSON_SETTINGS_BRANDID));
        brandModel.setName(object.getString(JSON_SETTINGS_BRANDNAME));

        return brandModel;

    }

    public SizeModel getSizeObjectValues(JSONObject object)
            throws JSONException {

        SizeModel sizeModel = new SizeModel();
        sizeModel.setId(object.getString(JSON_SETTINGS_SIZEID));
        sizeModel.setName(object.getString(JSON_SETTINGS_SIZENAME));
        return sizeModel;

    }

    public ColorModel getColorObjectValues(JSONObject object)
            throws JSONException {

        ColorModel colorModel = new ColorModel();
        colorModel.setId(object.getString(JSON_SETTINGS_COLORID));
        colorModel.setColorcode(object.getString(JSON_SETTINGS_COLORCODE));
        colorModel.setName(object.getString(JSON_SETTINGS_COLORNAME));
        return colorModel;

    }

    public PriceModel getPriceObjectValues(JSONObject object)
            throws JSONException {

        PriceModel priceModel = new PriceModel();
        priceModel.setId(object.getString(JSON_SETTINGS_PRICEID));
        priceModel.setFrom_range(object.getString(JSON_SETTINGS_PRICEFROM));
        priceModel.setTo_range(object.getString(JSON_SETTINGS_PRICETO));
        return priceModel;

    }

    private String[] getCategoryNameList() {

        ArrayList<String> stringsList = new ArrayList<String>();
        for (CategoryModel categoryModel : categoryArrayList) {
            if (categoryModel.getParentId().equals("0")) {
                stringsList.add(categoryModel.getName());

            }
        }
        String[] strings = new String[stringsList.size()];

        for (int i = 0; i < stringsList.size(); i++) {
            strings[i] = stringsList.get(i);
        }
        return strings;

    }

    private String[] getCategoryIdList() {

        ArrayList<String> stringsList = new ArrayList<String>();
        for (CategoryModel categoryModel : categoryArrayList) {
            if (categoryModel.getParentId().equals("0")) {
                stringsList.add(categoryModel.getId());

            }
        }
        String[] strings = new String[stringsList.size()];

        for (int i = 0; i < stringsList.size(); i++) {
            strings[i] = stringsList.get(i);
        }
        return strings;

    }

    private void sortCategory() {

        String[] mainCategoryName = getCategoryNameList();
        String[] mainCategoryId = getCategoryIdList();

        SortCategory category[] = new SortCategory[mainCategoryId.length];
        sortCategoryGlobal = new SortCategory();
        for (int i = 0; i < category.length; i++) {
            ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
            for (CategoryModel categoryModel : categoryArrayList) {

                if (categoryModel.getParentId().equals(mainCategoryId[i])) {
                    categoryModels.add(categoryModel);
                }

            }
            category[i] = new SortCategory();
            category[i].setCategoryModels(categoryModels);
        }
        sortCategoryGlobal.setSortCategorys(category);

    }

    public static SortCategory sortCategoryGlobal;

    private void goTODashBoard() {
        // This method will be executed once the timer is over
        // Start your app main activity
        Intent i = new Intent(VKCSplashActivity.this, DashboardFActivity.class);
        i.putExtra("MAINCATEGORYNAMELIST", getCategoryNameList());
        i.putExtra("MAINCATEGORYIDLIST", getCategoryIdList());
        sortCategory();
        startActivity(i);

        // close this activity
        finish();
    }

    private void loadSplash() {

        // splashImageView.setB
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity
                // Intent i = new Intent(VKCSplashActivity.this,
                // DashboardFActivity.class);
                if (AppPrefenceManager.getLoginStatusFlag(mActivity).equals("")) {

                    System.out.println("26022015:First login");
                    Intent i = new Intent(VKCSplashActivity.this,
                            LoginActivity.class);
                    i.putExtra("MAINCATEGORYNAMELIST", getCategoryNameList());
                    i.putExtra("MAINCATEGORYIDLIST", getCategoryIdList());
                    sortCategory();
                    startActivity(i);
                    // close this activity
                    finish();
                } else if (AppPrefenceManager.getLoginStatusFlag(mActivity)
                        .equals("true")) {
                    System.out.println("26022015:Already login");
                    Intent mIntent = new Intent(VKCSplashActivity.this,
                            DashboardFActivity.class);
                    mIntent.putExtra("MAINCATEGORYNAMELIST",
                            getCategoryNameList());
                    mIntent.putExtra("MAINCATEGORYIDLIST", getCategoryIdList());
                    mIntent.putExtra("USERTYPE",
                            AppPrefenceManager.getUserType(mActivity));
                    sortCategory();
                    startActivity(mIntent);
                    finish();
                } else {
                    System.out.println("26022015:Already logout");
                    Intent i = new Intent(VKCSplashActivity.this,
                            LoginActivity.class);
                    i.putExtra("MAINCATEGORYNAMELIST", getCategoryNameList());
                    i.putExtra("MAINCATEGORYIDLIST", getCategoryIdList());
                    sortCategory();
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

}
