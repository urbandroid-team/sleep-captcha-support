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
            Log.w(TAG, "Creating FAKE PREVIEW captcha support since it is activity with no captcha launch action (" + CaptchaConstant.CAPTCHA_ACTION_LAUNCH + ")");
            final FallbackPreviewCaptchaSupport captchaSupport = new FallbackPreviewCaptchaSupport(activity.getApplicationContext());
            CaptchaSupportHolder.set(captchaSupport);
            return captchaSupport;
        }
        final BaseCaptchaSupport baseCaptchaSupport = new BaseCaptchaSupport(
                activity.getApplicationContext(), activity.getIntent());

        CaptchaSupportHolder.set(baseCaptchaSupport);
        Log.w(TAG, "Captcha support created");
        return baseCaptchaSupport;
    }

}
