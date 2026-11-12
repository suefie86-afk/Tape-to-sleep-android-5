package com.din.taptosleep;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQUEST_ADMIN = 101;
    private DevicePolicyManager manager;
    private ComponentName admin;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        manager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        admin = new ComponentName(this, SleepAdminReceiver.class);
        if (manager.isAdminActive(admin)) {
            lockAndClose();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Benarkan Tap to Sleep mengunci skrin apabila ikon aplikasi ditekan. Tiada data dipadam.");
            startActivityForResult(intent, REQUEST_ADMIN);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADMIN) {
            if (manager.isAdminActive(admin)) lockAndClose();
            else { Toast.makeText(this, "Aktifkan Device Administrator untuk guna Tap to Sleep", Toast.LENGTH_LONG).show(); finish(); }
        }
    }

    private void lockAndClose() {
        try { manager.lockNow(); }
        catch (SecurityException e) { Toast.makeText(this, "Kebenaran kunci skrin tiada", Toast.LENGTH_SHORT).show(); }
        finish();
    }
}
