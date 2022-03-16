package com.mobatia.vkcretailer.miscellaneous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mobatia.vkcretailer.R;
import com.mobatia.vkcretailer.controller.AppController;
import com.mobatia.vkcretailer.customview.CustomToast;
import com.mobatia.vkcretailer.manager.LruBitmapCache;
import com.mobatia.vkcretailer.manager.VKCBitmapCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class VKCUtils {

    public static void setImageFromUrl(Activity activity, String Url,
                                       final ImageView imageView, final ProgressBar progressBar) {

        Url = Url.replaceAll(" ", "%20");
        Picasso.with(activity).load(Url).error(R.drawable.transparent_bg)
                .into(imageView, new Callback() {

                    public void onSuccess() {

                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
        // ImageRequest request = new ImageRequest(Url, new Listener<Bitmap>() {
        //
        // @Override
        // public void onResponse(Bitmap arg0) {
        // // TODO Auto-generated method stub
        // imageView.setImageBitmap(arg0);
        // progressBar.setVisibility(View.INVISIBLE);
        // }
        // }, 0, 0, null, new ErrorListener() {
        //
        // @Override
        // public void onErrorResponse(VolleyError arg0) {
        // imageView.setImageResource(R.drawable.transparent);
        // progressBar.setVisibility(View.VISIBLE);
        //
        // }
        // });

        // ImageLoader mImageLoader =
        // AppController.getInstance().getImageLoader();
        //
        // mImageLoader.get(Url, ImageLoader.getImageListener(imageView,
        // R.drawable.search, R.drawable.location));

        // ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new
        // LruBitmapCache(
        // LruBitmapCache.getCacheSize(activity)));

        // AppController.getInstance(activity).addToRequestQueue(request);

        // AppController.getInstance().addToRequestQueue(request);

        // //////
        // ImageLoader
        //
        // ImageLoader imageLoader.displayImage(imageUri, imageView, options,
        // new ImageLoadingListener() {
        // @Override
        // public void onLoadingStarted(String imageUri, View view) {
        //
        // }
        // @Override
        // public void onLoadingFailed(String imageUri, View view, FailReason
        // failReason) {
        //
        // }
        // @Override
        // public void onLoadingComplete(String imageUri, View view, Bitmap
        // loadedImage) {
        //
        // }
        // @Override
        // public void onLoadingCancelled(String imageUri, View view) {
        //
        // }
        // }, new ImageLoadingProgressListener() {
        // @Override
        // public void onProgressUpdate(String imageUri, View view, int current,
        // int total) {
        //
        // }
        // });
        //

        // /////////

    }

    public static String formatDateWithInput(String date, String formatOutput,
                                             String formatInput) {
        String lastLoginTimeFormat = "";
        Calendar calenderDate = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatInput, Locale.US);
            sdf.setTimeZone(calenderDate.getTimeZone());
            calenderDate.setTime(sdf.parse(date));
            sdf.applyLocalizedPattern(formatOutput);
            lastLoginTimeFormat = sdf.format(calenderDate.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lastLoginTimeFormat;

    }

    public static void setImageFromUrlBaseTransprant(Activity activity,
                                                     String Url, final ImageView imageView, final ProgressBar progressBar) {
        Picasso.with(activity)

                .load(Url).error(R.drawable.transparent_bg)
                .into(imageView, new Callback() {

                    public void onSuccess() {

                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                    }
                });
    }

    /**
     * Gets the device id.
     *
     * @param context the context
     * @return the device id
     */
    public static String getDeviceID(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return telephonyManager.getDeviceId();
            }
            return telephonyManager.getDeviceId();
        } else {
            return Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        }

    }

    /**
     * Show toast message
     *
     * @param activity the activity,message
     * @return void
     */

    public static void showtoast(Activity activity, int errorType) {

        CustomToast toast = new CustomToast(activity);
        toast.show(errorType);

    }

    // public static void showtoast(Activity activity, String message) {
    // LayoutInflater inflater = activity.getLayoutInflater();
    // View layout = inflater.inflate(R.layout.layout_custom_toast, null);
    //
    // ImageView image = (ImageView) layout.findViewById(R.id.imgIcon);
    // // image.setImageResource(R.drawable.android);
    // TextView text = (TextView) layout.findViewById(R.id.txtText);
    // text.setText(message);
    //
    // Toast toast = new Toast(activity);
    // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    // toast.setDuration(Toast.LENGTH_LONG);
    // toast.setView(layout);
    // toast.show();
    // }

    public static String getrespnse(Activity mActivity, String fileName) {
        StringBuilder returnString = new StringBuilder();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mActivity
                    .getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine = reader.readLine();
            returnString.append(mLine);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // log the exception
                    e.printStackTrace();
                }
            }
        }
        return returnString.toString();
    }

    /**
     * Check internet connection.
     *
     * @param context the context
     * @return true, if successful
     */
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connec.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Hexa to Color value
     *
     * @param colorString hexa value
     * @return int value, if successful
     */
    public static int parseColor(String colorString) {
        if (colorString.startsWith("#")) {
            return Color.parseColor(colorString);
        } else {
            return Color.parseColor("#FFFFFFFF");
        }
    }

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {

            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), 0);

        }
    }

    /**
     * Sets the error for edit text.
     *
     * @param edt the edt
     * @param msg the msg
     */
    public static void setErrorForEditText(EditText edt, String msg) {
        edt.setError(msg);
    }

    /**
     * Text watcher for edit text.
     *
     * @param edt the edt
     * @param msg the msg
     */
    public static void textWatcherForEditText(final EditText edt,
                                              final String msg) {
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0 || s.equals("")) {
                    VKCUtils.setErrorForEditText(edt, msg);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s.equals("")) {
                    VKCUtils.setErrorForEditText(edt, msg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0 && edt.getError() != null) {
                    edt.setError(null);
                } else if (s.length() == 0 || s.equals("")) {
                    VKCUtils.setErrorForEditText(edt, msg);
                }
            }
        });
    }

    /**
     * Checks if is valid email.
     *
     * @param email the email
     * @return the boolean
     */
    public static Boolean isValidEmail(String email) {
        Boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


}
