package com.urbandroid.sleep.captcha;

import android.support.annotation.NonNull;

import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

public interface RemoteCaptchaCallbackListener {

    void captchaSolved(@NonNull CaptchaInfo captchaInfo);

}
