package com.tongdao.sdk.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongdao.sdk.R;
import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.interfaces.InAppMessageCallback;
import com.tongdao.sdk.tools.TdDisplayUtil;
import com.tongdao.sdk.tools.TdImageManager;
import com.tongdao.sdk.tools.TdUtils;

import static com.tongdao.sdk.config.Constants.*;

/**
 * Created by agonch on 10/26/16.
 */

public class PopupManager implements TdImageManager.ImageLoadListener{
    private static final String MESSAGE = "td_message";
    private static final int DURATION = 500;

    private Handler displayHandler;
    private Runnable displayRunnable;
    private TranslateAnimation tempTranslateAnimation;
    private TdImageManager imageManager;
    private InAppMessageCallback inAppMessageCallback;
    private TdMessageBean tdMessageBean;
    private Activity activity;
    private ViewGroup rootView;
    private LinearLayout popupView;
    private int screenWidth;
    private int screenHeight;
    private int contentHeight;
    private int margin;
    private int statusBar;
    private int popupHeight;
    private int popupWidth;
    private ViewGroup topRootView;
    private ViewGroup bottomRootView;
    View darkenView;

    public PopupManager(Activity activity, TdMessageBean tdMessageBean, InAppMessageCallback inAppMessageCallback){
        this.activity = activity;
        this.tdMessageBean = tdMessageBean;
        this.inAppMessageCallback = inAppMessageCallback;
    }

    public void showInAppDialog(){
        inflateLayout();
    }

