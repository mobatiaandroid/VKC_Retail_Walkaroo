/**
 * 
 *//*

package com.mobatia.vkcretailer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.activities.VKCSplashActivity;
import com.mobatia.vkcretailer.manager.AppPrefenceManager;

*/
/**
 * @author mobatia-user
 * 
 *//*

public class GCMIntentService extends GCMBaseIntentService {

	*/
/** The Constant TAG. *//*

	private static final String TAG = "GCMIntentService";
	String message;
	// APA91bGmWK2XC8zPVo5BhxTQ4ah1ySkuaPcCuI7rsbOfATR4oxAG4v6r2SClMxoOVgB0ThU9SsFnFoOX4debvy2JvD2mXNSOPcPVucSBT2m7-QiXGkkrl8hOHfNd3KG2U5yusTMbOcx-rfKXLPGANo_5cBHX3_qbNg

	*/
/**
	 * Instantiates a new gCM intent service.
	 *//*

	public GCMIntentService() {
		// super("263321863434");
		// super("284824744261");
		//super("1064776722927");
		super("250313383419");
		
		Log.e("", "Inside GCM Service---");
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context mContext, Intent intent) {
		// TODO Auto-generated method stub
		String result = intent.getExtras().getString("message");
		
		try {
			JSONObject messageObject=new JSONObject(result);
			message=messageObject.getString("description");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "19022015:device message :" + message);
		generateNotification(mContext, message);

	}

	@Override
	protected void onRegistered(Context mContext, String registrationId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "19022015:device onRegistered:registrationId:"
				+ registrationId);
		AppPrefenceManager.saveRegid(registrationId, mContext);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	*/
/**
	 * Method called on receiving a deleted message.
	 * 
	 * @param context
	 *            the context
	 * @param total
	 *            the total
	 *//*

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		// displayMessage(context, message);
		// notifies user
		System.out.println("check......onDeletedMessages.");
		generateNotification(context, message);
		// generateNotification(context, message);
	}

	*/
/**
	 * Issues a notification to inform the user that server has sent a message.
	 * 
	 * @param context
	 *            the context
	 * @param message
	 *            the message
	 *//*

	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		if (message != null) {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context)
					.setAutoCancel(true)
					.setContentText(message)
					.setContentTitle(
							context.getResources().getString(R.string.app_name))
					.setDefaults(Notification.DEFAULT_ALL)
					.setSmallIcon(R.drawable.ic_launcher);

			Intent resultIntent = null;
			TaskStackBuilder stackBuilder = null;
			resultIntent = new Intent(context, VKCSplashActivity.class);
			resultIntent.putExtra("From", "GCM");
			resultIntent.putExtra("Message", message);
			resultIntent.putExtra("Type", 0);
			stackBuilder = TaskStackBuilder.create(context);
			if (resultIntent != null) {
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent = stackBuilder
						.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
			}
		}

	}
}
*/
