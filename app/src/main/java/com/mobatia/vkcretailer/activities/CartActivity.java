package com.mobatia.vkcretailer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.SalesOrderAdapter;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCObjectConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomTextView;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.fragments.SalesOrderFragment;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.CartModel;
import com.mobatia.vkcretailer.model.DealersListModel;
import com.mobatia.vkcretailer.model.DealersShopModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity implements VKCDbConstants,
        VKCUrlConstants {

    private View mRootView;
    int mDisplayWidth = 0;
    int mDisplayHeight = 0;

    private RelativeLayout mRelDealer;
    private RelativeLayout mRelRetailer;
    private CustomTextView mTextViewDealer;
    private CustomTextView mTextViewRetailer;
    private ListView mDealersListView;
    private SalesOrderAdapter mSalesAdapter;
    private DataBaseManager databaseManager;
    private CartModel cartModel;

    private LinearLayout lnrTableHeaders;
    private ImageView imageViewSubmit, imageSearchCat;
    private String salesOrderArray;
    private ArrayList<DealersListModel> dealersModel;
    public static Boolean isCart = false;
    private LinearLayout lnrOne, llCategory, llSearch;
    private LinearLayout llTop, llSecTwo, llSecOne;
    private TextView txtRefr;
    private TextView txtDate;
    private TextView txtQty;
    private TextView txtValue;
    private TextView txtName;
    TextView labelText;
    private TextView txtPlace, textRetailer;
    EditText edtSearch;
    String testSearch;
    List<String> categories = new ArrayList<String>();
    Spinner spinner;
    private String item, type;
    ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
    Context mActivity;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mActivity = this;
        initialiseUI();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

    private void setDisplayParams() {
        DisplayManagerScale displayManagerScale = new DisplayManagerScale(
                mActivity);
        mDisplayHeight = displayManagerScale.getDeviceHeight();
        mDisplayWidth = displayManagerScale.getDeviceWidth();

    }

    private void initialiseUI() {
        final ActionBar abar = getSupportActionBar();

   /*     View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("My Cart");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        setActionBar();
        getSupportActionBar().setLogo(R.drawable.back);*/
        setDisplayParams();
        getSupportActionBar().hide();

        imgBack = (ImageView)findViewById(R.id.imageBack);
        databaseManager = new DataBaseManager(this);
        mRelDealer = (RelativeLayout) findViewById(R.id.rlDealer);
        mRelRetailer = (RelativeLayout) findViewById(R.id.rlRetailer);
        mTextViewDealer = (CustomTextView) findViewById(R.id.textViewDealer);
        mTextViewRetailer = (CustomTextView) findViewById(R.id.textViewRetailer);
        mDealersListView = (ListView) findViewById(R.id.dealersListView);
        lnrTableHeaders = (LinearLayout) findViewById(R.id.ll2);
        imageViewSubmit = (ImageView) findViewById(R.id.imageViewSearch);
        imageSearchCat = (ImageView) findViewById(R.id.imageViewSearchCat);
        llTop = (LinearLayout) findViewById(R.id.llTop);
        llSecTwo = (LinearLayout) findViewById(R.id.secTwoLL);
        llSecOne = (LinearLayout) findViewById(R.id.secOneLL);
        lnrOne = (LinearLayout) findViewById(R.id.lnrOne);
        txtRefr = (TextView) findViewById(R.id.txtReferenceNumber);
        txtDate = (TextView) findViewById(R.id.txtDate);
        edtSearch = (EditText) findViewById(R.id.editSearch);
        textRetailer = (TextView) findViewById(R.id.textRetailer);
        llCategory = (LinearLayout) findViewById(R.id.secCatLL);
        llSearch = (LinearLayout) findViewById(R.id.secSearchLL);
        labelText = (TextView) findViewById(R.id.textView1);
        edtSearch.setText("");
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                edtSearch.setText("");
                // TODO Auto-generated method stub

                item = arg0.getItemAtPosition(position).toString();


                if (item.equals("Own Retail")) {
                    type = "5"; //1
                    labelText.setText("Retailer : ");
                    AppPrefenceManager.setUserTypeSelected(mActivity, "5");
                    if (AppPrefenceManager.getDealerIdName(mActivity).length() > 0) {

                    } else {
                        mTextViewDealer.setText("Retailer Name");
                    }
                    llSecOne.setVisibility(View.VISIBLE);
                    llSecTwo.setVisibility(View.GONE);
                } else if (item.equals("Franchise")) {
                    /*type = "6";//3

                    llSecOne.setVisibility(View.GONE);
                    llSecTwo.setVisibility(View.VISIBLE);
                    textRetailer.setText("Retailer : ");
                    mTextViewRetailer.setText("Retailer Name");*/

                    type = "6"; //1
                    labelText.setText("Retailer : ");
                    AppPrefenceManager.setUserTypeSelected(mActivity, "6");
                    if (AppPrefenceManager.getDealerIdName(mActivity).length() > 0) {

                    } else {
                        mTextViewDealer.setText("Retailer Name");
                    }
                    llSecOne.setVisibility(View.VISIBLE);
                    llSecTwo.setVisibility(View.GONE);
                } else if (item.equals("Premium Retail")) {
                    type = "7";//3

                    /*llSecOne.setVisibility(View.GONE);
                    llSecTwo.setVisibility(View.VISIBLE);
                    textRetailer.setText("Retailer : ");
                    mTextViewRetailer.setText("Retailer Name");*/
                    labelText.setText("Retailer : ");
                    AppPrefenceManager.setUserTypeSelected(mActivity, "7");
                    if (AppPrefenceManager.getDealerIdName(mActivity).length() > 0) {

                    } else {
                        mTextViewDealer.setText("Retailer Name");
                    }
                    llSecOne.setVisibility(View.VISIBLE);
                    llSecTwo.setVisibility(View.GONE);
                } else {
                    AppPrefenceManager.setUserTypeSelected(mActivity, "0");

                    mTextViewDealer.setText("Retailer Name");
                    llSecOne.setVisibility(View.VISIBLE);
                    llSecTwo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        txtDate.setText("Date :  " + formattedDate);
        txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText("Name : "
                + AppPrefenceManager.getLoginCustomerName(this));
        txtPlace = (TextView) findViewById(R.id.txtPlace);
        txtPlace.setText("Place : " + AppPrefenceManager.getLoginPlace(this));
        txtQty = (TextView) findViewById(R.id.txtTotalQty);
        txtValue = (TextView) findViewById(R.id.txtTotalValue);
        dealersModel = new ArrayList<DealersListModel>();
        getCartData();
        categories.clear();
        categories.add("Please Select");
        categories.add("Own Retail");
        categories.add("Franchise");
        categories.add("Premium Retail");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (AppPrefenceManager.getUserTypeSelected(mActivity).equals("5")) {
            spinner.setSelection(1);
        } else if (AppPrefenceManager.getUserTypeSelected(mActivity).equals("6")) {
            spinner.setSelection(2);

        } else if (AppPrefenceManager.getUserTypeSelected(mActivity).equals("7")) {
            spinner.setSelection(3);

        } else {
            spinner.setSelection(0);
        }

        if (AppPrefenceManager.getUserType(mActivity).equals("6")) { // UserType:Dealer
            mRelDealer.setClickable(false);
            mRelRetailer.setClickable(false);
            VKCObjectConstants.selectedDealerId = "";
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mTextViewDealer.setText(AppPrefenceManager.getUserName(mActivity));
            llTop.setVisibility(View.GONE);
            lnrOne.setVisibility(View.VISIBLE);
            llCategory.setVisibility(View.GONE);
        } else if (AppPrefenceManager.getUserType(mActivity).equals("5")) { // UserType:Retailer
            llCategory.setVisibility(View.GONE);
            llSearch.setVisibility(View.GONE);
            mRelDealer.setClickable(true);
            mRelRetailer.setClickable(false);
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mTextViewRetailer
                    .setText(AppPrefenceManager.getUserName(mActivity));
            mRelDealer.setOnClickListener(new OnClickView());
            llCategory.setVisibility(View.GONE);

        } else if (AppPrefenceManager.getUserType(this).equals("7")) { // UserType:Sub
            llCategory.setVisibility(View.GONE); // Dealer
            mRelRetailer.setVisibility(View.VISIBLE);
            llSearch.setVisibility(View.GONE);
            textRetailer.setVisibility(View.VISIBLE);
            mTextViewDealer.setText("Select Dealer");
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mRelDealer.setClickable(true);
            mRelDealer.setOnClickListener(new OnClickView());
        } else if (AppPrefenceManager.getUserType(mActivity).equals("4")) {
            // Dealer
            llSearch.setVisibility(View.VISIBLE);
            llCategory.setVisibility(View.VISIBLE);
            mRelRetailer.setVisibility(View.VISIBLE);
            textRetailer.setVisibility(View.VISIBLE);

            if (AppPrefenceManager.getDealerIdName(mActivity).equals("")) {
                mTextViewDealer.setText("Dealer");
            } else {
                mTextViewDealer.setText(AppPrefenceManager
                        .getDealerIdName(mActivity));

            }
            if (AppPrefenceManager.getRetailerName(mActivity).equals("")) {
                mTextViewRetailer.setText("Retailer");
            } else {
                mTextViewRetailer.setText(AppPrefenceManager.getRetailerName(mActivity));

            }

            VKCObjectConstants.selectedRetailerId = "";
            /*
             * mRelDealer.setClickable(true); mRelDealer.setOnClickListener(new
             * OnClickView());
             */
        } else { // Usertype:SalesHead
            mRelDealer.setClickable(true);
            llCategory.setVisibility(View.VISIBLE);
            llSearch.setVisibility(View.VISIBLE);
            VKCObjectConstants.selectedSubDealerId = "";
            // mRelRetailer.setClickable(true);
            // mRelDealer.setOnClickListener(new OnClickView());
            // mRelRetailer.setOnClickListener(new OnClickView());
            llCategory.setOnClickListener(new OnClickView());
        }

        imageViewSubmit.setOnClickListener(new OnClickView());
        imageSearchCat.setOnClickListener(new OnClickView());

    }

    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void getCartData() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};//PRODUCT_UNIT
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");
        // System.out.println("13022015:cursor.getCount()::" +
        // cursor.getCount());

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                AppController.cartArrayList.add(setCartModel(cursor));
                cursor.moveToNext();
            }
            if (AppController.cartArrayList.size() > 0) {
                System.out.println("the value of quty"
                        + AppController.quantity.size());

            } else {
                txtQty.setText("Total quantity :  " + "" + 0);
            }
            mSalesAdapter = new SalesOrderAdapter(mActivity,
                    AppController.cartArrayList, lnrTableHeaders, txtQty);
            mSalesAdapter.notifyDataSetChanged();
            mDealersListView.setAdapter(mSalesAdapter);
            SalesOrderFragment.isCart = true;
        } else {
            // VKCUtils.showtoast(mActivity, 9);
            SalesOrderFragment.isCart = false;
        }

    }

    private JSONObject createJson() {

        System.out.println("18022015:Within createJson ");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("user_id",
                    AppPrefenceManager.getUserId(mActivity));
            if (AppPrefenceManager.getUserType(mActivity).equals("")) {
                jsonObject.putOpt("state_code", "");
            } else {
                jsonObject.putOpt("state_code",
                        AppPrefenceManager.getStateCode(mActivity));
            }

            /*
             * jsonObject.putOpt("dealer_id",
             * VKCObjectConstants.selectedDealerId);
             * jsonObject.putOpt("retailer_id",
             * VKCObjectConstants.selectedRetailerId);
             * jsonObject.putOpt("subdealer_id",
             * VKCObjectConstants.selectedSubDealerId);
             */
            jsonObject.putOpt("user_type",
                    type);
            jsonObject.putOpt("dealer_id",
                    AppPrefenceManager.getDealerId(mActivity));
            jsonObject.putOpt("retailer_id",
                    AppPrefenceManager.getRetailerId(mActivity));
            jsonObject.putOpt("subdealer_id",
                    VKCObjectConstants.selectedSubDealerId);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        // ///////
        for (int i = 0; i < AppController.cartArrayList.size(); i++) {
            JSONObject object = new JSONObject();
            try {

                object.putOpt("product_id", AppController.cartArrayList.get(i)
                        .getProdId());
                object.putOpt("size", AppController.cartArrayList.get(i)
                        .getProdSizeId());
                object.putOpt("color_id", AppController.cartArrayList.get(i)
                        .getProdColorId());
                object.putOpt("quantity", AppController.cartArrayList.get(i)
                        .getProdQuantity());
                object.putOpt("unit", "pair");
                System.out.println("unit "
                        + AppController.cartArrayList.get(i).getProd_Unit());
                jsonArray.put(i, object);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // Log.e("18022015 Exception", e.toString());
            }

            // salesOrderArray=jsonArray.toString();
            //  Log.e(" JSONArray length:", "18022015 " + jsonArray.length());
            //  Log.e(" JSONArray length:", "18022015 " + jsonArray.toString());
        }

        try {
            jsonObject.put("order_details", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Log.v("LOG", "20022015 " + jsonObject);

        return jsonObject;

    }

    /**
     * Method to set cart model
     */
    private CartModel setCartModel(Cursor cursor) {
        cartModel = new CartModel();
        cartModel.setProdId(cursor.getString(0));
        cartModel.setProdName(cursor.getString(1));
        cartModel.setProdSizeId(cursor.getString(2));
        cartModel.setProdSize(cursor.getString(3));
        cartModel.setProdColorId(cursor.getString(4));
        cartModel.setProdColor(cursor.getString(5));
        cartModel.setProdQuantity(cursor.getString(6));
        cartModel.setProdGridValue(cursor.getString(7));
        //cartModel.setProd_Unit(cursor.getString(8));
        return cartModel;
    }

    /**
     * Post Api to submit sales order
     */
    public void submitSalesOrder() {
        String name[] = {"salesorder"};
        String values[] = {createJson().toString()};

        System.out.println("18022015:createJson:" + createJson());
        final VKCInternetManager manager = new VKCInternetManager(
                PRODUCT_SALESORDER_SUBMISSION);

        manager.getResponsePOST((Activity) mActivity, name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 success:" + successResponse);
                        parseResponse(successResponse);

                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 Errror:" + failureResponse);
                    }
                });
    }

    public void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseObj = jsonObject.getString("response");
            if (responseObj.equals("1")) {
                VKCUtils.showtoast(CartActivity.this, 15);
                // System.out.println("11111");
                clearDb();
                //System.out.println("2222");
                /*Intent intent=new Intent(CartActivity.this,DashboardFActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
                clearOrderDb();
                //  System.out.println("3333");
                DashboardFActivity.dashboardFActivity.displayView(-1);

                //  System.out.println("4444");

            } else {

                VKCUtils.showtoast(CartActivity.this, 13);
            }
        } catch (Exception e) {

        }
    }

    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(mActivity);
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }

    public void clearOrderDb() {
        DataBaseManager databaseManager = new DataBaseManager(mActivity);
        databaseManager.removeDb(TABLE_ORDERLIST);
    }

    // private void getDealerApi() {
    // final VKCInternetManager manager = new VKCInternetManager(
    // "http://dev.mobatia.com/vkc/api/getstate");
    // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
    //
    // manager.getResponse(mActivity, new ResponseListener() {
    //
    // @Override
    // public void responseSuccess(String successResponse) {
    //
    // parseJSON(successResponse);
    //
    // }
    //
    // @Override
    // public void responseFailure(String failureResponse) {
    // // TODO Auto-generated method stub
    // Log.v("LOG", "04122014FAIL " + failureResponse);
    // // mIsError = true;
    //
    // }
    // });
    //
    // }
    //
    // private void parseJSON(String response)
    // {
    // try {
    // JSONObject respObj = new JSONObject(response);
    // JSONArray respArray = respObj.getJSONArray("states");
    // for (int i = 0; i < respArray.length(); i++) {
    // // JSONObject objState = respArray.getJSONObject(i);
    // dealersModel.add(parseDealerObject(respArray.getJSONObject(i)));
    // // dealersStateModels.add(parseSateObject(respArray
    // // .getJSONObject(i)));
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // }
    //
    // private DealersListModel parseDealerObject(JSONObject objDealer) {
    // DealersListModel dealerModel = new DealersListModel();
    // dealerModel.setId("1");
    // dealerModel.setDealerName("Dealer1");
    //
    // return dealerModel;
    // // TODO Auto-generated method stub
    //
    // }
    /*
     * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
     */
    public Boolean doUserCheck() {

        if (AppPrefenceManager.getUserType(mActivity).equals("4")) { // Saleshead

            if (!(mTextViewDealer.getText().toString().equals(""))// &&
                // !(mTextViewRetailer.getText().toString().equals(""))
            ) {

                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(mActivity).equals("5")) { // Retailer
            if (!(mTextViewDealer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(mActivity).equals("6")) { // Dealer
            if ((mTextViewRetailer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(mActivity).equals("7")) { // Sub
            // Dealer
            if (!(mTextViewDealer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        }
        // if(SalesOrderFragment.isCart==true){
        // return true;
        // }else{
        // return false;
        // }

        return null;

    }

    public class OnClickView implements OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == imageViewSubmit) {

                if (AppController.cartArrayList.size() > 0) {
                    if (doUserCheck()) {
                        if (AppPrefenceManager.getUserType(mActivity).equals(
                                "4")) {
                            if (item.equals("Please Select")) {
                                VKCUtils.showtoast(CartActivity.this, 60);
                            } else {
                                submitSalesOrder();
                            }
                        } else {

                            submitSalesOrder();
                        }
                    } else {

                        VKCUtils.showtoast(CartActivity.this, 16);
                    }
                } else {

                    VKCUtils.showtoast(CartActivity.this, 16);
                }

            }

            if (v == mRelDealer) {
                if (AppPrefenceManager.getUserType(mActivity).equals("7")) {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SubDealer");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                } else if (AppPrefenceManager.getUserType(mActivity)
                        .equals("4")) {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SalesHead");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "Dealer");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                }

            }
            if (v == mRelRetailer) {

                Intent intent = new Intent(mActivity,
                        RetailersListViewOnSearch.class);
                intent.putExtra("mType", "Retailer");
                VKCObjectConstants.textRetailer = mTextViewRetailer;
                startActivity(intent);

            }

            if (v == imageSearchCat) {
                testSearch = edtSearch.getText().toString();
                if (testSearch.length() > 0 && item.equals("Please Select")) {
                    CustomToast toast = new CustomToast(CartActivity.this);
                    toast.show(39);
                } else if (testSearch.length() > 0
                        && !item.equals("Please Select")) {
                    Intent intent = new Intent(mActivity,
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SalesHead");
                    intent.putExtra("key", testSearch);
                    intent.putExtra("type", type);
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    VKCObjectConstants.textRetailer = mTextViewRetailer;
                    startActivity(intent);

                } else {
                    CustomToast toast = new CustomToast(CartActivity.this);
                    toast.show(38);
                }

            }

        }

        private void getMyDealersSalesHeadApi() {
            VKCInternetManager manager = null;
            dealersShopModels.clear();
            // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
            String name[] = {"saleshead_id"};
            String value[] = {AppPrefenceManager.getUserId(mActivity)};
            manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
            manager.getResponsePOST((Activity) mActivity, name, value,
                    new ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {

                            // parseJSON(successResponse);
                            Log.v("LOG", "06012015 " + successResponse);
                            parseMyDealerSalesHeadJSON(successResponse);

                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            // TODO Auto-generated method stub
                            Log.v("LOG", "04122014FAIL " + failureResponse);
                            // mIsError = true;

                        }
                    });

        }

        private void parseMyDealerSalesHeadJSON(String successResponse) {
            // TODO Auto-generated method stub

            try {
                // ArrayList<DealersShopModel> dealersShopModels = new
                // ArrayList<DealersShopModel>();
                JSONObject respObj = new JSONObject(successResponse);
                JSONObject response = respObj.getJSONObject("response");
                String status = response.getString("status");
                if (status.equals("Success")) {
                    JSONArray respArray = response.getJSONArray("dealers");
                    for (int i = 0; i < respArray.length(); i++) {

                        dealersShopModels.add(parseShop(respArray
                                .getJSONObject(i)));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private DealersShopModel parseShop(JSONObject jsonObject) {
            DealersShopModel dealersShopModel = new DealersShopModel();
            dealersShopModel.setAddress(jsonObject.optString("address"));

            dealersShopModel.setCity(jsonObject.optString("city"));
            dealersShopModel.setContact_person(jsonObject
                    .optString("contact_person"));
            dealersShopModel.setDealerId(jsonObject.optString("dealerId"));
            dealersShopModel.setCountry(jsonObject.optString("country"));
            dealersShopModel
                    .setCustomer_id(jsonObject.optString("customer_id"));
            dealersShopModel.setId(jsonObject.optString("id"));
            dealersShopModel.setName(jsonObject.optString("name"));
            dealersShopModel.setPhone(jsonObject.optString("phone"));
            dealersShopModel.setPincode(jsonObject.optString("pincode"));
            dealersShopModel.setState(jsonObject.optString("state"));
            dealersShopModel.setState_name(jsonObject.optString("state_name"));
            return dealersShopModel;

        }
    }
}
