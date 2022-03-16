package com.mobatia.vkcretailer.constants;

public interface VKCUrlConstants {
    /*
     * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
     */
    // Test URL
    //public String BASE_URL = "http://dev.mobatia.com/vkc_Retail/";
    public String BASE_URL = "http://retail.u4ic.com/vkc/";


    public String PRODUCT_DETAIL_NEW_URL = BASE_URL + "apiv2/getdetails_new";

    // public String BASE_URL="http://117.239.96.123/vkc/";
    // Live URL
    // public String BASE_URL = "http://mobile.walkaroo.in/vkc/";
    /* Settings Api */
    // live
    public String SETTINGS_URL = BASE_URL + "apiv3/getsettings";
    // dev
    // public String SETTINGS_URL =
    // "http://dev.mobatia.com/vkc2.5/apiv3/getsettings";

    /* Popularity count Api */

    public String POPULARITY_COUNT_URL = BASE_URL + "apiv2/popular";

    /* Product Detail Api */

    public String PRODUCT_DETAIL_URL = BASE_URL + "apiv3/getdetails";

    /* Search Api */

    public String SEARCH_PRODUCT_URL = BASE_URL + "apiv2/searchcontent";

    /* Login Req Api */

    public String LOGIN_REQUEST_URL = BASE_URL + "apiv2/signup";

    /* SignIn Api */

    public String LOGIN__URL = BASE_URL + "apiv2/login";

    /* Submit My Dealer */

    public String SUBMIT_MY_DEALER_URL = BASE_URL + "apiv2/addMyDealers";

    /* List My Dealers */

    public String LIST_MY_DEALERS_URL = BASE_URL + "apiv2/listMyDealers";

    /* Salesorder Api */

    public String LIST_MY_DEALERS_SALES_HEAD_URL = BASE_URL
            + "apiv2/listSalesHeadDealers";

    // public String PRODUCT_SALESORDER_SUBMISSION = BASE_URL +
    // "apiv2/salesOrder";
    public String PRODUCT_SALESORDER_SUBMISSION = BASE_URL + "apiv3/salesOrder";

    /* SubDealer Order Details */
    public String SUBDEALER_ORDER_DETAILS = BASE_URL + "apiv3/getOrderDetails";

    public String SUBDEALER_ORDER_URL = BASE_URL + "apiv3/getOrders";

    public String SUBDEALER_ORDER_URL_LIST = BASE_URL
            + "apiv2/getSubdealerOrders";
    /* SalesorderStatus Api */

    public String PRODUCT_SALESORDER_STATUS = BASE_URL
            + "apiv2/salesOrderStatus";
    /* Feedback Api */

    public String PRODUCT_FEEDBACK = BASE_URL + "apiv2/feedback";
    /* Complaint Api */

    public String PRODUCT_COMPLAINT = BASE_URL + "apiv2/compliant";

    /* State Api */

    public String DEALERS_GETSTATE = BASE_URL + "apiv2/getstate";
    /* Retailers Api */
    public String DEALERS_GETDISTRICT = BASE_URL + "apiv2/getdistrict";
    public String GET_RETAILERS = BASE_URL + "apiv2/getretailers";
    /* Dealers Api */

    public String GET_DEALERS = BASE_URL + "apiv2/getdealers";

    /* Approve,Reject Order */
    public String SET_ORDER_STATUS_API = BASE_URL + "apiv2/setOrderStatus";

    /* GCMIDregistration Api */

    public String GCM_INITIALISATION = BASE_URL + "apiv2/appinit";

    /* PaymentStatus Api */

    public String GET_PAYMENT_STATUS = BASE_URL + "apiv2/creditstatus";

    public String GET_SALES_ORDER_STATUS = BASE_URL
            + "apiv2/productSalesOrderStatus";

    public String PRODUCT_SALESORDER_DETAILS = BASE_URL
            + "apiv2/salesOrderStatusDetails";

    public String UPDATE_ORDER_STATUS = BASE_URL + "apiv2/setOrderStatus";
    /* Get Subdealer Orders */
    public String GET_SUBDEALER_ORDER_LIST = BASE_URL
            + "apiv2/getSubdealerOrders";
    /* Reorder Product */
    public String SUBMIT_REORDER_URL = BASE_URL + "apiv2/salesReorder";
    /* Like Product API */
    public String LIKE_PRODUCT_URL = BASE_URL + "apiv2/likeproduct";
    /* Get Like Count */
    public String LIKE_COUNT_URL = BASE_URL + "apiv2/getproductlikes";

    /* Recent Orders List */
    public String GET_RECENT_ORDERS = BASE_URL + "apiv2/getRecentOrders";
    /* Sales Head Orders List */
    // public String SALES_HEAD_ORDERS_URL = BASE_URL
    // + "apiv2/getSalesExecutiveOrders";
    public String GET_RETAILERDELEAR_LIST = BASE_URL + "apiv3/getRetailers";
    // // to get the next images for the listing in viewpager
    public String GET_IMAGES_LIST = BASE_URL + "apiv3/getProductImages";
    public String GET_IMAGELIST_BYCOLOR = BASE_URL
            + "apiv3/getProductImagesBycolor";
    public String URL_ARTICLE_SEARCH_PRODUCT = BASE_URL + "apiv3/searchProduct";
    // to get the details of product in the horizonatl list view
    public String URL_GET_PRODUCTDETAILPAGE = BASE_URL
            + "apiv3/getProductDetails";
}
