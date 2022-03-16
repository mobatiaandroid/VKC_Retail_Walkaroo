/**
 *
 */
package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Archana S
 *
 */

public class LoginActivity extends Activity implements OnClickListener,
        VKCJsonTagConstants, VKCUrlConstants, VKCDbConstants {

    private EditText mUserName;
    private EditText mPassword;
    private Button mBtnLogin;
    private Button mBtnSignUp;
    private TextView mTxtSignUp;
    private Activity mActivity;
    private String[] navMenuTitles;
    private String[] categoryIdList;
    String userType;
    private int dealerCount;
    private int roleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        mActivity = this;
        initialiseUI();
        final ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_imageview, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                // Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        getPassedValues();
        setActionBar();

    }

    private void getPassedValues() {

        navMenuTitles = getIntent().getExtras().getStringArray(
                "MAINCATEGORYNAMELIST");

        categoryIdList = getIntent().getExtras().getStringArray(
                "MAINCATEGORYIDLIST");
    }

    private void initialiseUI() {
        mUserName = (EditText) findViewById(R.id.etUserName);
        mPassword = (EditText) findViewById(R.id.etPassword);
        mBtnLogin = (Button) findViewById(R.id.btLogin);
        mBtnSignUp = (Button) findViewById(R.id.btLoginReq);
        mTxtSignUp = (TextView) findViewById(R.id.txtSignUp);
        mBtnLogin.setOnClickListener(this);
        // mTxtSignUp.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == mBtnLogin) {

            if ((mUserName.getText().toString().equalsIgnoreCase(""))
                    || (mPassword.getText().toString().equalsIgnoreCase(""))) {

                // CustomToast customToast = new CustomToast(mActivity);
                // customToast.show(10);
                VKCUtils.showtoast(mActivity, 10);
            } else {

                if (VKCUtils.checkInternetConnection(mActivity)) {
                    LoginApi();
                } else {
                    VKCUtils.showtoast(mActivity, 2);
                }
                clearValues();

            }
        }
        // else if (v == mTxtSignUp) {
        // Intent mIntent = new Intent(LoginActivity.this,
        // SignUpActivity.class);
        // startActivity(mIntent);
        // }
        else if (v == mBtnSignUp) {
            Intent mIntent = new Intent(LoginActivity.this,
                    SignUpActivity.class);
            startActivity(mIntent);
        }

    }

    public void clearValues() {
        mUserName.setText("");
        mPassword.setText("");
    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    private void LoginApi() {
        final VKCInternetManager manager = new VKCInternetManager(LOGIN__URL);
        String name[] = {"email", "password", "imei_no"};
        String value[] = {mUserName.getText().toString(),
                mPassword.getText().toString(), "12344455"};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                //	Log.v("LOG", "17022015 successResponse: " + successResponse);
                parseResponseJSON(successResponse);// api parsing   // parseJSON(successResponse);
                //	Log.v("LOG", "17022015 successResponse: " + successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                //	Log.v("LOG", "04122014FAIL " + failureResponse);
                // mIsError = true;

            }
        });

    }

    /*                                      RoleTypes:4->Sales Head,5->Retailers,6->Dealer 7->SubDealer             */
    private void parseResponseJSON(String successResponse) {

        if (successResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(successResponse);

                JSONArray jsonArray = jsonObject
                        .getJSONArray(JSON_LOGIN_RESPONSE);
                JSONObject resObject = jsonArray.getJSONObject(0);
                String loginResponse = resObject.getString(JSON_LOGIN_LOGIN);
                userType = resObject.getString(JSON_LOGIN_ROLEID);
                roleID = Integer.parseInt(userType);
                @SuppressWarnings("unused")
                String roleName = resObject.getString(JSON_LOGIN_ROLENAME);
                System.out.println("18022105:Username:"
                        + AppPrefenceManager.getUserName(mActivity));
                if (loginResponse.equals(JSON_LOGIN_SUCCESS)) {
                    //VKCUtils.showtoast(mActivity, 1);
                    AppPrefenceManager.saveUserType(mActivity,
                            resObject.getString(JSON_LOGIN_ROLEID));
					/*AppPrefenceManager.saveUserId(mActivity,
							resObject.getString(JSON_LOGIN_USERID));*/
                    AppPrefenceManager.saveCustomerId(mActivity,
                            resObject.getString(JSON_LOGIN_CUSTOMER_ID));

                    if (resObject.getString(JSON_LOGIN_DIST_NAME) != null) {
                        AppPrefenceManager.setLoginPlace(mActivity,
                                resObject.getString(JSON_LOGIN_DIST_NAME));
                    }

                    if (resObject.getString(JSON_LOGIN_CUSTOMER_NAME) != null) {
                        AppPrefenceManager.setLoginCustomerName(mActivity,
                                resObject.getString(JSON_LOGIN_CUSTOMER_NAME));
                    }
                    AppPrefenceManager.saveUserId(mActivity,
                            resObject.getString(JSON_LOGIN_USERID));
                    //Save Dealer Count
                    String mDealerCount = resObject
                            .getString(JSON_LOGIN_DEALER_COUNT);
                    AppPrefenceManager.saveDealerCount(mActivity, mDealerCount);
                    dealerCount = Integer.parseInt(mDealerCount);
                    String mUserName = resObject.getString(JSON_LOGIN_USERNAME);
                    // if(AppPrefenceManager.getLoginStatusFlag(mActivity).equals("false")){
                    AppPrefenceManager.saveUserName(mActivity, mUserName);
                    if (!AppPrefenceManager.getUserName(mActivity).equals("")) {
                        if (!AppPrefenceManager.getUserName(mActivity).equals(
                                mUserName)) {

                            System.out.println("26022015 Users differ");
                            clearDb();
                        } else {
                            System.out.println("26022015 Users do not differ");
                        }

                    }
                    System.out.println("26022015 Save values");
                    // }
                    AppPrefenceManager.saveUserName(mActivity,
                            resObject.getString(JSON_LOGIN_USERNAME));
                    AppPrefenceManager.saveStateCode(mActivity,
                            resObject.getString(JSON_LOGIN_STATECODE));
					/*AppPrefenceManager.saveLoginStatusFlag(mActivity, "true");
					gotoDashBoard();*/
                    if (AppPrefenceManager.getUserType(mActivity).equals("5") || AppPrefenceManager.getUserType(mActivity).equals("4")) {
                        VKCUtils.showtoast(mActivity, 1);
                        gotoDashBoard();
                        AppPrefenceManager.saveLoginStatusFlag(mActivity, "true");

                    } else {
                        VKCUtils.showtoast(mActivity, 42);

                    }
                } else if (loginResponse.equals(JSON_LOGIN_FAILED)) {
                    VKCUtils.showtoast(mActivity, 23);
                }
            } catch (Exception e) {

            }
        }
    }

    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(mActivity);
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }

    public void gotoDashBoard() {
        Intent mIntent = new Intent(LoginActivity.this,
                DashboardFActivity.class);
        mIntent.putExtra("MAINCATEGORYNAMELIST", navMenuTitles);
        mIntent.putExtra("MAINCATEGORYIDLIST", categoryIdList);
        mIntent.putExtra("USERTYPE", userType);
        mIntent.putExtra("DEALERCOUNT", dealerCount);
        mIntent.putExtra("ROLEID", roleID);
        // AppPrefenceManager.saveUserType(mActivity, userType);
        startActivity(mIntent);
        finish();
    }
}
