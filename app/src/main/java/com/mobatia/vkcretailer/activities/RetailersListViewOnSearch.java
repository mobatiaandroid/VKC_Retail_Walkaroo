/**
 *
 */
package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.DealersListViewAdapter;
import com.mobatia.vkcretailer.appdialogs.StateDistrictPlaceDialog;
import com.mobatia.vkcretailer.appdialogs.StateDistrictPlaceDialog.OnDialogItemSelectListener;
import com.mobatia.vkcretailer.constants.VKCObjectConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.DealersCityModel;
import com.mobatia.vkcretailer.model.DealersDistrictModel;
import com.mobatia.vkcretailer.model.DealersShopModel;
import com.mobatia.vkcretailer.model.DealersStateModel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author mobatia-user
 */
public class RetailersListViewOnSearch extends AppCompatActivity implements
        VKCUrlConstants {

    RelativeLayout rlState;
    RelativeLayout rlDistrict;
    RelativeLayout rlPlace;
    LinearLayout llTop;
    int mDisplayWidth = 0;
    int mDisplayHeight = 0;
    ListView dealersListView;
    boolean mIsError = false;
    TextView textViewState;
    TextView textViewDistrict;
    TextView textViewPlace;
    ImageView imageViewSearch;
    private Activity mActivity;
    private String mType;
    ArrayList<DealersStateModel> dealersStateModels = new ArrayList<DealersStateModel>();
    ArrayList<DealersDistrictModel> districtModels = new ArrayList<DealersDistrictModel>();
    ArrayList<DealersCityModel> cityModels = new ArrayList<DealersCityModel>();
    ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
    String key, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dealers_listview);
        //R.layout.fragment_dealers_listview
        mActivity = this;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mType = extras.getString("mType");

            if (mType.equals("SalesHead")) {

                key = extras.getString("key");
                userType = extras.getString("type");
            }
        }

        initialiseUI();
    }

    private void initialiseUI() {
        getSupportActionBar().setLogo(R.drawable.back);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        imageViewSearch = (ImageView) findViewById(R.id.imageViewSearch);
        textViewState = (TextView) findViewById(R.id.textViewState);
        textViewDistrict = (TextView) findViewById(R.id.textViewDistrict);
        textViewPlace = (TextView) findViewById(R.id.textViewPlace);

        rlState = (RelativeLayout) findViewById(R.id.rlState);
        rlDistrict = (RelativeLayout) findViewById(R.id.rlDistrict);
        rlPlace = (RelativeLayout) findViewById(R.id.rlPlace);
        llTop = (LinearLayout) findViewById(R.id.llTop);

        dealersListView = (ListView) findViewById(R.id.dealersListView);

        // setLayoutAlignment(v);
        rlState.setOnClickListener(new ClickOnView());
        rlDistrict.setOnClickListener(new ClickOnView());
        rlPlace.setOnClickListener(new ClickOnView());
        imageViewSearch.setOnClickListener(new ClickOnView());
        dealersListView.setOnItemClickListener(new ClickOnItemView());
        if (VKCUtils.checkInternetConnection(mActivity)) {
            if (AppPrefenceManager.getUserType(this).equals("7")) {
                llTop.setVisibility(View.GONE);
                getMyDealersApi();
            } else if (AppPrefenceManager.getUserType(this).equals("4")) {
                llTop.setVisibility(View.GONE);
                // getMyDealersApi();
                getMyDealersSalesHeadApi();
            } else {
                getStateApi();
            }
        } else {
            mIsError = true;
            // CustomToast toast = new CustomToast(mActivity);
            // toast.show(0);
            VKCUtils.showtoast(mActivity, 0);
        }
    }

    private void setDisplayParams() {
        DisplayManagerScale displayManagerScale = new DisplayManagerScale(
                mActivity);
        mDisplayHeight = displayManagerScale.getDeviceHeight();
        mDisplayWidth = displayManagerScale.getDeviceWidth();

    }

    private void setLayoutAlignment(View v) {
        int height = (int) (mDisplayHeight * .35);
        llTop.getLayoutParams().height = (int) height;
        LinearLayout secOneLL = (LinearLayout) v.findViewById(R.id.secOneLL);
        LinearLayout secTwoLL = (LinearLayout) v.findViewById(R.id.secTwoLL);
        LinearLayout secThreeLL = (LinearLayout) v
                .findViewById(R.id.secThreeLL);
        RelativeLayout secFourRL = (RelativeLayout) v
                .findViewById(R.id.secFourRL);
        secOneLL.getLayoutParams().height = ((int) height) / 4;
        secTwoLL.getLayoutParams().height = ((int) height) / 4;
        secThreeLL.getLayoutParams().height = ((int) height) / 4;
        secFourRL.getLayoutParams().height = ((int) height) / 4;
    }

    private void parseJSON(String successResponse) {
        // TODO Auto-generated method stub

        try {
            JSONObject respObj = new JSONObject(successResponse);
            JSONArray respArray = respObj.getJSONArray("states");
            for (int i = 0; i < respArray.length(); i++) {
                // JSONObject objState = respArray.getJSONObject(i);

                dealersStateModels.add(parseSateObject(respArray
                        .getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private DealersStateModel parseSateObject(JSONObject objState) {
        DealersStateModel stateModel = new DealersStateModel();
        stateModel.setStateCode(objState.optString("state"));
        stateModel.setStateName(objState.optString("state_name"));

        return stateModel;
        // TODO Auto-generated method stub

    }

    private void getStateApi() {
        final VKCInternetManager manager = new VKCInternetManager(
                DEALERS_GETSTATE);
        Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());

        manager.getResponse(mActivity, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                parseJSON(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                mIsError = true;

            }
        });

    }

    private void getDistrictApi(final String stateCode) {
        VKCInternetManager manager = null;

        if (mType.equals("Retailer")) {
            manager = new VKCInternetManager(GET_RETAILERS);
        } else if (mType.equals("Dealer")) {
            manager = new VKCInternetManager(GET_DEALERS);
        } else if (mType.equals("SalesHead")) {
            manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
        }
        // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
        String name[] = {"state"};
        String value[] = {stateCode};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                Log.v("LOG", "06012015 " + successResponse);
                parseDistrictJSON(successResponse, stateCode);

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                mIsError = true;

            }
        });

    }

    private void parseDistrictJSON(String successResponse, String stateCode) {
        // TODO Auto-generated method stub
        districtModels = new ArrayList<DealersDistrictModel>();
        try {
            JSONObject jsonObject = new JSONObject(successResponse);
            JSONArray respArray = jsonObject.getJSONArray("response");
            for (int i = 0; i < respArray.length(); i++) {
                districtModels.add(parseDistrict(respArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private DealersDistrictModel parseDistrict(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        ArrayList<DealersCityModel> cityModels = new ArrayList<DealersCityModel>();
        DealersDistrictModel dealersDistrictModel = new DealersDistrictModel();
        dealersDistrictModel.setDistrictName(jsonObject.optString("district"));
        JSONArray arrayShops;
        try {
            arrayShops = jsonObject.getJSONArray("city");
            for (int i = 0; i < arrayShops.length(); i++) {
                cityModels.add(parseCity(arrayShops.getJSONObject(i)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dealersDistrictModel.setCityModels(cityModels);
        return dealersDistrictModel;

    }

    private DealersCityModel parseCity(JSONObject jsonObject) {
        ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
        DealersCityModel cityModel = new DealersCityModel();
        cityModel.setCityName(jsonObject.optString("city"));
        Log.v("LOG", "06012015 " + cityModel.getCityName());
        JSONArray arrayShops;
        try {
            arrayShops = jsonObject.getJSONArray("shop");
            for (int i = 0; i < arrayShops.length(); i++) {
                dealersShopModels.add(parseShop(arrayShops.getJSONObject(i)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cityModel.setDealersShopModels(dealersShopModels);
        return cityModel;

    }

    private DealersShopModel parseShop(JSONObject jsonObject) {
        DealersShopModel dealersShopModel = new DealersShopModel();
        dealersShopModel.setAddress(jsonObject.optString("address"));

        dealersShopModel.setCity(jsonObject.optString("city"));
        dealersShopModel.setContact_person(jsonObject
                .optString("contact_person"));
        dealersShopModel.setDealerId(jsonObject.optString("dealerId"));
        dealersShopModel.setCountry(jsonObject.optString("country"));
        dealersShopModel.setCustomer_id(jsonObject.optString("customer_id"));
        dealersShopModel.setId(jsonObject.optString("id"));
        dealersShopModel.setName(jsonObject.optString("name"));
        dealersShopModel.setPhone(jsonObject.optString("phone"));
        dealersShopModel.setPincode(jsonObject.optString("pincode"));
        dealersShopModel.setState(jsonObject.optString("state"));
        dealersShopModel.setState_name(jsonObject.optString("state_name"));
        return dealersShopModel;

    }

    private class ClickOnView implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v == rlState) {
                dealersListView.setVisibility(View.INVISIBLE);
                String[] states = new String[dealersStateModels.size()];
                for (int i = 0; i < dealersStateModels.size(); i++) {
                    states[i] = dealersStateModels.get(i).getStateName();
                }
                StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
                        mActivity, states, textViewState, "States",
                        new OnDialogItemSelectListener() {

                            @Override
                            public void itemSelected(int position) {
                                // TODO Auto-generated method stub
                                getDistrictApi(dealersStateModels.get(position)
                                        .getStateCode());
                            }
                        });
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.show();
                textViewPlace.setText("");
                textViewDistrict.setText("");

            }
            if (v == rlDistrict) {
                dealersListView.setVisibility(View.INVISIBLE);
                if (textViewState.getText().length() != 0) {
                    String[] district = new String[districtModels.size()];
                    for (int i = 0; i < districtModels.size(); i++) {
                        district[i] = districtModels.get(i).getDistrictName();
                    }
                    StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
                            mActivity, district, textViewDistrict, "District",
                            new OnDialogItemSelectListener() {

                                @Override
                                public void itemSelected(int position) {
                                    // TODO Auto-generated method stub
                                    cityModels.clear();
                                    cityModels.addAll(districtModels.get(
                                            position).getCityModels());

                                }
                            });
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);
                    dialog.show();
                    textViewPlace.setText("");
                } else {
                    // CustomToast toast = new CustomToast(mActivity);
                    // toast.show(20);
                    VKCUtils.showtoast(mActivity, 20);
                }
            }
            if (v == rlPlace) {
                dealersListView.setVisibility(View.INVISIBLE);
                if (textViewDistrict.getText().length() != 0) {
                    String[] city = new String[cityModels.size()];
                    for (int i = 0; i < cityModels.size(); i++) {
                        city[i] = cityModels.get(i).getCityName();
                    }
                    StateDistrictPlaceDialog dialog = new StateDistrictPlaceDialog(
                            mActivity, city, textViewPlace, "Place",
                            new OnDialogItemSelectListener() {

                                @Override
                                public void itemSelected(int position) {
                                    dealersShopModels.clear();
                                    dealersShopModels.addAll(cityModels.get(
                                            position).getDealersShopModels());

                                }

                            });
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);
                    dialog.show();
                } else {
                    // CustomToast toast = new CustomToast(mActivity);
                    // toast.show(21);
                    VKCUtils.showtoast(mActivity, 21);
                }
            }
            if (v == imageViewSearch) {
                if (textViewPlace.getText().length() != 0) {
                    setDealerShopList(dealersShopModels);
                } else {
                    // CustomToast toast = new CustomToast(mActivity);
                    // toast.show(22);
                    VKCUtils.showtoast(mActivity, 22);
                }
            }
        }

    }

    private void setDealerShopList(ArrayList<DealersShopModel> dealersShopModels) {
        dealersListView.setVisibility(View.VISIBLE);
        dealersListView.setAdapter(new DealersListViewAdapter(
                dealersShopModels, mActivity));

    }

    private class ClickOnItemView implements OnItemClickListener {

        /*
         * (non-Javadoc)
         *
         * @see
         * android.widget.AdapterView.OnItemClickListener#onItemClick(android
         * .widget.AdapterView, android.view.View, int, long)
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            System.out.println("mType---" + mType);
            if (mType.equals("Retailer")) {
                VKCObjectConstants.textRetailer.setText(dealersShopModels.get(
                        position).getName());
                VKCObjectConstants.selectedRetailerId = dealersShopModels.get(
                        position).getId();
				/*AppPrefenceManager.setRetailerId(mActivity, dealersShopModels.get(
						position).getId());
				AppPrefenceManager.setRetailerName(mActivity, dealersShopModels.get(
						position).getName());*/
            } else if (mType.equals("Dealer")) {
                VKCObjectConstants.textDealer.setText(dealersShopModels.get(
                        position).getName());
                VKCObjectConstants.selectedDealerId = dealersShopModels.get(
                        position).getId();
                AppPrefenceManager.setDealerId(mActivity, dealersShopModels.get(
                        position).getId());
                AppPrefenceManager.setDealerIdName(mActivity, dealersShopModels.get(
                        position).getName());
                System.out.println("dealer name---" + dealersShopModels.get(
                        position).getName());

				/*AppPrefenceManager.setRetailerId(mActivity, dealersShopModels.get(
						position).getId());
				AppPrefenceManager.setRetailerName(mActivity, dealersShopModels.get(
						position).getName());*/
            } else if (mType.equals("SubDealer")) {

                VKCObjectConstants.textDealer.setText(dealersShopModels.get(
                        position).getName());
                String did = dealersShopModels.get(position).getDealerId();
                Log.i("dis", "" + did);
                VKCObjectConstants.selectedDealerId = dealersShopModels.get(
                        position).getDealerId();
            } else if (mType.equals("SalesHead")) {
                if (userType.equalsIgnoreCase("6")) {
                    VKCObjectConstants.textDealer.setText(dealersShopModels
                            .get(position).getName());
                    String did = dealersShopModels.get(position).getDealerId();
                   // Log.i("dis", "" + did);
                    VKCObjectConstants.selectedDealerId = dealersShopModels
                            .get(position).getDealerId();
                    AppPrefenceManager.setDealerId(mActivity, dealersShopModels.get(position).getDealerId());
                    AppPrefenceManager.setDealerIdName(mActivity, dealersShopModels.get(position).getName());
                    finish();
                } else if (userType.equalsIgnoreCase("5")) {
                    VKCObjectConstants.textDealer.setText(dealersShopModels
                            .get(position).getName());
                    String did = dealersShopModels.get(position).getDealerId();
                   // Log.i("dis", "" + did);
                    VKCObjectConstants.selectedDealerId = dealersShopModels
                            .get(position).getDealerId();
                    AppPrefenceManager.setDealerId(mActivity, dealersShopModels.get(position).getDealerId());
                    AppPrefenceManager.setDealerIdName(mActivity, dealersShopModels.get(position).getName());
                    finish();
                } else if (userType.equalsIgnoreCase("7")) {
                    VKCObjectConstants.textDealer.setText(dealersShopModels
                            .get(position).getName());
                  //  String did = dealersShopModels.get(position).getDealerId();
                    VKCObjectConstants.selectedDealerId = dealersShopModels
                            .get(position).getDealerId();
                    AppPrefenceManager.setDealerId(mActivity, dealersShopModels.get(position).getDealerId());
                    AppPrefenceManager.setDealerIdName(mActivity, dealersShopModels.get(position).getName());
                    finish();
                }
				/*AppPrefenceManager.setRetailerId(mActivity, dealersShopModels.get(
						position).getDealerId());
				AppPrefenceManager.setRetailerName(mActivity, dealersShopModels.get(
						position).getName());
				//AppPrefenceManager.setDealerId(mActivity, dealersShopModels);
*/
            }


        }

    }

    private void getMyDealersApi() {
        VKCInternetManager manager = null;
        dealersShopModels.clear();
        // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
        String name[] = {"subdealer_id"};
        String value[] = {AppPrefenceManager.getUserId(this)};
        Log.v("LOG", "04122014 CACHE " + value[0] + "--" + name[0]);
        manager = new VKCInternetManager(LIST_MY_DEALERS_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                Log.v("LOG", "06012015 " + successResponse);
                parseMyDealerJSON(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                mIsError = true;

            }
        });

    }

    private void parseMyDealerJSON(String successResponse) {
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

                    dealersShopModels
                            .add(parseShop(respArray.getJSONObject(i)));
                }
                dealersListView.setVisibility(View.VISIBLE);
                dealersListView.setAdapter(new DealersListViewAdapter(
                        dealersShopModels, mActivity));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getMyDealersSalesHeadApi() {
        VKCInternetManager manager = null;
        dealersShopModels.clear();
        // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
        String name[] = {"saleshead_id", "customer_type", "search_value"};
        String value[] = {AppPrefenceManager.getCustomerId(this), userType, key};//AppPrefenceManager.getUserId(this)
        manager = new VKCInternetManager(LIST_MY_DEALERS_SALES_HEAD_URL);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                //Log.v("LOG", "06012015 " + successResponse);
                parseMyDealerSalesHeadJSON(successResponse);
                if (dealersShopModels.size() == 0) {
                    CustomToast toast = new CustomToast(mActivity);
                    toast.show(37);
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                mIsError = true;

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

                    dealersShopModels
                            .add(parseShop(respArray.getJSONObject(i)));
                }
                dealersListView.setVisibility(View.VISIBLE);
                dealersListView.setAdapter(new DealersListViewAdapter(
                        dealersShopModels, mActivity));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
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

	/*@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_dealers_listview;
	}
*/
}
