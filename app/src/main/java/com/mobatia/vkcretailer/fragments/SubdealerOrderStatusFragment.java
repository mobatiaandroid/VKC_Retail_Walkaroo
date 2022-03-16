/**
 * 
 */
package com.mobatia.vkcretailer.fragments;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.SubDealerListByCategory;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.model.SalesRepOrderModel;

/**
 * @author Archana.S
 * 
 */
public class SubdealerOrderStatusFragment extends Fragment implements
		VKCUrlConstants, VKCJsonTagConstants, OnClickListener {
	Activity mActivity;
	boolean isError = false;
	private View mRootView;
	ListView mStatusList;
	Button buttonNew, buttonPending, buttonRejected;
	ArrayList<SalesRepOrderModel> salesRepOrderModels = new ArrayList<SalesRepOrderModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.subdealer_order_status_fragment,
				container, false);
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
		buttonNew = (Button) v.findViewById(R.id.btnPending);
		buttonPending = (Button) v.findViewById(R.id.btnApproved);
		buttonRejected = (Button) v.findViewById(R.id.btnRejected);
		buttonNew.setOnClickListener(this);
		buttonPending.setOnClickListener(this);
		buttonRejected.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnPending:
			Intent intent = new Intent(getActivity(),
					SubDealerListByCategory.class);
			intent.putExtra("order_status", "pending");
			intent.putExtra("title", "Pending Orders");
			startActivity(intent);
			break;
		case R.id.btnApproved:
			Intent intent1 = new Intent(getActivity(),
					SubDealerListByCategory.class);
			intent1.putExtra("order_status", "approved");
			intent1.putExtra("title", "Approved Orders");
			startActivity(intent1);
			break;
		case R.id.btnRejected:
			Intent intent2 = new Intent(getActivity(),
					SubDealerListByCategory.class);
			intent2.putExtra("order_status", "reject");
			intent2.putExtra("title", "Rejected Orders");
			startActivity(intent2);
			break;

		}
	}

}
