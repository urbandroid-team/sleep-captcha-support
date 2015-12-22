package com.urbandroid.sleep.captcha;

import android.content.Context;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.intent.IntentExtraSetter;

public class FallbackPreviewCaptchaSupport extends AbstractCaptchaSupport {

    public FallbackPreviewCaptchaSupport(final Context context) {
        super(context);
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
    protected void doAlive() {
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

}
