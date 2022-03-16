package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.ColorGridAdapter;
import com.mobatia.vkcretailer.appdialogs.DeleteAlert;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.customview.HorizontalListView;
import com.mobatia.vkcretailer.model.CartModel;
import com.mobatia.vkcretailer.model.ColorModel;

public class OrderedProductList extends BaseActivity {
	private ListView mDealersListView;
	private SalesOrderAdapter mSalesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init(savedInstanceState);
	}

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_ordered_product_list;
	}

	private void init(Bundle savedInstanceState) {

		mDealersListView = (ListView) findViewById(R.id.dealersListView);
		mSalesAdapter = new SalesOrderAdapter(OrderedProductList.this);
		mDealersListView.setAdapter(mSalesAdapter);
	}

}

class SalesOrderAdapter extends BaseAdapter {

	Activity mActivity;
	LayoutInflater mInflater;
	ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();

	public SalesOrderAdapter(BaseActivity mActivity) {
		this.mActivity = mActivity;
		mInflater = LayoutInflater.from(mActivity);
	}

	

	public SalesOrderAdapter(CartActivity mActivity,
			ArrayList<CartModel> cartArrayList, LinearLayout lnrTableHeaders,
			TextView txtQty) {
		this.mActivity = mActivity;
		mInflater = LayoutInflater.from(mActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			view = mInflater.inflate(R.layout.custom_ordered_product_list, null);
			TextView prodId = (TextView) view.findViewById(R.id.txtProdId);
			prodId.setText("1207");
			TextView size = (TextView) view.findViewById(R.id.txtSize);
			size.setText("8,9");
			TextView qty = (TextView) view.findViewById(R.id.txtQuantity);
			qty.setText("50");

			HorizontalListView relColor = (HorizontalListView) view
					.findViewById(R.id.listViewColor);

			ColorModel model1 = new ColorModel();
			model1.setColorcode("#000000");
			ColorModel model2 = new ColorModel();
			model2.setColorcode("#0000FF");
			ColorModel model3 = new ColorModel();
			model3.setColorcode("#a52a2a");
			colorArrayList.add(0, model1);
			colorArrayList.add(1, model2);
			colorArrayList.add(2, model3);

		} else {
			view = convertView;
		}
		return view;
	}

	private void showDeleteDialog(String str) {
		DeleteAlert appDeleteDialog = new DeleteAlert(mActivity);
		appDeleteDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		appDeleteDialog.setCancelable(true);
		appDeleteDialog.show();

	}
}
