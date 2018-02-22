package com.imansoft.contact.manager.utilies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class Database extends SQLiteOpenHelper {

    private static final String NAME = "db";
    private static final int    VER  = 1;



    public static final String GROUP_TABLE = "gp_table";
    public static final String CONTACT_TABLE = "contact_table";

    public Database(Context context) {
        super(context, NAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " +GROUP_TABLE  +" (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT)");
            db.execSQL("CREATE TABLE " +CONTACT_TABLE +" (id INTEGER PRIMARY KEY AUTOINCREMENT, gp_id INTEGER,name TEXT,number TEXT)");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
