package com.tongdao.sdk.permissions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.tongdao.sdk.R;
import com.tongdao.sdk.tools.I18N;

@SuppressWarnings("unused")
public class SystemPermissionDialog extends DialogFragment {

    public static final String TAG = "SystemPermissionDialog";

    public static SystemPermissionDialog newInstance(int text) {
        SystemPermissionDialog frag = new SystemPermissionDialog();
        Bundle args = new Bundle();
        args.putInt("text", text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int text = getArguments().getInt("text");

        return new AlertDialog.Builder(getActivity())
                .setTitle(I18N.DIALOG_TITLE)
                .setCancelable(false)
                .setMessage(text)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(I18N.SETTINGS,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                try {
                                    startActivity(i);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                ).create();
    }
}