    /**
     * Inflates the layouts used depending on the type of popup
     */
    private void inflateLayout(){
        //delaying to allow parent layouts to be inflated and have a width
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null){
                    return;
                }
                switch (tdMessageBean.getLayout()){
                    case POPUP_TOP:
                        inflateSmallNotification();
                        break;
                    case POPUP_MIDDLE_FULL:
                        inflateFullNotification();
                        break;
                    case POPUP_MIDDLE_TEMPLATE:
                        inflateTemplateNotification();
                        break;
                    case POPUP_BOTTOM:
                        inflateSmallNotification();
                        break;
                    case POPUP_CENTER:
                        inflateFullNotification();
                        break;
                    default:
                        return;
                }
                loadImage();
            }
        },100L);

    }

    private void inflateSmallNotification(){
        //if something is wrong, don't display popup for now
        //TODO: maybe have error reporting frameworks
        if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null) {
            return;
        }
        //set variables
        topRootView = (ViewGroup) activity.getWindow().getDecorView();
        screenWidth = activity.getWindow().getDecorView().getWidth();
        screenHeight = activity.getWindow().getDecorView().getHeight();
        contentHeight = activity.getWindow().getDecorView().findViewById(android.R.id.content).getHeight();
        margin = TdDisplayUtil.getRawPixel(activity, 4);
        statusBar = TdDisplayUtil.getRawPixel(activity, 25);
        popupHeight = TdDisplayUtil.getRawPixel(activity, 80);
        bottomRootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        popupView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.td_popup_small,null);

    }

    private void inflateFullNotification(){
        //if something is wrong, don't display popup for now
        //TODO: maybe have error reporting frameworks
        if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null) {
            return;
        }
        //set variables
        topRootView = (ViewGroup) activity.getWindow().getDecorView();
        screenWidth = activity.getWindow().getDecorView().getWidth();
        screenHeight = activity.getWindow().getDecorView().getHeight();
        contentHeight = activity.getWindow().getDecorView().findViewById(android.R.id.content).getHeight();
        margin = TdDisplayUtil.getRawPixel(activity, 4);
        statusBar = TdDisplayUtil.getRawPixel(activity, 25);
        popupHeight = TdDisplayUtil.getRawPixel(activity, 80);
        bottomRootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        popupView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.td_popup_full,null);

    }

    private void inflateTemplateNotification(){
        //if something is wrong, don't display popup for now
        //TODO: maybe have error reporting frameworks
        if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null) {
            return;
        }
        //set variables
        topRootView = (ViewGroup) activity.getWindow().getDecorView();
        screenWidth = activity.getWindow().getDecorView().getWidth();
        screenHeight = activity.getWindow().getDecorView().getHeight();
        contentHeight = activity.getWindow().getDecorView().findViewById(android.R.id.content).getHeight();
        margin = TdDisplayUtil.getRawPixel(activity, 4);
        statusBar = TdDisplayUtil.getRawPixel(activity, 25);
        popupHeight = TdDisplayUtil.getRawPixel(activity, 80);
        bottomRootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        popupView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.td_popup_template,null);

    }

    private void setLayoutItems(){
        TextView titleTV = (TextView) popupView.findViewById(R.id.titleTV);
        TextView messageTV = (TextView) popupView.findViewById(R.id.messageTV);
        ImageButton closeIV = (ImageButton) popupView.findViewById(R.id.closeIV);
        Button ctaButton = (Button) popupView.findViewById(R.id.ctaButton);
        if (tdMessageBean.getTitle() != null && tdMessageBean.getTitle().length()>0 && titleTV != null){
            titleTV.setText(tdMessageBean.getTitle());
        }
        if (tdMessageBean.getMessage() != null && tdMessageBean.getMessage().length()>0 && messageTV != null){
            messageTV.setText(tdMessageBean.getMessage());
        }
        if (tdMessageBean.getCta() != null & tdMessageBean.getCta().length() > 0 && ctaButton != null){
            ctaButton.setText(tdMessageBean.getCta());
        }
        if (closeIV != null){
            closeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closePopup();
                }
            });
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closePopup();
            }
        },tdMessageBean.getDisplayTime());
    }

    private void closePopup(){
        if (rootView != null && popupView != null){
            switch (tdMessageBean.getLayout()){
                case POPUP_TOP:
                    tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
                    tempTranslateAnimation.setDuration(DURATION);
                    tempTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    popupView.startAnimation(tempTranslateAnimation);
                    break;
                case POPUP_BOTTOM:
                    tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
                    tempTranslateAnimation.setDuration(DURATION);
                    tempTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    popupView.startAnimation(tempTranslateAnimation);
                    break;
                case POPUP_MIDDLE_TEMPLATE:
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(popupView,"y",-screenHeight);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.setRepeatCount(0);
                    objectAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    objectAnimator.start();
                    break;
                case POPUP_MIDDLE_FULL:
                    ObjectAnimator objectAnimatorFull = ObjectAnimator.ofFloat(popupView,"y",-screenHeight);
                    objectAnimatorFull.setDuration(DURATION);
                    objectAnimatorFull.setRepeatCount(0);
                    objectAnimatorFull.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    objectAnimatorFull.start();
                    lightenScreen();
                    break;
                case POPUP_CENTER:
                    ObjectAnimator objectAnimatorFull2 = ObjectAnimator.ofFloat(popupView,"y",-screenHeight);
                    objectAnimatorFull2.setDuration(DURATION);
                    objectAnimatorFull2.setRepeatCount(0);
                    objectAnimatorFull2.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            if (rootView != null && popupView != null){
                                rootView.removeView(popupView);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    objectAnimatorFull2.start();
                    lightenScreen();
                    break;
            }

        }
    }

    private void loadImage(){
        String imageUrl = tdMessageBean.getImageUrl();
        ImageView imageIV = (ImageView) popupView.findViewById(R.id.imageIV);
        if (imageUrl != null && !imageUrl.trim().equals("") && imageIV != null) {
            this.imageManager = new TdImageManager(activity.getApplicationContext(), TdUtils.HTTP_CACHE_DIR);
            this.imageManager.loadImage(imageUrl, imageIV, false, 1, null, null,this);
        } else {
        }
    }

    private void showNotification(){
        if (popupView == null || topRootView == null || bottomRootView == null){
            return;
        }
        topRootView.removeView(popupView);
        bottomRootView.removeView(popupView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth - margin * 2, popupHeight);

        popupView.setLayoutParams(layoutParams);
        int popupTop;
//        int popupWidth;
//        int popupHeight;

        switch (tdMessageBean.getLayout()){
            case POPUP_TOP:
                topRootView.addView(popupView,layoutParams);
                tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
                popupView.setTranslationY(margin + statusBar);
                popupView.setTranslationX(margin);
                PopupManager.this.rootView = topRootView;
                break;
            case POPUP_BOTTOM:
                bottomRootView.addView(popupView,layoutParams);
                tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                popupView.setTranslationY(contentHeight - margin - popupHeight);
                popupView.setTranslationX(margin);
                PopupManager.this.rootView = bottomRootView;
                break;
            case POPUP_MIDDLE_TEMPLATE:
                int top = screenHeight / 4;
                int height = screenHeight / 2;
                layoutParams = new LinearLayout.LayoutParams(screenWidth - margin * 2,height);
                popupView.setLayoutParams(layoutParams);
                topRootView.addView(popupView,layoutParams);
                tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
                popupView.setTranslationX(margin);
                popupView.setTranslationY(top);
                PopupManager.this.rootView = topRootView;
                break;
            case POPUP_MIDDLE_FULL:
                PopupManager.this.rootView = topRootView;
                darkenScreen();
                popupWidth = screenWidth - margin * 2;
                popupHeight = (int)((popupWidth * tdMessageBean.getImageHeight()) / tdMessageBean.getImageWidth());
                popupTop = (screenHeight - popupHeight) / 2;
                layoutParams = new LinearLayout.LayoutParams(popupWidth,popupHeight);
                popupView.setLayoutParams(layoutParams);
                topRootView.addView(popupView,layoutParams);
                tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
                popupView.setTranslationX(margin);
                popupView.setTranslationY(popupTop);
                break;
            case POPUP_CENTER:
                PopupManager.this.rootView = topRootView;
                darkenScreen();
                popupWidth = screenWidth - margin * 2;
                popupHeight = (int)((popupWidth * tdMessageBean.getImageHeight()) / tdMessageBean.getImageWidth());
                popupTop = (screenHeight - popupHeight) / 2;
                layoutParams = new LinearLayout.LayoutParams(popupWidth,popupHeight);
                popupView.setLayoutParams(layoutParams);
                topRootView.addView(popupView,layoutParams);
                tempTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
                popupView.setTranslationX(margin);
                popupView.setTranslationY(popupTop);
                break;
        }
        tempTranslateAnimation.setDuration(DURATION);
        popupView.startAnimation(tempTranslateAnimation);

    }


    //callbacks for image loading
    @Override
    public void onLoadComplete() {
        setLayoutItems();
        switch (tdMessageBean.getLayout()){
            case POPUP_TOP:
                showNotification();
                break;
            case POPUP_MIDDLE_FULL:
                showNotification();
                break;
            case POPUP_MIDDLE_TEMPLATE:
                showNotification();
                break;
            case POPUP_BOTTOM:
                showNotification();
                break;
            default:
                return;
        }
    }

    @Override
    public void onCancelled() {
        //TODO: Image not loaded. Check how to proceed.
    }

    private void darkenScreen(){
        darkenView = View.inflate(activity,R.layout.td_popup_background,null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth,screenHeight);
        if (rootView != null){
            rootView.removeView(darkenView);
            rootView.addView(darkenView,layoutParams);
            float[] animation = {0f,1f};
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(darkenView,"alpha",animation);
            objectAnimator.setDuration(DURATION);
            objectAnimator.setRepeatCount(0);
            objectAnimator.start();
        }
    }

    private void lightenScreen(){
        if (rootView != null){
            float[] animation = {1f,0f};
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(darkenView,"alpha",animation);
            objectAnimator.setDuration(DURATION);
            objectAnimator.setRepeatCount(0);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    rootView.removeView(darkenView);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    rootView.removeView(darkenView);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            objectAnimator.start();
        }
    }
}
