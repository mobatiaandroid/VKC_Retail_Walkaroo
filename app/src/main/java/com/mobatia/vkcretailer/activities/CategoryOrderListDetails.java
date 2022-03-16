package com.mobatia.vkcretailer.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcretailer.adapter.SubDealerOrderAdapter;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SubDealerOrderDetailModel;

public class CategoryOrderListDetails extends Activity implements
		VKCUrlConstants, VKCDbConstants, OnClickListener {
	private ListView mLstView;
	private TextView mOrdrNumbr;
	private TextView mDate;
	private Bundle extras;
	DatabaseHelper dataBase;
	LinearLayout llAppRej, llDispatch;
	private boolean isError;
	private View mView;
	int status = 1;
	String orderNumber, flag, listType;
	DataBaseManager databaseManager;
	Button btnApprove, btnReject, btnDispatch;
	CustomToast toast;
	int position;
	private TextView mTotalQuantity;
	int mTotalQty = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobatia.vkcsalesapp.controller.BaseActivity#getLayoutResourceId()
	 */
	/*
	 * @Override protected int getLayoutResourceId() { // TODO Auto-generated
	 * method stub return ; }
	 */
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
		setContentView(R.layout.order_detail_layout);
		AppController.listErrors.clear();
		initUi();
		final ActionBar abar = getActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Orders Details");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

		setActionBar();

		extras = getIntent().getExtras();
		if (extras != null) {
			orderNumber = extras.getString("ordr_no");
			flag = extras.getString("flag");
			listType = extras.getString("listtype");
			if (flag.equals("pending")) {
				llAppRej.setVisibility(View.GONE);
				llDispatch.setVisibility(View.GONE);
				AppController.isEditable = false;

			} else if (flag.equals("approved")) {

				llAppRej.setVisibility(View.GONE);
				llDispatch.setVisibility(View.GONE);
				AppController.isEditable = false;
			}

			position = extras.getInt("position");
			mOrdrNumbr.setText("Order number : " + orderNumber);

		}

		getSalesOrderDetails(orderNumber);

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
		/*
		 * dataBase = new DatabaseHelper(this, DBNAME); databaseManager = new
		 * DataBaseManager(this);
		 */
		llAppRej = (LinearLayout) findViewById(R.id.llAppOrRej);
		llDispatch = (LinearLayout) findViewById(R.id.llDispatch);
		toast = new CustomToast(CategoryOrderListDetails.this);
		mLstView = (ListView) findViewById(R.id.salesOrderList);
		mOrdrNumbr = (TextView) findViewById(R.id.txtViewOrder);
		mOrdrNumbr.setVisibility(View.VISIBLE);
		mDate = (TextView) findViewById(R.id.txtViewDate);
		// mDate.setVisibility(View.VISIBLE);
		mTotalQuantity = (TextView) findViewById(R.id.totalQty);
		mView = findViewById(R.id.view);
		mView.setVisibility(View.VISIBLE);
		btnApprove = (Button) findViewById(R.id.btnApprove);
		btnReject = (Button) findViewById(R.id.btnReject);
		btnDispatch = (Button) findViewById(R.id.btnDispatch);
		btnApprove.setOnClickListener(this);
		btnReject.setOnClickListener(this);
		btnDispatch.setOnClickListener(this);
		
		getActionBar().setLogo(R.drawable.back);
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
				parseResponse(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(CategoryOrderListDetails.this, 17);
				isError = true;
			}
		});
	}

	private void parseResponse(String result) {
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
					orderModel.setCaseId(obj.getString("case_id"));
					orderModel.setColorId(obj.getString("color_id"));
					orderModel.setCost(obj.getString("cost"));
					orderModel.setDescription(obj.getString("description"));
					orderModel.setName(obj.getString("name"));
					orderModel.setOrderDate(obj.getString("order_date"));
					orderModel.setProductId(obj.getString("product_id"));
					orderModel.setQuantity(obj.getString("quantity"));
					orderModel.setSapId(obj.getString("sap_id"));
					orderModel.setCaseDetail(obj.getString("caseName"));
					orderModel.setDetailid(obj.getString("detailid"));
					orderModel.setColor_code(obj.getString("color_code"));
					AppController.subDealerOrderDetailList.add(orderModel);
					AppController.TempSubDealerOrderDetailList.add(orderModel);
				}
				
				SubDealerOrderAdapter mSalesAdapter = new SubDealerOrderAdapter(
						CategoryOrderListDetails.this);
				mLstView.setAdapter(mSalesAdapter);
				for (int j = 0; j < AppController.subDealerOrderDetailList
						.size(); j++) {

					mTotalQty = mTotalQty
							+ Integer
									.parseInt(AppController.subDealerOrderDetailList
											.get(j).getQuantity());
				}

				mTotalQuantity.setText(String.valueOf(mTotalQty));

			}

		}

		catch (Exception e) {
			isError = true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnApprove:
			if (AppController.status != 2) // Status is used to set the status
			{ // of order
				AppController.status = 1;
			}
			if (AppController.listErrors.size() == 0) {
				showConfirmDialog();
			} else {

				CustomToast toast = new CustomToast(this);
				toast.show(30);
			}

			break;

		case R.id.btnReject:
			AppController.status = 3;
			showConfirmDialog();
			break;
		case R.id.btnDispatch:
			AppController.status = 4;
			showConfirmDialog();
			break;

		}
	}

	private void updateOrder() {
		String detail;
		if (AppController.status == 2) {
			detail = createJson().toString();
		} else {
			detail = "";
		}
		String name[] = { "order_id", "status", "orderDetails" };
		String values[] = { orderNumber, String.valueOf(AppController.status),
				detail };

		final VKCInternetManager manager = new VKCInternetManager(
				SET_ORDER_STATUS_API);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(this, name, values, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				Log.v("LOG", "19022015 success" + successResponse);
				parseResponseAfterUpdate(successResponse);

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(CategoryOrderListDetails.this, 17);
				isError = true;
			}
		});
	}

	private void parseResponseAfterUpdate(String result) {
		try {

			JSONArray arrayOrders = null;
			JSONObject jsonObjectresponse = new JSONObject(result);
			JSONObject response = jsonObjectresponse.getJSONObject("response");
			String status = response.getString("status");
			if (status.equals("Success")) {

				CustomToast toast = new CustomToast(this);
				toast.show(28);

				AppController.subDealerModels.clear();
				AppController.TempSubDealerOrderDetailList.clear();
				AppController.subDealerOrderDetailList.clear();
				finish();
			} else {
				Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
			}

		}

		catch (Exception e) {
			isError = true;
		}
	}

	private JSONObject createJson() {

		System.out.println("18022015:Within createJson ");

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.putOpt("order_id", orderNumber);

			jsonObject.putOpt("status", 2);
			/*
			 * status :- 1 - approve, 2- partial approve, 3 - reject Order
			 * details parameter only for status 2 (partial approve),
			 */
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < AppController.TempSubDealerOrderDetailList.size(); i++) {
			JSONObject object = new JSONObject();
			try {

				object.putOpt("order_detail_id",
						AppController.TempSubDealerOrderDetailList.get(i)
								.getDetailid());
				object.putOpt("product_id",
						AppController.TempSubDealerOrderDetailList.get(i)
								.getProductId());
				object.putOpt("new_quantity",
						AppController.TempSubDealerOrderDetailList.get(i)
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

	private void showConfirmDialog() {
		Confirmation_Dialog appExitDialog = new Confirmation_Dialog(this);
		appExitDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		appExitDialog.setCancelable(true);
		appExitDialog.show();

	}

	public class Confirmation_Dialog extends Dialog {

		public Confirmation_Dialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.confirm_dialog);
			init();

		}

		private void init() {

			Button buttonSet = (Button) findViewById(R.id.buttonYes);
			buttonSet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					dismiss();
					updateOrder();

				}
				// alrtDbldr.cancel();

			});
			Button buttonCancel = (Button) findViewById(R.id.buttonNo);
			buttonCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}

	}

}
