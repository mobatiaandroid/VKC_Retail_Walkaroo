package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.DashboardFActivity;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandBannerModel;

public class HomeBrandBannerTwoAdapter extends PagerAdapter {

    Activity mActivity;
    View layout;
    TextView pagenumber;
    Button click;
    int mType;
    int displayHeight;
    int displayWidth;
    ArrayList<BrandBannerModel> brandBannerModels;
    ;
    int mListSize;

    public HomeBrandBannerTwoAdapter(Activity mainActivity,
                                     ArrayList<BrandBannerModel> brandBannerModels, int type) {
        this.mActivity = mainActivity;
        this.brandBannerModels = brandBannerModels;
        this.mType = type;
        setDisplayParam(mActivity);
        setListSize();
    }

//	public HomeBrandBannerTwoAdapter(Activity mainActivity,
//			ArrayList<BrandBannerModel> brandBannerModels,
//			ArrayList<ProductModel> mProductsList, int type) {
//		mActivity = mainActivity;
//		mFeedbackFormModels = arg1;
//		this.mType = type;
//		this.mProductsList = mProductsList;
//		setDisplayParam(mActivity);
//		setListSize();
//	}

    private void setListSize() {
        if (mType == 0) {
            mListSize = brandBannerModels.size();
        } else {
            if (brandBannerModels.size() % 2 == 0) {
                mListSize = brandBannerModels.size() / 2;
            } else {
                mListSize = (brandBannerModels.size() / 2) + 1;
            }
        }
    }

    private void setDisplayParam(Activity activity) {
        DisplayManagerScale mDisplayManager = new DisplayManagerScale(activity);
        displayHeight = mDisplayManager.getDeviceHeight();
        displayWidth = mDisplayManager.getDeviceWidth();
    }

    @Override
    public int getCount() {
        return mListSize;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mType == 0) {
            layout = inflater.inflate(R.layout.custom_home_banner_pager, null);

            setViews(layout, position);
            ((ViewPager) container).addView(layout, 0);
        } else {
            layout = inflater.inflate(
                    R.layout.custom_home_brand_banner_pager_three_image, null);
            setBottomImages(layout, position);
            ((ViewPager) container).addView(layout, 0);
        }

        return layout;
    }

    private View setBottomImages(View collection, final int position) {
        LinearLayout images = (LinearLayout) collection
                .findViewById(R.id.linearImage);

        WraperClassFooter wraperClassFooter = new WraperClassFooter(collection);
        String imageUrl1 = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg";
        //String imageUrl3 = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg";
        String imageUrl2 = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg";
		/*if (((position * 2) + 1) <= brandBannerModels.size()) {
			imageUrl1 = brandBannerModels.get(position).getBrandBannerTwo();
		}
		if (((position * 2) + 2) <= brandBannerModels.size()) {
			imageUrl2 = brandBannerModels.get(position + 1).getBrandBannerTwo();
		}*/

        if (position == 0) {
            imageUrl1 = brandBannerModels.get(position).getBrandBannerTwo();
            imageUrl2 = brandBannerModels.get(position + 1).getBrandBannerTwo();
        } else {
            if (((position * 2)) <= brandBannerModels.size()) {
                imageUrl1 = brandBannerModels.get(position * 2).getBrandBannerTwo();
            }
            if ((((position * 2) + 1)) < brandBannerModels.size()) {
                imageUrl2 = brandBannerModels.get((position * 2) + 1)
                        .getBrandBannerTwo();
            }
        }
//		if (((position * 3) + 3) <= brandBannerModels.size()) {
//			imageUrl3 = brandBannerModels.get(position + 2).getBrandBannerTwo();
//		}

        // collection.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // Intent intent = new Intent(mActivity,
        // ProductDetailActivity.class);
        //
        // /*Toast.makeText(mActivity,
        // "image clicked"+mProductsList.size(),1000).show();*/
        //
        // intent.putExtra("MODEL", mProductsList.get(position));
        //
        // mActivity.startActivity(intent);
        //
        //
        // }
        // });

        wraperClassFooter.getFooterImage1().setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                       /* DashboardFActivity.dashboardFActivity.setDisplayView();
                        AppPrefenceManager.saveListingOption(mActivity, "4");
                        AppPrefenceManager.saveIDsForOffer(mActivity,
                                getcategoryIdList());
                        AppPrefenceManager.saveBrandIdForSearch(mActivity,
                                brandBannerModels.get(position).getId());*/

                        if (position == 0) {
                            DashboardFActivity.dashboardFActivity
                                    .setDisplayView();
                            AppPrefenceManager
                                    .saveListingOption(mActivity, "4");
                            AppPrefenceManager.saveIDsForOffer(mActivity,
                                    "");
                            AppPrefenceManager.saveBrandIdForSearch(mActivity,
                                    brandBannerModels.get(position).getId());
                        } else {

                            DashboardFActivity.dashboardFActivity
                                    .setDisplayView();
                            AppPrefenceManager
                                    .saveListingOption(mActivity, "4");
                            AppPrefenceManager.saveIDsForOffer(mActivity,
                                    "");
                            AppPrefenceManager
                                    .saveBrandIdForSearch(mActivity,
                                            brandBannerModels.get(position * 2)
                                                    .getId());
                        }
                    }
                });
        wraperClassFooter.getFooterImage2().setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                       /* DashboardFActivity.dashboardFActivity.setDisplayView();
                        AppPrefenceManager.saveListingOption(mActivity, "4");
                        AppPrefenceManager.saveIDsForOffer(mActivity,
                                getcategoryIdList());
                        AppPrefenceManager.saveBrandIdForSearch(mActivity,
                                brandBannerModels.get(position + 1).getId());*/

                        if (position == 0) {
                            DashboardFActivity.dashboardFActivity.setDisplayView();
                            AppPrefenceManager.saveListingOption(mActivity, "4");
                            AppPrefenceManager.saveIDsForOffer(mActivity,
                                    "");
                            AppPrefenceManager.saveBrandIdForSearch(mActivity,
                                    brandBannerModels.get(position + 1).getId());
                        } else {
                            DashboardFActivity.dashboardFActivity.setDisplayView();
                            AppPrefenceManager.saveListingOption(mActivity, "4");
                            AppPrefenceManager.saveIDsForOffer(mActivity,
                                    "");
                            AppPrefenceManager.saveBrandIdForSearch(mActivity,
                                    brandBannerModels.get((position * 2) + 1).getId());
                        }

                    }
                });
