/**
 *
 */
package com.mobatia.vkcretailer.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.DashboardFActivity;
import com.mobatia.vkcretailer.activities.VKCSplashActivity;
import com.mobatia.vkcretailer.adapter.FilterAdapter;
import com.mobatia.vkcretailer.adapter.FilterBrandContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterCategoryContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterCategoryMainContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterColorContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterOfferAdapter;
import com.mobatia.vkcretailer.adapter.FilterPriceContentAdapter;
import com.mobatia.vkcretailer.adapter.FilterSizeContentAdapter;
import com.mobatia.vkcretailer.adapter.ProductListAdapterNew;
import com.mobatia.vkcretailer.appdialogs.SortDialog;
import com.mobatia.vkcretailer.constants.VKCJsonTagConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.manager.SearchHeaderManager;
import com.mobatia.vkcretailer.manager.SearchHeaderManager.SearchActionInterface;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.BrandTypeModel;
import com.mobatia.vkcretailer.model.CategoryModel;
import com.mobatia.vkcretailer.model.ColorModel;
import com.mobatia.vkcretailer.model.OfferModel;
import com.mobatia.vkcretailer.model.PriceModel;
import com.mobatia.vkcretailer.model.ProductModel;
import com.mobatia.vkcretailer.model.SizeModel;
import com.mobatia.vkcretailer.model.SortCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

/**
 * @author archana.s
 *
 */
