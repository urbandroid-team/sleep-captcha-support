package com.urbandroid.sleep.captcha;

import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.finder.CaptchaFinder;
import com.urbandroid.sleep.captcha.intent.IntentExtraSetter;
import com.urbandroid.sleep.captcha.launcher.CaptchaLauncher;

public interface CaptchaSupport {


    String TAG = "captcha-support";

    int DEFAULT_ALIVE_TIMEOUT_IN_SECONDS = 60;
    int MAX_ALIVE_TIMEOUT_IN_SECONDS = 5 * 60;
    int MIN_ALIVE_TIMEOUT_IN_SECONDS = 5;

    CaptchaSupport setRemainingTimeListener(@Nullable RemainingTimeListener remainingTimeListener);

    CaptchaSupport aliveTimeout(int timeoutInSeconds);
    int getRemainingTime();

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
