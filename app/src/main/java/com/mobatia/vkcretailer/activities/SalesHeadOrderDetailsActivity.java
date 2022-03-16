package com.mobatia.vkcretailer.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.SQLiteServices.DatabaseHelper;
import com.mobatia.vkcretailer.adapter.SalesHeadOrderDetailAdapter;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.SubDealerOrderDetailModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SalesHeadOrderDetailsActivity extends AppCompatActivity implements
        VKCUrlConstants, VKCDbConstants {
    private ListView mLstView;
    private TextView mOrdrNumbr;
    private TextView mDate, mTotalQuantity;
    private Bundle extras;
    DatabaseHelper dataBase;
    TextView textTotPrice;
    LinearLayout llAppRej, llDispatch;
    private boolean isError;
    private View mView;
    int status = 1;
    String orderNumber, flag, listType;
    DataBaseManager databaseManager;
    // Button btnApprove, btnReject, btnDispatch;
    CustomToast toast;
    int position;
    int mTotalQty = 0;
    double cost=0.0;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.mobatia.vkcsalesapp.controller.BaseActivity#getLayoutResourceId()
     */
    /*
     * @Override protected int getLayoutResourceId() { // TODO Auto-generated
     * method stub return ; }
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * com.mobatia.vkcsalesapp.controller.BaseActivity#onCreate(android.os.Bundle
     * )
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_head_order_detail);
        AppController.listErrors.clear();
        initUi();

        getSupportActionBar().setLogo(R.drawable.back);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        View viewActionBar = getLayoutInflater().inflate(
                R.layout.actionbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                // Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar
                .findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Orders Details");
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.show();
        //setActionBar();

        extras = getIntent().getExtras();
        if (extras != null) {
            orderNumber = extras.getString("orderNumber");
            /*
             * flag = extras.getString("flag"); listType =
             * extras.getString("listtype"); if
             * (flag.equals("pending")||flag.equals("0")) {
             * llAppRej.setVisibility(View.VISIBLE);
             * llDispatch.setVisibility(View.GONE); AppController.isEditable =
             * true;
             *
             * } else {
             *
             * llAppRej.setVisibility(View.GONE);
             * llDispatch.setVisibility(View.VISIBLE); AppController.isEditable
             * = false; }
             */
            position = extras.getInt("position");
            mOrdrNumbr.setText("Order number : " + orderNumber);

        }
        mTotalQty = 0;
        getSalesOrderDetails(orderNumber);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:
                mTotalQty = 0;
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

    private void initUi() {
        /*
         * dataBase = new DatabaseHelper(this, DBNAME); databaseManager = new
         * DataBaseManager(this);
         */
        llAppRej = (LinearLayout) findViewById(R.id.llAppOrRej);
        llDispatch = (LinearLayout) findViewById(R.id.llDispatch);
        toast = new CustomToast(SalesHeadOrderDetailsActivity.this);
        mLstView = (ListView) findViewById(R.id.salesOrderList);
        mOrdrNumbr = (TextView) findViewById(R.id.txtViewOrder);
        mOrdrNumbr.setVisibility(View.VISIBLE);
        mDate = (TextView) findViewById(R.id.txtViewDate);
        mTotalQuantity = (TextView) findViewById(R.id.totalQty);
        // mDate.setVisibility(View.VISIBLE);
        mView = findViewById(R.id.view);
        mView.setVisibility(View.VISIBLE);
        		textTotPrice=(TextView)findViewById(R.id.textTotPrice);

        /*
         * btnApprove = (Button) findViewById(R.id.btnApprove); btnReject =
         * (Button) findViewById(R.id.btnReject); btnDispatch = (Button)
         * findViewById(R.id.btnDispatch); btnApprove.setOnClickListener(this);
         * btnReject.setOnClickListener(this);
         * btnDispatch.setOnClickListener(this);
         *
         * mLstView.setOnItemClickListener(new OnItemClickListener() {
         *
         * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
         * pos, long arg3) { // TODO Auto-generated method stub Intent intent =
         * new Intent(SubDealerOrderDetails.this,
         * SubDealerOrderDetailsUpdate.class); intent.putExtra("orderPosition",
         * pos); intent.putExtra("orderNumber", orderNumber);
         *
         * startActivity(intent); } });
         */
        getSupportActionBar().setLogo(R.drawable.back);
    }

    @SuppressLint("NewApi")
    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
		/*ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("");
		actionBar.setTitle("");
		actionBar.show();
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(true);*/

    }

    private void getSalesOrderDetails(String ordrNo) {
        AppController.TempSubDealerOrderDetailList.clear();
        AppController.subDealerOrderDetailList.clear();

        String name[] = {"order_id"};
        String values[] = {ordrNo};

        final VKCInternetManager manager = new VKCInternetManager(
                SUBDEALER_ORDER_DETAILS);
       /* for (int i = 0; i < name.length; i++) {
            Log.v("LOG", "12012015 name : " + name[i]);
            Log.v("LOG", "12012015 values : " + values[i]);

        }*/

        manager.getResponsePOST(this, name, values, new ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
                //  Log.v("LOG", "19022015 success" + successResponse);
                parseResponse(successResponse);

            }

            @Override
            public void responseFailure(String failureResponse) {
                VKCUtils.showtoast(SalesHeadOrderDetailsActivity.this, 17);
                isError = true;
            }
        });
    }

    private void parseResponse(String result) {
        try {

            JSONArray arrayOrders = null;
            JSONObject jsonObjectresponse = new JSONObject(result);
            JSONObject response = jsonObjectresponse.getJSONObject("response");
            String status = response.getString("status");
            if (status.equals("Success")) {
                arrayOrders = response.optJSONArray("orderdetails");
                for (int i = 0; i < arrayOrders.length(); i++) {
                    JSONObject obj = arrayOrders.getJSONObject(i);
                    SubDealerOrderDetailModel orderModel = new SubDealerOrderDetailModel();
                    orderModel.setCaseId(obj.getString("size"));
                    orderModel.setColorId(obj.getString("color_id"));
                    orderModel.setCost(obj.getString("cost"));
                    orderModel.setDescription(obj.getString("description"));
                    orderModel.setName(obj.getString("name"));
                    orderModel.setOrderDate(obj.getString("order_date"));
                    orderModel.setProductId(obj.getString("product_id"));
                    orderModel.setQuantity(obj.getString("quantity"));
                    orderModel.setSapId(obj.getString("sap_id"));
                    orderModel.setCaseDetail(obj.getString("size"));
                    orderModel.setDetailid(obj.getString("detailid"));
                    orderModel.setColor_code(obj.getString("color_name"));
                    orderModel.setTotalCost(obj.getString("totalcost"));
                    cost=cost+Double.valueOf(obj.getString("totalcost"));
                    AppController.subDealerOrderDetailList.add(orderModel);
                    AppController.TempSubDealerOrderDetailList.add(orderModel);

                }

                /*
                 * mLstView.setAdapter(new SubDealerOrderDetailsAdapter(
                 * SubDealerOrderDetails.this,
                 * AppController.subDealerOrderDetailList));
                 */
                // getOrderData();

                SalesHeadOrderDetailAdapter mSalesAdapter = new SalesHeadOrderDetailAdapter(
                        SalesHeadOrderDetailsActivity.this);
                mLstView.setAdapter(mSalesAdapter);
                mTotalQty = 0;
                for (int j = 0; j < AppController.TempSubDealerOrderDetailList
                        .size(); j++) {

                    mTotalQty = mTotalQty
                            + Integer
                            .parseInt(AppController.TempSubDealerOrderDetailList
                                    .get(j).getQuantity());
                }

                mTotalQuantity.setText("Total Qty. : "+String.valueOf(mTotalQty));

                textTotPrice.setText("Total Price : "+ String.valueOf(cost));

            }

        } catch (Exception e) {
            isError = true;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        mTotalQty = 0;
    }

}
