package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ORIGIN_INTENT;
import static com.urbandroid.sleep.captcha.CaptchaSupport.DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
import static com.urbandroid.sleep.captcha.CaptchaSupport.TAG;

public class CaptchaSupportFactory {

    public static CaptchaSupport create(final @NonNull Activity activity) {
        return create(activity, DEFAULT_ALIVE_TIMEOUT_IN_SECONDS);
    }

    @NonNull
    public static CaptchaSupport create(final @NonNull Activity activity, final int aliveTimeout) {

        final Context context = activity.getApplicationContext();
        final Intent intent = activity.getIntent();

        final boolean isCaptchaLaunchIntent = CaptchaConstant.CAPTCHA_ACTION_LAUNCH.equals(intent.getAction());
        final boolean isCaptchaSolvedCallbackIntent = intent.hasExtra(CAPTCHA_ORIGIN_INTENT);

        if (!isCaptchaLaunchIntent && !isCaptchaSolvedCallbackIntent) {
            Log.w(TAG, "Creating FAKE PREVIEW captcha support since it is activity with no captcha launch action (" + CaptchaConstant.CAPTCHA_ACTION_LAUNCH + ")");
            final CaptchaSupport captchaSupport = new FallbackPreviewCaptchaSupport(context).aliveTimeout(aliveTimeout);
            CaptchaSupportHolder.set(captchaSupport);
            return captchaSupport;
        }

        Log.i(TAG, "isCaptchaSolvedCallbackIntent: " + isCaptchaLaunchIntent);

        final Intent rootIntent = isCaptchaSolvedCallbackIntent ? (Intent) intent.getParcelableExtra(CAPTCHA_ORIGIN_INTENT) : intent;

        final BaseCaptchaSupport baseCaptchaSupport = new BaseCaptchaSupport(
                context, rootIntent);
        baseCaptchaSupport
                .aliveTimeout(aliveTimeout)
                .alive();

        CaptchaSupportHolder.set(baseCaptchaSupport);
        Log.w(TAG, "Captcha support created");
        return baseCaptchaSupport;
    }

}
