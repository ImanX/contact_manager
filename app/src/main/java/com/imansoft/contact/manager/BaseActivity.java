package com.imansoft.contact.manager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.imansoft.contact.manager.utilies.ContactResolver;
import com.imansoft.contact.manager.utilies.Database;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {


    private static final int REQUEST_PERMISSION = 100;
    private ProgressDialog              progressDialog;
    private OnRequestPermissionListener listener;

    @SuppressLint("NewApi")
    public void requestPermission(String permission, OnRequestPermissionListener listener) {
        this.listener = listener;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.listener.onAllowed(permission);
            return;
        }


        if (this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            this.listener.onAllowed(permission);
            return;
        }

        this.requestPermissions(new String[]{permission}, REQUEST_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode == REQUEST_PERMISSION) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            this.listener.onAllowed(permissions[0]);
            return;
        }

        this.listener.onDenied(permissions[0]);
    }


    public ContactResolver getContactResolver() {
        return ContactResolver.getResolver(this);
    }

    public Database getDatabase() {
        return new Database(this);
    }

    public ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(null);
        }

        return progressDialog;
    }
}


interface OnRequestPermissionListener {
    void onAllowed(String permission);

    void onDenied(String persmission);
}

