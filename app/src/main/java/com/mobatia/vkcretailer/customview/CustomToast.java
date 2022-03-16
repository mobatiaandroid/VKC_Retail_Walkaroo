/**
 *
 */
package com.mobatia.vkcretailer.customview;

import com.mobatia.vkcretailer.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author mobatia-user
 */
public class CustomToast {

    Activity mActivity;
    TextView mTextView;
    Toast mToast;

    public CustomToast(Activity mActivity) {
        this.mActivity = mActivity;
        init();

    }

    public void init() {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, null);
        mTextView = (TextView) layouttoast.findViewById(R.id.texttoast);

        mToast = new Toast(mActivity);
        mToast.setView(layouttoast);
    }

    public void show(int errorCode) {
        if (errorCode == 0) {
            mTextView.setText(mActivity.getResources().getString(
                    R.string.common_error_message));
        }
        if (errorCode == 1) {
            mTextView.setText("Successfully logged in.");
        }
        if (errorCode == 2) {
            mTextView.setText("Login failed.Please try again later");
        }
        if (errorCode == 3) {
            mTextView.setText("Please select size and color");
        }
        if (errorCode == 4) {
            mTextView.setText("Please select size,color and quantity");
        }
        if (errorCode == 5) {
            mTextView.setText("Succesfully added to cart");
        }
        if (errorCode == 6) {
            mTextView.setText("Successfully submitted login request");
        }
        if (errorCode == 7) {
            mTextView.setText("Registration failed.Try again later");
        }
        if (errorCode == 8) {
            mTextView.setText("Email Already Exists");
        }
        if (errorCode == 9) {
            mTextView.setText("No items in the cart");
        }
        if (errorCode == 10) {
            mTextView.setText("Please enter valid username and password");
        }
        if (errorCode == 11) {
            mTextView.setText("All fields are mandatory");
        }
        if (errorCode == 12) {
            mTextView.setText("Complaint send successfully");
        }
        if (errorCode == 13) {
            mTextView.setText("Failed.Try again later");
        }
        if (errorCode == 14) {
            mTextView.setText("Feedback send successfully");
        }
        if (errorCode == 15) {
            mTextView.setText("Salesorder submitted successfully");
        }
        if (errorCode == 16) {
            mTextView.setText("No enough values for placing sales order");
        }
        if (errorCode == 17) {
            mTextView.setText("Sorry, no result found");
        }
        if (errorCode == 18) {
            mTextView.setText("Please select any Category");
        }
        if (errorCode == 19) {
            mTextView.setText("Please select any Sub Category in filter");
        }
        if (errorCode == 20) {
            mTextView.setText("Please select state");
        }
        if (errorCode == 21) {
            mTextView.setText("Please select district");
        }
        if (errorCode == 22) {
            mTextView.setText("Please select place");
        }
        if (errorCode == 23) {
            mTextView
                    .setText("Login failed.Invalid login credentials or account not approved");
        }
        if (errorCode == 24) {
            mTextView.setText("Sorry, cannot add this item to cart");
        }
        if (errorCode == 25) {
            mTextView
                    .setText("Cannot enter value greater than existing quantity");
        }
        if (errorCode == 26) {
            mTextView.setText("Order updated successfully");
        }
        if (errorCode == 27) {
            mTextView
                    .setText("Cannot enter a quantity greater than order quantity");
        }
        if (errorCode == 28) {
            mTextView.setText("Updated successfully !");
        }
        if (errorCode == 29) {
            mTextView.setText("Quantity cannot be empty !");
        }
        if (errorCode == 30) {
            mTextView.setText("Please fill all the quantity fields correctly");
        }
        if (errorCode == 31) {
            mTextView.setText("Dealers added successfully !");
        }
        if (errorCode == 32) {
            mTextView.setText("There are no pending orders");
        }
        if (errorCode == 33) {
            mTextView.setText("State value missing");
        }
        if (errorCode == 34) {
            mTextView.setText("District value missing");
        }

        if (errorCode == 35) {
            mTextView.setText("Product already exist in cart adding quantity");
        }
        if (errorCode == 36) {
            mTextView.setText("Please enter keyword to search");
        }
        if (errorCode == 37) {
            mTextView.setText("No results found");
        }
        if (errorCode == 38) {
            mTextView.setText("Please enter a keyword to search");
        }
        if (errorCode == 39) {
            mTextView.setText("Please select a customer category");
        }
        if (errorCode == 40) {
            mTextView.setText("No Image Available for This Color");
        }

        if (errorCode == 41) {
            mTextView.setText("This product is disabled n this mobile app version, choose the appropriate VKC mobile app version. For more details and assistance please contact VKC IT Helpdesk");

        }
        if (errorCode == 42) {
            mTextView.setText("Invalid User");

        }
        if (errorCode == 43) {
            mTextView.setText("Please select a retailer from cart");

        }

        if (errorCode == 44) {
            mTextView.setText("Please select a retailer from cart");

        }
        if (errorCode == 45) {
            mTextView.setText("Please enter quantity");

        }
        if (errorCode == 59) {
            mTextView.setText("Please grant permission to continue");
        }
        if (errorCode == 60) {
            mTextView.setText("Please select user type");

        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    /*
     * CustomToast toast = new CustomToast(mActivity); toast.show(18);
     */
}
