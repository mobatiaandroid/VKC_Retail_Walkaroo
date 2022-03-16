/**
 * 
 */
package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.ProductDetailActivity;
import com.mobatia.vkcretailer.adapter.ColorGridAdapter.ViewHolder;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.HorizontalListView;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.CaseModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.SizeModel;

/**
 * @author Archana S
 * 
 */
public class SizeGridAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<SizeModel> caseModels;
	LayoutInflater mInflater;
	private View view;
	DisplayManagerScale mDisplayManager;
	int width, height;

	public SizeGridAdapter(Context mcontext, ArrayList<SizeModel> caseModels) {

		this.mContext = mcontext;
		this.caseModels = caseModels;
		mInflater = LayoutInflater.from(mContext);
		getDisplayScale();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (caseModels != null) {
			return caseModels.size();
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

	CheckBox checkBoxTemp;

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

		RelativeLayout relativeMain = new ViewHolder().getColorView(holderView);
		TextView txtSize = new ViewHolder().getTextView(holderView);
		txtSize.setText(caseModels.get(position).getName());

		/*LinearLayout lnrHolder = new ViewHolder().getLinearView(holderView);
		lnrHolder.getLayoutParams().height = (int) (height * 1.8);*/

		final CheckBox checkBox = new ViewHolder().getCheckBox(holderView);

		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				checkBoxTemp = (CheckBox) v;

				if (checkBox.isChecked()) {
					AppController.positionsize.add(String.valueOf(position));
					AppController.sizeName.add(caseModels.get(position)
							.getName());
					AppController.positionsize.add(String.valueOf(position));
					AppController.sizeId.add(caseModels.get(position).getId());

								} else {

					for (int i = 0; i < AppController.sizeName.size(); i++) {
						if (AppController.sizeName.get(i).equalsIgnoreCase(
								caseModels.get(position).getName())) {
							AppController.sizeName.remove(i);
							AppController.sizeId.remove(i);
						}
					}
				}

			}
		});
		
		return holderView;
	}

	public void getDisplayScale() {
		mDisplayManager = new DisplayManagerScale(mContext);
		width = mDisplayManager.getDeviceWidth();
		height = mDisplayManager.getDeviceHeight();
	}

	public class ViewHolder {

		/**
		 * @return the view
		 */
		public RelativeLayout getColorView(View holderView) {
			return (RelativeLayout) holderView.findViewById(R.id.viewColor);
		}

		public CheckBox getCheckBox(View holderView) {
			return (CheckBox) holderView.findViewById(R.id.checkBoxColor);
		}

		public LinearLayout getLinearView(View holderView) {
			return (LinearLayout) holderView.findViewById(R.id.lnrHolder);
		}

		public TextView getTextView(View holderView) {
			return (TextView) holderView.findViewById(R.id.textView1);
		}
	}

}
