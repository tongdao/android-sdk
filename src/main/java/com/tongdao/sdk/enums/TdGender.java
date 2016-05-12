package com.tongdao.sdk.enums;

import android.annotation.SuppressLint;


/**
 * 同道支持的性别类型
 */
public enum TdGender {
    MALE, FEMALE;

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
