/**
 * 
 */
package com.mobatia.vkcretailer.fragments;

import org.json.JSONObject;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.ArticleListActivity;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;

import android.app.ActionBar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author mobatia-user
 * 
 */
public class FeedbackFragment extends Fragment implements OnClickListener,
		VKCJsonTagConstants, VKCUrlConstants {

	private View mRootView;
	int mDisplayWidth = 0;
	int mDisplayHeight = 0;
	private TextView mTxtName;
	private EditText mEdtComplaint;
	private EditText mEdtName;
	private Button mBtnSend;
	private RadioGroup mRadioGroup;
	private RelativeLayout mRelFeedback;
	private RadioButton mRadioCustomer;
	private RadioButton mRadioProductLaunch;
	private String mFeedbackType;
	EditText edtSearch;
	int selectedId;
	private RadioButton mRadioButton;
	private String testSearch;
	private ImageView imageSearchCat;
	private String type = "0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_feedback, container,
				false);
		setDisplayParams();
		init(mRootView, savedInstanceState);
		AppController.isCart = false;
		final ActionBar abar = getActivity().getActionBar();

		View viewActionBar = getActivity().getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Feedback");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);
		return mRootView;

	}

	private void setDisplayParams() {
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				getActivity());
		mDisplayHeight = displayManagerScale.getDeviceHeight();
		mDisplayWidth = displayManagerScale.getDeviceWidth();

	}

	private void init(final View v, Bundle savedInstanceState) {

		mTxtName = (TextView) v.findViewById(R.id.txtName);
		mEdtComplaint = (EditText) v.findViewById(R.id.etMessage);
		mBtnSend = (Button) v.findViewById(R.id.imgSend);
		mEdtName = (EditText) v.findViewById(R.id.etName);
		mRelFeedback = (RelativeLayout) v.findViewById(R.id.relFeedback);
		mEdtName.setText(AppPrefenceManager.getUserName(getActivity()));
		mRadioCustomer = (RadioButton) v.findViewById(R.id.radioCustomer);
		edtSearch = (EditText) v.findViewById(R.id.editSearch);
		imageSearchCat = (ImageView) v.findViewById(R.id.imageViewSearchCat);
		imageSearchCat.setOnClickListener(this);
		mRadioProductLaunch = (RadioButton) v.findViewById(R.id.radioLaunch);
		mRadioGroup = (RadioGroup) v.findViewById(R.id.relativeType);
		mFeedbackType = "1";
		if (!AppPrefenceManager.getUserType(getActivity()).equals("4")) {
			mRelFeedback.setVisibility(View.GONE);
		}
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				int selectedId = mRadioGroup.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				mRadioButton = (RadioButton) v.findViewById(selectedId);
				if (mRadioButton.getText().toString().equals("Product Launch")) {
					mFeedbackType = "1";
				} else if (mRadioButton.getText().toString().equals("Customer")) {
					mFeedbackType = "2";
				}

			}
		});

		mBtnSend.setOnClickListener(this);
	}

	/**
	 * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
	 * 2015 Author:Archana.S
	 * 
	 */
	public void FeedbackSubmitApi() {
		String name[] = { "feedbacktype", "user_id", "message", "article_no" };
		String values[] = { mFeedbackType,
				AppPrefenceManager.getUserId(getActivity()),
				mEdtComplaint.getText().toString(),
				AppController.articleNumber.trim() };
		final VKCInternetManager manager = new VKCInternetManager(
				PRODUCT_FEEDBACK);

		manager.getResponsePOST(getActivity(), name, values,
				new ResponseListener() {

					@Override
					public void responseSuccess(String successResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 success" + successResponse);
						parseResponse(successResponse);
					}

					@Override
					public void responseFailure(String failureResponse) {
						// TODO Auto-generated method stub
						Log.v("LOG", "18022015 Errror" + failureResponse);
					}
				});
	}

	/**
	 * Method Name:parseResponse Return Type:void parameters:response Date:Feb
	 * 18, 2015 Author:Archana.S
	 * 
	 */
	public void parseResponse(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			String responseResult = jsonObject
					.getString(JSON_FEEDBACK_RESPONSE);
			if (responseResult.equals(JSON_FEEDBACK_SUCCESS)) {

				VKCUtils.showtoast(getActivity(), 14);
				clearEditText();

			} else if (responseResult.equals(JSON_FEEDBACK_FAILED)) {

				VKCUtils.showtoast(getActivity(), 13);
			}

		} catch (Exception e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == mBtnSend) {

			if (mEdtComplaint.getText().toString().equals("")) {
				VKCUtils.textWatcherForEditText(mEdtComplaint,
						"Mandatory field");
				VKCUtils.setErrorForEditText(mEdtComplaint, "Mandatory field");
			} else {
				FeedbackSubmitApi();
			}

		}
		if (v == imageSearchCat) {
			testSearch = edtSearch.getText().toString().trim();
			if (testSearch.length() == 0) {
				CustomToast toast = new CustomToast(getActivity());
				toast.show(38);
			} else if (testSearch.length() > 0) {
				Intent intent = new Intent(getActivity(),
						ArticleListActivity.class);

				intent.putExtra("key", testSearch);

				startActivity(intent);

			} else {
				CustomToast toast = new CustomToast(getActivity());
				toast.show(38);
			}
		}

	}

	private void clearEditText() {
		mEdtComplaint.setText("");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!AppController.articleNumber.equalsIgnoreCase("")) {
			edtSearch.setText(AppController.articleNumber.toString());
		}

	}

}
