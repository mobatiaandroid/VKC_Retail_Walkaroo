/**
 *
 */
package com.mobatia.vkcretailer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.SQLiteServices.SQLiteAdapter;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.HorizontalListView;
import com.mobatia.vkcretailer.fragments.SalesOrderFragment;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;
import com.mobatia.vkcretailer.model.CartModel;
import com.mobatia.vkcretailer.model.ColorModel;

/**
 * @author Bibin Johnson
 *
 */
public class SalesOrderAdapter extends BaseAdapter implements VKCDbConstants {

    Activity mActivity;
    LayoutInflater mInflater;
    ArrayList<CartModel> cartArrayList;
    ArrayList<ColorModel> colorArrayList = new ArrayList<ColorModel>();
    LinearLayout linearLayout;
    private TextView mTxtViewQty;
    DataBaseManager databaseManager;
    SQLiteAdapter mAdapter;
    DisplayManagerScale mDisplayManager;
    int width, height;

    public SalesOrderAdapter(FragmentActivity mActivity,
                             ArrayList<CartModel> cartArrayList, LinearLayout linearLayout,
                             TextView txtViewQty) {
        this.mActivity = mActivity;
        this.cartArrayList = cartArrayList;
        this.linearLayout = linearLayout;
        mTxtViewQty = txtViewQty;
        mInflater = LayoutInflater.from(mActivity);
        mAdapter = new SQLiteAdapter(mActivity, DBNAME);
        mAdapter.openToWrite();
    }

