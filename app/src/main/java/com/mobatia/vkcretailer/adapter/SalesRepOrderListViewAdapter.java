/**
 * 
 */
package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SalesRepOrderModel;

/**
 * @author mobatia-user
 * 
 */
public class SalesRepOrderListViewAdapter extends BaseAdapter {
	Activity mActivity;
	ArrayList<SalesRepOrderModel> dealersModels;
	LayoutInflater mLayoutInflater;

	public SalesRepOrderListViewAdapter(
			ArrayList<SalesRepOrderModel> dealersModels, BaseActivity mActivity) {
		this.dealersModels = dealersModels;
		this.mActivity = mActivity;
		mLayoutInflater = LayoutInflater.from(mActivity);
	}

	public SalesRepOrderListViewAdapter(
			ArrayList<SalesRepOrderModel> dealersModels, Activity mActivity) {
		this.dealersModels = dealersModels;
		this.mActivity = mActivity;
		mLayoutInflater = LayoutInflater.from(mActivity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dealersModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dealersModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = mLayoutInflater.inflate(R.layout.custom_salesrep_list_view,
					null);
		} else {
			view = convertView;
		}
		WraperClass wraperClass = new WraperClass(view);
		wraperClass.getOrderNoText().setText(
				"Order Number : " + dealersModels.get(position).getmOrderNo());
		wraperClass.getProductName()
				.setText(
						"Product Name : "
								+ dealersModels.get(position).getmPrdctName());
		wraperClass.getCustomerName()
				.setText(
						"Customer Name : "
								+ dealersModels.get(position).getmOrderQty());
		wraperClass.getOrderQtyText().setText(
				"Order Quantity : "
						+ dealersModels.get(position).getmOrderQty());
		wraperClass.getPendingText().setText(
				"Pending Quantity : "
						+ dealersModels.get(position).getmPendingQty());

		wraperClass.getOrderDateText().setText(
				"Order Date : "
						+ VKCUtils.formatDateWithInput(
								dealersModels.get(position).getmOrderDate(),
								"dd MMM yyyy", "yyyy-MM-dd hh:mm:ss"));

		return view;
	}

	private class WraperClass {
		View view = null;

		public WraperClass(View view) {
			this.view = view;
			// TODO Auto-generated constructor stub
		}

		public TextView getOrderNoText() {
			return (TextView) view.findViewById(R.id.textViewOrderNo);
		}

		public TextView getProductName() {
			return (TextView) view.findViewById(R.id.textViewPrdctNo);
		}

		public TextView getCustomerName() {
			return (TextView) view.findViewById(R.id.textViewCustomerName);
		}

		public TextView getOrderQtyText() {
			return (TextView) view.findViewById(R.id.textViewOrderQty);
		}

		public TextView getPendingText() {
			return (TextView) view.findViewById(R.id.textViewPending);
		}

		public TextView getOrderDateText() {
			return (TextView) view.findViewById(R.id.textViewOrderDate);
		}

	}

}
