package com.yberry.dinehawaii.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;

import java.util.ArrayList;


public class VendorBidDBHandler extends SQLiteOpenHelper {
    public static final String TAG = "VendorOrderDBHandler";
    public static final String KEYID = "id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_ITEMID = "item_id";
    public static final String KEY_ITEMNAME = "item_name";
    public static final String KEY_VENDOR_ID = "vendor_id";
    public static final String KEY_VENDOR_NAME = "vendor_name";
    public static final String KEY_VENDOR_ITEMPRICE = "vendor_item_price";
    public static final String KEY_VENDOR_ITEMQUANTITY = "vendor_item_qty";
    public static final String KEY_VENDOR_ITEMTOTALCOST = "vendor_item_total_cost";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DineHawaiiVendorBid    .db";
    private static final String TABLE_CART_BID = "vendor_cart_bid";

    String CREATE_TABLE_CART_BID = "CREATE TABLE " + TABLE_CART_BID +
            "( " + KEYID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_PRODUCT_ID + " TEXT, "
            + KEY_ITEMID + " TEXT, "
            + KEY_ITEMNAME + " TEXT, "
            + KEY_VENDOR_ID + " TEXT, "
            + KEY_VENDOR_NAME + " TEXT, "
            + KEY_VENDOR_ITEMPRICE + " TEXT, "
            + KEY_VENDOR_ITEMQUANTITY + " TEXT, "
            + KEY_VENDOR_ITEMTOTALCOST + " TEXT "
            + ")";

