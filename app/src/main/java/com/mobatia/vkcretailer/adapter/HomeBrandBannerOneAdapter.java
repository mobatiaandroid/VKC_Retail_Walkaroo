package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.DashboardFActivity;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandBannerModel;

public class HomeBrandBannerOneAdapter extends PagerAdapter {

	Activity mActivity;
	View layout;
	TextView pagenumber;
	Button click;

	int displayHeight;
	int displayWidth;
	ArrayList<BrandBannerModel> brandBannerModels;

	// DisplayView displayView;

	public HomeBrandBannerOneAdapter(Activity mActivity,
			ArrayList<BrandBannerModel> brandBannerModels/*
														 * , DisplayView
														 * displayView
														 */) {
		this.mActivity = mActivity;
		this.brandBannerModels = brandBannerModels;
		// this.displayView = displayView;
		setDisplayParam(mActivity);

	}

	private void setDisplayParam(Activity activity) {
		DisplayManagerScale mDisplayManager = new DisplayManagerScale(activity);
		displayHeight = mDisplayManager.getDeviceHeight();
		displayWidth = mDisplayManager.getDeviceWidth();
	}

	@Override
	public int getCount() {
		return brandBannerModels.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		layout = inflater
				.inflate(R.layout.custom_home_offer_banner_pager, null);

		setViews(layout, position);
		((ViewPager) container).addView(layout, 0);

		return layout;
	}

	class WraperClass {
		View view;

		public WraperClass(View view) {
			this.view = view;
		}

		public ImageView getImageBanner() {
			ImageView imageView;

			imageView = (ImageView) view.findViewById(R.id.bannerImageView);
			imageView.setScaleType(ScaleType.CENTER_CROP);

			return imageView;

		}

		public ProgressBar getProgressBar() {
			return (ProgressBar) view.findViewById(R.id.progressBar);

		}

	}

	private void setViews(View view, final int arg0) {
		final WraperClass wraperClass = new WraperClass(view);
System.out.println("Image brand ---"+brandBannerModels.get(arg0)
				.getBrandBannerOne());
		VKCUtils.setImageFromUrl(mActivity, brandBannerModels.get(arg0)
				.getBrandBannerOne().replace(" ", "%20"), wraperClass.getImageBanner(), wraperClass
				.getProgressBar());
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// displayView.setDisplayView();
				// DashboardFActivity.dashboardFActivity.setDisplayView();
				// AppPrefenceManager.saveListingOption(mActivity,"1");
				// AppPrefenceManager.saveIDsForOffer(mActivity,getcategoryIdList());
				//Log.v("LOG","22122014 Offfer baner clicked in onClick"+brandBannerModels.get(arg0).getId());

				DashboardFActivity.dashboardFActivity.setDisplayView();
				AppPrefenceManager.saveListingOption(mActivity, "4");
				AppPrefenceManager.saveIDsForOffer(mActivity,
						getcategoryIdList());
				AppPrefenceManager.saveBrandIdForSearch(mActivity,
						brandBannerModels.get(arg0).getId());
			}
		});

	}

	private String getcategoryIdList() {
		String strgetcategoryIdList = "";
		for (String str : DashboardFActivity.dashboardFActivity
				.getcategoryIdList()) {
			strgetcategoryIdList = strgetcategoryIdList + str + ",";
		}

		return strgetcategoryIdList.substring(0,
				strgetcategoryIdList.length() - 1);

	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}
