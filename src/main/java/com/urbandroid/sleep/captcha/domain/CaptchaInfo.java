package com.urbandroid.sleep.captcha.domain;

import android.os.Parcelable;
import android.support.annotation.NonNull;

public interface CaptchaInfo extends Parcelable {

    int FALLBACK_ID = 1;

    @NonNull
    String getPackageName();

    @NonNull
    String getActivityName();

    @NonNull
    String getLabel();

    boolean isExternal();
    boolean hasDifficulty();
    boolean isConfigurable();

    void setConfigurable(boolean configurable);

    int getId();
    int getOrder();

}
