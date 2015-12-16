package com.urbandroid.sleep.captcha;

import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.finder.CaptchaFinder;
import com.urbandroid.sleep.captcha.intent.IntentExtraSetter;
import com.urbandroid.sleep.captcha.launcher.CaptchaLauncher;

public interface CaptchaSupport {

    String TAG = "captcha-support";

    boolean isPreviewMode();

    boolean isConfigurationMode();

    boolean isOperationalMode();

    @CaptchaMode
    int getMode();

    @CaptchaDifficulty
    int getDifficulty();

    void alive();

    void solved();

    void solved(@Nullable IntentExtraSetter extraSetter);

    void unsolved();

    CaptchaFinder getFinder();

    CaptchaLauncher getLauncher();

}
