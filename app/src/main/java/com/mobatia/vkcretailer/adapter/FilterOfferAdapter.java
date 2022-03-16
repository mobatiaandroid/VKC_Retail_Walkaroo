package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.FilterActivity;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.OfferModel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author archana
 *
 */
public class FilterOfferAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private String[] filterListString;
	private ArrayList<OfferModel> offerModels;
	public ArrayList<OfferModel> tempOfferModels;
	public FilterOfferAdapter(Activity mContext,ArrayList<OfferModel> offerModels,ArrayList<OfferModel> tempArrayList)
	{
		this.mContext=mContext;
		this.offerModels=offerModels;
		this.tempOfferModels=tempArrayList;
		mInflater=LayoutInflater.from(mContext); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return filterListString.length;
		return offerModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return offerModels.get(position);
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
		
		
		if(convertView==null)
		{
			view=mInflater.inflate(R.layout.custom_filtercontent, null);
			
			
		}else{
			view=convertView;
		}
		
		final CheckBox chechBoxType=(CheckBox)view.findViewById(R.id.checkBoxType);
		chechBoxType.setText(offerModels.get(position).getName()+"%");
		chechBoxType.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        final boolean isChecked = chechBoxType.isChecked();
				if (isChecked) {
//					Toast.makeText(mContext, "Clicked " + isChecked, 1000)
//							.show();
					
					tempOfferModels.add(offerModels.get(position));
					AppController.tempofferCatArrayList.add(offerModels.get(position).getId());
				} else {
//					Toast.makeText(mContext, "Clicked " + isChecked, 1000)
//							.show();
					tempOfferModels.remove(offerModels.get(position));
					AppController.tempofferCatArrayList.remove(offerModels.get(position).getId());
				}
		    }
		});
		

		

	/*	To retain a checkbox selection state*/
		if(tempOfferModels.contains( offerModels.get(position))){
			chechBoxType.setChecked(true);
			//Log.v("LOG","04112014 "+filterArrayList.get(position).getName() );
		}else{
			chechBoxType.setChecked(false);
			//Log.v("LOG","04112014 "+filterArrayList.get(position).getName() );
		}

		return view;
	}

}
