package com.tongdao.sdk.ui;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdao.sdk.R;
import com.tongdao.sdk.TongDaoOO;
import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.beans.TdMessageButtonBean;
import com.tongdao.sdk.interfaces.InAppMessageCallback;
import com.tongdao.sdk.tools.TdDisplayTool;
import com.tongdao.sdk.tools.TdDisplayUtil;
import com.tongdao.sdk.tools.TdImageManager;
import com.tongdao.sdk.tools.TdUtils;

/**
 * Created by agonch on 10/21/16.
 */

public class InAppDialog extends DialogFragment {

    private static final String MESSAGE = "td_message";
    private Handler displayHandler;
    private Runnable displayRunnable;
    private TranslateAnimation tempTranslateAnimation;
    private static final int DURATION = 500;
    private TdImageManager imageManager;
    private InAppMessageCallback inAppMessageCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final TdMessageBean tempTdMessageBean = (TdMessageBean) getArguments().getSerializable(MESSAGE);
        View fragmentView = null;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (tempTdMessageBean.getLayout().equalsIgnoreCase("top")) {
            fragmentView = inflater.inflate(R.layout.td_top_message, container, false);
            tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
//            getDialog().getWindow().setGravity(Gravity.TOP);
        } else if (tempTdMessageBean.getLayout().equalsIgnoreCase("bottom")) {
            fragmentView = inflater.inflate(R.layout.td_bottom_message, container, false);
            tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
//            getDialog().getWindow().setGravity(Gravity.BOTTOM);
        } else if (tempTdMessageBean.getLayout().equalsIgnoreCase("full")) {
            fragmentView = inflater.inflate(R.layout.td_fullscreen_page, container, false);
            tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        } else {
            fragmentView = super.onCreateView(inflater, container, savedInstanceState);
            tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        }
        tempTranslateAnimation.setDuration(DURATION);