    public SalesOrderAdapter(Context mActivity,
                             ArrayList<CartModel> cartArrayList, LinearLayout lnrTableHeaders,
                             TextView txtQty) {
        this.mActivity = (Activity) mActivity;
        this.cartArrayList = cartArrayList;
        this.linearLayout = lnrTableHeaders;
        mAdapter = new SQLiteAdapter(mActivity, DBNAME);

        mTxtViewQty = txtQty;
        mInflater = LayoutInflater.from(mActivity);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return AppController.cartArrayList.size();
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
        final ViewHolder viewHolder;

        /*View view = convertView;*/
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.custom_salesorder_list, null);
            viewHolder.prodId = (TextView) convertView.findViewById(R.id.txtProdId);
            viewHolder.size = (TextView) convertView.findViewById(R.id.txtSize);
            viewHolder.qty = (EditText) convertView.findViewById(R.id.txtQuantity);
            viewHolder.imgClose = (ImageView) convertView.findViewById(R.id.imgClose);
            viewHolder.txtColor = (TextView) convertView.findViewById(R.id.txtColor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ref = position;
        viewHolder.prodId.setText(AppController.cartArrayList.get(position)
                .getProdName());
        viewHolder.size.setText(AppController.cartArrayList.get(position)
                .getProdSize());
        viewHolder.qty.setText(AppController.cartArrayList.get(position)
                .getProdQuantity());
        //viewHolder.txtColor.setText(AppController.cartArrayList.get(position).getProdColor());
        viewHolder.qty.setId(position);
        GradientDrawable gd = new GradientDrawable();
        // gd.setColor(Color.parseColor(colorModels.get(position).getColorcode()));

        // gd.setColor(VKCUtils.parseColor(AppController.cartArrayList.get(position).getProdColor()));

        gd.setCornerRadius(50);


        // Specify the shape of drawable
        gd.setShape(GradientDrawable.OVAL);

        // Set the fill color of drawable


        // Create a 2 pixels width red colored border for drawable

        // Make the border rounded
        gd.setCornerRadius(15.0f); // border corner radius

        // Finally, apply the GradientDrawable as TextView background

        // gd.setAlpha(100);
        // gd.setStroke(strokeWidth, strokeColor);
        viewHolder.txtColor.setText(AppController.cartArrayList.get(position).getProdColor());
       /* LinearLayout lnrHolder = new ViewHolder().getLinearView(holderView);
        lnrHolder.getLayoutParams().height = (int) (height * 1.8);*/
        /*
         * viewHolder.qty.addTextChangedListener(new TextWatcher() {
         *
         * @Override public void onTextChanged(CharSequence s, int start, int
         * before, int count) { // TODO Auto-generated method stub
         *
         * }
         *
         * @Override public void beforeTextChanged(CharSequence s, int start,
         * int count, int after) { // TODO Auto-generated method stub
         *
         * }
         *
         * @Override public void afterTextChanged(Editable s) { // TODO
         * Auto-generated method stub System.out.println("position" + position);
         * AppController.cartArrayList.get(position).setProdQuantity(
         * s.toString()); SQLiteAdapter mAdapter = new SQLiteAdapter(mActivity,
         * DBNAME);
         * mAdapter.updateData(AppController.cartArrayList.get(position)
         * .getProdName(), AppController.cartArrayList.get(position)
         * .getProdColor(), AppController.cartArrayList
         * .get(position).getProdSize(), s.toString());
         *
         *
         *
         * databaseManager = new DataBaseManager(mActivity); String[][]
         * constrain = new String[][] { { PRODUCT_ID,
         * AppController.cartArrayList.get(position).getProdId()},
         *
         *
         * { PRODUCT_SIZE,
         * AppController.cartArrayList.get(position).getProdSize() },
         *
         * { PRODUCT_COLOR, AppController.cartArrayList.get(position)
         * .getProdColor() }, }; String[][] values={{ PRODUCT_QUANTITY,
         * s.toString() },};
         *
         * databaseManager.updateTableRow(DBNAME, values, constrain);
         *
         *
         * } });
         */

        if (viewHolder.qtyWatcher != null) {
            viewHolder.qty.removeTextChangedListener(viewHolder.qtyWatcher);
        }
        viewHolder.qty.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    if (AppController.cartArrayList.size() > 0) {
                        AppController.cartArrayList.get(position).setProdQuantity(Caption.getText().toString());
                    }
                }
            }
        });
        viewHolder.qtyWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
					/*AppController.cartArrayList.get(position).setProdQuantity(
							viewHolder.qty.getText().toString());*/
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Update the quantity
            }
        };
        viewHolder.qty.addTextChangedListener(viewHolder.qtyWatcher);
        /*HorizontalListView relColor = (HorizontalListView) convertView
                .findViewById(R.id.listViewColor);

        relColor.setAdapter(new ColorGridAdapter(mActivity, AppController.cartArrayList.get(
                position).getProdColor(), 0));*/

        RelativeLayout rel1 = (RelativeLayout) convertView.findViewById(R.id.rel1);
        RelativeLayout rel2 = (RelativeLayout) convertView.findViewById(R.id.rel2);
        RelativeLayout rel3 = (RelativeLayout) convertView.findViewById(R.id.rel3);
        RelativeLayout rel4 = (RelativeLayout) convertView.findViewById(R.id.rel4);
        RelativeLayout rel5 = (RelativeLayout) convertView.findViewById(R.id.rel5);

        if (position % 2 == 0) {
            rel1.setBackgroundColor(Color.rgb(219, 188, 188));
            rel2.setBackgroundColor(Color.rgb(219, 188, 188));
            rel3.setBackgroundColor(Color.rgb(219, 188, 188));
            rel4.setBackgroundColor(Color.rgb(219, 188, 188));
            rel5.setBackgroundColor(Color.rgb(219, 188, 188));
        } else {
            rel1.setBackgroundColor(Color.rgb(208, 208, 208));
            rel2.setBackgroundColor(Color.rgb(208, 208, 208));
            rel3.setBackgroundColor(Color.rgb(208, 208, 208));
            rel4.setBackgroundColor(Color.rgb(208, 208, 208));
            rel5.setBackgroundColor(Color.rgb(208, 208, 208));
        }

        viewHolder.imgClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDeleteDialog(position);

            }
        });
