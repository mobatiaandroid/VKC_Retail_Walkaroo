/**
 *
 */
package com.mobatia.vkcretailer.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.controller.BaseActivity;
import com.mobatia.vkcretailer.customview.CustomProgressBar;
import com.mobatia.vkcretailer.customview.HorizontalListView;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CartModel;
import com.mobatia.vkcretailer.model.CaseModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.HomeImageBannerModel;
import com.mobatia.vkcretailer.model.ListImageModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.ProductModel;
import com.mobatia.vkcretailer.model.Related_Images;
import com.mobatia.vkcretailer.model.SizeModel;

/**
 * @author archana.s
 */
public class ProductDetailActivity extends AppCompatActivity implements
        VKCUrlConstants, OnClickListener, VKCDbConstants, OnItemClickListener,
        VKCJsonTagConstants {
    private static ArrayList<ProductModel> productModels;

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

    ProductModel productModel = new ProductModel();
    Button buttonShare, buttonLike;
    private DataBaseManager databaseManager;
    private EditText edtQuantity;
    private ArrayList<ProductImages> imageUrls;
    private ArrayList<ColorModel> colorArrayList;
    private ArrayList<SizeModel> sizeArrayList;
    private ArrayList<CaseModel> caseArrayList;
    ArrayList<Related_Images> imageUrls3;
    ArrayList<ProductModel> mProductsList;
    String pid = "";
    private TextView edtPendQuantity;
    ImageView imageProduct;
ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);
        mActivity = this;
        getSupportActionBar().hide();

       // productModel = (ProductModel) getIntent().getExtras().getSerializable(
              //  "MODEL");
       // if (productModel == null) {
            pid = getIntent().getExtras().getString("PID");
            getProduct_DetailPage(pid, 0);
       // } else {
