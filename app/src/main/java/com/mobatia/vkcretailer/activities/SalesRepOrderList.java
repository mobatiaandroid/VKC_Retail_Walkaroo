package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.SalesRepOrderListViewAdapter;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.model.SalesRepOrderModel;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SalesRepOrderList extends BaseActivity implements VKCUrlConstants {
	ListView salesRepOrderListView;
	ArrayList<SalesRepOrderModel> salesRepOrderModels = new ArrayList<SalesRepOrderModel>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar();
		init(savedInstanceState);
		
	}

	private void init(Bundle savedInstanceState) {
		getActionBar().setLogo(R.drawable.back);
		salesRepOrderListView = (ListView) findViewById(R.id.salesRepOrderListView);

		for (int i = 0; i < 10; i++) {
//			SalesRepOrderModel salesRepOrderModel = new SalesRepOrderModel();
//			salesRepOrderModel.setDate("28/12/2014");
//			salesRepOrderModel.setDealer("Dealer " + i);
//			salesRepOrderModel.setRetailer("Retailer " + i);
//			salesRepOrderModels.add(salesRepOrderModel);

		}
		salesRepOrderListView.setAdapter(new SalesRepOrderListViewAdapter(
				salesRepOrderModels, SalesRepOrderList.this));

		setItemSelectListener(salesRepOrderListView);
	}

	private void setItemSelectListener(ListView salesRepOrderListView2) {
		// TODO Auto-generated method stub
		salesRepOrderListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SalesRepOrderList.this, OrderedProductList.class);
						startActivity(intent);
					}
				});
	}

//	public class SalesRepOrderListViewAdapter extends BaseAdapter {
//		BaseActivity mActivity;
//		ArrayList<SalesRepOrderModel> dealersModels;
//		LayoutInflater mLayoutInflater;
//
//		public SalesRepOrderListViewAdapter(
//				ArrayList<SalesRepOrderModel> dealersModels, BaseActivity mActivity) {
//			this.dealersModels = dealersModels;
//			this.mActivity = mActivity;
//			mLayoutInflater = LayoutInflater.from(mActivity);
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return dealersModels.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return dealersModels.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View view = null;
//			if (convertView == null) {
//				view = mLayoutInflater.inflate(
//						R.layout.custom_salesrep_list_view, null);
//			} else {
//				view = convertView;
//			}
//			WraperClass wraperClass = new WraperClass(view);
//			wraperClass.getNameText().setText(
//					dealersModels.get(position).getDate());
//			wraperClass.getAddressText().setText(
//					dealersModels.get(position).getDealer());
//			wraperClass.getPhoneText().setText(
//					dealersModels.get(position).getRetailer());
//			return view;
//		}
//
//		private class WraperClass {
//			View view = null;
//
//			public WraperClass(View view) {
//				this.view = view;
//				// TODO Auto-generated constructor stub
//			}
//
//			public TextView getNameText() {
//				return (TextView) view.findViewById(R.id.textViewName);
//			}
//
//			public TextView getAddressText() {
//				return (TextView) view.findViewById(R.id.textViewAddress);
//			}
//
//			public TextView getPhoneText() {
//				return (TextView) view.findViewById(R.id.textViewPhone);
//			}
//
//		}
//	}
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

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_salesrep_order_list;
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

}
