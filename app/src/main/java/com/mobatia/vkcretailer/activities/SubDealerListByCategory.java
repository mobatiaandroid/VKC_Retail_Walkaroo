package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.SubDealerOrderListAdapter;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SubDealerOrderListModel;

public class SubDealerListByCategory extends BaseActivity implements
		VKCUrlConstants {
	Activity mActivity;
	Bundle extras;
	ArrayList<SubDealerOrderListModel> subDealerModels;
	int mTotalQty = 0;
	ListView mStatusList;
	SubDealerOrderListAdapter adapter;
	String listType, status, title, flag;
	TextView mTotalQuantity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mStatusList = (ListView) findViewById(R.id.salesOrderList);
		subDealerModels = new ArrayList<>();
		final ActionBar abar = getActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		getActionBar().setLogo(R.drawable.back);
		setActionBar();
		extras = getIntent().getExtras();
		if (extras != null) {
			status = extras.getString("order_status");
			title = extras.getString("title");
			flag = extras.getString("flag");
			if (status.equals("pending")) {
				listType = "pending";
			} else if (status.equals("approved")) {
				listType = "approved";
			} else if (status.equals("reject")) {
				listType = "reject";
			}
			textviewTitle.setText(title.toString());
			getSalesOrderStatus(listType);
		}
		mStatusList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (status.equals("reject")) {
					Intent intent = new Intent(SubDealerListByCategory.this,
							ReSubmitOrderActivity.class);
					intent.putExtra("orderNumber",
							AppController.subDealerModels.get(position)
									.getOrderid());
					intent.putExtra("dealerName", AppController.subDealerModels
							.get(position).getName());
					intent.putExtra("dealerId", AppController.subDealerModels
							.get(position).getDealerId());

					startActivity(intent);

				} else {
					Intent intent = new Intent(SubDealerListByCategory.this,
							CategoryOrderListDetails.class);
					intent.putExtra("ordr_no", AppController.subDealerModels
							.get(position).getOrderid());
					intent.putExtra("listtype", listType);
					if (listType.equals("pending")) {
						intent.putExtra("flag", "pending");
					} else if (listType.equals("approved")) {
						intent.putExtra("flag", "approved");
					} else if (listType.equals("reject")) {
						intent.putExtra("flag", "reject");
					}
					startActivity(intent);
				}
			}
		});

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

	private void getSalesOrderStatus(String listType) {
		AppController.subDealerModels.clear();
		String name[] = { "subdealer_id", "list_type" };
		String values[] = { AppPrefenceManager.getUserId(this), listType };

		final VKCInternetManager manager = new VKCInternetManager(
				SUBDEALER_ORDER_URL_LIST);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(this, name, values, new ResponseListener() {
			@Override
			public void responseSuccess(String successResponse) {
				Log.v("LOG", "19022015 success" + successResponse);
				parseResponse(successResponse);

				/*
				 * if (subDealerModels.size() > 0) { //setAdapter(); } else {
				 * VKCUtils.showtoast(getActivity(), 17); }
				 */
			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(SubDealerListByCategory.this, 17);

			}
		});
	}

	/*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */

	private void parseResponse(String result) {
		try {
			JSONArray arrayOrders = null;
			JSONObject jsonObjectresponse = new JSONObject(result);
			JSONObject response = jsonObjectresponse.getJSONObject("response");
			String status = response.getString("status");
			if (status.equals("Success")) {

				/* if (response.has("orders")) { */
				arrayOrders = response.optJSONArray("orders");
				// }

				// int len = arrayOrders.length();

				for (int i = 0; i < arrayOrders.length(); i++) {
					SubDealerOrderListModel orderModel = new SubDealerOrderListModel();
					JSONObject obj = arrayOrders.optJSONObject(i);
					// JSONArray arrayDetail=obj.getJSONArray("orderDetails");
					orderModel.setName(obj.getString("name"));
					// System.out.println("Name:"+orderModel.getName());
					orderModel.setOrderid(obj.getString("orderid"));
					orderModel.setAddress(obj.getString("city"));
					orderModel.setPhone(obj.getString("phone"));
					orderModel.setTotalqty(obj.getString("total_qty"));
					orderModel.setStatus(obj.getString("status"));
					AppController.subDealerModels.add(orderModel);

				}

				adapter = new SubDealerOrderListAdapter(this,
						AppController.subDealerModels);
				// adapter.notifyDataSetChanged();
				mStatusList.setAdapter(adapter);
				
			}

		} catch (Exception e) {

		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		getSalesOrderStatus(listType);
	}

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_salesorderstatus;
	}

}
