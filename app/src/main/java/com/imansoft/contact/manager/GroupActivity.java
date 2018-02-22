package com.imansoft.contact.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.imansoft.contact.manager.adapter.ContactAdapter;
import com.imansoft.contact.manager.models.Contact;
import com.imansoft.contact.manager.models.Group;

import java.util.List;


/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class GroupActivity extends BaseActivity {

    private static Group group;
    private RecyclerView recyclerView;


    public static void start(Context context , Group group){
        context.startActivity(new Intent(context , GroupActivity.class));
        GroupActivity.group = group;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        this.recyclerView.setAdapter(new ContactAdapter(getContactResolver().getContactList(group.getId()) ,false));


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactActivity.start(GroupActivity.this, new ContactActivity.OnCallbackSelectContactListener() {
                    @Override
                    public void onSelectedContact(List<Contact> list) {

                        getContactResolver().addContact(group.getId(),list);
                        recyclerView.setAdapter(new ContactAdapter(getContactResolver().getContactList(group.getId()),false));

                    }
                });
            }
        });


        findViewById(R.id.fab_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.start(GroupActivity.this ,getContactResolver().getContactList(group.getId()));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GroupActivity.group = null;
    }
}
