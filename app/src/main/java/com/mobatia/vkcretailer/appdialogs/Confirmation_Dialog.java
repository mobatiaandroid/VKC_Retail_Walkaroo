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

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.manager.DisplayManagerScale;

public class Confirmation_Dialog extends Dialog implements
android.view.View.OnClickListener {

public Activity mActivity;
public Dialog d;
CheckBox mCheckBoxDis;
ImageView mImageView;
// public Button yes, no;

Button bUploadImage;
String TEXTTYPE;

ProgressBar mProgressBar;

public Confirmation_Dialog(Activity a, String TEXTTYPE) {
super(a);
// TODO Auto-generated constructor stub
this.mActivity = a;

this.TEXTTYPE = TEXTTYPE;

}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.confirm_dialog);
init();

}

private void init() {
DisplayManagerScale disp = new DisplayManagerScale(mActivity);
int displayH = disp.getDeviceHeight();
int displayW = disp.getDeviceWidth();

RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBaseConfirm);

//relativeDate.getLayoutParams().height = (int) (displayH * .65);
//relativeDate.getLayoutParams().width = (int) (displayW * .90);

Button buttonSet = (Button) findViewById(R.id.buttonYes);
buttonSet.setOnClickListener(new View.OnClickListener() {

	@Override
	public void onClick(View v) {

		dismiss();
		mActivity.finish();

	}
	// alrtDbldr.cancel();

});
Button buttonCancel = (Button) findViewById(R.id.buttonNo);
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
