/**
 * 
 */
package com.mobatia.vkcretailer.appdialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.constants.VKCDbConstants;
import com.mobatia.vkcretailer.manager.DataBaseManager;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.model.CartModel;

/**
 * @author mobatia-user
 *
 */
public class DeleteAlert extends Dialog implements android.view.View.OnClickListener,VKCDbConstants{
	
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

	public DeleteAlert(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.mActivity = a;
        this.cartList=cartList;
		this.position=position;

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
		databaseManager=new DataBaseManager(mActivity);

		//relativeDate.getLayoutParams().height = (int) (displayH * .65);
		//relativeDate.getLayoutParams().width = (int) (displayW * .90);

		Button buttonSet = (Button) findViewById(R.id.buttonOk);
		buttonSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
//				databaseManager = new DatabaseManager(context);
				databaseManager.removeFromDb(TABLE_SHOPPINGCART,
						PRODUCT_ID, cartList
								.get(position).getProdId());
//				cartList.remove(position);
				
//				mActivity.finish();

			}
			// alrtDbldr.cancel();

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
