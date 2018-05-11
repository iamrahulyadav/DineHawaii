package com.yberry.dinehawaii.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;

import java.util.ArrayList;


public class VendorOrderDBHandler extends SQLiteOpenHelper {

    //new db
    public static final String TAG = "VendorOrderDBHandler";

    public static final String KEYID = "id";
    public static final String KEY_ITEMID = "item_id";
    public static final String KEY_VENDOR_ID = "vendor_id";
    public static final String KEY_ITEMNAME = "item_name";
    public static final String KEY_ITEMPRICE = "item_price";
    public static final String KEY_ITEMQUANTITY = "item_qty";
    public static final String KEY_ITEMTOTALCOST = "item_total_cost";
    public static final String KEY_PRODUCT_ID = "productId";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DineHawaiiVendor.db";
    private static final String TABLE_CART_ORDER = "vendor_cart_order";
    private static final String TABLE_CART_BID = "vendor_cart_bid";

    String CREATE_TABLE_CART_ORDER = "CREATE TABLE " + TABLE_CART_ORDER +
            "( " + KEYID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ITEMID + " TEXT, "
            + KEY_VENDOR_ID + " TEXT, "
            + KEY_ITEMNAME + " TEXT, "
            + KEY_ITEMPRICE + " TEXT, "
            + KEY_ITEMQUANTITY + " TEXT, "
            + KEY_ITEMTOTALCOST + " TEXT ,"
            + KEY_PRODUCT_ID + " TEXT "
            + ")";
    String CREATE_TABLE_CART_BID = "CREATE TABLE " + TABLE_CART_BID +
            "( " + KEYID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ITEMID + " TEXT, "
            + KEY_VENDOR_ID + " TEXT, "
            + KEY_ITEMNAME + " TEXT, "
            + KEY_ITEMPRICE + " TEXT, "
            + KEY_ITEMQUANTITY + " TEXT, "
            + KEY_ITEMTOTALCOST + " TEXT "
            + ")";


    public VendorOrderDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CART_ORDER);
//        sqLiteDatabase.execSQL(CREATE_TABLE_CART_BID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ORDER);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_BID);
        onCreate(sqLiteDatabase);
    }

    public void insertVendorOrderCartItem(VendorOrderItemsDetailsModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEMID, model.getItemId());
        values.put(KEY_VENDOR_ID, model.getVendorId());
        values.put(KEY_ITEMNAME, model.getItemName());
        values.put(KEY_ITEMPRICE, model.getItemPrice());
        values.put(KEY_ITEMTOTALCOST, model.getItemTotalCost());
        values.put(KEY_ITEMQUANTITY, model.getItemQuan());
        values.put(KEY_PRODUCT_ID, model.getProductId());

        String cartmenusId = existItemIdOrder(model.getItemId());
        if (cartmenusId.equalsIgnoreCase("")) {
            boolean bb = db.insert(TABLE_CART_ORDER, null, values) > 0;
            Log.e(TAG, "insertVendorCartItem: inserted >> " + bb);
        } else {
            boolean bb = db.update(TABLE_CART_ORDER, values, KEY_ITEMID + "=?", new String[]{model.getItemId()}) > 0;
            Log.e(TAG, "insertVendorCartItem: updated >> " + bb);
        }
        db.close();
    }

    public void updateOrderItemQty(String qty, String item_id, String item_total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEMQUANTITY, qty);
        values.put(KEY_ITEMTOTALCOST, item_total);
        boolean bb = db.update(TABLE_CART_ORDER, values, KEY_ITEMID + "=?", new String[]{item_id}) > 0;
        Log.e(TAG, "updateOrderItemQty: updated >> " + bb);
        db.close();
    }

    public String existItemIdOrder(String cart_menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String menu_id = "";
        String queary = "SELECT " + KEY_ITEMID + " FROM " + TABLE_CART_ORDER + " WHERE " + KEY_ITEMID + " = '" + cart_menuId + "'";
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

    public ArrayList<VendorOrderItemsDetailsModel> getOrderCartItems(String vendor_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VendorOrderItemsDetailsModel> modelList = new ArrayList<VendorOrderItemsDetailsModel>();
        String queary = "SELECT * FROM " + TABLE_CART_ORDER + " WHERE " + KEY_VENDOR_ID + "=" + vendor_id;
        Cursor cursor = db.rawQuery(queary, null);
        Log.e(TAG, "getOrderCartItems: cursor >> " + cursor);
        if (cursor == null) {
            return modelList;
        }
        if (cursor.moveToFirst()) {
            do {
                VendorOrderItemsDetailsModel itemsDetailsModel = new VendorOrderItemsDetailsModel();
                itemsDetailsModel.setId(cursor.getString(cursor.getColumnIndex(KEYID)));
                itemsDetailsModel.setItemId(cursor.getString(cursor.getColumnIndex(KEY_ITEMID)));
                itemsDetailsModel.setVendorId(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ID)));
                itemsDetailsModel.setItemName(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAME)));
                itemsDetailsModel.setItemPrice(cursor.getString(cursor.getColumnIndex(KEY_ITEMPRICE)));
                itemsDetailsModel.setItemTotalCost(cursor.getString(cursor.getColumnIndex(KEY_ITEMTOTALCOST)));
                itemsDetailsModel.setItemQuan(cursor.getString(cursor.getColumnIndex(KEY_ITEMQUANTITY)));
                itemsDetailsModel.setProductId(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_ID)));
                modelList.add(itemsDetailsModel);

            } while (cursor.moveToNext());
        }
        return modelList;
    }

    public boolean hasCartData(String vendor_id) {
        boolean hasResult = false;
        SQLiteDatabase myDataBase = this.getReadableDatabase();
        String queary = "SELECT * FROM " + TABLE_CART_ORDER + " WHERE " + KEY_VENDOR_ID + "=" + vendor_id;
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

    public String getOrderCartTotal(String vendor_id) {
        String total = "0";
        SQLiteDatabase myDataBase = this.getReadableDatabase();
        String queary = "SELECT SUM(item_total_cost) as total_amount FROM " + TABLE_CART_ORDER + " WHERE " + KEY_VENDOR_ID + "=" + vendor_id;
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
            total="0";
            return total;
        } else
            return total;
    }

    public int deleteCartOrderTtem(String menuid) {
        int rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        rowCount = db.delete(TABLE_CART_ORDER, KEY_ITEMID + "=?", new String[]{menuid});
        Log.e(TAG, "deleteCartOrderTtem: rowCount >> " + rowCount);
        return rowCount;
    }

    public int deleteVendorCartTtem(String menuid) {
        int rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        rowCount = db.delete(TABLE_CART_ORDER, KEY_VENDOR_ID + "=?", new String[]{menuid});
        Log.e(TAG, "deleteCartOrderTtem: rowCount >> " + rowCount);
        return rowCount;
    }

    public int deleteCartitem() {
        int rowCount1 = 0, rowCount2 = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        rowCount1 = db.delete(TABLE_CART_ORDER, "1", null);
        rowCount2 = db.delete(TABLE_CART_BID, "1", null);
        Log.e(TAG, "deleteCartitem: rowCount1 >> " + rowCount1);
        Log.e(TAG, "deleteCartitem: rowCount2 >> " + rowCount2);
        return rowCount1;
    }
}
