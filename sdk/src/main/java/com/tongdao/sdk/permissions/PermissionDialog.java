package com.tongdao.sdk.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tongdao.sdk.tools.TongDaoAppInfoTool;

import java.util.ArrayList;

public class PermissionDialog extends DialogFragment {

    public static final String TAG = "PermissionDialog";
    private ArrayList<String> permissions;
    private int request;
    private PermissionChecker checker;

    public static PermissionDialog newInstance(ArrayList<Integer> text, ArrayList<String> perms, int req) {
        PermissionDialog frag = new PermissionDialog();
        Bundle args = new Bundle();
        args.putIntegerArrayList("texts", text);
        args.putInt("req", req);
        args.putStringArrayList("perms", perms);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            checker = (PermissionChecker) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Activity doesn't implement PermissionChecker");
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checker = (PermissionChecker) activity;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Activity doesn't implement PermissionChecker");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        checker = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "You have to enable ";
        ArrayList<Integer> texts = getArguments().getIntegerArrayList("texts");
        permissions = getArguments().getStringArrayList("perms");
        request = getArguments().getInt("req");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("Permission Request")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (checker != null && checker.getManager() != null) {
                                    checker.getManager().onReasonAccepted(permissions, request);
                                }
                            }
                        }
                );

        if (texts == null)
            throw new IllegalArgumentException();

        String str = "";
        for (int i = 0; i < permissions.size(); ++i) {
            if( permissions.get(i).equalsIgnoreCase(TongDaoAppInfoTool.ACCESS_COARSE_LOCATION_PERMISSION) ||
                    permissions.get(i).equalsIgnoreCase(TongDaoAppInfoTool.ACCESS_FINE_LOCATION_PERMISSION) ) {
                str = "Location";
                message = "位置获取已关闭，可在手机设置中打开";
            }
            else if( permissions.get(i).equalsIgnoreCase(TongDaoAppInfoTool.ACCESS_TELEPHONY_PERMISSION) ){
                str = "Telephony";
                message = "信息获取已关闭，可在手机设置中打开";
            }
//            if (i == permissions.size() - 1)
//                message += str;
//            else
//                message += str + ", ";
        }
//        message += " permission";
        b.setMessage(message);
        return b.create();
    }
}
