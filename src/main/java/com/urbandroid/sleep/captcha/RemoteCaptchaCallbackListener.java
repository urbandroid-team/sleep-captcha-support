package com.urbandroid.sleep.captcha;

import android.support.annotation.NonNull;

import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

public interface RemoteCaptchaCallbackListener {

    void solved(@NonNull CaptchaInfo captchaInfo);
    void unsolved(@NonNull CaptchaInfo captchaInfo);

}
