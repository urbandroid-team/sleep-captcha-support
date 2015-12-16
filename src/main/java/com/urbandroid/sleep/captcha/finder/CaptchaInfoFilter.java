package com.urbandroid.sleep.captcha.finder;

import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

public interface CaptchaInfoFilter {

    boolean apply(@Nullable CaptchaInfo captchaInfo);

}
