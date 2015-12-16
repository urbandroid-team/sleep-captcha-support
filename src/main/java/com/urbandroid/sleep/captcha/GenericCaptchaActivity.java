package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;

public class GenericCaptchaActivity extends Activity {

    private CaptchaSupport captchaSupport;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        captchaSupport = CaptchaSupportFactory.create(this);
    }

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        captchaSupport = CaptchaSupportFactory.create(this);
    }

    @Override
    @CallSuper
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        captchaSupport.alive();
    }

    @Override
    @CallSuper
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        captchaSupport.alive();
    }

    @Override
    @CallSuper
    public void onBackPressed() {
        captchaSupport.unsolved();
    }

    public CaptchaSupport getCaptchaSupport() {
        return captchaSupport;
    }

}
