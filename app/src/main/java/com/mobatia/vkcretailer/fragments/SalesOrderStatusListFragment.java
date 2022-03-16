/**
 * 
 */
package com.mobatia.vkcretailer.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.SubDealerStatusListActivity;
import com.mobatia.vkcretailer.adapter.SalesRepOrderListViewAdapter;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SalesRepOrderModel;

/**
 * @author Archana.S
 * 
 */
public class SalesOrderStatusListFragment extends Fragment implements
		VKCUrlConstants, VKCJsonTagConstants, OnClickListener {
	Activity mActivity;
	boolean isError = false;
	private View mRootView;
	ListView mStatusList;
	Button buttonNew, buttonPending;
	ArrayList<SalesRepOrderModel> salesRepOrderModels = new ArrayList<SalesRepOrderModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater
				.inflate(R.layout.fragment_status, container, false);
		mActivity = getActivity();
		final ActionBar abar = getActivity().getActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Order Status");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setHasOptionsMenu(true);
		AppController.isCart=false;
		init(mRootView, savedInstanceState);
		// getSalesOrderStatus();
		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {
		buttonNew = (Button) v.findViewById(R.id.btnNewOrder);
		buttonPending = (Button) v.findViewById(R.id.btnPendingDespatch);
		buttonNew.setOnClickListener(this);
		buttonPending.setOnClickListener(this);

		// mStatusList = (ListView) v.findViewById(R.id.salesOrderList);
		/*
		 * mStatusList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { Intent intent = new Intent(getActivity(),
		 * SalesOrderDetailActivity.class); intent.putExtra("ordr_no",
		 * salesRepOrderModels.get(position) .getmOrderNo());
		 * intent.putExtra("ordr_date", salesRepOrderModels.get(position)
		 * .getmOrderDate()); startActivity(intent); } });
		 */

	}

	private void getSalesOrderStatus() {
		salesRepOrderModels.clear();
		String name[] = { "user_id" };
		String values[] = { AppPrefenceManager.getUserId(getActivity()) };

		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_SALESORDER_STATUS);
		for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						Log.v("LOG", "19022015 success" + successResponse);
						parseResponse(successResponse);
						if (salesRepOrderModels.size() > 0 && !isError) {
							setAdapter();
						} else {
							VKCUtils.showtoast(getActivity(), 17);
						}

					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);
						isError = true;
					}
				});
	}

	private void setAdapter() {
		mStatusList.setAdapter(new SalesRepOrderListViewAdapter(
				salesRepOrderModels, getActivity()));
	}

	private void parseResponse(String response) {
		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
			for (int j = 0; j < jsonArrayresponse.length(); j++) {
				SalesRepOrderModel salesRepOrderModel = new SalesRepOrderModel();
				JSONObject jsonObjectZero = jsonArrayresponse.getJSONObject(j);

				salesRepOrderModel.setmOrderNo(jsonObjectZero
						.optString(JSON_ORDERSTATUS_NO));
				salesRepOrderModel.setmOrderQty(jsonObjectZero
						.optString(JSON_ORDERSTATUS_QTY));
				salesRepOrderModel.setmPendingQty(jsonObjectZero
						.optString(JSON_ORDERSTATUS_PEN_QTY));
				salesRepOrderModel.setmOrderDate(jsonObjectZero
						.optString(JSON_ORDERSTATUS_DATE));
				salesRepOrderModel.setmCompany(jsonObjectZero
						.optString(JSON_ORDERSTATUS_COMPANY));
				// salesRepOrderModel.setmPrdctName(jsonObjectZero
				// .optString(JSON_ORDERSTATUS_PRDCT_NAME));
				salesRepOrderModel.setmCustomerName(jsonObjectZero
						.optString(JSON_ORDERSTATUS_NAME));
				salesRepOrderModels.add(salesRepOrderModel);
			}

		} catch (Exception e) {
			isError = true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnNewOrder:
			Intent intent = new Intent(getActivity(),
					SubDealerStatusListActivity.class); 
			intent.putExtra("order_status","new"
					 );
			intent.putExtra("title","New Orders"
					 );
			 startActivity(intent);
			break;
		case R.id.btnPendingDespatch:
			Intent intent1 = new Intent(getActivity(),
					SubDealerStatusListActivity.class); 
			intent1.putExtra("order_status","pending"
					 );
			intent1.putExtra("title","Pending Dispatch"
					 );
			 startActivity(intent1);
			break;

		}
	}

}
