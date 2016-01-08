package com.urbandroid.sleep.captcha.finder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaGroup;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

import java.util.List;

public interface CaptchaFinder {

    @NonNull
    List<CaptchaInfo> lookup();

    @NonNull
    List<CaptchaInfo> lookup(@Nullable CaptchaInfoFilter filter);

    @Nullable
    CaptchaInfo findById(final int id);

    @NonNull
    List<CaptchaGroup> findGroups(@Nullable CaptchaInfoFilter filter);

}
