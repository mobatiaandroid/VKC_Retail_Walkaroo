package com.mobatia.vkcretailer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.ProductDetailActivity;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CaseModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.HomeImageBannerModel;
import com.mobatia.vkcretailer.model.ProductImages;
import com.mobatia.vkcretailer.model.ProductModel;
import com.mobatia.vkcretailer.model.Related_Images;
import com.mobatia.vkcretailer.model.SizeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ProductListAdapterNew extends BaseAdapter implements VKCUrlConstants, VKCJsonTagConstants {
    Context mContext;
    static ArrayList<ProductModel> mProductsList;
    static ArrayList<ProductModel> mProductsListTemp;
    LayoutInflater mInflater;
    private View view;
    private int type;
    //ArrayList<ProductImages> imageUrls;
    private int currentPage;
    private int totalPage;
    ArrayList<ProductModel> productModels;

    private ScrollingAdapterinterface adapterinterface;

    //private int type;
    public ProductListAdapterNew(Context mcontext) {
        this.mContext = mcontext;

    }

	/*public ProductListAdapterNew(Context mcontext,
			ArrayList<ProductModel> mProductsList, int type) {

		this.mContext = mcontext;
		this.mProductsList = mProductsList;
		mProductsListTemp = new ArrayList<ProductModel>();
		this.type = type;
		mInflater = LayoutInflater.from(mContext);
		mProductsListTemp.addAll(mProductsList);
		imageUrls = new ArrayList<ProductImages>();
	}*/

    public ProductListAdapterNew(Context mcontext,
                                 ArrayList<ProductModel> mProductsList, int type, int currentPage, int totalPage, ScrollingAdapterinterface adapterinterface) {

        this.mContext = mcontext;
        this.mProductsList = mProductsList;
        mProductsListTemp = new ArrayList<ProductModel>();
        this.type = type;
        mInflater = LayoutInflater.from(mContext);
        mProductsListTemp.addAll(mProductsList);
        //imageUrls = new ArrayList<ProductImages>();
        this.adapterinterface = adapterinterface;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mProductsList.size();
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;

        View view;

        if (convertView == null) {

            if (type == 1) {

                view = mInflater.inflate(R.layout.custom_listlayout, parent,
                        false);
            } else {
                view = mInflater.inflate(R.layout.custom_gridlayout, parent,
                        false);
            }

        } else {
            view = convertView;
        }

        //imageUrls = mProductsList.get(position).getProductImages();
		/*System.out.println("img urls 21052015 size " + imageUrls.size());
		for (int i = 0; i < imageUrls.size(); i++) {
			System.out.println("img urls 21052015 "
					+ imageUrls.get(i).getImageName());
		}*/

        // if (convertView == null)
        {
            // view = mInflater.inflate(R.layout.custom_listlayout, parent,
            // false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.txtProductName = (TextView) view
                    .findViewById(R.id.txtProductName);
            holder.txtProductSize = (TextView) view
                    .findViewById(R.id.txtProductSize);
            holder.txtProductItemNumber = (TextView) view
                    .findViewById(R.id.txtProductItemNumber);
            holder.txtProductPrice = (TextView) view
                    .findViewById(R.id.txtProductPrice);
            holder.txtProductOff = (TextView) view
                    .findViewById(R.id.txtProductOff);
            // holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            // view.setTag(holder);

        }
        // else
        {
            // holder = (ViewHolder) view.getTag();
            // view = convertView;
        }
        // holder.imageView.setBackgroundResource(R.drawable.sandal);
        holder.imageView.setScaleType(ScaleType.CENTER_CROP);
        final ProgressBar progressBar = (ProgressBar) view
                .findViewById(R.id.progressBar1);

        if (mProductsList.size() != 0 && !mProductsList.get(position).getImage_name().equals("")) {

            String image_name = BASE_URL + mProductsList.get(position).getImage_name();
            if (mProductsList.get(position).getImage_name().equals("null")) {
                VKCUtils.setImageFromUrl((Activity) mContext, BASE_URL + "media/uploads/product_images/no_image.jpg", holder.imageView, progressBar);

            } else {
                image_name = image_name.replaceAll(" ", "%20");
                VKCUtils.setImageFromUrl((Activity) mContext, BASE_URL + mProductsList.get(position).getImage_name(), holder.imageView, progressBar);
            }
        } else {
            holder.imageView.setBackgroundResource(R.drawable.transparent_bg);
        }
        holder.txtProductName.setText(mProductsList.get(position)
                .getmProductName());
        holder.txtProductSize.setText("Size: "
                + mProductsList.get(position).getSize());
        // ₹
        holder.txtProductItemNumber.setText("Item Number: "
                + mProductsList.get(position).getId());
        holder.txtProductPrice.setText(" ₹ "
                + mProductsList.get(position).getmProductPrize());
        // holder.ratingBar.setRating(4);
        holder.txtProductOff.setText("Offer: "
                + mProductsList.get(position).getmProductOff() + " %");

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("search product 21052015");
			/*	Intent intent = new Intent(mContext,
						ProductDetailsActivityNew.class);
				AppController.selectedProductPosition = position;
				intent.putExtra("MODEL", mProductsList);
intent.putExtra("position", position);
				mContext.startActivity(intent);*/
                //  getProduct_DetailPage(mProductsList.get(position).getId());
                Intent intent = new Intent(mContext,
                        ProductDetailActivity.class);
                // intent.putExtra("MODEL", productModels.get(0));

                intent.putExtra("PID", mProductsList.get(position).getId());

                mContext.startActivity(intent);

            }
        });
        if (position == ((currentPage * 20) - 1) && currentPage <= totalPage) {
            adapterinterface.calledInterface(currentPage);
        }
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mProductsList.clear();
        if (charText.length() == 0) {
            mProductsList.addAll(mProductsListTemp);

        } else {
            for (ProductModel product : mProductsListTemp) {

                if ((product.getmProductName().contains(charText))
                        || (product.getmProductPrize().contains(charText))
                        || (product.getmProductSize().contains(charText))
                        || (product.getProductColor().contains(charText))
                        || (product.getProductDescription().contains(charText))
                        || (product.getmProductOff().contains(charText))) {
                    mProductsList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String getProductSizeList(ArrayList<SizeModel> sizeModels) {

        String size = "";

        for (SizeModel sizeModel : sizeModels) {

            size = size + sizeModel.getName() + ",";
        }
        if (size.length() != 0) {
            return size.substring(0, size.length() - 1);
        } else {
            return "";
        }

    }

    public class ViewHolder {
        ImageView imageView;
        TextView txtProductName, txtProductSize, txtProductItemNumber,
                txtProductPrice, txtProductOff, txtShop;
        // RatingBar ratingBar;
    }

    public void doSort(int sortOption) {
        inItComparator(sortOption);
        Collections.sort(mProductsList, myComparator);
		/*for (int i = 0; i < mProductsList.size(); i++) {
			System.out.println("23032015 "
					+ mProductsList.get(i).getmProductPrize());
		}*/

        /*if (sortOption == 2) {
            Collections.reverse(mProductsList);

        }*/
        notifyDataSetChanged();
    }

    private void inItComparator(final int sortOption) {
        myComparator = new Comparator<ProductModel>() {
            public int compare(ProductModel obj1, ProductModel obj2) {
                // return
                // obj1.getmProductPrize().compareTo(obj2.getmProductPrize());

                switch (sortOption) {
                    case 0: {

                        return obj1.getProductViews().compareTo(
                                obj2.getProductViews());
                    }
                    case 1: {

                        String[] parts_1 = obj1.getmProductPrize().split("\\."); // escape .
                        String part1 = parts_1[0];

                        String[] parts_2 = obj2.getmProductPrize().split("\\."); // escape .
                        String part2 = parts_2[0];
                        int val1 = Integer.parseInt(part1);
                        int val2 = Integer.parseInt(part2);
                        // low to high
                        System.out.println("23032015 LOG COMPARE 1 "
                                + obj1.getmProductPrize() + " "
                                + obj2.getmProductPrize());
                        System.out.println("23032015 LOG COMPARE 1 status "
                                + obj1.getmProductPrize().compareTo(
                                obj2.getmProductPrize()));




                        /*val1.compareTo(
                                val2);*/
                        try {
                            return val1-val2;
                        } catch (Exception ex) {
                            Log.e("LOG 03232015", "" + ex.getMessage());

                        }
                    }
                    case 2: {


                        String[] parts_1 = obj1.getmProductPrize().split("\\."); // escape .
                        String part1 = parts_1[0];

                        String[] parts_2 = obj2.getmProductPrize().split("\\."); // escape .
                        String part2 = parts_2[0];
                        int val1 = Integer.parseInt(part1);
                        int val2 = Integer.parseInt(part2);
                        //
                        // low to high
                        System.out.println("23032015 LOG COMPARE 1 "
                                + obj1.getmProductPrize() + " "
                                + obj2.getmProductPrize());
                        System.out.println("23032015 LOG COMPARE 1 status "
                                + obj1.getmProductPrize().compareTo(
                                obj2.getmProductPrize()));

                      /*  val1.compareTo(
                                val2);*/
                        System.out.println("23032015 compare 2 "
                                + obj2.getmProductPrize().compareTo(
                                obj1.getmProductPrize()));
                        try {
                            return val2-val1;
                        } catch (Exception ex) {
                            Log.e("LOG 03232015", "" + ex.getMessage());
                            return obj2.getmProductPrize().compareTo(
                                    obj1.getmProductPrize());
                        }
                    }
                    case 3: {
					/*return obj1.getProductquantity().compareTo(
							obj2.getProductquantity());*/
                        return obj2.getTimeStampP().compareTo(
                                obj1.getTimeStampP());

                    }
                    case 4: {
                        return obj2.getmProductOff().compareTo(
                                obj1.getmProductOff());

                    }
                    case 5: {					/*int reVal = 0;
					if (obj1.getmProductOrder() == obj2.getmProductOrder()) {
						reVal = 0;
					}
					if (obj1.getmProductOrder() < obj2.getmProductOrder()) {
						reVal = +1;
					}
					if (obj1.getmProductOrder() > obj2.getmProductOrder()) {
						reVal = -1;
					}

					return reVal;*/
                        return obj2.getProductquantity().compareTo(
                                obj1.getProductquantity());

                    }
                    default: {
                        return obj1.getmProductName().compareTo(
                                obj2.getmProductName());
                    }
                }

            }
        };
    }

    Comparator<ProductModel> myComparator;

    public interface ScrollingAdapterinterface {
        void calledInterface(int position);
    }

    protected void getProduct_DetailPage(String product_id) {
        // TODO Auto-generated method stub
        String name[] = {"productId"};
        String value[] = {product_id};
        VKCInternetManager manager = new VKCInternetManager(
                URL_GET_PRODUCTDETAILPAGE);
        manager.getResponsePOST(mContext, name, value, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {

                // parseJSON(successResponse);
                //   Log.v("LOG", "detail page " + successResponse);
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
                // System.out.println("16122014  IN loop" + j);
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

                /*((Activity)mContext).finish();*/
                //mContext.finish();
            }

            Intent intent = new Intent(mContext,
                    ProductDetailActivity.class);
            // intent.putExtra("MODEL", productModels.get(0));


            mContext.startActivity(intent);
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
