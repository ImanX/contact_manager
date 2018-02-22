package com.imansoft.contact.manager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imansoft.contact.manager.models.Contact;
import com.imansoft.contact.manager.utilies.MessageManager;

import java.util.List;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class MessageActivity extends BaseActivity {


    private static List<Contact> list;
    private        int           countOfMessageCharacter;

    public static void start(Context context, List<Contact> list) {
        context.startActivity(new Intent(context, MessageActivity.class));
        MessageActivity.list = list;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final EditText editText = (EditText) findViewById(R.id.edt_message);
        final TextView txtCount = (TextView) findViewById(R.id.txt_count);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.toString().length() > 70) {
                    return;
                }

                MessageActivity.this.countOfMessageCharacter = (70 - s.toString().length());
                txtCount.setText(String.format("%s/%s", countOfMessageCharacter, 70));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestPermission(Manifest.permission.SEND_SMS, new OnRequestPermissionListener() {
                    @Override
                    public void onAllowed(String permission) {

                        requestPermission(Manifest.permission.READ_PHONE_STATE, new OnRequestPermissionListener() {
                            @Override
                            public void onAllowed(String permission) {


                                String message = editText.getText().toString();


                                if (message.isEmpty()) {
                                    return;
                                }

                                if (message.length() > 70) {
                                    Toast.makeText(getApplicationContext(), getText(R.string.illegal_char_count), Toast.LENGTH_LONG).show();
                                    return;
                                }


                                getProgressDialog().show();
                                getProgressDialog().setMessage(getString(R.string.making_send_sms));


                                new MessageManager(message, list).send(new MessageManager.OnMessageSenderListener() {
                                    @Override
                                    public void onProgress(int countOfSend, int size) {
                                        getProgressDialog().setMessage(String.format("%s/%s  %s", countOfSend, size, getString(R.string.sent_sms)));
                                    }

                                    @Override
                                    public void onDone() {
                                        getProgressDialog().dismiss();
                                        Toast.makeText(getApplicationContext(), R.string.successfully_sent_sms, Toast.LENGTH_LONG).show();
                                        finish();

                                    }

                                    @Override
                                    public void onFailure() {
                                        getProgressDialog().dismiss();
                                        Toast.makeText(getApplicationContext(), R.string.unsuccessfully_sent_sms, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onDenied(String persmission) {

                            }
                        });


                    }

                    @Override
                    public void onDenied(String persmission) {

                    }
                });


            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageActivity.list = null;
    }
}
