/**
 * 
 *//*

package com.mobatia.vkcretailer.manager;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mobatia.vkcretailer.activities.SignUpActivity;
import com.mobatia.vkcretailer.constants.VKCUrlConstants;
import com.mobatia.vkcretailer.manager.VKCInternetManager.ResponseListener;
import com.mobatia.vkcretailer.miscellaneous.VKCUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

*/
/**
 * @author rahul
 * 
 *//*

public class GoogleRegisteration implements VKCUrlConstants {
	Context context;
	private String regId = "";
	private String response;

	// private String SENDER_ID="775452473759";
	//private String SENDER_ID = "1064776722927";
	private String SENDER_ID = "250313383419";
	
	public GoogleRegisteration(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		// registerBackground();
	}

	public void registerBackground() {
		*/
/*
		 * new AsyncTask<Void, Void, String>() {
		 * 
		 * @Override protected String doInBackground(Void... params) {
		 *//*

		String msg = "";
		String id = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regId = gcm.register(SENDER_ID);
			// Log.v("LOG", "FLOW!" + "DEV ID  :  "+regId);
			msg = "23022015 Device registered, registration id=!!" + regId;
			id = "23022015 deviceid::" + VKCUtils.getDeviceID(context);
			System.out.println("msg" + msg);
			System.out.println("id" + id);
			// You should send the registration ID to your server over HTTP, so
			// it
			// can use GCM/HTTP or CCS to send messages to your app.

			// For this demo: we don't need to send it because the device will
			// send
			// upstream messages to a server that echo back the message using
			// the
			// 'from' address in the message.

			// Save the regid - no need to register again.
			// setRegistrationId(activity, regId);
			// count=count+1;
			// regId="";
			if (!regId.equals("")) {
				// call api
				Log.v("LOG", "FLOW!" + "Call UpdateApicall");
				UpdateApicall();

			} else {
				Log.v("LOG", "FLOW!" + "Call Back");
				registerBackground();

			}
			// AppPreferenceManager.saveRegid(regId, context);
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
		}

	}

	private void UpdateApicall() {

		String name[] = { "imei_no", "gcm_id" };
		String values[] = { VKCUtils.getDeviceID(context), regId };
		for (int i = 0; i < name.length; i++) {
			System.out.println("name " + name[i]);
			System.out.println("value " + values[i]);
		}
		final VKCInternetManager manager = new VKCInternetManager(
				GCM_INITIALISATION);

		manager.getResponsePOST(context, name, values, new ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				// TODO Auto-generated method stub
				Log.v("LOG", "20022015 successappinit" + successResponse);
				parseResponse(successResponse);
			}

			@Override
			public void responseFailure(String failureResponse) {
				// TODO Auto-generated method stub
				Log.v("LOG", "20022015 Errror" + failureResponse);
			}
		});
	}

	*/
/**
	 * Method Name:parseResponse Return Type:void parameters:response Date:Feb
	 * 2, 2015 Author:Archana.S
	 * 
	 *//*

	public void parseResponse(String response) {

		// success,failed,email exists
		try {
			System.out.println("inside try");
			if (response != null) {
				if (! response.equalsIgnoreCase("null")) {
					JSONObject jsonObject = new JSONObject(response);
					String responseResult = jsonObject.getString("response");
				}
			}
			// if (responseResult.equals("1")) {
			//
			// VKCUtils.showtoast(mActivity, 6);
			// finish();
			// } else if (responseResult.equals(JSON_LOGINREQ_FAILED)) {
			//
			// VKCUtils.showtoast(mActivity, 7);
			// } else if (responseResult.equals(jSON_LOGINREQ_EMAILEXISTS)) {
			//
			// VKCUtils.showtoast(mActivity, 8);
			// }

		} catch (Exception e) {
			System.out.println("device reg exception " + e.toString());
		}

	}

}
*/
