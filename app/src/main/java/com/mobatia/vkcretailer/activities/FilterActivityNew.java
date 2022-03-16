package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CategoryModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.OfferModel;
import com.mobatia.vkcretailer.model.PriceModel;
import com.mobatia.vkcretailer.model.SizeModel;

public class FilterActivityNew extends BaseActivity implements VKCJsonTagConstants{
	Activity mActivity;
	private ListView mListFilter;
	private ListView mListFilterContent;
	private String[] mTextString = { "Category", "SubCategory", "Size",
			"Brand", "Price", "Color", "Offers" };
	//private Activity mActivity;
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
	boolean clearFlag=false;
	public List<String> content[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = this;

	}
	
	

	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_filter;
	}

}
