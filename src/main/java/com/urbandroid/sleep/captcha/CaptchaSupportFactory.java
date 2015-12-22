package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.urbandroid.sleep.captcha.CaptchaSupport.DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
import static com.urbandroid.sleep.captcha.CaptchaSupport.TAG;

public class CaptchaSupportFactory {

    public static CaptchaSupport create(final @NonNull Activity activity) {
        return create(activity, DEFAULT_ALIVE_TIMEOUT_IN_SECONDS);
    }

    @NonNull
    public static CaptchaSupport create(final @NonNull Activity activity, final int aliveTimeout) {
        if (!CaptchaConstant.CAPTCHA_ACTION_LAUNCH.equals(activity.getIntent().getAction())) {
            Log.w(TAG, "Creating FAKE PREVIEW captcha support since it is activity with no captcha launch action (" + CaptchaConstant.CAPTCHA_ACTION_LAUNCH + ")");
            final CaptchaSupport captchaSupport = new FallbackPreviewCaptchaSupport(activity.getApplicationContext()).aliveTimeout(aliveTimeout);
            CaptchaSupportHolder.set(captchaSupport);
            return captchaSupport;
        }
        final BaseCaptchaSupport baseCaptchaSupport = new BaseCaptchaSupport(
                activity.getApplicationContext(), activity.getIntent());
        baseCaptchaSupport
                .aliveTimeout(aliveTimeout)
                .alive();

        CaptchaSupportHolder.set(baseCaptchaSupport);
        Log.w(TAG, "Captcha support created");
        return baseCaptchaSupport;
    }

}