    public VendorBidDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CART_BID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_BID);
        onCreate(sqLiteDatabase);
    }

    public void insertVendorOrderCartItem(VendorBidItemModel model) {
        Log.e(TAG, "insertVendorOrderCartItem: data >> " + model);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, model.getProduct_id());
        values.put(KEY_ITEMID, model.getItem_id());
        values.put(KEY_ITEMNAME, model.getItem_name());
        values.put(KEY_VENDOR_ID, model.getVendor_id());
        values.put(KEY_VENDOR_NAME, model.getVendor_name());
        values.put(KEY_VENDOR_ITEMPRICE, model.getVendor_item_price());
        values.put(KEY_VENDOR_ITEMQUANTITY, model.getVendor_item_qty());
        values.put(KEY_VENDOR_ITEMTOTALCOST, model.getVendor_item_total_cost());

        String cartmenusId = existItemIdOrder(model.getItem_id(), model.getVendor_id());
        if (cartmenusId.equalsIgnoreCase("")) {
            boolean bb = db.insert(TABLE_CART_BID, null, values) > 0;
            Log.e(TAG, "insertVendorCartItem: inserted >> " + bb);
        } else {
            boolean bb = db.update(TABLE_CART_BID, values, KEY_ITEMID + "=? and " + KEY_VENDOR_ID + "=?", new String[]{model.getItem_id(), model.getVendor_id()}) > 0;
            Log.e(TAG, "insertVendorCartItem: updated >> " + bb);
        }
        db.close();
    }

    public void updateOrderItemQty(String qty, String item_id, String item_total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VENDOR_ITEMQUANTITY, qty);
        values.put(KEY_VENDOR_ITEMTOTALCOST, item_total);
        boolean bb = db.update(TABLE_CART_BID, values, KEY_ITEMID + "=?", new String[]{item_id}) > 0;
        Log.e(TAG, "updateOrderItemQty: updated >> " + bb);
        db.close();
    }

    public String existItemIdOrder(String item_id, String vendor_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String menu_id = "";
        String queary = "SELECT " + KEY_ITEMID + " FROM " + TABLE_CART_BID + " WHERE " + KEY_ITEMID + " = '" + item_id + "' and " + KEY_VENDOR_ID + " = " + "'" + vendor_id + "'";
        Cursor cursor = db.rawQuery(queary, null);
        if (cursor == null) {
            return menu_id;
        }
        if (cursor.moveToFirst()) {
            do {
                menu_id = cursor.getString(cursor.getColumnIndex(KEY_ITEMID));
            } while (cursor.moveToNext());
        }
        return menu_id;
    }

    public ArrayList<VendorBidItemModel> getOrderCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VendorBidItemModel> modelList = new ArrayList<VendorBidItemModel>();
        String queary = "SELECT * FROM " + TABLE_CART_BID;
        Cursor cursor = db.rawQuery(queary, null);
        Log.e(TAG, "getOrderCartItems: cursor >> " + cursor);
        if (cursor == null) {
            return modelList;
        }
        if (cursor.moveToFirst()) {
            do {
                VendorBidItemModel itemsDetailsModel = new VendorBidItemModel();
                itemsDetailsModel.setId(cursor.getString(cursor.getColumnIndex(KEYID)));
                itemsDetailsModel.setProduct_id(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_ID)));
                itemsDetailsModel.setItem_id(cursor.getString(cursor.getColumnIndex(KEY_ITEMID)));
                itemsDetailsModel.setItem_name(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAME)));
                itemsDetailsModel.setVendor_id(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ID)));
                itemsDetailsModel.setVendor_name(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME)));
                itemsDetailsModel.setVendor_item_price(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ITEMPRICE)));
                itemsDetailsModel.setVendor_item_qty(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ITEMQUANTITY)));
                itemsDetailsModel.setVendor_item_total_cost(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ITEMTOTALCOST)));
                modelList.add(itemsDetailsModel);

            } while (cursor.moveToNext());
        }
        return modelList;
    }

    public boolean hasCartData() {
        boolean hasResult = false;
        SQLiteDatabase myDataBase = this.getReadableDatabase();
        String queary = "SELECT * FROM " + TABLE_CART_BID;
        Cursor cursor = myDataBase.rawQuery(queary, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                hasResult = false;
            }
            if (cursor.getCount() > 0) {
                hasResult = true;
            }
        }
        return hasResult;
    }

    public String getOrderCartTotal() {
        String total = "0";
        SQLiteDatabase myDataBase = this.getReadableDatabase();
        String queary = "SELECT SUM(" + KEY_VENDOR_ITEMTOTALCOST + ") as total_amount FROM " + TABLE_CART_BID;
        Cursor cursor = myDataBase.rawQuery(queary, null);
        if (cursor == null) {
            return total;
        }
        if (cursor.moveToFirst()) {
            do {
                total = cursor.getString(cursor.getColumnIndex("total_amount"));
            } while (cursor.moveToNext());
        }
        Log.e(TAG, "getOrderCartTotal: total>>" + total);
        if (total == null) {
            total = "0";
            return total;
        } else
            return total;
    }

    public int deleteCartOrderTtem(String item_id, String vendor_id) {
        int rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        rowCount = db.delete(TABLE_CART_BID, KEY_ITEMID + "=? AND " + KEY_VENDOR_ID + "=?", new String[]{item_id, vendor_id});
        Log.e(TAG, "deleteCartOrderTtem: rowCount >> " + rowCount);
        return rowCount;
    }

    public int deleteVendorCartTtem(String menuid) {
        int rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        rowCount = db.delete(TABLE_CART_BID, KEY_VENDOR_ID + "=?", new String[]{menuid});
        Log.e(TAG, "deleteCartOrderTtem: rowCount >> " + rowCount);
        return rowCount;
    }

    public int deleteCartitem() {
        int rowCount1 = 0, rowCount2 = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        rowCount1 = db.delete(TABLE_CART_BID, "1", null);
        rowCount2 = db.delete(TABLE_CART_BID, "1", null);
        Log.e(TAG, "deleteCartitem: rowCount1 >> " + rowCount1);
        Log.e(TAG, "deleteCartitem: rowCount2 >> " + rowCount2);
        return rowCount1;
    }
}