        if (tempTdMessageBean.getLayout().equalsIgnoreCase("top") || tempTdMessageBean.getLayout().equalsIgnoreCase("bottom")) {
            ImageView tempImageArrow = (ImageView) fragmentView.findViewById(R.id.td_message_arrow);
            //click target
//            final String type = tempTdMessageBean.getActionType();
//            final String value = tempTdMessageBean.getActionValue();
//            if (type != null && value != null) {
//                tempImageArrow.setVisibility(View.VISIBLE);
//                fragmentView.findViewById(R.id.td_message_root).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //跟踪打开InAppMessage
//                        inAppMessageCallback.callbackTrackOpenInAppMessage(tempTdMessageBean);
//                        openPage(type, value);
//                    }
//                });
//            } else {
//                tempImageArrow.setVisibility(View.GONE);
//            }

            //show message
            TextView tempTextView = (TextView) fragmentView.findViewById(R.id.td_message_tv);
            tempTextView.setText(tempTdMessageBean.getMessage() == null ? "" : tempTdMessageBean.getMessage());

            //show img or not
            ImageView tempImageView = (ImageView) fragmentView.findViewById(R.id.td_message_iv);
            String imageUrl = tempTdMessageBean.getImageUrl();
            if (imageUrl != null && !imageUrl.trim().equals("")) {
                tempImageView.setVisibility(View.VISIBLE);
                this.imageManager = new TdImageManager(getActivity().getApplicationContext(), TdUtils.HTTP_CACHE_DIR);
                this.imageManager.loadImage(imageUrl, tempImageView, false, 1, null, null);
            } else {
                tempImageView.setVisibility(View.GONE);
            }

            final View finalFragmentView = fragmentView;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resizeTopBottomFragments(finalFragmentView);
                }
            }, 150);

        } else if (tempTdMessageBean.getLayout().equalsIgnoreCase("full")) {
            final View finalFragmentView = fragmentView;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initFullScreen(tempTdMessageBean, finalFragmentView);
                }
            }, 150);
        }

        //auto close
        long displayTime = tempTdMessageBean.getDisplayTime();
        if (displayTime > 0) {
            displayHandler = new Handler();
            displayRunnable = new Runnable() {
                @Override
                public void run() {
                    if (InAppDialog.this != null && InAppDialog.this.isVisible()) {
                        InAppDialog.this.dismiss();
                    }
                }
            };
            displayHandler.postDelayed(displayRunnable, displayTime * 1000);
        }

        if (tempTdMessageBean.getLayout().equalsIgnoreCase("top") || tempTdMessageBean.getLayout().equalsIgnoreCase("bottom") || tempTdMessageBean.getLayout().equalsIgnoreCase("full")) {
            fragmentView.findViewById(R.id.td_message_root).startAnimation(tempTranslateAnimation);
        } else {
            //ToDo
        }
        inAppMessageCallback.callbackTrackReceivedInAppMessage(tempTdMessageBean);

        return fragmentView;
    }

    @SuppressWarnings("deprecation")
    private void initFullScreen(TdMessageBean tempTdMessageBean, View rootView) {
        //just for testing
        int cutDp = TdDisplayUtil.getRawPixel(getActivity().getApplicationContext(), 10);
        int width = rootView.findViewById(R.id.td_root_container).getWidth();
        int height = rootView.findViewById(R.id.td_root_container).getHeight();

        int[] datas = TdDisplayTool.configDisplayForFullScreen(getActivity().getApplicationContext(), width, height, true);

        RelativeLayout lq_page_out_container = (RelativeLayout) rootView.findViewById(R.id.td_message_root);
        ImageView coverImageView = (ImageView) rootView.findViewById(R.id.lq_full_iv);
        FrameLayout lq_btn_container = (FrameLayout) rootView.findViewById(R.id.lq_btn_container);

        final ImageView lq_page_close_iv = (ImageView) rootView.findViewById(R.id.lq_page_close_iv);

        //for out container
        RelativeLayout.LayoutParams outPara = (RelativeLayout.LayoutParams) lq_page_out_container.getLayoutParams();
        outPara.width = datas[0];
        outPara.height = datas[1];
        lq_page_out_container.setLayoutParams(outPara);
        lq_page_out_container.setPadding(cutDp, cutDp, cutDp, cutDp);

        //for image view
        RelativeLayout.LayoutParams inImgPara = (RelativeLayout.LayoutParams) coverImageView.getLayoutParams();
        inImgPara.width = datas[0] - 2 * cutDp;
        inImgPara.height = datas[1] - 2 * cutDp;
        coverImageView.setLayoutParams(inImgPara);
        coverImageView.setPadding(datas[2] / 2, datas[2] / 2, datas[2] / 2, datas[2] / 2);

        RelativeLayout.LayoutParams framePara = (RelativeLayout.LayoutParams) lq_btn_container.getLayoutParams();
        framePara.width = datas[0] - 2 * cutDp;
        framePara.height = datas[1] - 2 * cutDp;
        lq_btn_container.setLayoutParams(framePara);
        lq_btn_container.setPadding(datas[2] / 2, datas[2] / 2, datas[2] / 2, datas[2] / 2);

        int realW = rootView.findViewById(R.id.lq_fake_view).getWidth() - 2 * cutDp - datas[2];
        int realH = rootView.findViewById(R.id.lq_fake_view).getHeight() - 2 * cutDp - datas[2];

        //for buttons
        initButtons(tempTdMessageBean, realW, realH, rootView);

        //for close image view
        RelativeLayout.LayoutParams closeImgPara = (RelativeLayout.LayoutParams) lq_page_close_iv.getLayoutParams();
        closeImgPara.width = datas[2];
        closeImgPara.height = datas[2];
        lq_page_close_iv.setLayoutParams(closeImgPara);
        lq_page_close_iv.setVisibility(View.VISIBLE);

        lq_page_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InAppDialog.this.dismiss();
            }
        });

        String imageUrl = tempTdMessageBean.getImageUrl();
        if (imageUrl != null && !imageUrl.trim().equals("")) {
            this.imageManager = new TdImageManager(getActivity().getApplicationContext(), TdUtils.HTTP_CACHE_DIR);
            this.imageManager.loadImage(imageUrl, coverImageView, false, 1, null, null);
        }


