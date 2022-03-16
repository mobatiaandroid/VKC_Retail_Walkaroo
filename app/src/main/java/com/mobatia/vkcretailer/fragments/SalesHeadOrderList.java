package com.mobatia.vkcretailer.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.SalesHeadOrderDetailsActivity;
import com.mobatia.vkcretailer.adapter.SubDealerOrderListAdapter;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SubDealerOrderListModel;

public class SalesHeadOrderList extends Fragment implements VKCUrlConstants {

	Activity mActivity;

	private View mRootView;
	ListView orderList;
	static String mDealerName;
	ArrayList<SubDealerOrderListModel> subDealerModels = new ArrayList<SubDealerOrderListModel>();
	FragmentTransaction mFragmentTransaction;
	FragmentManager mFragmentManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.sales_head_list_fragment,
				container, false);

		mActivity = getActivity();
		final androidx.appcompat.app.ActionBar abar = ((AppCompatActivity) getActivity()).getSupportActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("My Orders");
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				// Center the textview in the ActionBar !
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		setHasOptionsMenu(true);
		abar.setHomeAsUpIndicator(R.drawable.back);
		abar.setDisplayHomeAsUpEnabled(true);
		abar.setHomeButtonEnabled(true);
		init(mRootView, savedInstanceState);
		getOrderList();
		return mRootView;
	}

	private void init(View v, Bundle savedInstanceState) {

		orderList = (ListView) v.findViewById(R.id.salesHeadOrderList);
		orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						SalesHeadOrderDetailsActivity.class);
				intent.putExtra("orderNumber", subDealerModels.get(position)
						.getOrderid());
				/*
				 * intent.putExtra("dealerName", subDealerModels.get(position)
				 * .getName()); intent.putExtra("dealerId",
				 * subDealerModels.get(position) .getDealerId());
				 */

				startActivity(intent);

			}
		});

	}

	private void getOrderList() {
		subDealerModels.clear();
		AppController.subDealerorderList.clear();
		AppController.subDealerModels.clear();
		String name[] = { "saleshead_id", "list_type" };
		String values[] = { AppPrefenceManager.getUserId(mActivity), "all" };

		final VKCInternetManager manager = new VKCInternetManager(
				SUBDEALER_ORDER_URL);
		/*for (int i = 0; i < name.length; i++) {
			Log.v("LOG", "12012015 name : " + name[i]);
			Log.v("LOG", "12012015 values : " + values[i]);

		}*/

		manager.getResponsePOST(mActivity, name, values,
				new ResponseListener() {
					@Override
					public void responseSuccess(String successResponse) {
						//Log.v("LOG", "19022015 success" + successResponse);
						parseResponse(successResponse);

					}

					@Override
					public void responseFailure(String failureResponse) {
						VKCUtils.showtoast(getActivity(), 17);

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

				if (response.has("orders")) {
					arrayOrders = response.optJSONArray("orders");
					// }

					//int len = arrayOrders.length();

					for (int i = 0; i < arrayOrders.length(); i++) {
						SubDealerOrderListModel orderModel = new SubDealerOrderListModel();
						JSONObject obj = arrayOrders.getJSONObject(i);

						orderModel.setName(obj.getString("retailerName"));
						orderModel.setOrderid(obj.getString("orderid"));
						orderModel.setAddress(obj.getString("retailerCity"));
						orderModel.setPhone(obj.getString("retailerPhone"));
						orderModel.setTotalqty(obj.getString("total_qty"));
						orderModel.setStatus(obj.getString("status"));

						// JSONArray JsonarrayOrders = obj
						// .optJSONArray("orderDetails");
						//

						subDealerModels.add(orderModel);
						AppController.subDealerModels.add(orderModel);
/*
						System.out.println("Order List"
								+ AppController.subDealerorderList);*/

					}
					if (subDealerModels.size() > 0) {
						SubDealerOrderListAdapter adapter = new SubDealerOrderListAdapter(
								getActivity(), subDealerModels);
						orderList.setAdapter(adapter);

					}

					else {
						CustomToast toast = new CustomToast(getActivity());
						toast.show(32);
					}

				}
			}

		} catch (Exception e) {
			System.out.println("Exception is" + e);

		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SubDealerOrderListAdapter adapter = new SubDealerOrderListAdapter(
				getActivity(), AppController.subDealerModels);
		orderList.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId())
		{
			case android.R.id.home:
				mFragmentManager = getActivity().getSupportFragmentManager();
				mFragmentTransaction = mFragmentManager.beginTransaction();

				HomeFragment homeFragment = new HomeFragment();
				Bundle bundle = new Bundle();
				bundle.putString("NAME", "VALUE");
				// bundle.putSerializable("OBJECT", displayView);

				homeFragment.setArguments(bundle);
				mFragmentTransaction.add(R.id.frame_container, homeFragment,
						"Home");

				mFragmentTransaction.commit();
				break;

		}
		return true;
	}
}