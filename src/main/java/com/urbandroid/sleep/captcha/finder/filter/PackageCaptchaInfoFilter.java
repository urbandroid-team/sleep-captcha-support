package com.urbandroid.sleep.captcha.finder.filter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaGroup;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.finder.CaptchaInfoFilter;

public class PackageCaptchaInfoFilter implements CaptchaInfoFilter {

    private final String packageName;

    public PackageCaptchaInfoFilter(final @NonNull Context context) {
        this.packageName = context.getPackageName();
    }

    public PackageCaptchaInfoFilter(final @NonNull String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean apply(@Nullable CaptchaGroup group, @Nullable CaptchaInfo captcha) {
        return captcha != null && packageName.equals(captcha.getPackageName());
    }
}
