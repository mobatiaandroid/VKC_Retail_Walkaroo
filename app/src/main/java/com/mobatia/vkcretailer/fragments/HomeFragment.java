package com.mobatia.vkcretailer.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.DashboardFActivity;
import com.mobatia.vkcretailer.adapter.HomeBrandBannerOneAdapter;
import com.mobatia.vkcretailer.adapter.HomeBrandBannerTwoAdapter;
import com.mobatia.vkcretailer.adapter.HomeImageBannerAdapter;
import com.mobatia.vkcretailer.adapter.HomeOfferBannerAdapter;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.SearchHeaderManager;
import com.mobatia.vkcretailer.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcretailer.model.BrandBannerModel;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CaseModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.HomeImageBannerModel;
import com.mobatia.vkcretailer.model.OfferModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.ProductModel;
import com.mobatia.vkcretailer.model.Related_Images;
import com.mobatia.vkcretailer.model.SizeModel;

//import com.mobatia.vkcsalesapp.activities.DashboardFActivity.DisplayView;

public class HomeFragment extends Fragment implements VKCUrlConstants,
        VKCJsonTagConstants {
    // this Fragment will be called from MainActivity
    private View mRootView;
    private RelativeLayout mRelBanner;
    private RelativeLayout mRelArrivalBanner;
    private RelativeLayout mRelMiddleBanner;
    private RelativeLayout mRelFooter;
    private RelativeLayout mBottomBar;
    private LinearLayout mLnrFooter;
    private DisplayManagerScale mDisplayManager;
    int height, width;
    private ArrayList<ProductModel> productModels;
    private ImageView imgSearch;
    private EditText edtSearch;

    private ArrayList<HomeImageBannerModel> homeImageBannerModels;
    ViewPager myPager;
    ViewPager offerPager;
    ViewPager brandBannerOne;
    boolean bannerShow = true;

    int sliderBannerOfferCount = 0;
    int sliderBannermyPager = 0;
    int sliderBannerbrandBannerOne = 0;

    // DisplayView displayView;

    public HomeFragment() {

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
          Log.v("LOG", "13012015 onResume");
        bannerShow = true;
        startBannerShow();
        final ActionBar abar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        abar.setDisplayHomeAsUpEnabled(false);
        abar.setHomeButtonEnabled(false);

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // Log.v("LOG", "13012015 onPause");
        bannerShow = false;
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        //  Log.v("LOG", "13012015 onStop");
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // Log.v("LOG", "13012015 onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String mLabel = bundle.getString("NAME");
        mRootView = inflater.inflate(R.layout.home_activity_fragment,
                container, false);
        final ActionBar abar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        AppController.isCart = false;
        AppPrefenceManager.saveOfferIDs(getActivity(), "");

        View viewActionBar = getActivity().getLayoutInflater().inflate(
                R.layout.actionbar_imageview, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        initialiseUI();

        setImageBanner(mRootView);

        return mRootView;
    }

    private void setImageBanner(View v) {

        ArrayList<HomeImageBannerModel> homeImageBannerModels = new ArrayList<HomeImageBannerModel>();
        String NewArrival = AppPrefenceManager
                .getNewArrivalBannerResponse(getActivity());

        productModels = new ArrayList<ProductModel>();

        try {

            JSONArray jsonArrayresponse = new JSONArray(NewArrival);
            for (int j = 0; j < jsonArrayresponse.length(); j++) {
                HomeImageBannerModel bannerModel = new HomeImageBannerModel();
                JSONObject productJSONObject = jsonArrayresponse.getJSONObject(
                        j).getJSONObject(JSON_ARRIVAL_RESPONSE);
                ProductModel productModel = parseProductModelTemp(productJSONObject);

                productModels.add(productModel);
                JSONObject jsonArrayZero = jsonArrayresponse.getJSONObject(j);
                JSONObject bannerArray = jsonArrayZero
                        .getJSONObject(JSON_ARRIVAL_BANNER);
                bannerModel.setId(bannerArray.getString(JSON_ARRIVAL_BANNERID));
                bannerModel.setBannerUrl(BASE_URL
                        + bannerArray.getString(JSON_ARRIVAL_BANNER_IMAGE));
                homeImageBannerModels.add(bannerModel);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HomeImageBannerAdapter adapter = new HomeImageBannerAdapter(
                getActivity(), homeImageBannerModels, productModels, 0);
        sliderBannermyPager = homeImageBannerModels.size();
        myPager = (ViewPager) v.findViewById(R.id.reviewpager);
        myPager.setVisibility(View.GONE);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
        setBannerPosition(v);
        setOfferBanners(v);
        setThreeImageBanner(v);
        setBrandBannersOneAndTwo(v);

    }

    private void setOfferBanner(View v) {

        ArrayList<HomeImageBannerModel> homeImageBannerModels = new ArrayList<HomeImageBannerModel>();
        String hreeImaegResp = AppPrefenceManager
                .getNewArrivalBannerResponse(getActivity());
        System.out
                .println("16122014:"
                        + AppPrefenceManager
                        .getNewArrivalBannerResponse(getActivity()));
        HomeImageBannerModel bannerModel = null;
        try {
            JSONArray jsonArray = new JSONArray(hreeImaegResp);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public BrandBannerModel getBandBannerObjectValues(JSONObject object)
            throws JSONException {

        BrandBannerModel brandBannerModel = new BrandBannerModel();
        BrandTypeModel typeModel = getBrandTypeModel(object
                .getJSONObject(JSON_BRAND_RESPONSE));

        JSONObject jsonObjectBrandImage = object
                .getJSONObject(JSON_BRAND_IMAGE);


        brandBannerModel.setId(jsonObjectBrandImage.getString(JSON_BRAND_ID));

        brandBannerModel.setBrandBannerOne(BASE_URL
                + jsonObjectBrandImage.getString(JSON_BRAND_BANNERIMAGE));
       /* Log.v("LOG",
                "20141228 getBrandBannerOne"
                        + BASE_URL
                        + jsonObjectBrandImage.getString(JSON_BRAND_BANNERIMAGE));*/
        brandBannerModel.setBrandBannerTwo(BASE_URL
                + jsonObjectBrandImage.getString(JSON_BRAND_IMAGENAME));
/*
        Log.v("LOG",
                "20141228 getBrandBannerOne"
                        + brandBannerModel.getBrandBannerOne());
        brandBannerModel.setTypeModel(typeModel);
*/

        return brandBannerModel;

    }

    public OfferModel getOffersObjectValues(JSONObject object)
            throws JSONException {

        OfferModel offerModel = new OfferModel();
        offerModel.setId(object.getString(JSON_SETTINGS_OFFERID));
        offerModel.setName(object.getString(JSON_SETTINGS_OFFER));
        offerModel.setOfferBanner(BASE_URL
                + object.getString(JSON_SETTINGS_OFFERIMAGE));

        return offerModel;

    }

    private void setOfferBanners(View v) {
        ArrayList<OfferModel> offerModels = new ArrayList<OfferModel>();
        try {
            JSONArray offerObjArray = new JSONArray(
                    AppPrefenceManager.getJsonOfferResponse(getActivity()));
           /* Log.v("LOG",
                    "18122014 offer"
                            + AppPrefenceManager
                            .getJsonOfferResponse(getActivity()));*/

            for (int i = 0; i < offerObjArray.length(); i++) {
                JSONObject responseObj = offerObjArray.getJSONObject(i);

                offerModels.add(getOffersObjectValues(responseObj));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        offerPager = (ViewPager) v.findViewById(R.id.reviewpagerOffer);

        offerPager.setVisibility(View.VISIBLE);

        HomeOfferBannerAdapter adapter = new HomeOfferBannerAdapter(
                getActivity(), offerModels/* ,displayView */);
        sliderBannerOfferCount = offerModels.size();
        offerPager.setAdapter(adapter);
        offerPager.setCurrentItem(0);

    }

    private void setBrandBannersOneAndTwo(View v) {
        ArrayList<BrandBannerModel> brandBannerModels = new ArrayList<BrandBannerModel>();
        try {


            JSONArray bannerObjArray = new JSONArray(
                    AppPrefenceManager.getBrandBannerResponse(getActivity()));

            for (int i = 0; i < bannerObjArray.length(); i++) {
                JSONObject responseObj = bannerObjArray.getJSONObject(i);

                brandBannerModels.add(getBandBannerObjectValues(responseObj));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//New Arrival Pager
        brandBannerOne = (ViewPager) v.findViewById(R.id.pagerBrandBannerOne);

        brandBannerOne.setVisibility(View.VISIBLE);

        HomeBrandBannerOneAdapter bannerOneAdapter = new HomeBrandBannerOneAdapter(
                getActivity(), brandBannerModels/* ,displayView */);
        sliderBannerbrandBannerOne = brandBannerModels.size();
        brandBannerOne.setAdapter(bannerOneAdapter);
        brandBannerOne.setCurrentItem(0);

        ViewPager brandBannerTwo = (ViewPager) v
                .findViewById(R.id.pagerBrandBannerTwo);

        brandBannerTwo.setVisibility(View.VISIBLE);

        HomeBrandBannerTwoAdapter bannerTwoAdapter = new HomeBrandBannerTwoAdapter(
                getActivity(), brandBannerModels/* ,displayView */, 1);

        brandBannerTwo.setAdapter(bannerTwoAdapter);
        brandBannerTwo.setCurrentItem(0);

    }

    private ProductModel parseProductModelTemp(JSONObject jsonObjectZero) {
        ProductModel productModel = new ProductModel();
        try {


            productModel.setCategoryId(jsonObjectZero
                    .optString(JSON_CATEGORY_ID));
            productModel.setCategoryName(jsonObjectZero
                    .optString(JSON_CATEGORY_NAME));
            productModel.setmProductPrize(jsonObjectZero
                    .optString(JSON_CATEGORY_COST));
            productModel.setId(jsonObjectZero.optString(JSON_PRODUCT_ID));

            productModel.setmProductName(jsonObjectZero
                    .optString(JSON_PRODUCT_NAME));
            productModel.setProductquantity(jsonObjectZero
                    .optString(JSON_PRODUCT_QTY));

            productModel.setProductDescription(jsonObjectZero
                    .optString(JSON_PRODUCT_DESCRIPTION));

            productModel.setProductViews(jsonObjectZero
                    .optString(JSON_PRODUCT_VIEWS));

            productModel.setTimeStamp(jsonObjectZero
                    .optString(JSON_PRODUCT_OFFER));

            productModel.setmProductOff(jsonObjectZero
                    .optString(JSON_PRODUCT_TIMESTAMP));

            // product_color
            // product_image
            // product_size
            // product_type
            JSONArray productColorArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_COLOR);
            JSONArray productImageArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_IMAGE);
            System.out.println("prodd image arr " + productImageArray.length());
            JSONArray productSizeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_SIZE);
            JSONArray productTypeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_TYPE);
            JSONArray productCaseArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_CASE);
            JSONArray productRelated = jsonObjectZero
                    .getJSONArray(JSON_IMAGE_ARRAY);
            ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
            for (int i = 0; i < productColorArray.length(); i++) {

                ColorModel colorModel = new ColorModel();
                JSONObject jsonObject = productColorArray.getJSONObject(i);
                colorModel.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));

                colorModels.add(colorModel);

            }
            productModel.setProductColor(colorModels);
            ArrayList<Related_Images> related_Images = new ArrayList<>();
            for (int i = 0; i < productRelated.length(); i++) {
                Related_Images related_Images2 = new Related_Images();
                JSONObject jsonObject = productRelated.getJSONObject(i);
                related_Images2.setImageId(jsonObject.getString("image_id"));
                related_Images2.setProduct_id(jsonObject.getString("product_id"));
                related_Images2.setmageurl(BASE_URL
                        + jsonObject.optString("image_name"));
                //related_Images2.setProduct_id(jsonObject.getString(JSON_PRODUCT_ID));
                // related_Images2.setmageurl(jsonObject
                // .getString("image_name"));
                related_Images.add(related_Images2);
            }
            productModel.setRelateedImages(related_Images);

            // ////////////
            ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
            for (int i = 0; i < productImageArray.length(); i++) {

                ProductImages images = new ProductImages();
                JSONObject jsonObject = productImageArray.getJSONObject(i);
                images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                images.setImageName(BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE));
                ColorModel colorModel = new ColorModel();
                colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                images.setColorModel(colorModel);
                productImages.add(images);

            }
            productModel.setProductImages(productImages);
            // ///
            ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
            for (int i = 0; i < productSizeArray.length(); i++) {

                SizeModel sizeModel = new SizeModel();
                JSONObject jsonObject = productSizeArray.getJSONObject(i);
                sizeModel.setId(jsonObject.optString(JSON_SETTINGS_SIZEID));
                sizeModel.setName(jsonObject.optString(JSON_SETTINGS_SIZENAME));

                sizeModels.add(sizeModel);

            }
            productModel.setmProductSize(sizeModels);
            // /////
            ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
            for (int i = 0; i < productTypeArray.length(); i++) {

                // BrandTypeModel typeModel = new BrandTypeModel();
                // JSONObject jsonObject = productTypeArray.getJSONObject(i);
                // typeModel.setId(jsonObject.optString("id"));
                // typeModel.setName(jsonObject.optString("type_name"));

                brandTypeModels.add(getBrandTypeModel(productTypeArray
                        .getJSONObject(i)));

            }
            productModel.setProductType(brandTypeModels);
            ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
            for (int i = 0; i < productCaseArray.length(); i++) {

                CaseModel caseModel = new CaseModel();
                JSONObject jsonObject = productCaseArray.getJSONObject(i);
                caseModel.setId(jsonObject.optString(JSON_SETTINGS_CASEID));
                caseModel.setName(jsonObject.optString(JSON_SETTINGS_CASENAME));

                caseModels.add(caseModel);

            }
            productModel.setmProductCases(caseModels);

        } catch (Exception e) {

            System.out.println("Error 16122014" + e);

        }
        return productModel;
    }

    private BrandTypeModel getBrandTypeModel(JSONObject jsonObject) {
        BrandTypeModel typeModel = new BrandTypeModel();
        typeModel.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
        typeModel.setName(jsonObject.optString(JSON_SETTINGS_BRANDNAME));
        return typeModel;

    }

    private ProductModel parseProductModel(JSONObject jsonObjectZero) {
        ProductModel productModel = new ProductModel();
        try {
            System.out.println("28122014 parseProductModel"
                    + jsonObjectZero.toString());
            // categoryname
            // productcost
            // productid
            // productname
            // productquantity
            // productoffer
            //
            productModel.setCategoryId(jsonObjectZero
                    .optString(JSON_CATEGORY_ID));
            productModel.setCategoryName(jsonObjectZero
                    .optString(JSON_CATEGORY_NAME));
            productModel.setmProductPrize(jsonObjectZero
                    .optString(JSON_CATEGORY_COST));
            productModel.setId(jsonObjectZero.optString(JSON_PRODUCT_ID));

            productModel.setmProductName(jsonObjectZero
                    .optString(JSON_PRODUCT_NAME));
            productModel.setProductquantity(jsonObjectZero
                    .optString(JSON_PRODUCT_QTY));

            productModel.setProductDescription(jsonObjectZero
                    .optString(JSON_PRODUCT_DESCRIPTION));

            productModel.setProductViews(jsonObjectZero
                    .optString(JSON_PRODUCT_VIEWS));

            productModel.setTimeStamp(jsonObjectZero
                    .optString(JSON_PRODUCT_OFFER));

            productModel.setmProductOff(jsonObjectZero
                    .optString(JSON_PRODUCT_TIMESTAMP));

            // product_color
            // product_image
            // product_size
            // product_type
            JSONArray productColorArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_COLOR);
            JSONArray productImageArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_IMAGE);
            JSONArray productSizeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_SIZE);
            JSONArray productTypeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_TYPE);
            JSONArray productCaseArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_CASE);
            JSONArray product_related = jsonObjectZero
                    .getJSONArray(JSON_IMAGE_ARRAY);
            ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
            for (int i = 0; i < productColorArray.length(); i++) {

                ColorModel colorModel = new ColorModel();
                JSONObject jsonObject = productColorArray.getJSONObject(i);
                colorModel.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                colorModel.setName(jsonObject
                        .optString(JSON_SETTINGS_COLORNAME));
                colorModels.add(colorModel);

            }
            productModel.setProductColor(colorModels);

            // ////////////
            ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
            for (int i = 0; i < productImageArray.length(); i++) {

                ProductImages images = new ProductImages();
                JSONObject jsonObject = productImageArray.getJSONObject(i);
                images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
                images.setImageName(BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE));
                // System.out.println("imgs url 123 " + images.getImageName());
                ColorModel colorModel = new ColorModel();
                colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
                colorModel.setColorcode(jsonObject
                        .optString(JSON_SETTINGS_COLORCODE));
                colorModel.setName(jsonObject
                        .optString(JSON_SETTINGS_COLORNAME));
                images.setColorModel(colorModel);
                productImages.add(images);

            }
            productModel.setProductImages(productImages);
            // //
            ArrayList<Related_Images> related_Images = new ArrayList<>();
            for (int i = 0; i < product_related.length(); i++) {
                Related_Images related_Images2 = new Related_Images();
                JSONObject jsonObject = product_related.getJSONObject(i);
                related_Images2
                        .setImageId(jsonObject.getString("image_id"));
                related_Images2.setmageurl(BASE_URL
                        + jsonObject.optString("image_name"));
                related_Images2.setProduct_id(jsonObject.getString("product_id"));

                related_Images.add(related_Images2);
            }
            productModel.setRelateedImages(related_Images);

            // ///
            ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
            for (int i = 0; i < productSizeArray.length(); i++) {

                SizeModel sizeModel = new SizeModel();
                JSONObject jsonObject = productSizeArray.getJSONObject(i);
                sizeModel.setId(jsonObject.optString(JSON_SETTINGS_SIZEID));
                sizeModel.setName(jsonObject.optString(JSON_SETTINGS_SIZENAME));

                sizeModels.add(sizeModel);

            }
            productModel.setmProductSize(sizeModels);
            // /////
            ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
            for (int i = 0; i < productTypeArray.length(); i++) {

                BrandTypeModel typeModel = new BrandTypeModel();
                JSONObject jsonObject = productTypeArray.getJSONObject(i);
                typeModel.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
                typeModel
                        .setName(jsonObject.optString(JSON_SETTINGS_BRANDNAME));

                brandTypeModels.add(typeModel);

            }
            productModel.setProductType(brandTypeModels);
            ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
            for (int i = 0; i < productCaseArray.length(); i++) {

                CaseModel caseModel = new CaseModel();
                JSONObject jsonObject = productCaseArray.getJSONObject(i);
                caseModel.setId(jsonObject.optString(JSON_SETTINGS_CASEID));
                caseModel.setName(jsonObject.optString(JSON_SETTINGS_CASENAME));

                caseModels.add(caseModel);

            }
            productModel.setmProductCases(caseModels);

        } catch (Exception e) {

            System.out.println("Error 16122014" + e);

        }
        return productModel;
    }

    private void setThreeImageBanner(View v) {
        ArrayList<HomeImageBannerModel> homeImageBannerModels = new ArrayList<HomeImageBannerModel>();
        String hreeImaegResp = AppPrefenceManager
                .getPopularProductSliderResponse(getActivity());

        productModels = new ArrayList<ProductModel>();

        try {

            JSONArray jsonArrayresponse = new JSONArray(
                    AppPrefenceManager
                            .getPopularProductSliderResponse(getActivity()));

           /* System.out.println("28122014 setThreeImageBanner"
                    + jsonArrayresponse.toString());*/
            for (int j = 0; j < jsonArrayresponse.length(); j++) {
                //System.out.println("16122014  IN loop" + j);
                HomeImageBannerModel bannerModel = new HomeImageBannerModel();

                ProductModel productModel = parseProductModel(jsonArrayresponse
                        .getJSONObject(j));

                productModels.add(productModel);

                bannerModel.setId(bannerModel.getId());

                bannerModel.setBannerUrl(productModel.getProductImages().get(0)
                        .getImageName());

                bannerModel.setSlideId(productModel.getProductImages().get(0)
                        .getId());
                homeImageBannerModels.add(bannerModel);
                ArrayList<Related_Images> related_Images = new ArrayList<>();

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Error 16122014" + e);
        }
//Popular Products Pager
        HomeImageBannerAdapter adapter = new HomeImageBannerAdapter(
                getActivity(), homeImageBannerModels, productModels, 1);
        ViewPager myPager = (ViewPager) v
                .findViewById(R.id.reviewpagerThreeImage);
        myPager.setVisibility(View.VISIBLE);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
    }

    private void startBannerShow() {
        new AsyncTask<Void, Integer, Void>() {
            int count = 0;

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub

                while (bannerShow) {
                    try {
                        Thread.sleep(1000);
                        count++;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    publishProgress(count);
                }
                return null;
            }

            protected void onProgressUpdate(Integer[] values) {

                if (values[0] % 3 == 0) {
                    if (sliderBannermyPager - 1 == myPager.getCurrentItem()) {
                        myPager.setCurrentItem(0);
                    } else {
                        myPager.setCurrentItem(myPager.getCurrentItem() + 1);
                    }
                } else if (values[0] % 3 == 1) {
                    if (sliderBannerbrandBannerOne - 1 == brandBannerOne
                            .getCurrentItem()) {
                        brandBannerOne.setCurrentItem(0);
                    } else {
                        brandBannerOne.setCurrentItem(brandBannerOne
                                .getCurrentItem() + 1);
                    }
                } else if (values[0] % 3 == 2) {
                    if (sliderBannerOfferCount - 1 == offerPager
                            .getCurrentItem()) {
                        offerPager.setCurrentItem(0);
                        //Log.v("LOG", "13012015 if");
                    } else {
                        offerPager
                                .setCurrentItem(offerPager.getCurrentItem() + 1);
                        //Log.v("LOG", "13012015 else");
                    }
                }

            }
        }.execute();
    }

    private void setBannerPosition(View v) {
        RelativeLayout relArrivalBannerNext, relArrivalBannerPrevious;
        relArrivalBannerNext = (RelativeLayout) v
                .findViewById(R.id.relArrivalBannerNext);
        relArrivalBannerPrevious = (RelativeLayout) v
                .findViewById(R.id.relArrivalBannerPrevious);
        relArrivalBannerNext.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                myPager.setCurrentItem(myPager.getCurrentItem() - 1);
                return false;
            }
        });
        relArrivalBannerPrevious.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                myPager.setCurrentItem(myPager.getCurrentItem() + 1);
                return false;
            }
        });

    }

    public int getActionBarHeight(Activity activity) {
        final TypedArray styledAttributes = activity.getTheme()
                .obtainStyledAttributes(
                        new int[]{android.R.attr.actionBarSize});
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return mActionBarSize;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initialiseUI() {

        RelativeLayout fragmentHomeBase = (RelativeLayout) mRootView
                .findViewById(R.id.fragmentHomeBase);

        RelativeLayout relBrandBannerOne = (RelativeLayout) mRootView
                .findViewById(R.id.relBrandBannerOne);

        RelativeLayout relBrandBannerTwo = (RelativeLayout) mRootView
                .findViewById(R.id.relBrandBannerTwo);

        RelativeLayout relSearchHeader = (RelativeLayout) mRootView
                .findViewById(R.id.relSearchHeader);

        mRelBanner = (RelativeLayout) mRootView.findViewById(R.id.relBanner);
        mRelArrivalBanner = (RelativeLayout) mRootView
                .findViewById(R.id.relArrivalBanner);
        mRelMiddleBanner = (RelativeLayout) mRootView
                .findViewById(R.id.relMiddleBanner);
        mRelFooter = (RelativeLayout) mRootView.findViewById(R.id.relFooter);
        mBottomBar = (RelativeLayout) mRootView.findViewById(R.id.bottomBar);
        mLnrFooter = (LinearLayout) mRootView.findViewById(R.id.lnrFooter);

        mDisplayManager = new DisplayManagerScale(getActivity());
        height = mDisplayManager.getDeviceHeight();
        width = mDisplayManager.getDeviceWidth();

		/*Log.v("LOG", "23122014 height " + height);
		Log.v("LOG", "23122014 getActionBarHeight "
				+ getActionBarHeight(getActivity()));

		Log.v("LOG", "23122014 getStatusBarHeight " + getStatusBarHeight());*/

        height = height
                - (getStatusBarHeight() + getActionBarHeight(getActivity()));
        int bannerHeight = (int) (height * 0.45);
        int arrBannerHeight = (int) (height * 0.07);

        int relBrandBannerTwoHeight = width / 3;

        int footerBannerHeight = /* (int) (height * 0.245) */width / 3;
        int bootomBarHeight = (int) (height * 0.04);
        int middleBannerHeight = (int) /* (height * 0.245) */height
                - (bannerHeight + arrBannerHeight + bootomBarHeight + footerBannerHeight);
        int relBrandBannerOneHeight = height
                - (bannerHeight + arrBannerHeight + relBrandBannerTwoHeight);

        relBrandBannerOne.getLayoutParams().height = relBrandBannerOneHeight;// 1

        relBrandBannerTwo.getLayoutParams().height = relBrandBannerTwoHeight;// 1

        mRelBanner.getLayoutParams().height = bannerHeight;// 1

        mRelMiddleBanner.getLayoutParams().height = relBrandBannerOneHeight;// 3
        mRelFooter.getLayoutParams().height = footerBannerHeight;// 4
        mBottomBar.getLayoutParams().height = bootomBarHeight;// 5

        SearchHeaderManager manager = new SearchHeaderManager(getActivity());
        manager.getSearchHeader(relSearchHeader);

        imgSearch = manager.getSearchImage();
        edtSearch = manager.getEditText();

        manager.searchAction(getActivity(), new SearchActionInterface() {

            @Override
            public void searchOnTextChange(String key) {
                // TODO Auto-generated method stub
                if (!edtSearch.getText().toString().equals("")) {

                    DashboardFActivity.dashboardFActivity
                            .goToSearchWithKey(edtSearch.getText().toString());
                    AppPrefenceManager.saveListingOption(getActivity(), "3");

                }
                edtSearch.setText("");

            }
        }, edtSearch.getText().toString());

        mRelArrivalBanner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        mRelMiddleBanner.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

    }
}