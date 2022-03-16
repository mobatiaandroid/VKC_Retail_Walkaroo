package com.mobatia.vkcretailer.controller;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mobatia.vkcretailer.activities.VKCSplashActivity;
import com.mobatia.vkcretailer.manager.VKCBitmapCache;
import com.mobatia.vkcretailer.model.CartModel;
import com.mobatia.vkcretailer.model.CategoryModel;
import com.mobatia.vkcretailer.model.DealersShopModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.ProductModel;
import com.mobatia.vkcretailer.model.SubDealerOrderDetailModel;
import com.mobatia.vkcretailer.model.SubDealerOrderListModel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

public class AppController extends Application implements Serializable,
		java.lang.Thread.UncaughtExceptionHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
	public static final String TAG = AppController.class.getSimpleName();
	public static EditText editText;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	public static ArrayList<String> sizeName = new ArrayList();
	public static ArrayList<String> sizeId = new ArrayList<>();
	public static ArrayList<String> quantity = new ArrayList<>();
	public static ArrayList<EditText> listEdit = new ArrayList<EditText>();
	public static String case_count;
	public static String size_string = "";
	public static ArrayList<String> positionquty = new ArrayList<>();
	public static ArrayList<String> positionsize = new ArrayList<>();
	public static ArrayList<ProductImages> product_images = new ArrayList<>();
	public static ProductModel productModel = new ProductModel();
	private static AppController mInstance;
	public static List<String> listDealers;
	private static Context mContext;
	public static boolean isFilterSet;
	public static ArrayList<DealersShopModel> dealersModels = new ArrayList<>();
	public static ArrayList<SubDealerOrderDetailModel> subDealerOrderDetailList = new ArrayList<>();
	public static ArrayList<SubDealerOrderDetailModel> TempSubDealerOrderDetailList = new ArrayList<SubDealerOrderDetailModel>();
	public static List<SubDealerOrderListModel> subDealerModels = new ArrayList<SubDealerOrderListModel>();
	public static ArrayList<SubDealerOrderDetailModel> subDealerorderList = new ArrayList<SubDealerOrderDetailModel>();
	public static ArrayList<CartModel> cartArrayList = new ArrayList<CartModel>();;
	public static List<String> listErrors = new ArrayList<>();
	public static int status = 0;
	public static boolean isCart = false;
	public static boolean isSubmitError = false;
	public static boolean isEditable = true;
	public static boolean isDealerList = false;
	public static int selectedProductPosition = 0;
	public static String articleNumber = "";
	public static ArrayList<String> tempmainCatArrayList=new ArrayList<String>();
	public static ArrayList<String> tempsubCatArrayList=new ArrayList<String>();
	public static ArrayList<String> tempsizeCatArrayList=new ArrayList<String>();
	public static ArrayList<String> tempbrandCatArrayList=new ArrayList<String>();
	public static ArrayList<String> temppriceCatArrayList=new ArrayList<String>();
	public static ArrayList<String> tempcolorCatArrayList=new ArrayList<String>();
	public static ArrayList<String> tempofferCatArrayList=new ArrayList<String>();

	/** The mClass */
	private final Class<?> mClass;
	/** The mIntent */
	private Intent mIntent;

	@Override
	public void onCreate() {
		super.onCreate();
		listDealers = new ArrayList<String>();
		mInstance = this;
	}

	/**
	 * 
	 */
	public AppController() {
		// TODO Auto-generated constructor stub
		this.mContext = null;
		this.mClass = null;
	}

	public AppController(Context mContext) {

		this.mContext = mContext;
		this.mClass = null;
	}

	public AppController(Context mContext, Class<?> mClass) {

		this.mContext = mContext;
		this.mClass = mClass;
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public static Context getContext() {

		return mContext;

	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new VKCBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
	 * .Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		// TODO Auto-generated method stub
		StringWriter mStringWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(mStringWriter));
		System.out.println(mStringWriter);

		mIntent = new Intent(mContext, VKCSplashActivity.class);
		String s = mStringWriter.toString();
		Log.e("LOG", s);
		// you can use this String to know what caused the exception and in
		// which Activity
		mIntent.putExtra("uncaught Exception",
				"Exception is: " + mStringWriter.toString());
		mIntent.putExtra("uncaught stacktrace", s);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(mIntent);
		System.out.println("error " + s);
		// for restarting the Activity
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);

	}
}
