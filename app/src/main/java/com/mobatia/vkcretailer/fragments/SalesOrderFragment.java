/**
 *
 */
package com.mobatia.vkcretailer.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.mobatia.vkcretailer.activities.DashboardFActivity;
import com.mobatia.vkcretailer.activities.RetailersListViewOnSearch;
import com.mobatia.vkcretailer.adapter.SalesOrderAdapter;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCObjectConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomTextView;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.CartModel;
import com.mobatia.vkcretailer.model.DealersListModel;
import com.mobatia.vkcretailer.model.DealersShopModel;

/**
 * @author Archana.S
 */
public class SalesOrderFragment extends Fragment implements VKCDbConstants,
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
    FragmentTransaction mFragmentTransaction;
    FragmentManager mFragmentManager;

    private LinearLayout lnrTableHeaders;
    private ImageView imageViewSubmit, imageSearchCat;
    private String salesOrderArray;
    private ArrayList<DealersListModel> dealersModel;
    public static Boolean isCart = false;
    private LinearLayout lnrOne, llCategory, llSearch;
    private LinearLayout llTop;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_salesorder, container,
                false);

        final ActionBar abar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        View viewActionBar = getActivity().getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(

                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("My Cart");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        setHasOptionsMenu(true);
        abar.setHomeAsUpIndicator(R.drawable.back);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
        setDisplayParams();
        AppController.isCart = true;
        init(mRootView, savedInstanceState);
        getCartData();

        return mRootView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon


        switch (item.getItemId())
        {
            case android.R.id.home:
                mFragmentManager = getActivity().getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();

                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", "VALUE");
                // bundle.putSerializable("OBJECT", displayView);

                homeFragment.setArguments(bundle);
                mFragmentTransaction.add(R.id.frame_container, homeFragment,
                        "Home");

                mFragmentTransaction.commit();
                break;

        }
        return true;
    }

    private void setDisplayParams() {
        DisplayManagerScale displayManagerScale = new DisplayManagerScale(
                getActivity());
        mDisplayHeight = displayManagerScale.getDeviceHeight();
        mDisplayWidth = displayManagerScale.getDeviceWidth();

    }

    private void init(View v, Bundle savedInstanceState) {
        databaseManager = new DataBaseManager(getActivity());
        mRelDealer = (RelativeLayout) v.findViewById(R.id.rlDealer);
        mRelRetailer = (RelativeLayout) v.findViewById(R.id.rlRetailer);
        mTextViewDealer = (CustomTextView) v.findViewById(R.id.textViewDealer);
        mTextViewRetailer = (CustomTextView) v
                .findViewById(R.id.textViewRetailer);
        mDealersListView = (ListView) v.findViewById(R.id.dealersListView);
        lnrTableHeaders = (LinearLayout) v.findViewById(R.id.ll2);
        imageViewSubmit = (ImageView) v.findViewById(R.id.imageViewSearch);
        imageSearchCat = (ImageView) v.findViewById(R.id.imageViewSearchCat);
        llTop = (LinearLayout) v.findViewById(R.id.llTop);
        lnrOne = (LinearLayout) v.findViewById(R.id.lnrOne);
        txtRefr = (TextView) v.findViewById(R.id.txtReferenceNumber);
        txtDate = (TextView) v.findViewById(R.id.txtDate);
        edtSearch = (EditText) v.findViewById(R.id.editSearch);
        textRetailer = (TextView) v.findViewById(R.id.textRetailer);
        llCategory = (LinearLayout) v.findViewById(R.id.secCatLL);
        llSearch = (LinearLayout) v.findViewById(R.id.secSearchLL);
        labelText = (TextView) v.findViewById(R.id.textView1);
        edtSearch.setText("");
        spinner = (Spinner) v.findViewById(R.id.spinner);
        mTextViewDealer.setText(AppPrefenceManager.getDealerIdName(getActivity()));
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                edtSearch.setText("");
                // TODO Auto-generated method stub
                item = arg0.getItemAtPosition(position).toString();

/*

                if (item.equals("Dealer")) {
                    type = "6"; //1
                    labelText.setText("Dealer : ");
                    mTextViewDealer.setText("Dealer Name");
                } else if (item.equals("Retailer")) {
                    type = "5"; //3
                    // labelText.setText("Sub Dealer : ");
                    // mTextViewDealer.setText("Sub Dealer Name");
                }
*/


                if (item.equals("Own Retail")) {
                    type = "5"; //1
                    labelText.setText("Retailer : ");

                    AppPrefenceManager.setUserTypeSelected(getActivity(), "5");
                    if (AppPrefenceManager.getDealerIdName(getActivity()).length() > 0) {

                    } else {
                        mTextViewDealer.setText("Retailer Name");
                    }

                } else if (item.equals("Franchise")) {
                    /*type = "6";//3

                    llSecOne.setVisibility(View.GONE);
                    llSecTwo.setVisibility(View.VISIBLE);
                    textRetailer.setText("Retailer : ");
                    mTextViewRetailer.setText("Retailer Name");*/

                    type = "6"; //1
                    AppPrefenceManager.setUserTypeSelected(getActivity(), "6");

                    labelText.setText("Retailer : ");

                    if (AppPrefenceManager.getDealerIdName(getActivity()).length() > 0) {

                    } else {
                        mTextViewDealer.setText("Retailer Name");
                    }

                } else if (item.equals("Premium Retail")) {
                    type = "7";//3

                    /*llSecOne.setVisibility(View.GONE);
                    llSecTwo.setVisibility(View.VISIBLE);
                    textRetailer.setText("Retailer : ");
                    mTextViewRetailer.setText("Retailer Name");*/
                    labelText.setText("Retailer : ");
                    AppPrefenceManager.setUserTypeSelected(getActivity(), "7");

                    if (AppPrefenceManager.getDealerIdName(getActivity()).length() > 0) {

                    } else {
                        mTextViewDealer.setText("Retailer Name");
                    }
                } else {
                    AppPrefenceManager.setUserTypeSelected(getActivity(), "0");

                    mTextViewDealer.setText("Retailer Name");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        txtDate.setText("Date :  " + formattedDate);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtName.setText("Name : "
                + AppPrefenceManager.getLoginCustomerName(getActivity()));
        txtPlace = (TextView) v.findViewById(R.id.txtPlace);
        txtPlace.setText("Place : "
                + AppPrefenceManager.getLoginPlace(getActivity()));
        txtQty = (TextView) v.findViewById(R.id.txtTotalQty);
        txtValue = (TextView) v.findViewById(R.id.txtTotalValue);
        dealersModel = new ArrayList<DealersListModel>();
        categories.clear();
       /* categories.add("Dealer");
        // categories.add("Sub Dealer");
        categories.add("Retailer");*/
        categories.add("Please Select");
        categories.add("Own Retail");
        categories.add("Franchise");
        categories.add("Premium Retail");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (AppPrefenceManager.getUserTypeSelected(getActivity()).equals("5")) {
            spinner.setSelection(1);
        } else if (AppPrefenceManager.getUserTypeSelected(getActivity()).equals("6")) {
            spinner.setSelection(2);

        } else if (AppPrefenceManager.getUserTypeSelected(getActivity()).equals("7")) {
            spinner.setSelection(3);

        } else {
            spinner.setSelection(0);
        }


        if (AppPrefenceManager.getUserType(getActivity()).equals("6")) { // UserType:Dealer
            mRelDealer.setClickable(false);
            mRelRetailer.setClickable(false);
            VKCObjectConstants.selectedDealerId = "";
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mTextViewDealer.setText(AppPrefenceManager
                    .getUserName(getActivity()));
            llTop.setVisibility(View.GONE);
            lnrOne.setVisibility(View.VISIBLE);
            llCategory.setVisibility(View.GONE);
        } else if (AppPrefenceManager.getUserType(getActivity()).equals("5")) { // UserType:Retailer
            llCategory.setVisibility(View.GONE);
            llSearch.setVisibility(View.GONE);
            mRelDealer.setClickable(true);
            mRelRetailer.setClickable(false);
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mTextViewRetailer.setText(AppPrefenceManager
                    .getUserName(getActivity()));
            mRelDealer.setOnClickListener(new OnClickView());
            llCategory.setVisibility(View.GONE);

        } else if (AppPrefenceManager.getUserType(getActivity()).equals("7")) { // UserType:Sub
            llCategory.setVisibility(View.GONE); // Dealer
            mRelRetailer.setVisibility(View.VISIBLE);
            llSearch.setVisibility(View.GONE);
            textRetailer.setVisibility(View.VISIBLE);
            mTextViewDealer.setText("Select Dealer");
            VKCObjectConstants.selectedSubDealerId = "";
            VKCObjectConstants.selectedRetailerId = "";
            mRelDealer.setClickable(true);
            mRelDealer.setOnClickListener(new OnClickView());
        } else if (AppPrefenceManager.getUserType(getActivity()).equals("4")) {
            // Dealer
            llSearch.setVisibility(View.VISIBLE);
            llCategory.setVisibility(View.VISIBLE);
            mRelRetailer.setVisibility(View.VISIBLE);
            textRetailer.setVisibility(View.VISIBLE);
            //	mTextViewDealer.setText("Dealer");

            if (AppPrefenceManager.getDealerIdName(getActivity()).equals("")) {
                mTextViewDealer.setText("Select Dealer");
            } else {
                mTextViewDealer.setText(AppPrefenceManager
                        .getDealerIdName(getActivity()));

            }
            if (AppPrefenceManager.getRetailerName(getActivity()).equals("")) {
                mTextViewRetailer.setText("Select Retailer");
            } else {
                mTextViewRetailer.setText(AppPrefenceManager.getRetailerName(getActivity()));

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

    /**
     * Method to retrieve items added to cart
     */
    private void getCartData() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};//PRODUCT_UNIT
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");

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
            mSalesAdapter = new SalesOrderAdapter(getActivity(),
                    AppController.cartArrayList, lnrTableHeaders, txtQty);
            mSalesAdapter.notifyDataSetChanged();
            mDealersListView.setAdapter(mSalesAdapter);
            SalesOrderFragment.isCart = true;
        } else {
            VKCUtils.showtoast(getActivity(), 9);
            SalesOrderFragment.isCart = false;
        }

    }

    private JSONObject createJson() {

        // System.out.println("18022015:Within createJson ");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("user_id",
                    AppPrefenceManager.getUserId(getActivity()));
            if (AppPrefenceManager.getUserType(getActivity()).equals("")) {
                jsonObject.putOpt("state_code", "");
            } else {
                jsonObject.putOpt("state_code",
                        AppPrefenceManager.getStateCode(getActivity()));
            }

			/*jsonObject.putOpt("dealer_id", VKCObjectConstants.selectedDealerId);
			jsonObject.putOpt("retailer_id",
					VKCObjectConstants.selectedRetailerId);
			jsonObject.putOpt("subdealer_id",
					VKCObjectConstants.selectedSubDealerId);*/
            jsonObject.putOpt("user_type",
                    type);
            jsonObject.putOpt("dealer_id", AppPrefenceManager.getDealerId(getActivity()));
            jsonObject.putOpt("retailer_id",
                    AppPrefenceManager.getRetailerId(getActivity()));
            jsonObject.putOpt("subdealer_id",
                    VKCObjectConstants.selectedSubDealerId);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
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
                Log.e("18022015 Exception", e.toString());
            }

            // salesOrderArray=jsonArray.toString();
            /*Log.e(" JSONArray length:", "18022015 " + jsonArray.length());
            Log.e(" JSONArray length:", "18022015 " + jsonArray.toString());*/
        }

        try {
            jsonObject.put("order_details", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       /* Log.v("LOG", "20022015 " + jsonObject);
        System.out.println("the json response is" + jsonObject.toString());*/
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
        // cartModel.setProd_Unit(cursor.getString(8));
        return cartModel;
    }

    /**
     * Post Api to submit sales order
     */
    public void submitSalesOrder() {
        String name[] = {"salesorder"};
        String values[] = {createJson().toString()};

       // System.out.println("18022015:createJson:" + createJson());
        final VKCInternetManager manager = new VKCInternetManager(
                PRODUCT_SALESORDER_SUBMISSION);

        manager.getResponsePOST(getActivity(), name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        //Log.v("LOG", "18022015 success:" + successResponse);
                        parseResponse(successResponse);

                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 Errror:" + failureResponse);
                    }
                });
    }

    public void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseObj = jsonObject.getString("response");
            if (responseObj.equals("1")) {
                VKCUtils.showtoast(getActivity(), 15);
                clearDb();

                DashboardFActivity.dashboardFActivity.displayView(-1);
                mFragmentManager = getActivity().getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();

                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", "VALUE");
                // bundle.putSerializable("OBJECT", displayView);

                homeFragment.setArguments(bundle);
                mFragmentTransaction.add(R.id.frame_container, homeFragment,
                        "Home");

                mFragmentTransaction.commit();

            } else {

                VKCUtils.showtoast(getActivity(), 13);
            }
        } catch (Exception e) {

        }
    }

    public void clearDb() {
        DataBaseManager databaseManager = new DataBaseManager(getActivity());
        databaseManager.removeDb(TABLE_SHOPPINGCART);
    }

    public void clearOrderDb() {
        DataBaseManager databaseManager = new DataBaseManager(getActivity());
        databaseManager.removeDb(TABLE_ORDERLIST);
    }

    /*
     * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
     */
    public Boolean doUserCheck() {

        if (AppPrefenceManager.getUserType(getActivity()).equals("4")) { // Saleshead

            if (!(mTextViewDealer.getText().toString().equals(""))// &&
                // !(mTextViewRetailer.getText().toString().equals(""))
            ) {

                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(getActivity()).equals("5")) { // Retailer
            if (!(mTextViewDealer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(getActivity()).equals("6")) { // Dealer
            if ((mTextViewRetailer.getText().toString().equals(""))) {
                return true;
            } else {
                return false;
            }
        } else if (AppPrefenceManager.getUserType(getActivity()).equals("7")) { // Sub
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
                // submitSalesOrder(); /// need to comment
               // createJson();
                if (AppController.cartArrayList.size() > 0) {
                    if (doUserCheck()) {
                        if (mTextViewDealer.getText().toString().trim()
                                .equalsIgnoreCase("")
                                || mTextViewRetailer.getText().toString()
                                .trim().equalsIgnoreCase("")) {
                            VKCUtils.showtoast(getActivity(), 36);
                        } else {
                            if (AppPrefenceManager.getUserType(getActivity())
                                    .equals("4")) {
                                if (item.equals("Please Select") && (mTextViewDealer.getText().toString().trim()
                                        .equalsIgnoreCase("")
                                        || mTextViewRetailer.getText().toString()
                                        .trim().equalsIgnoreCase(""))) {
                                    VKCUtils.showtoast(getActivity(), 11);
                                } else {
                                    submitSalesOrder();
                                }
                            } else {

                                submitSalesOrder();
                            }
                        }
                    } else {

                        VKCUtils.showtoast(getActivity(), 16);
                    }
                } else {

                    VKCUtils.showtoast(getActivity(), 16);
                }

            }

            if (v == mRelDealer) {
                if (AppPrefenceManager.getUserType(getActivity()).equals("7")) {
                    Intent intent = new Intent(getActivity(),
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SubDealer");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                } else if (AppPrefenceManager.getUserType(getActivity())
                        .equals("4")) {
                    Intent intent = new Intent(getActivity(),
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "SalesHead");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(),
                            RetailersListViewOnSearch.class);
                    intent.putExtra("mType", "Dealer");
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    startActivity(intent);
                }

            }
            if (v == mRelRetailer) {

                Intent intent = new Intent(getActivity(),
                        RetailersListViewOnSearch.class);
                intent.putExtra("mType", "Retailer");
                VKCObjectConstants.textRetailer = mTextViewRetailer;
                startActivity(intent);

            }

            if (v == imageSearchCat) {
                testSearch = edtSearch.getText().toString();
                if (testSearch.length() > 0 && item.equals("Please Select")) {
                    CustomToast toast = new CustomToast(getActivity());
                    toast.show(39);
                } else if (testSearch.length() > 0
                        && !item.equals("Please Select")) {
                    Intent intent = new Intent(getActivity(),
                            RetailersListViewOnSearch.class);
                    edtSearch.setText("");
                    intent.putExtra("mType", "SalesHead");
                    intent.putExtra("key", testSearch);
                    intent.putExtra("type", type);
                    VKCObjectConstants.textDealer = mTextViewDealer;
                    VKCObjectConstants.textRetailer = mTextViewRetailer;
                    startActivity(intent);

                } else {
                    CustomToast toast = new CustomToast(getActivity());
                    toast.show(38);
                }

            }

        }

        private void getMyDealersSalesHeadApi() {
            VKCInternetManager manager = null;
            dealersShopModels.clear();
            String name[] = {"saleshead_id"};
            String value[] = {AppPrefenceManager.getUserId(getActivity())};
            manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
            manager.getResponsePOST(getActivity(), name, value,
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
