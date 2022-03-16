/**
 * 
 */
package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.ColorGridAdapter.ViewHolder;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.ColorModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

/**
 * @author archana.s
 * 
 */
public class FilterColorContentAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private String[] filterListString;
	private ArrayList<ColorModel> filterArrayList;
	public ArrayList<ColorModel> tempArrayList;

	public FilterColorContentAdapter(Activity mContext,
			ArrayList<ColorModel> filterArrayList,
			ArrayList<ColorModel> tempArrayList) {
		this.mContext = mContext;
		this.filterArrayList = filterArrayList;
		this.tempArrayList = tempArrayList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return filterListString.length;
		return filterArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filterArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.custom_filtercolor, null);

		} else {
			view = convertView;
		}

		GradientDrawable gd = new GradientDrawable();
		
		
		gd.setCornerRadius(50);
		gd.setAlpha(100);
		// gd.setStroke(strokeWidth, strokeColor);
		RelativeLayout relativeMain = (RelativeLayout) view
				.findViewById(R.id.viewColor);
		relativeMain.setBackgroundDrawable(gd);
		final CheckBox chechBoxType = (CheckBox) view
				.findViewById(R.id.checkBoxType);
		if (filterArrayList.get(position).getColorcode().startsWith("#")) {
			gd.setColor(VKCUtils.parseColor((filterArrayList.get(position)
					.getColorcode())));
		} else {
			chechBoxType.setText("No color");
		}
		chechBoxType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final boolean isChecked = chechBoxType.isChecked();
				if (isChecked) {
					// Toast.makeText(mContext, "Clicked " + isChecked, 1000)
					// .show();

					tempArrayList.add(filterArrayList.get(position));
					AppController.tempcolorCatArrayList.add(filterArrayList.get(position).getId());
				} else {
					// Toast.makeText(mContext, "Clicked " + isChecked, 1000)
					// .show();
					tempArrayList.remove(filterArrayList.get(position));
					AppController.tempcolorCatArrayList.remove(filterArrayList.get(position).getId());
				}
			}
		});

		/* To retain a checkbox selection state */
		if (tempArrayList.contains(filterArrayList.get(position))) {
			chechBoxType.setChecked(true);
			// Log.v("LOG","04112014 "+filterArrayList.get(position).getName()
			// );
		} else {
			chechBoxType.setChecked(false);
			// Log.v("LOG","04112014 "+filterArrayList.get(position).getName()
			// );
		}

		return view;
	}

}
