package com.urbandroid.sleep.captcha.finder;

import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

public class IdCaptchaInfoFilter implements CaptchaInfoFilter {

    private final int id;

    public IdCaptchaInfoFilter(final int id) {
        this.id = id;
    }

    @Override
    public boolean apply(final @Nullable CaptchaInfo captchaInfo) {
        return captchaInfo != null && captchaInfo.getId() == id;
    }
}
