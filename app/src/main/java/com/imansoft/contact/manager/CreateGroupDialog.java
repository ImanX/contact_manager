package com.imansoft.contact.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.imansoft.contact.manager.models.Group;
import com.imansoft.contact.manager.utilies.ContactResolver;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class CreateGroupDialog extends AlertDialog {

    private OnCreateGroupListener listener;

    protected CreateGroupDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_group);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final EditText editText = findViewById(R.id.edt_name);
        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String gpName = editText.getText().toString();

                if (gpName.isEmpty()){
                    return;
                }


               Group group = ContactResolver
                        .getResolver(getContext())
                        .addGroup(gpName);


                CreateGroupDialog.this.listener.onResponse(group);
                CreateGroupDialog.this.dismiss();


            }
        });
    }


    public void show(OnCreateGroupListener listener) {
        this.listener = listener;
        super.show();
    }

    public interface OnCreateGroupListener{
        void onResponse(@Nullable Group group);
    }
}
