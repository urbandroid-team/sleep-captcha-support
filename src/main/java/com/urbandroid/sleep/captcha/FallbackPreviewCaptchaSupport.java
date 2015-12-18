package com.urbandroid.sleep.captcha;

import android.content.Context;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.finder.BaseCaptchaFinder;
import com.urbandroid.sleep.captcha.finder.CaptchaFinder;
import com.urbandroid.sleep.captcha.intent.IntentExtraSetter;
import com.urbandroid.sleep.captcha.launcher.BaseCaptchaLauncher;
import com.urbandroid.sleep.captcha.launcher.CaptchaLauncher;

public class FallbackPreviewCaptchaSupport implements CaptchaSupport {

    private final CaptchaLauncher launcher;
    private final CaptchaFinder finder;

    public FallbackPreviewCaptchaSupport(final Context context) {
        launcher = new BaseCaptchaLauncher(context);
        finder = new BaseCaptchaFinder(context);
    }

    @Override
    public boolean isPreviewMode() {
        return true;
    }

    @Override
    public boolean isConfigurationMode() {
        return false;
    }

    @Override
    public boolean isOperationalMode() {
        return false;
    }

    @Override
    public int getMode() {
        return CaptchaMode.CAPTCHA_MODE_PREVIEW;
    }

    @Override
    public int getDifficulty() {
        return CaptchaDifficulty.VERY_SIMPLE;
    }

    @Override
    public void alive() {
    }

    @Override
    public void solved() {
    }

    @Override
    public void solved(@Nullable IntentExtraSetter extraSetter) {
    }

    @Override
    public void unsolved() {
    }

    @Override
    public CaptchaFinder getFinder() {
        return finder;
    }

    @Override
    public CaptchaLauncher getLauncher() {
        return launcher;
    }


}