//		wraperClassFooter.getFooterImage3().setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//
//						if (((position * 3) + 3) <= mProductsList.size()) {
//							Intent intent = new Intent(mActivity,
//									ProductDetailActivity.class);
//							intent.putExtra("MODEL",
//									mProductsList.get(position + 2));
//							mActivity.startActivity(intent);
//						}
//
//					}
//				});
        VKCUtils.setImageFromUrlBaseTransprant(mActivity, imageUrl1,
                wraperClassFooter.getFooterImage1(),
                wraperClassFooter.getProgressBar1());
        VKCUtils.setImageFromUrlBaseTransprant(mActivity, imageUrl2,
                wraperClassFooter.getFooterImage2(),
                wraperClassFooter.getProgressBar2());
//		VKCUtils.setImageFromUrlBaseTransprant(mActivity, imageUrl3,
//				wraperClassFooter.getFooterImage3(),
//				wraperClassFooter.getProgressBar3());

        return images;
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

    class WraperClassFooter {
        View view;

        public WraperClassFooter(View view) {
            this.view = view;
        }

        public ImageView getFooterImage1() {

            ImageView images1 = (ImageView) view
                    .findViewById(R.id.footerImage1);
            images1.getLayoutParams().width = displayWidth / 2;

            images1.setScaleType(ScaleType.FIT_CENTER);

            return images1;

        }

        public ImageView getFooterImage2() {

            ImageView images1 = (ImageView) view
                    .findViewById(R.id.footerImage2);
            images1.getLayoutParams().width = displayWidth / 2;

            images1.setScaleType(ScaleType.FIT_CENTER);

            return images1;

        }

//		public ImageView getFooterImage3() {
//
//			ImageView images1 = (ImageView) view
//					.findViewById(R.id.footerImage3);
//			images1.getLayoutParams().width = displayWidth / 3;
//
//			images1.setScaleType(ScaleType.CENTER_CROP);
//
//			return images1;
//
//		}

        public ProgressBar getProgressBar1() {
            return (ProgressBar) view.findViewById(R.id.progressBar1);

        }

        public ProgressBar getProgressBar2() {
            return (ProgressBar) view.findViewById(R.id.progressBar2);

        }

//		public ProgressBar getProgressBar3() {
//			return (ProgressBar) view.findViewById(R.id.progressBar3);
//
//		}

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

        VKCUtils.setImageFromUrl(
                mActivity,
                brandBannerModels.get(arg0).getBrandBannerTwo()/* "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg" */,
                wraperClass.getImageBanner(), wraperClass.getProgressBar());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				Intent intent = new Intent(mActivity,
//						ProductDetailActivity.class);
//				intent.putExtra("MODEL", mProductsList.get(arg0));
//				mActivity.startActivity(intent);

            }
        });

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
