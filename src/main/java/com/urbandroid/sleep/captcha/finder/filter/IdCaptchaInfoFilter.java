package com.urbandroid.sleep.captcha.finder.filter;

import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaGroup;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.finder.CaptchaInfoFilter;

public class IdCaptchaInfoFilter implements CaptchaInfoFilter {

    private final int id;

    public IdCaptchaInfoFilter(final int id) {
        this.id = id;
    }

    @Override
    public boolean apply(@Nullable CaptchaGroup group, @Nullable CaptchaInfo captcha) {
        return captcha != null && captcha.getId() == id;
    }

}
