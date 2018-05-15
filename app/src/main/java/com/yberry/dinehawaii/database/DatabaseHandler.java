package com.yberry.dinehawaii.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String KEYID = "id";
    public static final String KEY_MENUID = "Menu_id";
    public static final String KEY_CAT_ID = "Cat_id";
    public static final String KEY_BUSS_ID = "Buss_id";
    public static final String KEY_MESSAGE = "Message";
    public static final String KEY_DATE = "Date";
    public static final String KEY_STATUS = "Status";
    public static final String KEY_NAME = "Name";
    public static final String KEY_DELIVERYADDRESS = "DeliveryAddress";
    public static final String KEY_SMARTPHONENO = "SmartphoneNo";
    public static final String KEY_ITEMNAME = "ItemName";
    public static final String KEY_ITEMPRICE = "ItemPrice";
    public static final String KEY_ITEMQUANTITY = "ItemQuantity";
    public static final String KEY_ITEMTOTALCOST = "ItemTotalCost";
    public static final String KEY_ITEMCUSTOMIATIONLIST = "ItemCustomiationList";
    public static final String KEY_QUANT_TYPE = "quant_type";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DineHawaii.db";
    private static final String TABLE_CART = "cartitems";
    String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART +
            "( " + KEYID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_MENUID + " TEXT, "
            + KEY_CAT_ID + " TEXT, "
            + KEY_BUSS_ID + " TEXT, "
            + KEY_MESSAGE + " TEXT, "
            + KEY_DATE + " TEXT, "
            + KEY_STATUS + " TEXT, "
            + KEY_DELIVERYADDRESS + " TEXT, "
            + KEY_SMARTPHONENO + " TEXT, "
            + KEY_ITEMNAME + " TEXT, "
            + KEY_ITEMPRICE + " TEXT, "
            + KEY_ITEMCUSTOMIATIONLIST + " TEXT, "
            + KEY_ITEMQUANTITY + " TEXT, "
            + KEY_ITEMTOTALCOST + " TEXT, "
            + KEY_QUANT_TYPE + " TEXT "
            + ")";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    //------------------CREATE CART TABLE-------------//

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(sqLiteDatabase);
    }

    public void insertCartlist(OrderItemsDetailsModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENUID, model.getMenu_id());
        values.put(KEY_NAME, model.getName());
        values.put(KEY_CAT_ID, model.getCat_id());
        values.put(KEY_BUSS_ID, model.getBuss_id());
        values.put(KEY_MESSAGE, model.getMessage());
        values.put(KEY_DATE, model.getDate());
        values.put(KEY_STATUS, model.getStatus());
        values.put(KEY_DELIVERYADDRESS, model.getDeliveryAddress());
        values.put(KEY_SMARTPHONENO, model.getSmartphoneNo());
        values.put(KEY_ITEMNAME, model.getItemName());
        values.put(KEY_ITEMPRICE, model.getItemPrice());
        values.put(KEY_ITEMQUANTITY, model.getItemQuantity());
        values.put(KEY_ITEMTOTALCOST, model.getItemTotalCost());
        values.put(KEY_ITEMCUSTOMIATIONLIST, model.getItemCustomiationList());
        values.put(KEY_QUANT_TYPE, model.getQuantityType());

