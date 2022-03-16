package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.model.CategoryModel;

public class FilterCategoryMainContentAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private String[] filterListString;
	private ArrayList<CategoryModel> filterArrayList;
	public ArrayList<CategoryModel> tempArrayList;

	public FilterCategoryMainContentAdapter(Activity mContext,
			ArrayList<CategoryModel> filterArrayList,
			ArrayList<CategoryModel> tempArrayList) {
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
			view = mInflater.inflate(R.layout.custom_filtercontent, null);

		} else {
			view = convertView;
		}

		final CheckBox chechBoxType = (CheckBox) view
				.findViewById(R.id.checkBoxType);
		chechBoxType.setText(filterArrayList.get(position).getName());
		chechBoxType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final boolean isChecked = chechBoxType.isChecked();
				if (isChecked) {
					

					tempArrayList.add(filterArrayList.get(position));
					AppController.tempmainCatArrayList.add(filterArrayList.get(position).getId());
System.out.println("id selected---"+filterArrayList.get(position).getId());
				} else {
					
					//tempArrayList.remove(filterArrayList.get(position));
					removeCategoryModel(filterArrayList.get(position));
					AppController.tempmainCatArrayList.remove(filterArrayList.get(position).getId());

				}
			}
		});
		// for (CategoryModel categoryModel : tempArrayList) {
		// Log.v("LOG", "13012015 tempArrayList " + categoryModel.getName());
		// }
		// for (CategoryModel categoryModel : filterArrayList) {
		// Log.v("LOG", "13012015 filterArrayList " + categoryModel.getName());
		// }
		//
		// /* To retain a checkbox selection state */
		// if (tempArrayList.contains(filterArrayList.get(position))) {
		// chechBoxType.setChecked(true);
		// // Log.v("LOG", "13012015 " +
		// // filterArrayList.get(position).getName());
		// } else {
		// chechBoxType.setChecked(false);
		// // Log.v("LOG","13012015 "+filterArrayList.get(position).getName()
		// // );
		// }
		setCheckBoxStatus(tempArrayList, filterArrayList, chechBoxType,
				position);

		return view;
	}
	private void removeCategoryModel(CategoryModel categoryModel){
		//for(CategoryModel categoryModelLoop:tempArrayList)
			for(int i=0;i<tempArrayList.size();i++)
				
			
		{
			if(categoryModel.getName().equals(tempArrayList.get(i).getName())){
				tempArrayList.remove(i);
			}
		}
		
	}

	private void setCheckBoxStatus(ArrayList<CategoryModel> tempArrayList,
			ArrayList<CategoryModel> filterArrayList, CheckBox chechBoxType,
			int position) {

		for (CategoryModel model : tempArrayList) {
			if (filterArrayList.get(position).getName().equals(model.getName())) {
				chechBoxType.setChecked(true);
			} /*else {
				chechBoxType.setChecked(false);
			}*/
		}

	}

}
