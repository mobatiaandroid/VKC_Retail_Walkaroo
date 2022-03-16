/**
 * 
 */
package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.ProductDetailActivity;
import com.mobatia.vkcretailer.adapter.ColorGridAdapter.ViewHolder;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.CaseModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.SizeModel;

/**
 * @author Archana S
 * 
 */
public class Quantity_Adapter extends BaseAdapter {

	Context mContext;
	private CheckBox box;
	private RelativeLayout viewColor;
	ArrayList<SizeModel> caseModels;
	private String tempdata;
	LayoutInflater mInflater;
	private View view;
	DisplayManagerScale mDisplayManager;
	int width, height;
	int counts;

	public Quantity_Adapter(Context mcontext, int counts) {

		this.mContext = mcontext;
		this.counts = counts;
		mInflater = LayoutInflater.from(mContext);
		getDisplayScale();
	}

	public void validate(TextView textView, String text) {
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (counts != 0) {
			return counts;
		} else {
			return 0;
		}
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

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View holderView;

		if (convertView == null) {

			holderView = mInflater.inflate(R.layout.custom_size_grid, parent,
					false);

		} else {

			holderView = convertView;
		}
		viewColor = (RelativeLayout) holderView.findViewById(R.id.viewColor);
		box = (CheckBox) holderView.findViewById(R.id.checkBoxColor);
		viewColor.setVisibility(holderView.GONE);
		box.setVisibility(holderView.GONE);

		final EditText edttext = (EditText) new ViewHolder()
				.getTextView(holderView);
		AppController.editText = (EditText) new ViewHolder()
				.getTextView(holderView);
		for (int i = 0; i < AppController.positionsize.size(); i++) {
			System.out.println("The length from array is "
					+ AppController.positionsize.get(i)
					+ "The length of selected is" + position);
			if (Integer.parseInt(AppController.positionsize.get(i)) == position) {
				edttext.setEnabled(true);
			}
		}
		edttext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				for (int i = 0; i <= start; i++) {
					System.out.println("The text changed111"
							+ edttext.getText() + "count " + count + "start"
							+ start + "before" + before);
					AppController.listEdit.get(position).setText(edttext.getText().toString());
					AppController.quantity.add(edttext.getText().toString());
					AppController.positionquty.add(String.valueOf(position));

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// System.out.println("The text changed222"
				// + new String(s.toString()));
				System.out.println("The text changed222" + edttext.getText()
						+ "count " + count + "start" + start);

			}

			@Override
			public void afterTextChanged(Editable s) {

				System.out.println("The text changed333"
						+ new String(s.toString()));
				// AppController.quantity.add(new String(s.toString()));
			}

		});

		edttext.setVisibility(holderView.VISIBLE);
		System.out.println("the type of content is"
				+ edttext.getText().toString());

		System.out.println("the length is" + AppController.quantity.size());
		return holderView;
}

	public void getDisplayScale() {
		mDisplayManager = new DisplayManagerScale(mContext);
		width = mDisplayManager.getDeviceWidth();
		height = mDisplayManager.getDeviceHeight();
	}

	public class ViewHolder {

		/**o
		 * @return the view
		 */

		public EditText getTextView(View holderView) {
			return (EditText) holderView.findViewById(R.id.edtquty);
		}
	}
}
