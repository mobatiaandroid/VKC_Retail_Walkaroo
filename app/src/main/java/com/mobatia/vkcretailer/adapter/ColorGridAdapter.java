package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.ProductDetailActivity;
import com.mobatia.vkcretailer.adapter.ProductListAdapter.ViewHolder;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.ProductModel;

public class ColorGridAdapter extends BaseAdapter implements VKCUrlConstants,
        VKCDbConstants {

    Context mContext;

    ArrayList<ColorModel> colorModels;

    LayoutInflater mInflater;
    private ViewpagerAdapter mViewPagerAdapter;
    ArrayList<ProductImages> mProductImages = new ArrayList<>();
    private ArrayList<ColorModel> colorModels2 = new ArrayList<>();
    private View view;
    DisplayManagerScale mDisplayManager;
    int width, height;
    int type;
    private ArrayList<ProductImages> imageUrls;

    String colorValue;
    String product_id;

    public ColorGridAdapter(Context mcontext,
                            ArrayList<ColorModel> colorModels, int type, String product_id) {

        this.mContext = mcontext;
        this.colorModels = colorModels;
        this.type = type;
        this.product_id = product_id;
        mInflater = LayoutInflater.from(mContext);
        getDisplayScale();
    }
    public ColorGridAdapter(Context mcontext,
                            ArrayList<ColorModel> colorModels, int type) {

        this.mContext = mcontext;
        this.colorModels = colorModels;
        this.type = type;
        this.product_id = product_id;
        mInflater = LayoutInflater.from(mContext);
        getDisplayScale();
    }
    public ColorGridAdapter(Context mcontext, String colorValue, int type) {

        this.mContext = mcontext;
        this.colorValue = colorValue;
        this.type = type;
        mInflater = LayoutInflater.from(mContext);
        getDisplayScale();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (type == 0) {
            return 1;
        } else {
            return colorModels.size();
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

            holderView = mInflater.inflate(R.layout.custom_color_grid, parent,
                    false);

        } else {

            holderView = convertView;
        }
        /*GradientDrawable gd = new GradientDrawable();
        // gd.setColor(Color.parseColor(colorModels.get(position).getColorcode()));
        if (type == 1) {
            gd.setColor(VKCUtils.parseColor(colorModels.get(position)
                    .getColorcode()));
        } else {
            gd.setColor(VKCUtils.parseColor(colorValue));
        }
        gd.setCornerRadius(50);*/
        // gd.setAlpha(100);
        // gd.setStroke(strokeWidth, strokeColor);

        /*RelativeLayout relativeMain = new ViewHolder().getColorView(holderView);
        relativeMain.setBackgroundDrawable(gd);
        LinearLayout lnrHolder = new ViewHolder().getLinearView(holderView);
        lnrHolder.getLayoutParams().height = (int) (height * 1.8);
*/
        final CheckBox checkBox = new ViewHolder().getCheckBox(holderView);
        if (type == 1) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setText(colorModels
                    .get(position).getName());
        } else {
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (checkBoxTemp != null) {
                    checkBoxTemp.setChecked(false);
                }

                checkBoxTemp = (CheckBox) v;
                if (checkBox.isChecked()) {

                    ProductDetailActivity.selectedFromColorList = colorModels
                            .get(position).getName();//.getColorcode()
                    ProductDetailActivity.selectedIDFromColorList = colorModels
                            .get(position).getId();
                   /* System.out.println("the color id is"
                            + colorModels.get(position).getColorcode()
                            + "id is" + colorModels.get(position).getId());
                    System.out.println("product id " + product_id);*/
                    if (!colorModels.get(position).getId().equalsIgnoreCase("")
                            || !product_id.equalsIgnoreCase("")) {
                        mProductImages.clear();
                        String[] name = {"productId", "colorId"};
                        String[] values = {product_id,
                                colorModels.get(position).getId()};
                        final VKCInternetManager manager = new VKCInternetManager(
                                GET_IMAGELIST_BYCOLOR);
                        manager.getResponsePOST(mContext, name, values,
                                new ResponseListener() {

                                    @Override
                                    public void responseSuccess(
                                            String successResponse) {
                                        // TODO Auto-generated method stub

                                        try {

                                            JSONObject jsonObject = new JSONObject(
                                                    successResponse);
                                            JSONObject firstobj = jsonObject
                                                    .getJSONObject("response");
                                            JSONArray jsonArray = firstobj
                                                    .getJSONArray("orderdetails");

                                            if (jsonArray.length() != 0) {
                                                for (int i = 0; i < jsonArray
                                                        .length(); i++) {
                                                    ProductImages images = new ProductImages();

                                                    ColorModel colorModel = new ColorModel();
                                                    JSONObject jsonObject1 = jsonArray
                                                            .getJSONObject(i);
                                                    colorModel.setId(jsonObject
                                                            .optString("id"));
                                                    colorModel
                                                            .setProduct_id(jsonObject1
                                                                    .getString("product_id"));
                                                    colorModel.setUrl(BASE_URL
                                                            + jsonObject1
                                                            .getString("image_name"));
                                                    images.setImageName(BASE_URL
                                                            + jsonObject1
                                                            .getString("image_name"));
                                                    mProductImages.add(images);

                                                }

                                                AppController.productModel
                                                        .setProductImages(mProductImages);

                                                mViewPagerAdapter = new ViewpagerAdapter(
                                                        (Activity) mContext,
                                                        AppController.productModel
                                                                .getProductImages(),
                                                        0);

                                                ProductDetailActivity.mImagePager
                                                        .setAdapter(mViewPagerAdapter);


                                            } else {
                                                VKCUtils.showtoast(
                                                        (Activity) mContext, 40);
                                            }

                                        } catch (Exception exception) {
                                            System.out.println("Exception is"
                                                    + exception);
                                        }
                                    }

                                    @Override
                                    public void responseFailure(
                                            String failureResponse) {
                                        // TODO Auto-generated method stub
                                        Log.v("LOG", "20072016 Errror"
                                                + failureResponse);
                                    }
                                });
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

    }
}