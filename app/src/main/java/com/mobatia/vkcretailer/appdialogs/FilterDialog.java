package com.mobatia.vkcretailer.appdialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.FilterActivity;
import com.mobatia.vkcretailer.activities.VKCSplashActivity;
import com.mobatia.vkcretailer.adapter.FilterAdapter;
import com.mobatia.vkcretailer.adapter.FilterBrandContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterCategoryContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterCategoryMainContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterColorContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterOfferAdapter;
import com.mobatia.vkcretailer.adapter.FilterPriceContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterSizeContentAdapter;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CategoryModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.OfferModel;
import com.mobatia.vkcretailer.model.PriceModel;
import com.mobatia.vkcretailer.model.SizeModel;
import com.mobatia.vkcretailer.model.SortCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_BRANDID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_BRANDNAME;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_CATEGORYID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_CATEGORYNAME;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_COLORCODE;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_COLORID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_OFFER;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_OFFERID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_OFFERIMAGE;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_PRICEFROM;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_PRICEID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_PRICETO;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_PRODUCTID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_SIZEID;
import static com.mobatia.vkcretailer.constants.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME;

/*public class FilterDialog extends Dialog implements
        android.view.View.OnClickListener {

    private ListView mListFilter;
    private ListView mListFilterContent;
    private String[] mTextString = {"Category", "Size",
            "Brand", "Price", "Color"};

    *//*private String[] mTextString = {"Category", "SubCategory", "Size",
            "Brand", "Price", "Color", "Offers"};*//*
    private Activity mActivity;
    private ArrayList<CategoryModel> tempArrayListCategory;
    private ArrayList<CategoryModel> tempArrayListMainCategory;
    private ArrayList<SizeModel> tempArrayListSize;
    private ArrayList<BrandTypeModel> tempArrayListBrand;
    private ArrayList<PriceModel> tempArrayListPrice;
    private ArrayList<ColorModel> tempArrayListColor;
    private RelativeLayout relApply;
    private RelativeLayout relClear;
    ArrayList<BrandTypeModel> typeArrayList;
    ArrayList<SizeModel> sizeArrayList;
    ArrayList<ColorModel> colorArrayList;
    ArrayList<PriceModel> priceArrayList;
    ArrayList<CategoryModel> categoryArrayList;
    ArrayList<CategoryModel> mainCategoryArrayList;
    ArrayList<OfferModel> offerModels;
    ArrayList<OfferModel> tempofferModels;
    boolean clearFlag = false;
    public List<String> content[];
    ProgressBar mProgressBar;



    public FilterDialog(@NonNull Activity mActivity) {
        super(mActivity);
        this.mActivity=mActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);
        AppController.tempmainCatArrayList.clear();
        AppController.tempsubCatArrayList.clear();
        AppController.tempsizeCatArrayList.clear();
        AppController.tempbrandCatArrayList.clear();
        AppController.temppriceCatArrayList.clear();
        AppController.tempcolorCatArrayList.clear();
        AppController.tempofferCatArrayList.clear();
        initialiseUI();
        setArrayLists();
        // setSubCategory();
        setCategory();        init();

    }

    private void init() {
        DisplayManagerScale disp = new DisplayManagerScale(mActivity);
        int displayH = disp.getDeviceHeight();
        int displayW = disp.getDeviceWidth();}







    public void setArrayLists() {
        typeArrayList = new ArrayList<BrandTypeModel>();
        categoryArrayList = new ArrayList<CategoryModel>();
        sizeArrayList = new ArrayList<SizeModel>();
        tempArrayListSize = new ArrayList<SizeModel>();
        colorArrayList = new ArrayList<ColorModel>();
        priceArrayList = new ArrayList<PriceModel>();
        tempArrayListBrand = new ArrayList<BrandTypeModel>();
        tempArrayListPrice = new ArrayList<PriceModel>();
        tempArrayListColor = new ArrayList<ColorModel>();
        tempArrayListCategory = new ArrayList<CategoryModel>();
        tempArrayListMainCategory = new ArrayList<CategoryModel>();
        offerModels = new ArrayList<OfferModel>();
        tempofferModels = new ArrayList<OfferModel>();
        // Log.v("LOG",
        // "18122014 offer"
        // + AppPrefenceManager
        // .getJsonOfferResponse(VKCSplashActivity.this));

        try {

           *//* JSONArray offerObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonOfferResponse(mActivity));
            for (int i = 0; i < offerObjArray.length(); i++) {
                JSONObject responseObj = offerObjArray.getJSONObject(i);

                offerModels.add(getOffersObjectValues(responseObj));

            }*//*

            JSONArray typeObjArray = new JSONArray(
                    AppPrefenceManager.getJsonTypeResponse(mActivity));
            for (int i = 0; i < typeObjArray.length(); i++) {
                JSONObject responseObj = typeObjArray.getJSONObject(i);

                typeArrayList.add(getTypeObjectValues(responseObj));

            }

            System.out
                    .println("11122014:AppPrefenceManager.getJsonSizeResponse(FilterActivity.this):"
                            + AppPrefenceManager
                            .getJsonSizeResponse(mActivity));
            JSONArray sizeObjArray = new JSONArray(
                    AppPrefenceManager.getJsonSizeResponse(mActivity));
            for (int i = 0; i < sizeObjArray.length(); i++) {
                JSONObject responseObj = sizeObjArray.getJSONObject(i);

                sizeArrayList.add(getSizeObjectValues(responseObj));

            }
            for (int i = 0; i < sizeArrayList.size(); i++) {
                Log.v("05122014", "LOG " + sizeArrayList.get(i).getName());
            }

            JSONArray colorObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonColorResponse(mActivity));
            for (int i = 0; i < colorObjArray.length(); i++) {
                JSONObject responseObj = colorObjArray.getJSONObject(i);
                colorArrayList.add(getColorObjectValues(responseObj));
            }
            for (int i = 0; i < colorArrayList.size(); i++) {
                Log.v("05122014", "LOG " + colorArrayList.get(i).getColorcode());
            }
            JSONArray priceObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonPriceResponse(mActivity));
            for (int i = 0; i < priceObjArray.length(); i++) {
                JSONObject responseObj = priceObjArray.getJSONObject(i);

                priceArrayList.add(getPriceObjectValues(responseObj));

            }
            for (int i = 0; i < priceArrayList.size(); i++) {
                Log.v("05122014", "LOG "
                        + priceArrayList.get(i).getFrom_range() + ","
                        + priceArrayList.get(i).getTo_range());
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public OfferModel getOffersObjectValues(JSONObject object)
            throws JSONException {

        OfferModel offerModel = new OfferModel();
        offerModel.setId(object.getString(JSON_SETTINGS_OFFERID));
        offerModel.setName(object.getString(JSON_SETTINGS_OFFER));
        offerModel.setOfferBanner(object.getString(JSON_SETTINGS_OFFERIMAGE));

        return offerModel;

    }

    public BrandTypeModel getTypeObjectValues(JSONObject object)
            throws JSONException {

        BrandTypeModel brandModel = new BrandTypeModel();
        brandModel.setId(object.getString(JSON_SETTINGS_BRANDID));
        brandModel.setName(object.getString(JSON_SETTINGS_BRANDNAME));
        return brandModel;

    }

    public CategoryModel getCategoryObjectValues(JSONObject object)
            throws JSONException {

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(object.getString(JSON_SETTINGS_CATEGORYID));
        categoryModel.setName(object.getString(JSON_SETTINGS_CATEGORYNAME));
        categoryModel.setParentId(object.getString(JSON_SETTINGS_PRODUCTID));
        return categoryModel;

    }

    public SizeModel getSizeObjectValues(JSONObject object)
            throws JSONException {

        SizeModel sizeModel = new SizeModel();
        sizeModel.setId(object.getString(JSON_SETTINGS_SIZEID));
        sizeModel.setName(object.getString(JSON_SETTINGS_SIZENAME));
        return sizeModel;

    }

    public ColorModel getColorObjectValues(JSONObject object)
            throws JSONException {

        ColorModel colorModel = new ColorModel();
        colorModel.setId(object.getString(JSON_SETTINGS_COLORID));
        colorModel.setColorcode(object.getString(JSON_SETTINGS_COLORCODE));
        return colorModel;

    }

    public PriceModel getPriceObjectValues(JSONObject object)
            throws JSONException {

        PriceModel priceModel = new PriceModel();
        priceModel.setId(object.getString(JSON_SETTINGS_PRICEID));
        priceModel.setFrom_range(object.getString(JSON_SETTINGS_PRICEFROM));
        priceModel.setTo_range(object.getString(JSON_SETTINGS_PRICETO));
        return priceModel;

    }

    private void initialiseUI() {
        mListFilter = (ListView) findViewById(R.id.listFilter);
        relApply = (RelativeLayout) findViewById(R.id.relApply);
        relClear = (RelativeLayout) findViewById(R.id.relClear);

        relApply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSelectedOptions();
                AppPrefenceManager.saveListingOption(mActivity, "2");
                AppController.isFilterSet=true;
dismiss();
                // Toast.makeText(mActivity,

                // 1000).show();

            }
        });

        relClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tempArrayListBrand.clear();
                tempArrayListCategory.clear();
                tempArrayListColor.clear();
                tempArrayListMainCategory.clear();
                tempArrayListPrice.clear();
                tempArrayListSize.clear();
                tempofferModels.clear();
                AppController.tempmainCatArrayList.clear();
                AppController.tempsubCatArrayList.clear();
                AppController.tempsizeCatArrayList.clear();
                AppController.tempbrandCatArrayList.clear();
                AppController.temppriceCatArrayList.clear();
                AppController.tempcolorCatArrayList.clear();
                AppController.tempofferCatArrayList.clear();

                AppPrefenceManager.saveFilterDataCategory(mActivity, "");
                AppPrefenceManager.saveFilterDataSize(mActivity, "");
                AppPrefenceManager.saveFilterDataBrand(mActivity, "");
                AppPrefenceManager.saveFilterDataColor(mActivity, "");
                AppPrefenceManager.saveFilterDataPrice(mActivity, "");
                AppPrefenceManager.saveFilterDataOffer(mActivity, "");

                // setSubCategory();
                clearFlag = true;
                setSubCategory();
                clearFlag = false;
                setSize();

                setBrand();

                setPrice();

                setColor();

                setOffers();
                setCategory();
                dismiss();


            }
        });

        mListFilterContent = (ListView) findViewById(R.id.listFilterContent);

        mListFilter.setAdapter(new FilterAdapter(mActivity, mTextString));

        // setSubCategory();
        mListFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                if (position == 0) {

                    // setSubCategory();
                    setCategory();

                } *//*else if (position == 1) {

                    setSubCategory();

                } *//*else if (position == 1) {
                    setSize();

                } else if (position == 2) {
                    setBrand();

                } else if (position == 3) {
                    setPrice();

                } else if (position == 4) {
                    setColor();

                } *//*else if (position == 6) {

                    setOffers();

                }*//*

            }

        });

        mListFilterContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selectedFromList = (String) (mListFilterContent
                        .getItemAtPosition(position));
                System.out.println("selected ----" + selectedFromList);
//				Toast.makeText(mActivity, "Text:" + selectedFromList, 1000)
//						.show();

            }
        });
    }

    // Method to return comma separated string of selected filters
	*//*public void getSelectedOptions() {
		String responseCategory = "";
		String responseSize = "";
		String responseBrand = "";
		String responsePrice = "";

		String responseColor = "";
		String responseOffer = "";

		if (tempArrayListCategory.size() > 0) {
			for (int i = 0; i < tempArrayListCategory.size(); i++) {
				responseCategory = responseCategory
						+ tempArrayListCategory.get(i).getId() + ",";
			}
			AppPrefenceManager.saveFilterDataCategory(mActivity,
					getSubString(responseCategory, responseCategory.length()));
			System.out.println("1-------"+AppPrefenceManager.getFilterDataCategory(mActivity));
		}

		if (tempArrayListSize.size() > 0) {
			for (int i = 0; i < tempArrayListSize.size(); i++) {
				responseSize = responseSize + tempArrayListSize.get(i).getId()
						+ ",";
			}
			AppPrefenceManager.saveFilterDataSize(mActivity,
					getSubString(responseSize, responseSize.length()));
			System.out.println("1-------"+AppPrefenceManager.getFilterDataSize(mActivity));

		}
		if (tempofferModels.size() > 0) {
			for (int i = 0; i < tempofferModels.size(); i++) {
				responseOffer = responseOffer + tempofferModels.get(i).getId()
						+ ",";
			}
			AppPrefenceManager.saveFilterDataOffer(mActivity,
					getSubString(responseOffer, responseOffer.length()));
			System.out.println("1-------"+AppPrefenceManager.getFilterDataOffer(mActivity));
		}

		if (tempArrayListBrand.size() > 0) {
			for (int i = 0; i < tempArrayListBrand.size(); i++) {
				responseBrand = responseBrand
						+ tempArrayListBrand.get(i).getId() + ",";
			}
			AppPrefenceManager.saveFilterDataBrand(mActivity,
					getSubString(responseBrand, responseBrand.length()));
			System.out.println("1-------"+AppPrefenceManager.getFilterDataBrand(mActivity));

		}

		if (tempArrayListPrice.size() > 0) {
			for (int i = 0; i < tempArrayListPrice.size(); i++) {
				responsePrice = responsePrice
						+ tempArrayListPrice.get(i).getId() + ",";
			}
			AppPrefenceManager.saveFilterDataPrice(mActivity,
					getSubString(responsePrice, responsePrice.length()));
			System.out.println("1-------"+AppPrefenceManager.getFilterDataPrice(mActivity));

		}
		if (tempArrayListColor.size() > 0) {
			for (int i = 0; i < tempArrayListColor.size(); i++) {
				responseColor = responseColor
						+ tempArrayListColor.get(i).getId() + ",";
			}
			AppPrefenceManager.saveFilterDataColor(mActivity,
					getSubString(responseColor, responseColor.length()));
			System.out.println("1-------"+AppPrefenceManager.getFilterDataColor(mActivity));

		}
			}
*//*

    public void getSelectedOptions() {
        String responseCategory = "";
        String responseSubCategory = "";

        String responseSize = "";
        String responseBrand = "";
        String responsePrice = "";

        String responseColor = "";
        String responseOffer = "";
        //System.out.println("Response---1000"+tempArrayListCategory.size());

        if (AppController.tempmainCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempmainCatArrayList.size(); i++) {
                responseCategory = responseCategory
                        + AppController.tempmainCatArrayList.get(i) + ",";
            }
            if (responseCategory.endsWith(",")) {

                responseCategory = responseCategory.substring(0, responseCategory.length() - 1);
                System.out.println("Response---1" + responseCategory.substring(0, responseCategory.length() - 1));
            }
            AppPrefenceManager.saveFilterDataCategory(mActivity, responseCategory);
            System.out.println("Response---1" + AppPrefenceManager.getFilterDataCategory(mActivity));

        }

        if (AppController.tempsubCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempsubCatArrayList.size(); i++) {
                responseSubCategory = responseSubCategory
                        + AppController.tempsubCatArrayList.get(i) + ",";
            }
            if (responseSubCategory.endsWith(",")) {

                responseSubCategory = responseSubCategory.substring(0, responseSubCategory.length() - 1);
                System.out.println("Response---1" + responseSubCategory.substring(0, responseSubCategory.length() - 1));
            }
            AppPrefenceManager.saveFilterDataCategory(mActivity, responseSubCategory);
            System.out.println("Response---1" + AppPrefenceManager.getFilterDataCategory(mActivity));

        }

        if (AppController.tempsizeCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempsizeCatArrayList.size(); i++) {
                responseSize = responseSize + AppController.tempsizeCatArrayList.get(i)
                        + ",";
            }
            if (responseSize.endsWith(",")) {
                responseSize = responseSize.substring(0, responseSize.length() - 1);
            }
            AppPrefenceManager.saveFilterDataSize(mActivity,
                    responseSize);
            System.out.println("Response---2-" + responseSize);

        }
        if (AppController.tempofferCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempofferCatArrayList.size(); i++) {
                responseOffer = responseOffer + AppController.tempofferCatArrayList.get(i)
                        + ",";
            }
            if (responseOffer.endsWith(",")) {

                responseOffer = responseOffer.substring(0, responseOffer.length() - 1);
                System.out.println("Response---1" + responseOffer.substring(0, responseOffer.length() - 1));
            }
            AppPrefenceManager.saveFilterDataOffer(mActivity,
                    responseOffer);
        }

        if (tempArrayListBrand.size() > 0) {
            for (int i = 0; i < tempArrayListBrand.size(); i++) {
                responseBrand = responseBrand
                        + tempArrayListBrand.get(i).getId() + ",";
            }
            if (responseBrand.endsWith(",")) {

                responseBrand = responseBrand.substring(0, responseBrand.length() - 1);
                System.out.println("Response---1" + responseBrand.substring(0, responseBrand.length() - 1));
            }
            AppPrefenceManager.saveFilterDataBrand(mActivity,
                    responseBrand);
        }

        if (tempArrayListPrice.size() > 0) {
            for (int i = 0; i < tempArrayListPrice.size(); i++) {
                responsePrice = responsePrice
                        + tempArrayListPrice.get(i).getId() + ",";
            }
            if (responsePrice.endsWith(",")) {

                responsePrice = responsePrice.substring(0, responsePrice.length() - 1);
                System.out.println("Response---1" + responsePrice.substring(0, responsePrice.length() - 1));
            }
            AppPrefenceManager.saveFilterDataPrice(mActivity,
                    responsePrice);
        }
        if (tempArrayListColor.size() > 0) {
            for (int i = 0; i < tempArrayListColor.size(); i++) {
                responseColor = responseColor
                        + tempArrayListColor.get(i).getId() + ",";
            }
            if (responseColor.endsWith(",")) {

                responseColor = responseColor.substring(0, responseColor.length() - 1);
                System.out.println("Response---1" + responseColor.substring(0, responseColor.length() - 1));
            }
            AppPrefenceManager.saveFilterDataColor(mActivity,
                    responseColor);
        }
        System.out.print("Everything from filter is---" +
                responseCategory + "-" + responseSize + "-" + responseBrand + "-" +
                responsePrice + "-" + "-" +
                responseColor + "-" +
                responseOffer);
        if (responseCategory.equals("1") || (responseCategory.equals("2")) || (responseCategory.equals("3"))
                || (responseCategory.equals("4")) || (responseCategory.equals("5")) || (responseCategory.equals("31")) || (responseCategory.equals("33"))) {
            System.out.println("cat id---1");

            AppPrefenceManager.setCatId(mActivity, "");
            AppPrefenceManager.setParentCatId(mActivity, responseCategory);
        } else {
            System.out.println("cat id---2");

            AppPrefenceManager.setCatId(mActivity, responseCategory);
            AppPrefenceManager.setParentCatId(mActivity, "");
        }
        *//*
         * int length = 0;
         *
         * length = response.length(); if (length != 0) { String newString =
         * response.substring(0, length - 1);
         * System.out.println("10122014:"+newString);
         *
         * return newString; } else { return
         * "Select any filter option and apply"; }
         *//*

    }

    private String getSubString(String response, int length) {
        String newString = "";
        if (length != 0) {
            newString = response.substring(0, length - 1);
            System.out.println("10122014:" + newString);
            return newString;
        } else {
            return "Select any filter option and apply";
        }

    }

    private void setCategory() {
        // TODO Auto-generated method stub

        // 12/01/2015
        String respMainCategory = AppPrefenceManager
                .getMainCategory(mActivity);
        ArrayList<CategoryModel> mainCategoryArrayList = new ArrayList<CategoryModel>();
        JSONArray categoryObjArray = null;
        try {
            categoryObjArray = new JSONArray(respMainCategory);

            for (int i = 0; i < categoryObjArray.length(); i++) {
                JSONObject responseObj = categoryObjArray.getJSONObject(i);
                CategoryModel model = getCategoryObjectValues(responseObj);
                if (model.getParentId().equalsIgnoreCase("0")) {
                    mainCategoryArrayList.add(model);
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // SortCategory sortCategory[] = VKCSplashActivity.sortCategoryGlobal
        // .getSortCategorys();
        //
        // ArrayList<CategoryModel> filterArrayList = new
        // ArrayList<CategoryModel>();
        // if (AppPrefenceManager.getListingOption(FilterActivity.this)
        // .equals("1")
        // || AppPrefenceManager.getListingOption(FilterActivity.this)
        // .equals("3")) {
        // for (int i = 0; i < sortCategory.length; i++) {
        //
        // {
        //
        // for (CategoryModel categoryModel : sortCategory[i]
        // .getCategoryModels()) {
        // filterArrayList.add(categoryModel);
        // }
        //
        // }
        //
        // }
        // } else {
        // filterArrayList =
        // sortCategory[DashboardFActivity.categorySelectionPosition]
        // .getCategoryModels();
        // }

        FilterCategoryMainContentAdapter adapter = new FilterCategoryMainContentAdapter(
                mActivity,
                *//*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 *//*mainCategoryArrayList, tempArrayListMainCategory);
        mListFilterContent.setAdapter(adapter);

        //tempArrayListMainCategory.clear();

        // ///

    }

    private void setSubCategory() {
        SortCategory sortCategory[] = VKCSplashActivity.sortCategoryGlobal
                .getSortCategorys();

        ArrayList<CategoryModel> filterArrayList = new ArrayList<CategoryModel>();
        // //
        // if (AppPrefenceManager.getListingOption(FilterActivity.this)
        // .equals("1")
        // || AppPrefenceManager.getListingOption(FilterActivity.this)
        // .equals("3")) {
        // for (int i = 0; i < sortCategory.length; i++) {
        //
        // {
        //
        // for (CategoryModel categoryModel : sortCategory[i]
        // .getCategoryModels()) {
        // filterArrayList.add(categoryModel);
        // }
        //
        // }
        //
        // }
        // } else
        // {
        // filterArrayList =
        // sortCategory[DashboardFActivity.categorySelectionPosition]
        // .getCategoryModels();
        // }

        // //tempArrayListMainCategory


        if (tempArrayListMainCategory.size() == 0 && !clearFlag) {

//			CustomToast toast = new CustomToast(mActivity);
//			toast.show(18);
            VKCUtils.showtoast(mActivity, 18);

        }
        for (int i = 0; i < sortCategory.length; i++) {
            for (CategoryModel categoryModel : sortCategory[i]
                    .getCategoryModels()) {

                // filterArrayList.add(categoryModel);

                for (CategoryModel categoryModelMain : tempArrayListMainCategory) {
                    if (categoryModel.getParentId().equals(
                            categoryModelMain.getId()))
                        filterArrayList.add(categoryModel);
                }
            }
        }

        // //

        FilterCategoryContentAdapter adapter = new FilterCategoryContentAdapter(
                mActivity,
                *//*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 *//*filterArrayList, tempArrayListCategory);
        mListFilterContent.setAdapter(adapter);
    }

    private void setSize() {

        System.out.println("11122014:" + sizeArrayList.size());

        FilterSizeContentAdapter adapter = new FilterSizeContentAdapter(
                mActivity, sizeArrayList, tempArrayListSize);
        mListFilterContent.setAdapter(adapter);
    }

    private void setBrand() {

        FilterBrandContentAdapter adapter = new FilterBrandContentAdapter(
                mActivity, typeArrayList, tempArrayListBrand);
        mListFilterContent.setAdapter(adapter);
    }

    private void setPrice() {

        System.out.println("11122014:" + priceArrayList.size());

        FilterPriceContentAdapter adapter = new FilterPriceContentAdapter(
                mActivity, priceArrayList, tempArrayListPrice);
        mListFilterContent.setAdapter(adapter);
    }

    private void setColor() {

        System.out.println("11122014:" + colorArrayList.size());

        FilterColorContentAdapter adapter = new FilterColorContentAdapter(
                mActivity, colorArrayList, tempArrayListColor);
        mListFilterContent.setAdapter(adapter);
    }

    private void setOffers() {
        // offerModels
        FilterOfferAdapter adapter = new FilterOfferAdapter(mActivity,
                offerModels, tempofferModels);
        mListFilterContent.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }
}*/
