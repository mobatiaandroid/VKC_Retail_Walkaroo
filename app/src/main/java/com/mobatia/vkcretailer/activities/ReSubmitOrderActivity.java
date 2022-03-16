package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.OrderDetailAdapter;
import com.mobatia.vkcretailer.adapter.SubDealerOrderDetailsAdapter;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.DealersShopModel;
import com.mobatia.vkcretailer.model.SubDealerOrderDetailModel;

public class ReSubmitOrderActivity extends BaseActivity implements
		VKCUrlConstants, OnClickListener {

	private Bundle extras;
	SubDealerOrderDetailModel orderModel;
	TextView productId, caseId, mColour, mDate, mTotalQuantity;
	EditText mQuantity;
	Button btnUpdate;
	static int quantity;
	int status = 1, mTotalQty = 0;
	String orderNumber, dealerName, dealerId;
	ListView listOrders;
	Spinner spinnerDealer;
	ArrayList<DealersShopModel> dealersShopModels = new ArrayList<DealersShopModel>();
	List<String> dealerNameList = new ArrayList<>();

	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_subdealer_order_update;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobatia.vkcsalesapp.controller.BaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extras = getIntent().getExtras();
		// int position = extras.getInt("orderPosition");
		orderNumber = extras.getString("orderNumber");
		dealerName = extras.getString("dealerName");
		getSalesOrderDetails(orderNumber);
		initUi();
		final ActionBar abar = getActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Reorder Product");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setActionBar();

		if (extras != null) {

			productId.setText(" " + orderNumber);

		}

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

	private void initUi() {

		getActionBar().setLogo(R.drawable.back);
		productId = (TextView) findViewById(R.id.textProductName);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		spinnerDealer = (Spinner) findViewById(R.id.spinnerDealer);
		listOrders = (ListView) findViewById(R.id.listOrders);
		mTotalQuantity = (TextView) findViewById(R.id.totalQty);
		getMyDealersApi();
		spinnerDealer.setPrompt(dealerName);
		spinnerDealer.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				dealerId = dealersShopModels.get(pos).getDealerId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		btnUpdate.setOnClickListener(this);

	}

	@SuppressLint("NewApi")
	public void setActionBar() {
		// Enable action bar icon_luncher as toggle Home Button
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("");
		actionBar.setTitle("");
		actionBar.show();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(true);

	}

	private void updateOrder() {
		String detail;
		detail = createJson().toString();
		String name[] = { "salesorder" };

		String values[] = { detail, AppPrefenceManager.getUserId(this),
				dealerId, orderNumber };

		final VKCInternetManager manager = new VKCInternetManager(
				SUBMIT_REORDER_URL);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(this, name, values, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				Log.v("LOG", "19022015 success" + successResponse);
				parseResponse(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(ReSubmitOrderActivity.this, 17);

			}
		});
	}

	private void parseResponse(String resultValue) {
		try {
			JSONArray arrayOrders = null;
			JSONObject jsonObjectresponse = new JSONObject(resultValue);

			String result = jsonObjectresponse.getString("response");
			if (result.equals("1")) {
				VKCUtils.showtoast(ReSubmitOrderActivity.this, 26);
				finish();
			}

		} catch (Exception e) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnUpdate:
			updateOrder();
			break;

		}
	}

	private void getMyDealersApi() {
		VKCInternetManager manager = null;

		// Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
		String name[] = { "subdealer_id" };
		String value[] = { AppPrefenceManager.getUserId(this) };
		manager = new VKCInternetManager(LIST_MY_DEALERS_URL);
		manager.getResponsePOST(ReSubmitOrderActivity.this, name, value,
				new ResponseListener() {

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

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item,
						dealerNameList);
				spinnerDealer.setAdapter(dataAdapter);
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
		dealersShopModel.setCustomer_id(jsonObject.optString("customer_id"));
		dealersShopModel.setId(jsonObject.optString("id"));
		dealersShopModel.setName(jsonObject.optString("name"));
		dealersShopModel.setPhone(jsonObject.optString("phone"));
		dealersShopModel.setPincode(jsonObject.optString("pincode"));
		dealersShopModel.setState(jsonObject.optString("state"));
		dealersShopModel.setState_name(jsonObject.optString("state_name"));
		dealerNameList.add(jsonObject.optString("name"));
		return dealersShopModel;

	}

	private JSONObject createJson() {

		System.out.println("18022015:Within createJson ");

		JSONObject jsonObject = new JSONObject();
		// "order_details", "user_id", "dealer_id", "order_id" };
		try {
			jsonObject.putOpt("user_id", AppPrefenceManager.getUserId(this));

			jsonObject.putOpt("dealer_id", dealerId);
			jsonObject.putOpt("order_id", orderNumber);
			/*
			 * status :- 1 - approve, 2- partial approve, 3 - reject Order
			 * details parameter only for status 2 (partial approve),
			 */
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < AppController.subDealerOrderDetailList.size(); i++) {
			JSONObject object = new JSONObject();
			try {

				object.putOpt("product_id",
						AppController.subDealerOrderDetailList.get(i)
								.getProductId());
				object.putOpt("color_id",
						AppController.subDealerOrderDetailList.get(i)
								.getColorId());
				object.putOpt("case_id", AppController.subDealerOrderDetailList
						.get(i).getCaseId());
				object.putOpt("quantity",
						AppController.subDealerOrderDetailList.get(i)
								.getQuantity());
				// object.putOpt("grid_value",cartArrayList.get(i).getProdGridValue());
				jsonArray.put(i, object);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("18022015 Exception", e.toString());
			}

			// salesOrderArray=jsonArray.toString();
			Log.e(" JSONArray length:", "18022015 " + jsonArray.length());
			Log.e(" JSONArray length:", "18022015 " + jsonArray.toString());
		}

		try {
			jsonObject.put("order_details", jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.v("LOG", "20022015 " + jsonObject);

		return jsonObject;

	}

	private void getSalesOrderDetails(String ordrNo) {
		AppController.subDealerOrderDetailList.clear();
		String name[] = { "order_id" };
		String values[] = { ordrNo };

		final VKCInternetManager manager = new VKCInternetManager(
				SUBDEALER_ORDER_DETAILS);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(this, name, values, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				Log.v("LOG", "19022015 success" + successResponse);
				parseResponseOrder(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(ReSubmitOrderActivity.this, 17);

			}
		});
	}

	private void parseResponseOrder(String result) {
		try {

			JSONArray arrayOrders = null;
			JSONObject jsonObjectresponse = new JSONObject(result);
			JSONObject response = jsonObjectresponse.getJSONObject("response");
			String status = response.getString("status");
			if (status.equals("Success")) {
				arrayOrders = response.optJSONArray("orderdetails");
				for (int i = 0; i < arrayOrders.length(); i++) {
					JSONObject obj = arrayOrders.getJSONObject(i);
					SubDealerOrderDetailModel orderModel = new SubDealerOrderDetailModel();
					orderModel.setCaseId(obj.getString("size"));
					orderModel.setColorId(obj.getString("color_id"));
					orderModel.setCost(obj.getString("cost"));
					orderModel.setDescription(obj.getString("description"));
					orderModel.setName(obj.getString("name"));
					orderModel.setOrderDate(obj.getString("order_date"));
					orderModel.setProductId(obj.getString("product_id"));
					orderModel.setQuantity(obj.getString("quantity"));
					orderModel.setSapId(obj.getString("sap_id"));
					orderModel.setCaseDetail(obj.getString("size"));
					orderModel.setDetailid(obj.getString("detailid"));
					orderModel.setColor_code(obj.getString("color_code"));
					AppController.subDealerOrderDetailList.add(orderModel);
					// AppController.TempSubDealerOrderDetailList.add(orderModel);
				}

				OrderDetailAdapter mSalesAdapter = new OrderDetailAdapter(
						ReSubmitOrderActivity.this);
				listOrders.setAdapter(mSalesAdapter);
				for (int j = 0; j < AppController.subDealerOrderDetailList
						.size(); j++) {

					mTotalQty = mTotalQty
							+ Integer
									.parseInt(AppController.subDealerOrderDetailList
											.get(j).getQuantity());
				}

				mTotalQuantity.setText(String.valueOf(mTotalQty));

				// getOrderData();
			}

		}

		catch (Exception e) {
			// isError = true;
		}
	}

}
