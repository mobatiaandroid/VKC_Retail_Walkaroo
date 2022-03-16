/*
package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.adapter.ColorGridAdapter;
import com.mobatia.vkcretailer.adapter.ListBottomImageAdapter;
import com.mobatia.vkcretailer.adapter.ListImageAdapter;
import com.mobatia.vkcretailer.adapter.Quantity_Adapter;
import com.mobatia.vkcretailer.adapter.SizeGridAdapter;
import com.mobatia.vkcretailer.adapter.ViewpagerAdapter;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.customview.HorizontalListView;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CaseModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.HomeImageBannerModel;
import com.mobatia.vkcretailer.model.ListImageModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.ProductModel;
import com.mobatia.vkcretailer.model.Related_Images;
import com.mobatia.vkcretailer.model.SizeModel;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProductDetailsActivityNew extends BaseActivity implements
VKCUrlConstants, OnClickListener, VKCDbConstants, OnItemClickListener,
VKCJsonTagConstants {
	private ArrayList<ProductModel> productModels;

	private View view;
	private HorizontalListView mHorizontalListView;
	private HorizontalListView listViewColor, listViewSize, listViewQuty;
	private ListImageAdapter mListAdapter;
	private ListBottomImageAdapter mListAdapter1;
	private Quantity_Adapter quantity_Adapter;
	private ListImageModel mListModel;
	private ColorGridAdapter colorGridAdapter;
	private SizeGridAdapter sizeGridAdapter;
	boolean isUpdated = false;
	private ListImageModel mListModel1;
	public static String imgid;
	private RelativeLayout mRelImage;
	private TextView txtdescription;
	private RelativeLayout mRelativText;
	private EditText caseedt;
	RelativeLayout relativSecondSec;
	private LinearLayout mRelBottom;
	private ImageView mImgArrowRight;
	private ImageView mImgArrowLeft;
	private RelativeLayout first;
	private Activity mActivity;
	private ViewpagerAdapter mViewPagerAdapter;
	int width;
	private RelativeLayout cart;
	int height;
	TextView txtlikeCount;
	TextView txtNameText;
	TextView txtViewPrice, txtCartCount;
	RelativeLayout relShare, relCart;
	public static String selectedFromSizeList;
	public static String selectedFromColorList;
	public static String selectedIDFromSizeList;
	public static String selectedIDFromColorList;
	boolean isClicked;
	int likeCount = 0;
	public static ViewPager mImagePager;

	DisplayManagerScale displayManagerScale;

	//ProductModel productModel = new ProductModel();
	Button buttonShare, buttonLike;
	private DataBaseManager databaseManager;
	private EditText edtQuantity;
	private ArrayList<ProductImages> imageUrls;
	private ArrayList<ColorModel> colorArrayList;
	private ArrayList<SizeModel> sizeArrayList;
	private ArrayList<CaseModel> caseArrayList;
	ArrayList<Related_Images> imageUrls3;
	ArrayList<ProductModel> mProductsList;

	private TextView edtPendQuantity;
	int pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = this;
		mProductsList =  (ArrayList<ProductModel>) getIntent().getExtras()
				.getSerializable("MODEL");
		pos=getIntent().getExtras().getInt("position");
		initialiseUI();
		final ActionBar abar = getActionBar();

		View viewActionBar = getLayoutInflater().inflate(
				R.layout.actionbar_title, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(

		ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		TextView textviewTitle = (TextView) viewActionBar
				.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Product Details");
		abar.setCustomView(viewActionBar, params);
		abar.setDisplayShowCustomEnabled(true);

	}
	private void initialiseUI() {
		// TODO Auto-generated method stub
		colorArrayList = new ArrayList<ColorModel>();
		sizeArrayList = new ArrayList<SizeModel>();
		caseArrayList = new ArrayList<CaseModel>();
		databaseManager = new DataBaseManager(mActivity);
		relativSecondSec = (RelativeLayout) findViewById(R.id.relativSecondSec);
		mHorizontalListView = (HorizontalListView) findViewById(R.id.listView);
		listViewColor = (HorizontalListView) findViewById(R.id.listViewColor);
		listViewSize = (HorizontalListView) findViewById(R.id.listViewSize);
		first = (RelativeLayout) findViewById(R.id.first);
		int width = first.getWidth();
		System.out
				.println("the width is " + width + "8888" + first.getHeight());
		txtlikeCount = (TextView) findViewById(R.id.txtLikeCount);
		listViewQuty = (HorizontalListView) findViewById(R.id.listViewquty);
		caseedt = (EditText) findViewById(R.id.textViewCase);
		mRelImage = (RelativeLayout) findViewById(R.id.relImage);
		mImgArrowRight = (ImageView) findViewById(R.id.imgArrowRight);
		mImgArrowLeft = (ImageView) findViewById(R.id.imgArrowLeft);
		edtPendQuantity = (TextView) findViewById(R.id.edtViewQtyOneData);
		buttonLike = (Button) findViewById(R.id.btnLike);
		mRelativText = (RelativeLayout) findViewById(R.id.relativText);
		mRelBottom = (LinearLayout) findViewById(R.id.relBottomLayout);
		txtNameText = (TextView) findViewById(R.id.txtNameText);
		txtdescription = (TextView) findViewById(R.id.txtdescription);
		
		txtViewPrice = (TextView) findViewById(R.id.txtViewPrice);
		
		relShare = (RelativeLayout) findViewById(R.id.relShare);
		relCart = (RelativeLayout) findViewById(R.id.relCart);
		getActionBar().setLogo(R.drawable.back);
		cart = (RelativeLayout) findViewById(R.id.cart);
		cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						CartActivity.class);
				startActivity(intent);
			}
		});
		txtCartCount = (TextView) findViewById(R.id.txtCartSize);
		setListeners();
		txtNameText
		.setText("" + mProductsList.get(pos).getmProductName());
		txtViewPrice.setText("Rs." + mProductsList.get(pos).getmProductPrize());
		txtdescription.setText(mProductsList.get(pos).getProductDescription());
getProduct_DetailPage(mProductsList.get(pos).getId());
	}
	private void setListeners() {
		// TODO Auto-generated method stub
		relShare.setOnClickListener(this);
		relCart.setOnClickListener(this);
		mImgArrowRight.setOnClickListener(this);
		mImgArrowLeft.setOnClickListener(this);
		listViewSize.setOnItemClickListener(this);
	}
	@Override
	protected int getLayoutResourceId() {
		// TODO Auto-generated method stub
		return R.layout.activity_productdetail;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	protected void getProduct_DetailPage(String product_id) {
		// TODO Auto-generated method stub
		String name[] = { "productId" };
		String value[] = { product_id };
		VKCInternetManager manager = new VKCInternetManager(
				URL_GET_PRODUCTDETAILPAGE);
		manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {

				// parseJSON(successResponse);
				Log.v("LOG", "detail page " + successResponse);
				parseDetails(successResponse);
			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub
				Log.v("LOG", "04122014FAIL " + failureResponse);
				// mIsError = true;

			}
		});
	}
	
	private void parseDetails(String successResponse) {
		// TODO Auto-generated method stub
		productModels = new ArrayList<ProductModel>();

		try {
			JSONObject jsonObject = new JSONObject(successResponse);
			JSONObject jsonresponse = jsonObject.getJSONObject("response");
			JSONArray jsonArrayresponse = jsonresponse.getJSONArray("details");

			for (int j = 0; j < jsonArrayresponse.length(); j++) {
				System.out.println("16122014  IN loop" + j);
				HomeImageBannerModel bannerModel = new HomeImageBannerModel();

				ProductModel productModel = parseProductModel(jsonArrayresponse
						.getJSONObject(j));

				productModels.add(productModel);

				bannerModel.setId(bannerModel.getId());

				bannerModel.setBannerUrl(productModel.getProductImages().get(0)
						.getImageName());

				bannerModel.setSlideId(productModel.getProductImages().get(0)
						.getId());

				//ArrayList<Related_Images> related_Images = new ArrayList<>();
				Intent intent = new Intent(mActivity,
						ProductDetailActivity.class);
				intent.putExtra("MODEL", productModel);
				mActivity.startActivity(intent);
				finish();
			}
		} catch (Exception ex) {
			System.out.println("Exception in getting the detail page is"
					+ ex.toString());
		}
	}
	private ProductModel parseProductModel(JSONObject jsonObjectZero) {
		// TODO Auto-generated method stub
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
				System.out.println("imgs url 123 " + images.getImageName());
				ColorModel colorModel = new ColorModel();
				colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
				colorModel.setColorcode(jsonObject
						.optString(JSON_SETTINGS_COLORCODE));
				images.setColorModel(colorModel);
				productImages.add(images);

			}
			productModel.setProductImages(productImages);
			// //
			ArrayList<Related_Images> related_Images = new ArrayList<>();
			for (int i = 0; i < product_related.length(); i++) {
				Related_Images related_Images2 = new Related_Images();
				JSONObject jsonObject = product_related.getJSONObject(i);
				related_Images2.setImageId(jsonObject.getString("image_id"));
				related_Images2.setmageurl(BASE_URL
						+ jsonObject.optString("image_name"));
				related_Images2.setProduct_id(jsonObject
						.getString("product_id"));

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
}
*/
