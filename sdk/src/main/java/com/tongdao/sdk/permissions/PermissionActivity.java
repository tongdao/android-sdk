package com.tongdao.sdk.permissions;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.tongdao.sdk.tools.TongDaoAppInfoTool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankitthakkar on 28/04/16.
 */
public class PermissionActivity extends Activity implements PermissionChecker {
    private String permission;
    private Integer requestCode;

    private IPermissionResponse permissionResponse;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        permission = intent.getStringExtra("permission");
        requestCode = intent.getIntExtra("requestCode", 0);
//        TongDaoPermissionModule.PermissionCall parcelable = (TongDaoPermissionModule.PermissionCall)intent.getSerializableExtra("callback");
        this.permissionResponse = TongDaoPermissionModule.getPermissionResponse();

        checkForPermission();
    }

    private void checkForPermission() {
        permissionManager = new PermissionManager(this);
        Map<String, Integer> map = new HashMap<>();
        map.put(permission, 1);
        if (permissionManager.requestPermissions(map, requestCode)) {
            try {
                if( permissionResponse != null ) {
                    permissionResponse.permissionGranted();
                }
                TongDaoAppInfoTool.LOCK.release();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != this.requestCode || grantResults.length == 0) {
            finish();
            return;
        }

        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                try {
                    permissionResponse.permissionGranted();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                try {
                    permissionResponse.permissionDenied();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            finish();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public PermissionManager getManager() {
        return permissionManager;
    }
}