@SuppressLint("NewApi")
public class ProductListFragmentNew extends Fragment implements VKCUrlConstants,
        VKCJsonTagConstants {
    private int mContainerId;

    private ListView listView;
    private GridView gridProductList;
    ArrayList<ProductModel> productModels;
    private Activity mActivity;
    FragmentTransaction mFragmentTransaction;
    private boolean exitFlag = true;
    String total_records = "0", tolal_pages = "0", current_page = "0";

    private RelativeLayout mRelFilter;

    private RelativeLayout mRelSortBy;
    private RelativeLayout mRelList;
    private View view;
    private View viewFilter;
    private View viewSortBy;
    private View viewList;
    private RelativeLayout relShare;
    private ImageView imgSearch;
    private EditText edtSearch;
    Boolean flag = false;
    private TextView tvList;
    private ImageView imgList;

    private int count = 0;
    String cat_id = "", parent_cat_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppController.isFilterSet = false;
        view = inflater.inflate(R.layout.fragment_productlist, null);
        mActivity = getActivity();
        initialiseUI();
        productModels = new ArrayList<ProductModel>();
        AppPrefenceManager.saveListType(getActivity(), "ProductList");
        // Log.v("LOG", "22122014 onCreateView in ProductListFragment ");
        getProducts(1, "");
        return view;
    }

    private void initialiseUI() {
        listView = (ListView) view.findViewById(R.id.list);
        gridProductList = (GridView) view.findViewById(R.id.gridProducts);
        mRelFilter = (RelativeLayout) view.findViewById(R.id.relFilter);
        mRelSortBy = (RelativeLayout) view.findViewById(R.id.relSortBy);
        mRelList = (RelativeLayout) view.findViewById(R.id.relList);
        viewFilter = (View) view.findViewById(R.id.viewFilter);
        viewSortBy = (View) view.findViewById(R.id.viewSortBy);
        viewList = (View) view.findViewById(R.id.viewList);
        tvList = (TextView) view.findViewById(R.id.tvList);
        imgList = (ImageView) view.findViewById(R.id.imgList);
        relShare = (RelativeLayout) view.findViewById(R.id.relShare);
        RelativeLayout relSearchHeader = (RelativeLayout) view
                .findViewById(R.id.relSearchHeader);
        relSearchHeader.setVisibility(View.GONE);
        SearchHeaderManager manager = new SearchHeaderManager(getActivity());
        manager.getSearchHeader(relSearchHeader);
        imgSearch = manager.getSearchImage();
        edtSearch = manager.getEditText();
        AppController.isCart = false;

        manager.searchAction(getActivity(), new SearchActionInterface() {

            @Override
            public void searchOnTextChange(String key) {
                // TODO Auto-generated method stub
                if (!edtSearch.getText().toString().equals("")) {

					/*ProductListFragment.setFilter(edtSearch.getText()
							.toString());*/
                    getProducts(1, edtSearch.getText().toString());
                    VKCUtils.hideKeyBoard(mActivity);
                }

            }
        }, edtSearch.getText().toString());

        relShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                shareIntent("http://vkcgroup.com/");

            }
        });
        mRelFilter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                viewFilter.setVisibility(View.VISIBLE);
                viewSortBy.setVisibility(View.GONE);
                viewList.setVisibility(View.GONE);
                showFilterDialog("Filter");
                AppPrefenceManager.saveFilterDataCategory(mActivity, "");
                AppPrefenceManager.saveFilterDataSize(mActivity, "");
                AppPrefenceManager.saveFilterDataBrand(mActivity, "");
                AppPrefenceManager.saveFilterDataColor(mActivity, "");
                AppPrefenceManager.saveFilterDataPrice(mActivity, "");
                /*Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);*/

            }
        });

        mRelList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewFilter.setVisibility(View.GONE);
                viewSortBy.setVisibility(View.GONE);
                viewList.setVisibility(View.VISIBLE);
                if (tvList.getText().equals("LIST")) {
                    flag = true;
                } else {
                    flag = false;
                }
                setList();

            }
        });

        mRelSortBy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                viewFilter.setVisibility(View.GONE);
                viewSortBy.setVisibility(View.VISIBLE);
                viewList.setVisibility(View.GONE);
                showDialog("Sort By");

            }
        });

    }

    public static void setFilter(String key) {
        // Toast.makeText(DashboardFActivity.dashboardFActivity, key,
        // 1000).show();
        listAdapter.filter(key);
    }

    private void shareIntent(String link) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(emailIntent, getActivity()
                .getResources().getString(R.string.app_name)));
    }

    public void getProducts(int pageNumber, String key) // int pageNumber
    {


        String dataCategory = "";
        String dataColor = "";
        String dataSize = "";
        String dataType = "";
        String dataOffer = "";
        String dataPrice = "";

        if (AppPrefenceManager.getListingOption(getActivity()).equals("0")) {
            dataCategory = DashboardFActivity.categoryId;
            if (dataCategory.equals("1") || (dataCategory.equals("2")) ||
                    (dataCategory.equals("3")) || (dataCategory.equals("4")) || (dataCategory.equals("5")) ||
                    (dataCategory.equals("31")) || (dataCategory.equals("33")) || dataCategory.equals("67")) {
				/*AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				AppPrefenceManager.setCatId(mActivity, "");*/
                parent_cat_id = dataCategory;
                cat_id = "";
				/*AppPrefenceManager.setParentCatId(mActivity, pa);
				AppPrefenceManager.setCatId(mActivity, "");*/
            } else {
				/*AppPrefenceManager.setParentCatId(mActivity, "");
				AppPrefenceManager.setCatId(mActivity, dataCategory);*/
                parent_cat_id = "";
                cat_id = dataCategory;
            }
			/*parent_cat_id=DashboardFActivity.categoryId;
			System.out.println("Id---"+8989+parent_cat_id);*/
        } else if (AppPrefenceManager.getListingOption(getActivity()).equals(
                "1")) {
            dataCategory = AppPrefenceManager.getIDsForOffer(getActivity());
			/*if(dataCategory.equals("1")||(dataCategory.equals("2"))||
					(dataCategory.equals("3"))||(dataCategory.equals("4"))||(dataCategory.equals("5"))||
					(dataCategory.equals("31"))||(dataCategory.equals("33"))||dataCategory.equals("67")){
				AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				AppPrefenceManager.setCatId(mActivity, "");
				parent_cat_id=dataCategory;
				cat_id="";
			}else{
				AppPrefenceManager.setParentCatId(mActivity, "");
				AppPrefenceManager.setCatId(mActivity, dataCategory);
				parent_cat_id="";
				cat_id=dataCategory;
			}*/
            parent_cat_id = dataCategory;
            cat_id = "";
        } else if (AppPrefenceManager.getListingOption(getActivity()).equals(
                "2")) {
            dataCategory = AppPrefenceManager
                    .getFilterDataCategory(getActivity());
            parent_cat_id = AppPrefenceManager.getParentCatId(getActivity());
            cat_id = AppPrefenceManager.getCatId(getActivity());


        } else if (AppPrefenceManager.getListingOption(getActivity()).equals(
                "4")) {
            dataCategory = AppPrefenceManager.getIDsForOffer(getActivity());
            parent_cat_id = "";
            cat_id = "";
			/*if(dataCategory.equals("1")||(dataCategory.equals("2"))||
					(dataCategory.equals("3"))||(dataCategory.equals("4"))||(dataCategory.equals("5"))||
					(dataCategory.equals("31"))||(dataCategory.equals("33"))||dataCategory.equals("67")){
				AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				AppPrefenceManager.setCatId(mActivity, "");
				parent_cat_id=dataCategory;
				cat_id="";
			}else{
				parent_cat_id=dataCategory;

			}
			// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
			// .show();
*/
        } else if (AppPrefenceManager.getListingOption(getActivity()).equals(
                "5")) {
            dataCategory = AppPrefenceManager.getSubCategoryId(getActivity());
            if (dataCategory.equals("1") || (dataCategory.equals("2")) ||
                    (dataCategory.equals("3")) || (dataCategory.equals("4")) || (dataCategory.equals("5")) ||
                    (dataCategory.equals("31")) || (dataCategory.equals("33")) || dataCategory.equals("67")) {
				/*AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				AppPrefenceManager.setCatId(mActivity, "");*/
                parent_cat_id = dataCategory;
                cat_id = "";
            } else {
				/*AppPrefenceManager.setParentCatId(mActivity, "");
				AppPrefenceManager.setCatId(mActivity, dataCategory);*/
                parent_cat_id = "";
                cat_id = dataCategory;
            }
            // Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
            // .show();
        }

        if (!AppPrefenceManager.getFilterDataSize(getActivity()).equals("")) {
            dataSize = AppPrefenceManager.getFilterDataSize(getActivity());
        } else {
            dataSize = "";
        }
        if (!AppPrefenceManager.getFilterDataColor(getActivity()).equals("")) {
            dataColor = AppPrefenceManager.getFilterDataColor(getActivity());
        } else {
            dataColor = "";
        }

        if (!AppPrefenceManager.getFilterDataPrice(getActivity()).equals("")) {
            dataPrice = AppPrefenceManager.getFilterDataPrice(getActivity());
        } else {
            dataPrice = "";
        }


        if (AppPrefenceManager.getListingOption(getActivity()).equals("4")) {
            dataType = "";

            //AppPrefenceManager.getBrandIdForSearch(getActivity());
            // Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
            // .show();

        } else {
            if (!AppPrefenceManager.getFilterDataBrand(getActivity())
                    .equals("")) {

                dataType = "";
                // dataType = AppPrefenceManager.getFilterDataBrand(getActivity()); //Bibin
            } else {
                dataType = "";
            }

            // Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
            // .show();
        }
        //  System.out.println("Datatype-" + dataType);

        if (!AppPrefenceManager.getFilterDataOffer(getActivity()).equals("")) {
            dataOffer = AppPrefenceManager.getFilterDataOffer(getActivity());
        } else {
            dataOffer = "";
        }

        if (!AppPrefenceManager.getOfferIDs(getActivity()).equals("")) {
            dataOffer = AppPrefenceManager.getOfferIDs(getActivity());
            parent_cat_id = "";
            cat_id = "";
        } else {
            dataOffer = "";
        }

        String brand;
        //  System.out.println("Cate Id---" + DashboardFActivity.categoryId);
        if (AppPrefenceManager.getListingOption(getActivity()).equals(
                "4")) {
            brand = AppPrefenceManager.getBrandIdForSearch(mActivity);

        } else {

            if (AppPrefenceManager.getFilterDataBrand(mActivity).equals("")) {
                brand = AppPrefenceManager.getBrandIdForSearch(mActivity);
            } else {
                brand = AppPrefenceManager.getFilterDataBrand(mActivity);

            }
        }

        String name[] = {"parent_category_id", "category_id", "color_id",
                "size_id", "type_id", "content", "offer_id", "currentpage", "brand_id", "price_range"};
        String values[] = {parent_cat_id,
                cat_id, dataColor, dataSize,
                dataType, key, dataOffer, String.valueOf(pageNumber), brand,dataPrice};// ,
        // String.valueOf(pageNumber)
		/*for(int i=0;i<name.length;i++){
			System.out.println("values---"+name[i]+"-"+values[i]);
		}getBrandIdForSearch
*/
        final VKCInternetManager manager = new VKCInternetManager(
                PRODUCT_DETAIL_NEW_URL);

        manager.getResponsePOST(getActivity(), name, values,
                new ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "12012015 success" + successResponse);
                        parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "08012015 Errror" + failureResponse);
                    }
                });
    }

    private void parseResponse(String response) {

        try {
            JSONObject jsonObjectresponse = new JSONObject(response);
            total_records = jsonObjectresponse.getString("total_records");
            tolal_pages = jsonObjectresponse.getString("tolal_pages");
            current_page = jsonObjectresponse.getString("current_page");
            // Log.d("TAG", "Response of Get Products---" + response);
            JSONArray jsonArrayresponse = jsonObjectresponse
                    .getJSONArray(JSON_TAG_SETTINGS_RESPONSE);
            // /productModels=new ArrayList<ProductNewModel>();
            // productModels.clear();
            ArrayList<ProductModel> tempProductArrayList = new ArrayList<ProductModel>();
            if (jsonArrayresponse.length() > 0) {
                for (int j = 0; j < jsonArrayresponse.length(); j++) {
                    JSONObject obj = jsonArrayresponse.getJSONObject(j);
                    ProductModel model = new ProductModel();
                    model.setmProductName(obj.getString("name"));
                    model.setId(obj.getString("id"));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date date = format.parse(obj.getString("timestamp"));
                        //   System.out.println("converted date=-" + date);
                        model.setTimeStampP(date);

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    model.setProductDescription(obj.getString("description"));
                    model.setParent_category(obj.getString("parent_category"));
                    model.setCategoryId(obj.getString("category_id"));
                    model.setType(obj.getString("type"));
                    //   System.out.println("setType---" + model.getType());

                    model.setSize(obj.getString("size"));
                    model.setProductViews(obj.getString("views"));
                    model.setmProductPrize(obj.getString("cost"));
                    model.setmProductOff(obj.getString("offer"));
                    model.setImage_name(obj.getString("image_name"));
                    model.setProductquantity(obj.getString("orderqty"));
                    productModels.add(model);
                }
            } else {
                VKCUtils.showtoast(mActivity, 17);
                productModels.clear();
            }

            // productModels.addAll(tempProductArrayList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        setList();

    }

    public void setList() {
        if (productModels.size() == 0) {
            // CustomToast toast = new CustomToast(mActivity);
            // toast.show(17);
            VKCUtils.showtoast(mActivity, 17);
        }
        if (flag == true) {

            gridProductList.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            tvList.setText("GRID");
            imgList.setImageResource(R.drawable.grid);
            final int currentPage = Integer.parseInt(current_page);
            final int totalPage = Integer.parseInt(tolal_pages);
            listAdapter = new ProductListAdapterNew(getActivity(),
                    productModels, 1, currentPage, totalPage,
                    new ProductListAdapterNew.ScrollingAdapterinterface() {

                        @Override
                        public void calledInterface(int position) {
                            // TODO Auto-generated method stub
                            if (currentPage == totalPage) {
                                AppPrefenceManager.saveFilterDataCategory(mActivity, "");
                                AppPrefenceManager.saveFilterDataSize(mActivity, "");
                                AppPrefenceManager.saveFilterDataBrand(mActivity, "");
                                AppPrefenceManager.saveFilterDataColor(mActivity, "");
                                AppPrefenceManager.saveFilterDataPrice(mActivity, "");
                            } else {
                                getProducts(position + 1, "");
                            }
                            // pos=2*position-1;

                        }
                    });
            listAdapter.notifyDataSetChanged();

            listView.setAdapter(listAdapter);
            if (currentPage == 1) {
                listView.setSelection(0);

            } else {
                listView.setSelection((currentPage - 1) * 20);
            }

        } else {

            gridProductList.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            tvList.setText("LIST");
            imgList.setImageResource(R.drawable.list);
            //  System.out.println("23122014:Flag::" + flag);
			/*listAdapter = new ProductListAdapterNew(getActivity(), productModels,
					2);

			gridProductList.setAdapter(listAdapter);
			gridProductList.setSelection(AppController.selectedProductPosition);*/
            final int currentPage = Integer.parseInt(current_page);
            final int totalPage = Integer.parseInt(tolal_pages);
            listAdapter = new ProductListAdapterNew(getActivity(),
                    productModels, 2, currentPage, totalPage,
                    new ProductListAdapterNew.ScrollingAdapterinterface() {

                        @Override
                        public void calledInterface(int position) {
                            // TODO Auto-generated method stub
                            if (currentPage == totalPage) {

                            } else {
                                getProducts(position + 1, "");
                            }
                            // pos=2*position-1;

                        }
                    });
            listAdapter.notifyDataSetChanged();

            gridProductList.setAdapter(listAdapter);
            if (currentPage == 1) {
                gridProductList.setSelection(0);

            } else {
                gridProductList.setSelection((currentPage - 1) * 20);
            }

        }
        if (listAdapter != null) {
            String option = AppPrefenceManager
                    .getProductListSortOption(mActivity);
            if (option.equals("0")) {
                listAdapter.doSort(0);
            } else if (option.equals("1")) {
                listAdapter.doSort(1);
            } else if (option.equals("2")) {
                listAdapter.doSort(2);
            } else if (option.equals("3")) {
                listAdapter.doSort(3);
            } else if (option.equals("4")) {
                listAdapter.doSort(4);
            } else if (option.equals("5")) {
                listAdapter.doSort(5);
            }
        }

    }

    static ProductListAdapterNew listAdapter;
    ProductModel model = null;
    SortDialog sortDialog;

    private void showDialog(String str) {
        sortDialog = new SortDialog(getActivity(), str,
                new SortDialog.SortOptionSelectionListener() {

                    @Override
                    public void selectedOption(String option) {
                        // TODO Auto-generated method stub

                        if (option.equals("Popularity")) {
                            listAdapter.doSort(0);
                            AppPrefenceManager.saveProductListSortOption(
                                    mActivity, "0");

                        } else if (option.equals("Price(Low to High)")) {
                            listAdapter.doSort(1);
                            AppPrefenceManager.saveProductListSortOption(
                                    mActivity, "1");
                        } else if (option.equals("Price(High to Low)")) {
                            listAdapter.doSort(2);
                            AppPrefenceManager.saveProductListSortOption(
                                    mActivity, "2");
                        } else if (option.equals("New Arrivals")) {
                            listAdapter.doSort(3);
                            AppPrefenceManager.saveProductListSortOption(
                                    mActivity, "3");
                        } else if (option.equals("Discount")) {
                            listAdapter.doSort(4);
                            AppPrefenceManager.saveProductListSortOption(
                                    mActivity, "4");
                        } else if (option.equals("Most Order")) {
                            listAdapter.doSort(5);
                            AppPrefenceManager.saveProductListSortOption(
                                    mActivity, "5");
                        }

                    }
                });
        sortDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        sortDialog.setCancelable(true);
        sortDialog.show();

    }

    /*@Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        edtSearch.setText("");
        if (VKCUtils.checkInternetConnection(getActivity())) {
            getProducts();
        } else {
            // CustomToast toast = new CustomToast(getActivity());
            // toast.show(0);
            VKCUtils.showtoast(mActivity, 0);
        }
    }*/
    @Override
    public void onResume() {
        // TODO Auto-generated method stub

       /* if(AppController.isFilterSet)
        {
            productModels.clear();

            getProducts(1, "");
        }
        AppController.isFilterSet=false;*/
        super.onResume();
        //edtSearch.setText("");
        /*productModels.clear();

        if (VKCUtils.checkInternetConnection(getActivity())) {
            // getProducts(mPageNumber);
            getProducts(1, "");
            edtSearch.setText("");
        } else {
            CustomToast toast = new CustomToast(getActivity()); // toast.show(0);
            VKCUtils.showtoast(mActivity, 0);
        }*/

    }


    FilterDialog filterDialog;

    private void showFilterDialog(String str) {
        filterDialog = new FilterDialog(mActivity, new FilterDialog.FilterOptionSelectionListener() {
            @Override
            public void selectedOption(String option) {
                if (option.equals("1")) {
                    productModels=new ArrayList<>();

                    getProducts(1, "");

                } else {
                    productModels.clear();
                    listAdapter.notifyDataSetChanged();

                    getProducts(1, "");

                }
            }
        });


        filterDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        filterDialog.setCancelable(true);
        filterDialog.show();

    }

}

