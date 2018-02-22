package com.imansoft.contact.manager.utilies;

import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.imansoft.contact.manager.models.Contact;

import java.util.List;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class MessageManager {

    private List<Contact> list;
    private String message;
    private Handler handler;

    public MessageManager(String message , List<Contact> list){
        this.list = list;
        this.message = message;
        this.handler = new Handler();
    }


    public void send(final OnMessageSenderListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (final Contact contact : list) {
                        SmsManager
                                .getDefault()
                                .sendTextMessage(contact.getNumber(), null, message, null, null);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onProgress(list.indexOf(contact) + 1 , list.size());
                            }
                        });
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDone();
                        }
                    });


                }catch (Exception ex){
                    ex.printStackTrace();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure();
                        }
                    });

                }
            }
        }).start();

    }



    public interface OnMessageSenderListener{
        void onProgress(int countOfSend, int size);
        void onDone();
        void onFailure();
    }
}