//update cartdata
        String cartmenusId = cartId(model.getMenu_id());

        // hasCart(myMenu.getMenuId());

        if (cartmenusId.equalsIgnoreCase("")) {
            boolean bb = db.insert(TABLE_CART, null, values) > 0;
            Log.d("CARTDATABASE", "insertCARTDATA>>>" + bb);
        } else {
            boolean bb = db.update(TABLE_CART, values, KEY_MENUID + "=?", new String[]{model.getMenu_id()}) > 0;
            Log.d("CARTDATABASE", "updateCARTDATA>>>" + bb);
        }


        db.close();

    }

    public String cartId(String cart_menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String menu_id = "";
        String queary = "SELECT " + KEY_MENUID + " FROM " + TABLE_CART + " WHERE " + KEY_MENUID + " = '" + cart_menuId + "'";
        Cursor cursor = db.rawQuery(queary, null);
        if (cursor == null) {
            return menu_id;
        }
        if (cursor.moveToFirst()) {
            do {
                menu_id = cursor.getString(cursor.getColumnIndex(KEY_MENUID));
            } while (cursor.moveToNext());
        }
        return menu_id;
    }

    //get data
    public ArrayList<OrderItemsDetailsModel> getCartItems(String busiID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<OrderItemsDetailsModel> modelList = new ArrayList<OrderItemsDetailsModel>();
        String queary = "SELECT * FROM " + TABLE_CART + " WHERE " + KEY_BUSS_ID + " = '" + busiID + "'";
        Cursor cursor = db.rawQuery(queary, null);
        Log.d("Cursordata", cursor.toString());
        if (cursor == null) {
            return modelList;
        }
        if (cursor.moveToFirst()) {
            do {
                OrderItemsDetailsModel itemsDetailsModel = new OrderItemsDetailsModel();
                itemsDetailsModel.setMenu_id(cursor.getString(cursor.getColumnIndex(KEY_MENUID)));
                itemsDetailsModel.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                itemsDetailsModel.setCat_id(cursor.getString(cursor.getColumnIndex(KEY_CAT_ID)));
                itemsDetailsModel.setBuss_id(cursor.getString(cursor.getColumnIndex(KEY_BUSS_ID)));
                itemsDetailsModel.setItemPrice(cursor.getString(cursor.getColumnIndex(KEY_ITEMPRICE)));
                itemsDetailsModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                itemsDetailsModel.setDeliveryAddress(cursor.getString(cursor.getColumnIndex(KEY_DELIVERYADDRESS)));
                itemsDetailsModel.setItemQuantity(cursor.getString(cursor.getColumnIndex(KEY_ITEMQUANTITY)));
                itemsDetailsModel.setItemTotalCost(cursor.getString(cursor.getColumnIndex(KEY_ITEMTOTALCOST)));
                itemsDetailsModel.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
                itemsDetailsModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                itemsDetailsModel.setSmartphoneNo(cursor.getString(cursor.getColumnIndex(KEY_SMARTPHONENO)));
                itemsDetailsModel.setItemName(cursor.getString(cursor.getColumnIndex(KEY_ITEMNAME)));
                itemsDetailsModel.setItemCustomiationList(cursor.getString(cursor.getColumnIndex(KEY_ITEMCUSTOMIATIONLIST)));
                itemsDetailsModel.setQuantityType(cursor.getString(cursor.getColumnIndex(KEY_QUANT_TYPE)));


                modelList.add(itemsDetailsModel);
                Log.d("LISTCart", itemsDetailsModel.getItemCustomiationList());

            } while (cursor.moveToNext());
        }
        return modelList;
    }

    public boolean hasCartData() {
        boolean hasResult = false;
        SQLiteDatabase myDataBase = getReadableDatabase();

        String queary = "select * from cartitems";

        Cursor cursor = myDataBase.rawQuery(queary, null);

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                hasResult = false;
            }
            if (cursor.getCount() > 0) {
                hasResult = true;
            }
        }

        cursor.close();
        myDataBase.close();
        return hasResult;
    }

    //delete single item
    public int deleteCartitem(String menuid) {
        int rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        rowCount = db.delete(TABLE_CART, KEY_MENUID + "=?", new String[]{menuid});
        Log.e("REMOFVECART", "DELETEITEM: rowCount" + rowCount);
        return rowCount;
    }

    //remove alldata
    public int deleteCartitem() {
        int rowCount = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        rowCount = db.delete(TABLE_CART, "1", null);
        Log.d("COUNT>>", +rowCount + "");
        return rowCount;
    }

}
