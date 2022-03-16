package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.ProductDetailActivity;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.ProductImages;

/**
 * @author archana.s
 */
public class ViewpagerAdapter extends PagerAdapter {

    Activity mActivity;
    ArrayList<ProductImages> mImageList;
    private String imageUrl;
    LayoutInflater mInflater;
    int x = 0;
    private View view;

    public ViewpagerAdapter(Activity mActivity,
                            ArrayList<ProductImages> mImageList, int x) {
        this.mActivity = mActivity;
        this.mImageList = mImageList;
    }

    public ViewpagerAdapter(Activity mActivity,
                            ArrayList<ProductImages> mImageList, String imageUrl, int x) {
        this.mActivity = mActivity;
        this.mImageList = mImageList;
        this.imageUrl = imageUrl;
        this.x = x;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        mInflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(R.layout.custom_viepagerlayout,
                container, false);

        ImageView img = (ImageView) itemView.findViewById(R.id.imageView);
        img.setScaleType(ScaleType.FIT_CENTER);
        final ProgressBar progressBar = (ProgressBar) itemView
                .findViewById(R.id.progressBar1);

        if (x == 0) {

            VKCUtils.setImageFromUrl(mActivity, mImageList.get(position)
                    .getImageName(), img, progressBar);
            ProductDetailActivity.imgid = mImageList.get(position).getId();
        } else {
            VKCUtils.setImageFromUrl(mActivity, imageUrl, img, progressBar);
        }

        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