/*
		for (int i = 0; i < cartArrayList.size(); i++) {

			System.out.println("18022015:Values:getProdId:"
					+ cartArrayList.get(position).getProdId());
			System.out.println("18022015:Values:getProdName:"
					+ cartArrayList.get(position).getProdName());
			System.out.println("18022015:Values:ProdSizeId:"
					+ cartArrayList.get(position).getProdSizeId());
			System.out.println("18022015:Values:getProdSize:"
					+ cartArrayList.get(position).getProdSize());
			System.out.println("18022015:Values:ProdColorId:"
					+ cartArrayList.get(position).getProdColorId());
			System.out.println("18022015:Values:getProdColor:"
					+ cartArrayList.get(position).getProdColor());
			System.out.println("18022015:Values:getProdQuantity:"
					+ cartArrayList.get(position).getProdQuantity());
			System.out.println("18022015:Values:GridValue:"
					+ cartArrayList.get(position).getProdGridValue());
		}*/
        return convertView;
    }

    private void showDeleteDialog(int position) {
        DeleteAlert appDeleteDialog = new DeleteAlert(mActivity, position);

        appDeleteDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        appDeleteDialog.setCancelable(true);
        appDeleteDialog.show();

    }

    static class ViewHolder {
        TextView prodId;
        TextView size;
        EditText qty;
        TextView txtColor;
        ImageView imgClose;
        int ref;
        TextWatcher qtyWatcher;

        public RelativeLayout getColorView(View holderView) {
            return (RelativeLayout) holderView.findViewById(R.id.rel3);
        }

        public LinearLayout getLinearView(View holderView) {
            return (LinearLayout) holderView.findViewById(R.id.lnrHolder);
        }
    }

    public class DeleteAlert extends Dialog implements
            android.view.View.OnClickListener, VKCDbConstants {

        public Activity mActivity;
        public Dialog d;
        CheckBox mCheckBoxDis;
        ImageView mImageView;
        // public Button yes, no;

        Button bUploadImage;
        String TEXTTYPE;

        ProgressBar mProgressBar;
        DataBaseManager databaseManager;
        int position;
        ArrayList<CartModel> cartList;

        public DeleteAlert(Activity a, int position) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.position = position;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_delete_alert);
            init();

        }

        private void init() {
            DisplayManagerScale disp = new DisplayManagerScale(mActivity);
            int displayH = disp.getDeviceHeight();
            int displayW = disp.getDeviceWidth();

            RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);

            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);

            Button buttonSet = (Button) findViewById(R.id.buttonOk);
            buttonSet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    databaseManager = new DataBaseManager(mActivity);
                    databaseManager
                            .removeFromDb(TABLE_SHOPPINGCART, PRODUCT_ID,
                                    cartArrayList.get(position).getProdId());
                    cartArrayList.remove(position);

                    notifyDataSetChanged();
                    int totQty = 0;
                    for (int i = 0; i < cartArrayList.size(); i++) {
                        totQty = totQty
                                + Integer.parseInt(cartArrayList.get(i)
                                .getProdQuantity());
                    }
                    if (cartArrayList.size() > 0) {
                        mTxtViewQty.setText("Total quantity :  " + "" + totQty);
                    }

                    if (cartArrayList.size() == 0) {
                        mTxtViewQty.setText("Total quantity :  " + "" + 0);
                        VKCUtils.showtoast(mActivity, 9);
                        // linearLayout.setVisibility(View.GONE);
                        SalesOrderFragment.isCart = false;
                        AppController.cartArrayList.clear();
                    }
                    dismiss();

                }

            });
            Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public void getDisplayScale() {
        mDisplayManager = new DisplayManagerScale(mActivity);
        width = mDisplayManager.getDeviceWidth();
        height = mDisplayManager.getDeviceHeight();
    }
}
