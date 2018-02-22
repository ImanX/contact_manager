package com.imansoft.contact.manager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.imansoft.contact.manager.adapter.GroupAdapter;
import com.imansoft.contact.manager.models.Contact;
import com.imansoft.contact.manager.models.Group;
import com.imansoft.contact.manager.utilies.ContactResolver;
import com.imansoft.contact.manager.utilies.OnContactResolverListener;

import java.util.List;

public class MainActivity extends BaseActivity {


    private RecyclerView recyclerView;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.adapter = new GroupAdapter(getContactResolver().getGroupList()) {
            @Override
            public void onDeleteGroup(final int gpID) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.are_you_sure_delete));
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getProgressDialog().show();
                        getContactResolver().deleteGroup(gpID , new OnContactResolverListener(){
                            @Override
                            public void onDoneDeleted() {
                                adapter.deleteItem(gpID);
                                getProgressDialog().dismiss();
                            }
                        });


                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();


            }
        };
        this.recyclerView.setAdapter(adapter);


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CreateGroupDialog(MainActivity.this).show(new CreateGroupDialog.OnCreateGroupListener() {
                    @Override
                    public void onResponse(@Nullable Group group) {

                        if (group == null) {

                            return;
                        }

                        adapter.getList().add(group);
                        adapter.notifyDataSetChanged();
                        GroupActivity.start(MainActivity.this, group);

                    }
                });
            }
        });


    }

}