class FilterDialog extends Dialog implements
        android.view.View.OnClickListener {

    private ListView mListFilter;
    private ListView mListFilterContent;
    private String[] mTextString = {"Brand","Category", "Size",
              "Color"};

    /*private String[] mTextString = {"Category", "SubCategory", "Size",
            "Brand", "Price", "Color", "Offers"};*/
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
    FilterOptionSelectionListener filterOptionSelectionListener;


    public FilterDialog(@NonNull Activity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }


    public FilterDialog(@NonNull Activity mActivity, FilterOptionSelectionListener filterOptionSelectionListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.filterOptionSelectionListener = filterOptionSelectionListener;
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
        setBrand();
        init();

    }

    private void init() {
        DisplayManagerScale disp = new DisplayManagerScale(mActivity);
        int displayH = disp.getDeviceHeight();
        int displayW = disp.getDeviceWidth();
    }


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

           /* JSONArray offerObjArray = new JSONArray(
                    AppPrefenceManager
                            .getJsonOfferResponse(mActivity));
            for (int i = 0; i < offerObjArray.length(); i++) {
                JSONObject responseObj = offerObjArray.getJSONObject(i);

                offerModels.add(getOffersObjectValues(responseObj));

            }*/

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
                AppPrefenceManager.saveListingOption(mActivity, "2");

                getSelectedOptions();

                AppController.isFilterSet = true;
                dismiss();
                //1 Denotes filter applied
                filterOptionSelectionListener.selectedOption("1");
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
                AppPrefenceManager.saveListingOption(mActivity, "4");

                AppPrefenceManager.saveFilterDataCategory(mActivity, "");
                AppPrefenceManager.saveFilterDataSize(mActivity, "");
                AppPrefenceManager.saveFilterDataBrand(mActivity, "");
                AppPrefenceManager.saveFilterDataColor(mActivity, "");
                AppPrefenceManager.saveFilterDataPrice(mActivity, "");
                //AppPrefenceManager.saveFilterDataOffer(mActivity, "");

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
                filterOptionSelectionListener.selectedOption("0");


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
                    setBrand();

                    // setSubCategory();

                } /*else if (position == 1) {

                    setSubCategory();

                } */ else if (position == 1) {
                    setCategory();


                } else if (position == 2) {
                    setSize();

                } else if (position == 3) {
                    //setPrice();

                    setColor();


                } else if (position == 4) {
                   // setColor();

                } /*else if (position == 6) {

                    setOffers();

                }*/

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
	/*public void getSelectedOptions() {
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
*/

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
                // System.out.println("Response---1" + responseCategory.substring(0, responseCategory.length() - 1));
            }
            AppPrefenceManager.saveFilterDataCategory(mActivity, responseCategory);
            // System.out.println("Response---1" + AppPrefenceManager.getFilterDataCategory(mActivity));

        }

      /*  if (AppController.tempsubCatArrayList.size() > 0) {
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

        }*/

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
      /*  if (AppController.tempofferCatArrayList.size() > 0) {
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
        }*/

        if (AppController.tempbrandCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempbrandCatArrayList.size(); i++) {
                responseBrand = responseBrand
                        + AppController.tempbrandCatArrayList.get(i) + ",";
            }
            if (responseBrand.endsWith(",")) {

                responseBrand = responseBrand.substring(0, responseBrand.length() - 1);
                //  System.out.println("Response---1" + responseBrand.substring(0, responseBrand.length() - 1));
            }
            AppPrefenceManager.saveFilterDataBrand(mActivity,
                    responseBrand);
        }

        if (AppController.temppriceCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.temppriceCatArrayList.size(); i++) {
                responsePrice = responsePrice
                        + AppController.temppriceCatArrayList.get(i) + ",";
            }
            if (responsePrice.endsWith(",")) {

                responsePrice = responsePrice.substring(0, responsePrice.length() - 1);
                //System.out.println("Response---1" + responsePrice.substring(0, responsePrice.length() - 1));
            }
            AppPrefenceManager.saveFilterDataPrice(mActivity,
                    responsePrice);
        }
        if (AppController.tempcolorCatArrayList.size() > 0) {
            for (int i = 0; i < AppController.tempcolorCatArrayList.size(); i++) {
                responseColor = responseColor
                        + AppController.tempcolorCatArrayList.get(i) + ",";
            }
            if (responseColor.endsWith(",")) {

                responseColor = responseColor.substring(0, responseColor.length() - 1);
                // System.out.println("Response---1" + responseColor.substring(0, responseColor.length() - 1));
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



        /*
         * int length = 0;
         *
         * length = response.length(); if (length != 0) { String newString =
         * response.substring(0, length - 1);
         * System.out.println("10122014:"+newString);
         *
         * return newString; } else { return
         * "Select any filter option and apply"; }
         */

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
                /*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 */mainCategoryArrayList, tempArrayListMainCategory);
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
                /*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 */filterArrayList, tempArrayListCategory);
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

    public static interface FilterOptionSelectionListener {
        public void selectedOption(String option);
    }
}