package com.imansoft.contact.manager.utilies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import com.imansoft.contact.manager.adapter.ContactAdapter;
import com.imansoft.contact.manager.models.Contact;
import com.imansoft.contact.manager.models.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class ContactResolver {

    private static ContactResolver instance;
    private        Context         context;
    private        Database        database;


    private ContactResolver(Context context) {
        this.context = context;
        this.database = new Database(context);
    }

    public static ContactResolver getResolver(Context context) {
        if (instance == null) {
            instance = new ContactResolver(context);
        }
        return instance;
    }


    public void getContacts(final OnContactResolverListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Contact> list = new ArrayList<>();
                Cursor cursor = ContactResolver.this.context
                        .getContentResolver()
                        .query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                getSelection(),
                                null,
                                null
                                , null
                        );
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(getSelection()[0]));
                    String name = cursor.getString(cursor.getColumnIndex(getSelection()[1]));
                    String number = cursor.getString(cursor.getColumnIndex(getSelection()[2]));
                    list.add(new Contact(id, name, number));
                }

                cursor.close();
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onContactResolved(list);
                    }
                });

            }
        }).start();

    }


    public Group addGroup(String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        try {

            this.database
                    .getWritableDatabase()
                    .insert(Database.GROUP_TABLE, null, values);

            Cursor cursor = this.database
                    .getReadableDatabase()
                    .rawQuery("SELECT * FROM " + Database.GROUP_TABLE, null);
            if (cursor.moveToLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                cursor.close();
                return new Group(id, name, 0);
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void addContact(int gpID, List<Contact> list) {
        ContentValues values = new ContentValues();
        values.put("gp_id", gpID);
        try {
            for (Contact item : list) {
                values.put("name", item.getName());
                values.put("number", item.getNumber());
                this.database
                        .getWritableDatabase()
                        .insert(Database.CONTACT_TABLE, null, values);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Contact> getContactList(int gpID) {
        List<Contact> list = new ArrayList<>();
        Cursor cursor = this.database
                .getReadableDatabase()
                .rawQuery("SELECT * FROM " + Database.CONTACT_TABLE + " WHERE gp_id = " + gpID, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            list.add(new Contact(id, name, number));
        }

        cursor.close();
        return list;
    }

    public List<Group> getGroupList() {
        List<Group> list = new ArrayList<>();
        Cursor cursor = this.database
                .getReadableDatabase()
                .rawQuery("SELECT * FROM " + Database.GROUP_TABLE, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(new Group(id, name, getContactList(id).size()));
        }
        cursor.close();
        return list;
    }

    private Handler getHandler() {
        return new Handler(Looper.getMainLooper());
    }

    private String[] getSelection() {
        return new String[]{
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
    }

    public void deleteGroup(final int gpID, final OnContactResolverListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {

                database
                        .getWritableDatabase()
                        .execSQL("DELETE FROM "+Database.GROUP_TABLE +" WHERE id="+ gpID);


                database
                        .getWritableDatabase()
                        .execSQL("DELETE FROM "+ Database.CONTACT_TABLE +" WHERE gp_id="+gpID);



                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDoneDeleted();

                    }
                });
            }
        }).start();





    }


//    public interface OnContactResolverListener {
//        void onContactResolved(List<Contact> list);
//    }


}

