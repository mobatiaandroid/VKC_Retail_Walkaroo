package com.mobatia.vkcretailer.SQLiteServices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobatia.vkcretailer.controller.AppController;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DB_PATH = "/data/data/" + AppController.getContext().getPackageName() + "/databases/";
    //public static String DB_PATH = "/data/data/com.storefrontbase/databases/";
    private String DB_NAME;
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private static final String TABLE_CART = "shoppingcart";
    private static final String PRODUCT_ID = "productid";
    private static final String PRODUCT_NAME = "productname";
    private static final String SIZE_ID = "sizeid";
    private static final String PRODUCT_SIZE = "productsize";
    private static final String COLOR_ID = "colorid";
    private static final String PRODUCT_COLOR = "productcolor";
    private static final String PRODUCT_QTY = "productqty";
    private static final String GRID_VALUE = "gridvalue";
    private static final String PID = "pid";
    private static final String SAP_ID = "sapid";
    private static final String CAT_ID = "catid";
    private static final String STATUS = "status";
    private static final String PRICE = "price";
    private static final String UNIT = "unit";

    public DatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        this.myContext = context;
        this.DB_NAME = dbName;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {
            Log.v("Need to create", "Need to create");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            Log.v("DB Exists", "DB Exists");
        } catch (SQLiteException e) {
            Log.v("Database Not Exist", "Database Not Exist");
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
     //   Log.v("Inside Copy", "DataBase");

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String S = "CREATE TABLE " + TABLE_CART + "(" + PRODUCT_ID + " INTEGER," + PRODUCT_NAME + " TEXT," + SIZE_ID + " INTEGER,"
                + PRODUCT_SIZE + " TEXT," + COLOR_ID + " INTEGER," + PRODUCT_COLOR + " TEXT," + PRODUCT_QTY + " TEXT," + GRID_VALUE + " TEXT," + PID +
                " TEXT," + SAP_ID + " TEXT," + CAT_ID + " TEXT," + STATUS + " TEXT," + PRICE + " TEXT," + UNIT + " TEXT" + ")";
        db.execSQL(S);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