//            initialiseUI();

       // }

        /*final ActionBar abar = getSupportActionBar();

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

        setActionBar();*/
        /*LikeCountApi();
        try {
            setCartQuantity();
        } catch (Exception e) {
            System.out.println("Error " + e);
        }*/
        /*setLayoutDimension();
        setActionBar();*/
        // increasePopularCount();
        //  productDetailStatus();
    }

    private void productDetailStatus() {
        final VKCInternetManager manager = new VKCInternetManager(
                GET_SALES_ORDER_STATUS);
        String name[] = {"user_id", "product_id"};
        String value[] = {AppPrefenceManager.getUserId(mActivity),
                productModel.getId()};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                // Log.v("LOG", "19052015 successResponse: " + successResponse);
                try {
                    JSONObject jsonObj = new JSONObject(successResponse);
                    JSONArray jsonArray = jsonObj
                            .getJSONArray("productSalesOrderStatus");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        System.out.println("pending12 " + jObj);
                        jObj.opt("id");
                        jObj.opt("CusId");
                        jObj.opt("productSapId");
                        jObj.opt("CusName");
                        jObj.opt("MaterialNo");
                        jObj.opt("OrderDate");
                        jObj.opt("OrderQty");
                        jObj.opt("PendingQty");
                        edtPendQuantity.setText(jObj.opt("PendingQty")
                                .toString());
                       /* System.out.println("pending qty "
                                + jObj.opt("PendingQty").toString());*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Exception " + e.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                // mIsError = true;

            }
        });

    }

   /* @Override
    protected int getLayoutResourceId() {
        // TODO Auto-generated method stub
        return R.layout.activity_productdetail;
    }*/

    private void increasePopularCount() {
        String name[] = {"product_id"};
        String values[] = {productModel.getId()};
        final VKCInternetManager manager = new VKCInternetManager(
                POPULARITY_COUNT_URL);
        manager.getResponsePOST(ProductDetailActivity.this, name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "12122014 success" + successResponse);
                        // parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "12122014 Errror" + failureResponse);
                    }
                });
    }

    /*
     * Method Name:setLayoutDimension() Parameter:nill Description:Set layout
     * according to device height and width
     */

    private void setLayoutDimension() {
        displayManagerScale = new DisplayManagerScale(mActivity);
        width = displayManagerScale.getDeviceWidth();
        height = displayManagerScale.getDeviceHeight();
        mRelImage.getLayoutParams().height = (int) (height * 0.35);
        relativSecondSec.getLayoutParams().height = (int) (height * .35);
        /*System.out.println("The height is" + (int) (height * .40)
                + "the second height is" + (int) (height * .30));*/
        mRelativText.getLayoutParams().height = (int) (height * .08);
        mRelBottom.getLayoutParams().height = (int) (height * .07);

    }

    /*
     * Method Name:initialiseUI() Parameter:nill Description:Initialise UI
     * elements
     */

    private void initialiseUI() {

        databaseManager = new DataBaseManager(mActivity);
        relativSecondSec = (RelativeLayout) findViewById(R.id.relativSecondSec);
       // mHorizontalListView = (HorizontalListView) findViewById(R.id.listView);
        listViewColor = (HorizontalListView) findViewById(R.id.listViewColor);
        listViewSize = (HorizontalListView) findViewById(R.id.listViewSize);
        first = (RelativeLayout) findViewById(R.id.first);
        int width = first.getWidth();
        imgBack=(ImageView)findViewById(R.id.imageBack);
        txtlikeCount = (TextView) findViewById(R.id.txtLikeCount);
        listViewQuty = (HorizontalListView) findViewById(R.id.listViewquty);
        // listViewColor.setBackgroundColor(Color.parseColor("#FF00FF"));
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
        txtNameText
                .setText("" + productModel.getProductType().get(0).getName());
        txtViewPrice = (TextView) findViewById(R.id.txtViewPrice);
        txtViewPrice.setText("Rs." + productModel.getmProductPrize());
        txtdescription.setText(productModel.getProductDescription());
        relShare = (RelativeLayout) findViewById(R.id.relShare);
        relCart = (RelativeLayout) findViewById(R.id.relCart);
       // getSupportActionBar().setLogo(R.drawable.back);
        cart = (RelativeLayout) findViewById(R.id.cart);
        LikeCountApi(productModel.getId());

        setLayoutDimension();
        //setActionBar();
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(),
                        CartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txtCartCount = (TextView) findViewById(R.id.txtCartSize);
        setListeners();
        colorArrayList = new ArrayList<ColorModel>();
        sizeArrayList = new ArrayList<SizeModel>();

        imageUrls = productModel.getProductImages();
        imageUrls3 = productModel.getRelated_Images();
        colorArrayList = productModel.getProductColor();
        sizeArrayList = productModel.getmProductSize();
        caseArrayList = productModel.getmProductCases();
       /* mImagePager = (ViewPager) findViewById(R.id.imagePager);
        mViewPagerAdapter = new ViewpagerAdapter(mActivity, imageUrls, 0);
        mImagePager.setAdapter(mViewPagerAdapter);*/
       // mListAdapter = new ListImageAdapter(mActivity, imageUrls3);
       // mHorizontalListView.setAdapter(mListAdapter);
        colorGridAdapter = new ColorGridAdapter(mActivity, colorArrayList, 1,
                productModel.getId());
        AppController.listEdit.clear();
        for (int i = 0; i < sizeArrayList.size(); i++) {
            EditText edt = new EditText(mActivity);
            AppController.listEdit.add(edt);
        }
        sizeGridAdapter = new SizeGridAdapter(mActivity, sizeArrayList);
        quantity_Adapter = new Quantity_Adapter(mActivity, sizeArrayList.size());
        listViewColor.setAdapter(colorGridAdapter);
        listViewSize.setAdapter(sizeGridAdapter);
        setColorGridClickListener(listViewColor);
        listViewQuty.setAdapter(quantity_Adapter);
        caseedt.setText("");
        try {
            setCartQuantity();
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
        //setHorizontalListAction(mHorizontalListView);

        buttonLike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == R.id.btnLike) {

                    LikeApi(v);

                }
            }
        });

    }

    private void initialiseUIFromAPI() {

        databaseManager = new DataBaseManager(mActivity);
        relativSecondSec = (RelativeLayout) findViewById(R.id.relativSecondSec);
       // mHorizontalListView = (HorizontalListView) findViewById(R.id.listView);
        listViewColor = (HorizontalListView) findViewById(R.id.listViewColor);
        listViewSize = (HorizontalListView) findViewById(R.id.listViewSize);
        first = (RelativeLayout) findViewById(R.id.first);
        int width = first.getWidth();
        txtlikeCount = (TextView) findViewById(R.id.txtLikeCount);
        listViewQuty = (HorizontalListView) findViewById(R.id.listViewquty);
        // listViewColor.setBackgroundColor(Color.parseColor("#FF00FF"));
        imgBack=(ImageView)findViewById(R.id.imageBack);
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
        txtNameText
                .setText("" + productModels.get(0).getmProductName());
        txtViewPrice = (TextView) findViewById(R.id.txtViewPrice);
        txtViewPrice.setText("Rs." + productModels.get(0).getmProductPrize());
        txtdescription.setText(productModels.get(0).getProductDescription());
        relShare = (RelativeLayout) findViewById(R.id.relShare);
        relCart = (RelativeLayout) findViewById(R.id.relCart);
        getSupportActionBar().setLogo(R.drawable.back);
        cart = (RelativeLayout) findViewById(R.id.cart);
        imageProduct=(ImageView)findViewById(R.id.imageProduct);
        txtCartCount = (TextView) findViewById(R.id.txtCartSize);
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LikeCountApi(productModels.get(0).getId());
        try {
            setCartQuantity();
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
        setLayoutDimension();
       // setActionBar();
        cart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(),
                        CartActivity.class);
                startActivity(intent);
            }
        });

        setListeners();
        colorArrayList = new ArrayList<ColorModel>();
        sizeArrayList = new ArrayList<SizeModel>();
        caseArrayList = new ArrayList<CaseModel>();
        imageUrls = productModels.get(0).getProductImages();
        imageUrls3 = productModels.get(0).getRelated_Images();
        colorArrayList = productModels.get(0).getProductColor();
        sizeArrayList = productModels.get(0).getmProductSize();
        caseArrayList = productModels.get(0).getmProductCases();
        /*mImagePager = (ViewPager) findViewById(R.id.imagePager);
        mViewPagerAdapter = new ViewpagerAdapter(mActivity, imageUrls, 0);
        mImagePager.setAdapter(mViewPagerAdapter);*/
        //mListAdapter = new ListImageAdapter(mActivity, imageUrls3);
        //mHorizontalListView.setAdapter(mListAdapter);
        colorGridAdapter = new ColorGridAdapter(mActivity, colorArrayList, 1,
                productModels.get(0).getId());
        AppController.listEdit.clear();
        for (int i = 0; i < sizeArrayList.size(); i++) {
            EditText edt = new EditText(mActivity);
            AppController.listEdit.add(edt);
        }
        sizeGridAdapter = new SizeGridAdapter(mActivity, sizeArrayList);
        quantity_Adapter = new Quantity_Adapter(mActivity, sizeArrayList.size());
        listViewColor.setAdapter(colorGridAdapter);
        listViewSize.setAdapter(sizeGridAdapter);
        setColorGridClickListener(listViewColor);
        listViewQuty.setAdapter(quantity_Adapter);
        caseedt.setText("");
        //setHorizontalListAction(mHorizontalListView);

        buttonLike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (v.getId() == R.id.btnLike) {

                    LikeApi(v);

                }
            }
        });

    }

    private void setListeners() {
        relShare.setOnClickListener(this);
        relCart.setOnClickListener(this);
        mImgArrowRight.setOnClickListener(this);
        mImgArrowLeft.setOnClickListener(this);
        listViewSize.setOnItemClickListener(this);

    }

  /*  private void setHorizontalListAction(HorizontalListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                *//*System.out.println("inside the onclick item" + position
                        + "image url is"
                        + imageUrls3.get(position).getImageurl()
                        + "image id is"
                        + imageUrls3.get(position).getProduct_id());*//*
                getProduct_DetailPage(imageUrls3.get(position).getProduct_id(), position);
               *//* mViewPagerAdapter = new ViewpagerAdapter(mActivity, imageUrls,
                        imageUrls3.get(position).getImageurl(), 1);
                mImagePager.setAdapter(mViewPagerAdapter);*//*

            }
        });

    }*/

    protected void getProduct_DetailPage(String product_id, int position) {
        // TODO Auto-generated method stub
        String name[] = {"productId"};
        String value[] = {product_id};
        VKCInternetManager manager = new VKCInternetManager(
                URL_GET_PRODUCTDETAILPAGE);
        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                // Log.v("LOG", "detail page " + successResponse);
                parseDetails(successResponse, position);
            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                // mIsError = true;

            }
        });
    }

    private void parseDetails(String successResponse, int position) {
        // TODO Auto-generated method stub
        productModels = new ArrayList<ProductModel>();

        try {
            JSONObject jsonObject = new JSONObject(successResponse);
            JSONObject jsonresponse = jsonObject.getJSONObject("response");
            JSONArray jsonArrayresponse = jsonresponse.getJSONArray("details");

            for (int j = 0; j < jsonArrayresponse.length(); j++) {
                //  System.out.println("16122014  IN loop" + j);
                HomeImageBannerModel bannerModel = new HomeImageBannerModel();

                ProductModel productModel = parseProductModel(jsonArrayresponse
                        .getJSONObject(j));

                productModels.add(productModel);

                bannerModel.setId(bannerModel.getId());

                bannerModel.setBannerUrl(productModel.getProductImages().get(0)
                        .getImageName());

                bannerModel.setSlideId(productModel.getProductImages().get(0)
                        .getId());

                ArrayList<Related_Images> related_Images = new ArrayList<>();
                /*Intent intent = new Intent(mActivity,
                        ProductDetailActivity.class);
                intent.putExtra("MODEL", productModel);
                mActivity.startActivity(intent);
                //finish();*/
            }
            initialiseUIFromAPI();
            final ProgressBar progressBar = (ProgressBar)
                    findViewById(R.id.progressBar1);
            VKCUtils.setImageFromUrl(mActivity, productModels.get(0).getProductImages().get(0).getImageName(), imageProduct, progressBar);
          /*  mViewPagerAdapter = new ViewpagerAdapter(mActivity, productModels.get(0).getProductImages(), 0);
            mImagePager.setAdapter(mViewPagerAdapter);*/


/*
            Intent intent = new Intent(mActivity,
                    ProductDetailActivity.class);
            intent.putExtra("MODEL", productModel);
            mActivity.startActivity(intent);
          ///  finish();*/
        } catch (Exception ex) {
            System.out.println("Exception in getting the detail page is"
                    + ex.toString());
        }
    }

    private ProductModel parseProductModel(JSONObject jsonObjectZero) {
        // TODO Auto-generated method stub
        ProductModel productModel = new ProductModel();
        try {
          /*  System.out.println("28122014 parseProductModel"
                    + jsonObjectZero.toString());*/
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
                //  System.out.println("imgs url 123 " + images.getImageName());
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

    private void setColorGridClickListener(HorizontalListView listView) {
        listView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                ///   Log.d("TAG 04022015", "04022015 " + position);

                ArrayList<Related_Images> imageUrls = new ArrayList<Related_Images>();
                ArrayList<Related_Images> imageUrls2 = new ArrayList<Related_Images>();

                ArrayList<ProductImages> imageUrlsTemp = new ArrayList<ProductImages>();
                ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();

                if (productModel == null) {
                    imageUrls = productModels.get(0).getRelated_Images();
                    colorArrayList = productModels.get(0).getProductColor();

                    for (int i = 0; i < imageUrls.size(); i++) {

                        if (imageUrls.get(i).equals(
                                colorArrayList.get(position).getColorcode())) {
                            imageUrls2.add(imageUrls.get(i));
                        }

                    }
                } else {
                    imageUrls = productModels.get(0).getRelated_Images();
                    colorArrayList = productModels.get(0).getProductColor();

                    for (int i = 0; i < imageUrls.size(); i++) {

                        if (imageUrls.get(i).equals(
                                colorArrayList.get(position).getColorcode())) {
                            imageUrls2.add(imageUrls.get(i));
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                Log.d("TAG 04022015", "04022015 0");

            }

        });

        // listViewColor.set
        listViewColor.setSelection(0);

    }

    private void shareIntent(String link) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("text/plain");

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(emailIntent, getApplicationContext()
                .getResources().getString(R.string.app_name)));
    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.show();
        getSupportActionBar().setLogo(R.drawable.back);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                /*
                 * Intent intent=new Intent(this,DashboardFActivity.class);
                 * startActivity(intent);
                 */
                finish();

        }
        return (super.onOptionsItemSelected(item));
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == relShare) {

            shareIntent("http://vkcgroup.com/");

        } else if (v == relCart) {
            /*System.out.println("The length of the list is"
                    + AppController.quantity.size());*/
            if (AppPrefenceManager.getDealerId(mActivity).equals("")) {
                VKCUtils.showtoast(mActivity, 44);
            } else {
                if (!caseedt.getText().toString().equalsIgnoreCase("")) {
                    if (!caseedt.getText().toString().equalsIgnoreCase("0")) {
                        //  System.out.println("inside the case");
                        String case_test = caseedt.getText().toString();
                        AppController.case_count = case_test;
                        AppPrefenceManager.saveUnit(mActivity, "1");
                        listViewQuty.setAdapter(quantity_Adapter);
                        if (AppController.sizeName.size() != 0
                                && !ProductDetailActivity.selectedFromColorList
                                .equals("")) {
                            if (AppController.sizeName.size() != 0) {

                                int cartCount = getCartData();
                                /*System.out.println("The cartSize is"
                                        + AppController.sizeName.size());
                                System.out
                                        .println("tjhe array size is Cartarray size is "
                                                + AppController.cartArrayList
                                                .size());*/
                                for (int i = 0; i < AppController.sizeName.size(); i++) {
                                   /* System.out.println("the value of i"
                                            + AppController.sizeName.get(i));*/
                                    AppController.size_string = AppController.size_string
                                            + (AppController.sizeName.get(i));
                                    AppController.size_string = AppController.size_string
                                            + (",");
                                }
                               /* System.out.println("The content of size string is"
                                        + AppController.size_string);*/
                                if (cartCount > 0) {// AppController.cartArrayList.size()
                                    // > 0
                                    for (int i = 0; i < AppController.cartArrayList
                                            .size(); i++) {
                                        String productId = AppController.cartArrayList
                                                .get(i).getProdId();
                                        String productColor = AppController.cartArrayList
                                                .get(i).getProdColor();
                                        String productSize = AppController.cartArrayList
                                                .get(i).getProdSize();
                                        String Id = AppController.cartArrayList
                                                .get(i).getProdSizeId();
                                        String qty = AppController.cartArrayList
                                                .get(i).getProdQuantity();

                                        for (int j = 0; j < AppController.sizeName
                                                .size(); j++) {

                                            if (productModel != null) {
                                                if (productId.equals(productModel
                                                        .getId())
                                                        && productColor
                                                        .equals(ProductDetailActivity.selectedFromColorList)
                                                        && productSize
                                                        .equals(AppController.sizeName
                                                                .get(j))) {
                                                    int newQty = Integer.parseInt(qty)
                                                            + Integer
                                                            .parseInt(AppController.case_count);
                                                    isUpdated = true;
                                                    String data[][] = {{
                                                            PRODUCT_QUANTITY,
                                                            String.valueOf(newQty)}};
                                                    String constrain[][] = {{
                                                            PRODUCT_QUANTITY, qty}};
                                                    databaseManager.updateTableRow(
                                                            TABLE_SHOPPINGCART, data,
                                                            constrain);
                                                    VKCUtils.showtoast(mActivity, 35);
                                                    setCartQuantity();
                                                    colorGridAdapter = new ColorGridAdapter(
                                                            mActivity, colorArrayList,
                                                            1, productModel.getId());
                                                    sizeGridAdapter = new SizeGridAdapter(
                                                            mActivity, sizeArrayList);
                                                    listViewColor
                                                            .setAdapter(colorGridAdapter);
                                                    listViewSize
                                                            .setAdapter(sizeGridAdapter);
                                                    AppController.sizeName.clear();
                                                    AppController.sizeId.clear();
                                                    AppController.quantity.clear();
                                                    AppController.positionquty.clear();
                                                    AppController.positionsize.clear();
                                                    AppController.editText.setText("");
                                                    listViewQuty
                                                            .setAdapter(quantity_Adapter);
                                                    caseedt.setText("");
                                                    ProductDetailActivity.selectedFromColorList = "";
                                                }
                                            } else {
                                                if (productId.equals(productModels.get(0)
                                                        .getId())
                                                        && productColor
                                                        .equals(ProductDetailActivity.selectedFromColorList)
                                                        && productSize
                                                        .equals(AppController.sizeName
                                                                .get(j))) {
                                                    int newQty = Integer.parseInt(qty)
                                                            + Integer
                                                            .parseInt(AppController.case_count);
                                                    isUpdated = true;
                                                    String data[][] = {{
                                                            PRODUCT_QUANTITY,
                                                            String.valueOf(newQty)}};
                                                    String constrain[][] = {{
                                                            PRODUCT_QUANTITY, qty}};
                                                    databaseManager.updateTableRow(
                                                            TABLE_SHOPPINGCART, data,
                                                            constrain);
                                                    VKCUtils.showtoast(mActivity, 35);
                                                    setCartQuantity();
                                                    colorGridAdapter = new ColorGridAdapter(
                                                            mActivity, colorArrayList,
                                                            1, productModels.get(0).getId());
                                                    sizeGridAdapter = new SizeGridAdapter(
                                                            mActivity, sizeArrayList);
                                                    listViewColor
                                                            .setAdapter(colorGridAdapter);
                                                    listViewSize
                                                            .setAdapter(sizeGridAdapter);
                                                    AppController.sizeName.clear();
                                                    AppController.sizeId.clear();
                                                    AppController.quantity.clear();
                                                    AppController.positionquty.clear();
                                                    AppController.positionsize.clear();
                                                    AppController.editText.setText("");
                                                    listViewQuty
                                                            .setAdapter(quantity_Adapter);
                                                    caseedt.setText("");
                                                    ProductDetailActivity.selectedFromColorList = "";

                                                }
                                            }
                                        }
                                    }

                                    if (!isUpdated) {
									/*
									if(AppPrefenceManager.getUserType(mActivity).equals("5")&&AppPrefenceManager.getDealerId(mActivity).equals("")){
										VKCUtils.showtoast(mActivity, 42);
									}
									else
									{*/
                                        AddToCart addToCart = new AddToCart();
                                        addToCart.execute();
                                        isUpdated = false;
                                        //}
                                    }
                                } else {
								
								/*if(AppPrefenceManager.getUserType(mActivity).equals("5")&&AppPrefenceManager.getDealerId(mActivity).equals("")){
									VKCUtils.showtoast(mActivity, 42);
								}
								else
								{*/
                                    AddToCart addToCart = new AddToCart();
                                    addToCart.execute();
                                    isUpdated = false;
                                    //}

                                }
                            } else {

                                VKCUtils.showtoast(mActivity, 24);
                            }
                        } else {

                            VKCUtils.showtoast(mActivity, 3);
                        }
                    } else {
                        VKCUtils.showtoast(mActivity, 4);

                    }
                }
                // //////////////////////////////////////////////////////////////////////////
                else {
                    AppPrefenceManager.saveUnit(mActivity, "0");
                    //  System.out.println("inside the else quantity");

                    if ((AppController.sizeName.size() != 0)
                            && (AppController.quantity.size() != 0)
                            && (ProductDetailActivity.selectedFromColorList != null)) {
                        if (AppController.sizeName.size() != 0
                                && !ProductDetailActivity.selectedFromColorList
                                .equals("")) {
                            if (AppController.sizeName.size() != 0) {

                                int cartCount = getCartData();
                               /* System.out.println("The cartSize is" + cartCount);
                                System.out
                                        .println("tjhe array size is Cartarray size is "
                                                + AppController.cartArrayList
                                                .size());*/

                                if (cartCount > 0) {// AppController.cartArrayList.size()
                                    // > 0
                                    for (int i = 0; i < AppController.cartArrayList
                                            .size(); i++) {
                                        String productId = AppController.cartArrayList
                                                .get(i).getProdId();
                                        String productColor = AppController.cartArrayList
                                                .get(i).getProdColor();
                                        String productSize = AppController.cartArrayList
                                                .get(i).getProdSize();
                                        String Id = AppController.cartArrayList
                                                .get(i).getProdSizeId();
                                        String qty = AppController.cartArrayList
                                                .get(i).getProdQuantity();
                                        System.out
                                                .println("the cartarray element is"
                                                        + AppController.cartArrayList
                                                        .get(i)
                                                        .getProdQuantity());
                                        for (int j = 0; j < AppController.sizeName
                                                .size(); j++) {


                                            if (productModel != null) {
                                                if (productId.equals(productModel
                                                        .getId())
                                                        && productColor
                                                        .equals(ProductDetailActivity.selectedFromColorList)
                                                        && productSize
                                                        .equals(AppController.sizeName
                                                                .get(j))) {
                                                    int newQty = Integer.parseInt(qty)
                                                            + Integer
                                                            .parseInt(AppController.quantity
                                                                    .get(j));
                                                    isUpdated = true;
                                                    String data[][] = {{
                                                            PRODUCT_QUANTITY,
                                                            String.valueOf(newQty)}};
                                                    String constrain[][] = {{
                                                            PRODUCT_QUANTITY, qty}};
                                                    databaseManager.updateTableRow(
                                                            TABLE_SHOPPINGCART, data,
                                                            constrain);
                                                    VKCUtils.showtoast(mActivity, 35);
                                                    setCartQuantity();
                                                    colorGridAdapter = new ColorGridAdapter(
                                                            mActivity, colorArrayList,
                                                            1, productModel.getId());
                                                    sizeGridAdapter = new SizeGridAdapter(
                                                            mActivity, sizeArrayList);
                                                    listViewColor
                                                            .setAdapter(colorGridAdapter);
                                                    listViewSize
                                                            .setAdapter(sizeGridAdapter);
                                                    AppController.sizeName.clear();
                                                    AppController.sizeId.clear();
                                                    AppController.quantity.clear();
                                                    AppController.positionquty.clear();
                                                    AppController.positionsize.clear();
                                                    listViewQuty
                                                            .setAdapter(quantity_Adapter);
                                                    ProductDetailActivity.selectedFromColorList = "";

                                                }
                                            } else {
                                                if (productId.equals(productModels.get(0)
                                                        .getId())
                                                        && productColor
                                                        .equals(ProductDetailActivity.selectedFromColorList)
                                                        && productSize
                                                        .equals(AppController.sizeName
                                                                .get(j))) {
                                                    int newQty = Integer.parseInt(qty)
                                                            + Integer
                                                            .parseInt(AppController.quantity
                                                                    .get(j));
                                                    isUpdated = true;
                                                    String data[][] = {{
                                                            PRODUCT_QUANTITY,
                                                            String.valueOf(newQty)}};
                                                    String constrain[][] = {{
                                                            PRODUCT_QUANTITY, qty}};
                                                    databaseManager.updateTableRow(
                                                            TABLE_SHOPPINGCART, data,
                                                            constrain);
                                                    VKCUtils.showtoast(mActivity, 35);
                                                    setCartQuantity();
                                                    colorGridAdapter = new ColorGridAdapter(
                                                            mActivity, colorArrayList,
                                                            1, productModels.get(0).getId());
                                                    sizeGridAdapter = new SizeGridAdapter(
                                                            mActivity, sizeArrayList);
                                                    listViewColor
                                                            .setAdapter(colorGridAdapter);
                                                    listViewSize
                                                            .setAdapter(sizeGridAdapter);
                                                    AppController.sizeName.clear();
                                                    AppController.sizeId.clear();
                                                    AppController.quantity.clear();
                                                    AppController.positionquty.clear();
                                                    AppController.positionsize.clear();
                                                    listViewQuty
                                                            .setAdapter(quantity_Adapter);
                                                    ProductDetailActivity.selectedFromColorList = "";

                                                }
                                            }

                                        }
                                    }

                                    if (!isUpdated) {
                                        AddToCart addToCart = new AddToCart();
                                        addToCart.execute();
                                        isUpdated = false;
                                    }
                                } else {
                                    AddToCart addToCart = new AddToCart();
                                    addToCart.execute();
                                    isUpdated = false;

                                }
                            } else {

                                VKCUtils.showtoast(mActivity, 24);
                            }
                        } else {

                            VKCUtils.showtoast(mActivity, 3);
                        }
                    } else {

                        VKCUtils.showtoast(mActivity, 4);
                    }
                }
            }
        } else if (v == mImgArrowRight) {
            getImageURL();
            // mImagePager.setCurrentItem(mImagePager.getCurrentItem() + 1);

        } else if (v == mImgArrowLeft) {
            mViewPagerAdapter = new ViewpagerAdapter(mActivity, imageUrls, 0);
            mImagePager.setAdapter(mViewPagerAdapter);
            mImagePager.setCurrentItem(mImagePager.getCurrentItem() - 1);
            // System.out.println("inside the click");

        }

    }

    private void getImageURL() {
        // TODO Auto-generated method stub
        final VKCInternetManager manager = new VKCInternetManager(
                GET_IMAGES_LIST + "?imgid=" + imgid + "?pid="
                        + productModel.getId());
        manager.getResponse(mActivity, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                // TODO Auto-generated method stub
               /* System.out.println("inside the success response "
                        + successResponse);*/
                parseResponse(successResponse);
            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub

            }
        });
    }

    protected void parseResponse(String successResponse) {
        // TODO Auto-generated method stub
        try {
            ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();

            JSONObject jsonObject = new JSONObject(successResponse);
            JSONArray response = jsonObject.getJSONArray("response");
            if (response.length() != 0) {
                for (int i = 0; i < response.length(); i++) {
                    ProductImages images = new ProductImages();
                    JSONObject jsonObject1 = response.getJSONObject(i);
                    images.setImageName(BASE_URL
                            + jsonObject1.optString(JSON_COLOR_IMAGE));
                    ColorModel colorModel = new ColorModel();
                    colorModel.setId(jsonObject1
                            .optString(JSON_SETTINGS_COLORID));
                    colorModel.setColorcode(jsonObject1
                            .optString(JSON_SETTINGS_COLORCODE));
                    images.setColorModel(colorModel);
                    productImages.add(images);
                }
                productModel.setProductImages(productImages);
                imageUrls = productModel.getProductImages();

                mViewPagerAdapter = new ViewpagerAdapter(mActivity, imageUrls,
                        0);
                mImagePager.setAdapter(mViewPagerAdapter);

            }

        } catch (Exception ex) {
            System.out.println("the exception uid" + ex.toString());
        }

    }

    private class AddToCart extends AsyncTask<Void, Void, Void> {

        final CustomProgressBar pDialog = new CustomProgressBar(mActivity,
                R.drawable.loading);

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog.show();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (productModel != null) {
                String[][] values = null;
                if (AppPrefenceManager.getUnit(mActivity).equalsIgnoreCase("1")) {
                    values = new String[][]{
                            {PRODUCT_ID, productModel.getId()},
                            {PRODUCT_NAME, productModel.getmProductName()},
                            {PRODUCT_SIZEID, AppController.size_string},
                            {PRODUCT_SIZE, AppController.size_string},
                            {
                                    PRODUCT_COLORID,
                                    ProductDetailActivity.selectedIDFromColorList},
                            {PRODUCT_COLOR,
                                    ProductDetailActivity.selectedFromColorList},
                            {PRODUCT_QUANTITY, AppController.case_count},
                            {PRODUCT_GRIDVALUE, ""}, {PRODUCT_UNIT, "case"}, {ORDER_PRODUCT_PRICE, "case"}};
//ProductDetailActivity.selectedFromColorList
                    databaseManager.insertIntoDb(TABLE_SHOPPINGCART, values);

                } else {
                    for (int i = 0; i < AppController.listEdit.size(); i++) {

                        if (AppController.listEdit.get(i).getText().length() > 0) {
                            values = new String[][]{
                                    {PRODUCT_ID, productModels.get(0).getId()},
                                    {PRODUCT_NAME,
                                            productModels.get(0).getmProductName()},
                                    {PRODUCT_SIZEID,
                                            sizeArrayList.get(i).getId()},
                                    {PRODUCT_SIZE,
                                            sizeArrayList.get(i).getName()},
                                    {
                                            PRODUCT_COLORID,
                                            ProductDetailActivity.selectedIDFromColorList},
                                    {
                                            PRODUCT_COLOR,
                                            ProductDetailActivity.selectedFromColorList},
                                    {
                                            PRODUCT_QUANTITY,
                                            AppController.listEdit.get(i)
                                                    .getText().toString()},
                                    {PRODUCT_GRIDVALUE, ""},
                                    {PRODUCT_UNIT, "pair"}};

                            databaseManager.insertIntoDb(TABLE_SHOPPINGCART,
                                    values);
                            AppController.listEdit.get(i).setText("");
                        }
                        /*
                         * else { VKCUtils.showtoast(mActivity, 45); }
                         */
                    }
                }

            } else if (productModels != null) {
                String[][] values = null;
                if (AppPrefenceManager.getUnit(mActivity).equalsIgnoreCase("1")) {
                    values = new String[][]{
                            {PRODUCT_ID, productModels.get(0).getId()},
                            {PRODUCT_NAME, productModels.get(0).getmProductName()},
                            {PRODUCT_SIZEID, AppController.size_string},
                            {PRODUCT_SIZE, AppController.size_string},
                            {
                                    PRODUCT_COLORID,
                                    ProductDetailActivity.selectedIDFromColorList},
                            {PRODUCT_COLOR,
                                    ProductDetailActivity.selectedFromColorList},
                            {PRODUCT_QUANTITY, AppController.case_count},
                            {PRODUCT_GRIDVALUE, ""}, {PRODUCT_UNIT, "case"}, {ORDER_PRODUCT_PRICE, "case"}};
//ProductDetailActivity.selectedFromColorList
                    databaseManager.insertIntoDb(TABLE_SHOPPINGCART, values);

                } else {
                    for (int i = 0; i < AppController.listEdit.size(); i++) {

                        if (AppController.listEdit.get(i).getText().length() > 0) {
                            values = new String[][]{
                                    {PRODUCT_ID, productModels.get(0).getId()},
                                    {PRODUCT_NAME,
                                            productModels.get(0).getmProductName()},
                                    {PRODUCT_SIZEID,
                                            sizeArrayList.get(i).getId()},
                                    {PRODUCT_SIZE,
                                            sizeArrayList.get(i).getName()},
                                    {
                                            PRODUCT_COLORID,
                                            ProductDetailActivity.selectedIDFromColorList},
                                    {
                                            PRODUCT_COLOR,
                                            ProductDetailActivity.selectedFromColorList},
                                    {
                                            PRODUCT_QUANTITY,
                                            AppController.listEdit.get(i)
                                                    .getText().toString()},
                                    {PRODUCT_GRIDVALUE, ""},
                                    {PRODUCT_UNIT, "pair"}};

                            databaseManager.insertIntoDb(TABLE_SHOPPINGCART,
                                    values);
                            AppController.listEdit.get(i).setText("");
                        }
                        /*
                         * else { VKCUtils.showtoast(mActivity, 45); }
                         */
                    }
                }

            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            VKCUtils.showtoast(mActivity, 5);
            AppController.sizeName.clear();
            AppController.sizeId.clear();
            AppController.positionquty.clear();
            AppController.positionsize.clear();
            listViewQuty.setAdapter(quantity_Adapter);
            AppController.quantity.clear();
            caseedt.setText("");
            AppController.case_count = "";
            AppController.size_string = "";
            ProductDetailActivity.selectedFromColorList = "";
            setCartQuantity();
            if (productModel != null) {
                colorGridAdapter = new ColorGridAdapter(mActivity, colorArrayList,
                        1, productModel.getId());
            } else {

                colorGridAdapter = new ColorGridAdapter(mActivity, colorArrayList,
                        1, productModels.get(0).getId());

            }
            sizeGridAdapter = new SizeGridAdapter(mActivity, sizeArrayList);

            listViewColor.setAdapter(colorGridAdapter);
            listViewSize.setAdapter(sizeGridAdapter);
        }

    }

    private void LikeApi(final View v) {
        final VKCInternetManager manager = new VKCInternetManager(
                LIKE_PRODUCT_URL);
        String name[] = {"product_id", "user_id"};
        String value[] = {productModel.getId(),
                AppPrefenceManager.getUserId(this)};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                //    Log.v("LOG", "17022015 successResponse: " + successResponse);
                try {
                    JSONObject jobj = new JSONObject(successResponse);
                    JSONObject response = jobj.optJSONObject("response");
                    String status = response.optString("status");
                    if (status.equals("Success")) {

                        v.setBackgroundResource(R.drawable.likepress);
                        likeCount = likeCount + 1;
                        txtlikeCount.setText(String.valueOf(likeCount));
                    } else {
                        v.setBackgroundResource(R.drawable.like);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);

            }
        });

    }

    private void LikeCountApi(String id) {
        final VKCInternetManager manager = new VKCInternetManager(
                LIKE_COUNT_URL);
        String name[] = {"product_id", " user_id"};
        String value[] = {id,
                AppPrefenceManager.getUserId(this)};

        manager.getResponsePOST(mActivity, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                // Log.v("LOG", "17022015 successResponse: " + successResponse);
                try {
                    JSONObject jobj = new JSONObject(successResponse);
                    JSONObject response = jobj.optJSONObject("response");
                    String status = response.optString("status");
                    int likCount = Integer.valueOf(response.optString("count"));
                    String isLiked = response.optString("isliked");

                    if (status.equals("Success")) {

                        likeCount = likCount;
                        txtlikeCount.setText(String.valueOf(likeCount));
                        if (isLiked.equals("1")) {
                            buttonLike
                                    .setBackgroundResource(R.drawable.likepress);
                        } else {
                            buttonLike.setBackgroundResource(R.drawable.like);
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                // TODO Auto-generated method stub
                Log.v("LOG", "04122014FAIL " + failureResponse);
                // mIsError = true;

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

    public void setCartQuantity() {
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};//PRODUCT_UNIT
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");
        int mCount = 0;
        int cartount = 0;
        if (cursor.moveToFirst()) {
            do {
                String count = cursor.getString(cursor
                        .getColumnIndex("productqty"));
                cartount = Integer.parseInt(count);
                mCount = mCount + cartount;
                // do what ever you want here
            } while (cursor.moveToNext());
        }
        cursor.close();
        txtCartCount.setText(String.valueOf(mCount));
        System.out.println("the cart cursor size is" + cursor.getCount());

    }

    @Override
    protected void onResume() {
        super.onResume();

        //setCartQuantity();
    }

    private int getCartData() {
        AppController.cartArrayList.clear();
        String cols[] = {PRODUCT_ID, PRODUCT_NAME, PRODUCT_SIZEID,
                PRODUCT_SIZE, PRODUCT_COLORID, PRODUCT_COLOR, PRODUCT_QUANTITY,
                PRODUCT_GRIDVALUE};//PRODUCT_UNIT;
        Cursor cursor = databaseManager.fetchFromDB(cols, TABLE_SHOPPINGCART,
                "");
        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                AppController.cartArrayList.add(setCartModel(cursor));

                cursor.moveToNext();
            }

        } else {
            // System.out.println("Inside the getCartData");
        }
        return cursor.getCount();
    }

    private CartModel setCartModel(Cursor cursor) {
        CartModel cartModel = new CartModel();
        cartModel.setProdId(cursor.getString(0));
        cartModel.setProdName(cursor.getString(1));
        cartModel.setProdSizeId(cursor.getString(2));
        cartModel.setProdSize(cursor.getString(3));
        cartModel.setProdColorId(cursor.getString(4));
        cartModel.setProdColor(cursor.getString(5));
        cartModel.setProdQuantity(cursor.getString(6));
        cartModel.setProdGridValue(cursor.getString(7));
//        cartModel.setProd_Unit(cursor.getString(8));
        return cartModel;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();
        /*
         * Intent intent=new Intent(this,DashboardFActivity.class);
         * startActivity(intent);
         */
        finish();
    }

    @Override
    protected void onRestart() {

        setCartQuantity();
        super.onRestart();
    }
}
