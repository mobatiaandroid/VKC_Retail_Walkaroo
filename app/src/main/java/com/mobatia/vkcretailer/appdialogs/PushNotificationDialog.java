package com.mobatia.vkcretailer.appdialogs;

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
import android.widget.TextView;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.VKCSplashActivity;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;

public class PushNotificationDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity mActivity;
	public Dialog d;
	CheckBox mCheckBoxDis;
	ImageView mImageView;
	// public Button yes, no;

	Button bUploadImage;

	ProgressBar mProgressBar;
	private TextView textView1;
	private DialogListener dialogLstnr;

	public PushNotificationDialog(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.mActivity = a;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate dialog");

	}

	public void init(String message, DialogListener dL) {
		this.dialogLstnr = dL;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_push_notification);
		DisplayManagerScale disp = new DisplayManagerScale(mActivity);
		int displayH = disp.getDeviceHeight();
		int displayW = disp.getDeviceWidth();
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(message);
		RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);

		// relativeDate.getLayoutParams().height = (int) (displayH * .65);
		// relativeDate.getLayoutParams().width = (int) (displayW * .90);

		Button buttonSet = (Button) findViewById(R.id.buttonOk);
		buttonSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				dialogLstnr.dismissDialog();
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

	public interface DialogListener {
		public void dismissDialog();

	}
}