//        String closeImageUrl = tempTdMessageBean.getCloseBtn();
//        if (closeImageUrl != null && !closeImageUrl.trim().equals("")) {
//            if (this.imageManager == null) {
//                this.imageManager = new TdImageManager(getActivity().getApplicationContext(), TdUtils.HTTP_CACHE_DIR);
//            }
//            this.imageManager.loadImage(closeImageUrl, lq_page_close_iv, false, 1, null, null, new TdImageManager.ImageLoadListener() {
//
//                @Override
//                public void onLoadComplete() {
//                    // TODO Auto-generated method stub
//                }
//
//                @Override
//                public void onCancelled() {
//                    if (InAppDialog.this != null && !InAppDialog.this.isVisible()) {
//                        lq_page_close_iv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.td_page_close));
//                    }
//                }
//            });
//        } else {
            lq_page_close_iv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.td_page_close));
//        }

    }

    private void initButtons(final TdMessageBean tempTdMessageBean, int realW, int realH, View fragmentView) {
        for (int i = 0; i < tempTdMessageBean.getButtons().size(); i++) {
            final TdMessageButtonBean eachButton = tempTdMessageBean.getButtons().get(i);
            double rateX = eachButton.getRateX();
            double rateY = eachButton.getRateY();
            double rateW = eachButton.getRateW();
            double rateH = eachButton.getRateH();

            Button btn = null;
            if (i == 0) {
                btn = (Button) fragmentView.findViewById(R.id.td_btn1);
            } else if (i == 1) {
                btn = (Button) fragmentView.findViewById(R.id.td_btn2);
            }

            if (btn != null) {
                FrameLayout.LayoutParams btnPara = (FrameLayout.LayoutParams) btn.getLayoutParams();
                btnPara.width = (int) (rateW * realW);
                btnPara.height = (int) (rateH * realH);
                btnPara.setMargins((int) (rateX * realW), (int) (rateY * realH), 0, 0);
                btn.setLayoutParams(btnPara);
                btn.setBackgroundColor(Color.TRANSPARENT);
                btn.setVisibility(View.VISIBLE);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跟踪打开InAppMessage
                        inAppMessageCallback.callbackTrackOpenInAppMessage(tempTdMessageBean);
                        openPage(eachButton.getActionType(), eachButton.getActionValue());
                    }
                });
            }
        }
    }

    private void resizeTopBottomFragments(View rootView){
        int cutDp = TdDisplayUtil.getRawPixel(getActivity().getApplicationContext(), 10);
        int statusBar = TdDisplayUtil.getRawPixel(getActivity().getApplicationContext(), 25);
        int width = rootView.findViewById(R.id.td_message_root).getWidth();
        int height = TdDisplayUtil.getRawPixel(getActivity().getApplicationContext(), 64);
        getDialog().getWindow().setLayout(width, height);
//        rootView.setPadding(0,statusBar,0,0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME,android.R.style.Theme_Holo_Light);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public static InAppDialog newInstance(TdMessageBean message,InAppMessageCallback inAppMessageCallback) {
        InAppDialog inAppDialog = new InAppDialog();
        Bundle args = new Bundle();
        args.putSerializable(MESSAGE, message);
        inAppDialog.setArguments(args);
        inAppDialog.inAppMessageCallback = inAppMessageCallback;
        return inAppDialog;
    }

    private void openPage(String type, String value) {
        if (type == null || value == null) {
            return;
        }

        if (type.equals("") || value.equals("")) {
            return;
        }

        if (type.equalsIgnoreCase("deeplink")) {
            Intent deepLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
            if (isIntentCallable(deepLinkIntent)) {
                this.startActivity(deepLinkIntent);
            }
        } else if (type.equalsIgnoreCase("url")) {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
            this.startActivity(viewIntent);
        }
    }

    private boolean isIntentCallable(Intent intent) {
        return getActivity().getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }


}
