package com.imansoft.contact.manager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.imansoft.contact.manager.adapter.ContactAdapter;
import com.imansoft.contact.manager.models.Contact;
import com.imansoft.contact.manager.utilies.ContactResolver;
import com.imansoft.contact.manager.utilies.OnContactResolverListener;

import java.util.List;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class ContactActivity extends BaseActivity {

    private        RecyclerView                    recyclerView;
    private static OnCallbackSelectContactListener listener;

    public static void start(Context context , OnCallbackSelectContactListener listener) {
       context.startActivity(new Intent(context, ContactActivity.class));
       ContactActivity.listener  = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getProgressDialog().show();
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        requestPermission(Manifest.permission.READ_CONTACTS, new OnRequestPermissionListener() {
            @Override
            public void onAllowed(String permission) {



                getContactResolver().getContacts(new OnContactResolverListener(){
                    @Override
                    public void onContactResolved(List<Contact> list) {
                        recyclerView.setAdapter(new ContactAdapter(list,true));
                        getProgressDialog().dismiss();
                    }
                });


            }

            @Override
            public void onDenied(String persmission) {

            }
        });




        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectedContact(((ContactAdapter)recyclerView.getAdapter()).getSelectedList());
                finish();
            }
        });

        ((EditText)findViewById(R.id.edt_search)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((ContactAdapter)recyclerView.getAdapter()).search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactActivity.listener = null;
    }

    public interface OnCallbackSelectContactListener {
        void onSelectedContact(List<Contact> list);
    }
}
