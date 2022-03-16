/**
 * 
 */
package com.mobatia.vkcretailer.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.ListImageModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.Related_Images;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * @author mobatia-user
 * 
 */
public class ListImageAdapter extends BaseAdapter {

	Activity mActivity;
	ArrayList<Related_Images> mImageArrayList;
	LayoutInflater mInflater;
	DisplayManagerScale displayManagerScale;
	int viewWidth;
	int viewHeight;
	float dimension;
	int imageWidthHeight;

	public ListImageAdapter(Activity mActivity,
			ArrayList<Related_Images> mImageArrayList) {

		this.mActivity = mActivity;
		this.mImageArrayList = mImageArrayList;
		DisplayManagerScale displayManagerScale = new DisplayManagerScale(
				mActivity);
		imageWidthHeight = displayManagerScale.getDeviceWidth();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mImageArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.custom_griditem, null);
		} else {
			view = convertView;
		}

		ImageView img = (ImageView) view.findViewById(R.id.imageView);
		img.setScaleType(ScaleType.CENTER_CROP);
		final ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar1);

		System.out
				.println("URL:" + mImageArrayList.get(position).getImageurl());
		VKCUtils.setImageFromUrl(mActivity, mImageArrayList.get(position)
				.getImageurl(), img, progressBar);

		return view;
	}

}
