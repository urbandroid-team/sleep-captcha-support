package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.intent.IntentExtraSetter;

public class FallbackPreviewCaptchaSupport extends AbstractCaptchaSupport {

    public FallbackPreviewCaptchaSupport(final Activity activity, final int aliveTimeout) {
        super(activity, null, aliveTimeout);
    }

    @Override
    public boolean isOperationalMode() {
        return false;
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
