package com.tongdao.demo;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.tongdao.sdk.TongDao;

import java.util.Currency;
import java.util.Locale;


public class PaymentDialog extends DialogFragment {


    Button btn_payment;
    EditText etx_quantity, etx_price;

    public PaymentDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialoge_payment, container);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String price = getArguments().getString("price", "10");
            String name = getArguments().getString("name", "10");
        }

        btn_payment = (Button) view.findViewById(R.id.btn_payment);
        etx_quantity = (EditText) view.findViewById(R.id.etx_quantity);
        etx_price = (EditText) view.findViewById(R.id.etx_price);

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TongDao.trackPlaceOrder("product 2", Float.parseFloat(etx_price.getText().toString()), Currency.getInstance(Locale.CHINA),
                        Integer.parseInt(etx_quantity.getText().toString()));
                dismiss();
            }
        });

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialoge_payment, null);

        final Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(130);

        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    public static PaymentDialog newInstance() {
        PaymentDialog frag = new PaymentDialog();
        return frag;
    }

}
