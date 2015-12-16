package com.urbandroid.sleep.captcha.launcher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.RemoteCaptchaCallbackListener;
import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

public interface CaptchaLauncher {

    void startCaptcha(
            @NonNull CaptchaInfo captchaInfo,
            @Nullable RemoteCaptchaCallbackListener callbackListener
    );

    void startCaptcha(
            @NonNull CaptchaInfo captchaInfo,
            @Nullable RemoteCaptchaCallbackListener callbackListener,
            @CaptchaDifficulty int difficulty
    );

    @NonNull
    Intent prepareCaptchaIntent(
            @NonNull CaptchaInfo captchaInfo,
            @CaptchaDifficulty int difficulty,
            int flags
    );
}
