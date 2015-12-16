package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.urbandroid.sleep.captcha.CaptchaSupport.TAG;

public class CaptchaSupportFactory {

    @Nullable
    public static CaptchaSupport create(final @NonNull Activity activity) {
        if (!CaptchaConstant.CAPTCHA_ACTION_LAUNCH.equals(activity.getIntent().getAction())) {
            Log.w(TAG, "Creating captcha support for non captcha launcher activity");
            return CaptchaSupportHolder.get();
        }
        final BaseCaptchaSupport baseCaptchaSupport = new BaseCaptchaSupport(activity.getApplicationContext(), activity.getIntent());
        CaptchaSupportHolder.set(baseCaptchaSupport);
        return baseCaptchaSupport;
    }

}
