/**
 * 
 */
package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.SalesOrderDetailAdapter;
import com.mobatia.vkcretailer.adapter.SalesRepOrderListViewAdapter;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SalesRepOrderModel;

/**
 * @author Vandana Surendranath
 * 
 */
public class SalesOrderDetailActivity extends BaseActivity implements
		VKCUrlConstants, VKCJsonTagConstants {

	private ListView mLstView;
	private TextView mOrdrNumbr;
	private TextView mDate;
	private Bundle extras;
	private ArrayList<SalesRepOrderModel> salesRepOrderModels;
	private boolean isError;
	private View mView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobatia.vkcsalesapp.controller.BaseActivity#getLayoutResourceId()
	 */
	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_salesorderstatus;
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
		initUi();
		setActionBar();
		extras = getIntent().getExtras();
		if (extras != null) {
			mOrdrNumbr.setText("Order number : " + extras.getString("ordr_no"));
			mDate.setText("Order date : " + extras.getString("ordr_date"));
		}
		getSalesOrderDetails(extras.getString("ordr_no"));
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

	/*******************************************************
	 * Method name : initUi Description : initialise UI elements Parameters :
	 * nil Return type : void Date : 29-May-2015 Author : Vandana Surendranath
	 *****************************************************/
	private void initUi() {
		mLstView = (ListView) findViewById(R.id.salesOrderList);
		mOrdrNumbr = (TextView) findViewById(R.id.txtViewOrder);
		mOrdrNumbr.setVisibility(View.VISIBLE);
		mDate = (TextView) findViewById(R.id.txtViewDate);
		mDate.setVisibility(View.VISIBLE);
		mView = findViewById(R.id.view);
		mView.setVisibility(View.VISIBLE);
		salesRepOrderModels = new ArrayList<SalesRepOrderModel>();
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

	/*******************************************************
	 * Method name : getSalesOrderDetails Description : getSalesOrderDetails
	 * from api Parameters : ordrNo Return type : void Date : 29-May-2015 Author
	 * : Vandana Surendranath
	 *****************************************************/
	private void getSalesOrderDetails(String ordrNo) {
		salesRepOrderModels.clear();
		String name[] = { "OrderNo" };
		String values[] = { ordrNo };

		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_SALESORDER_DETAILS);
		/*for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}*/

		manager.getResponsePOST(this, name, values, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				//Log.v("LOG", "19022015 success" + successResponse);
				parseResponse(successResponse);
				if (salesRepOrderModels.size() > 0 && !isError) {
					setAdapter();
				} else {
					VKCUtils.showtoast(SalesOrderDetailActivity.this, 17);
				}

			}

			@Override
			public void responseFailure(String failureResponse) {
				VKCUtils.showtoast(SalesOrderDetailActivity.this, 17);
				isError = true;
			}
		});
	}

	private void setAdapter() {
		mLstView.setAdapter(new SalesOrderDetailAdapter(salesRepOrderModels,
				this));
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
				salesRepOrderModel.setmPrdctName(jsonObjectZero
						.optString(JSON_ORDERSTATUS_PRDCT_NAME));
				salesRepOrderModel.setmPrdctDesc(jsonObjectZero
						.optString(JSON_ORDER_PRDCT_DESC));
				salesRepOrderModels.add(salesRepOrderModel);
			}

		} catch (Exception e) {
			isError = true;
		}
	}

}
