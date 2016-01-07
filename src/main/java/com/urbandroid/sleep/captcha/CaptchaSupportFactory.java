package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ORIGIN_INTENT;
import static com.urbandroid.sleep.captcha.CaptchaSupport.DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
import static com.urbandroid.sleep.captcha.CaptchaSupport.TAG;

public class CaptchaSupportFactory {

    public static CaptchaSupport create(final @NonNull Activity activity) {
        return create(activity, activity.getIntent(), DEFAULT_ALIVE_TIMEOUT_IN_SECONDS);
    }

    public static CaptchaSupport create(final @NonNull Activity activity, final @Nullable Intent intent) {
        return create(activity, intent, DEFAULT_ALIVE_TIMEOUT_IN_SECONDS);
    }

    public static CaptchaSupport create(final @NonNull Activity activity, final int aliveTimeout) {
        return create(activity, activity.getIntent(), aliveTimeout);
    }

    @NonNull
    public static CaptchaSupport create(final @NonNull Activity activity, final @Nullable Intent intent, final int aliveTimeout) {

        final boolean isCaptchaLaunchIntent = intent != null && CaptchaConstant.CAPTCHA_ACTION_LAUNCH.equals(intent.getAction());
        final boolean isCaptchaSolvedCallbackIntent = intent != null && intent.hasExtra(CAPTCHA_ORIGIN_INTENT);

        if (!isCaptchaLaunchIntent && !isCaptchaSolvedCallbackIntent) {
            Log.w(TAG, "Creating FAKE PREVIEW captcha support since it is activity with no captcha launch action (" + CaptchaConstant.CAPTCHA_ACTION_LAUNCH + ")");
            final CaptchaSupport captchaSupport = new FallbackPreviewCaptchaSupport(activity).aliveTimeout(aliveTimeout);
            CaptchaSupportHolder.set(captchaSupport);
            return captchaSupport;
        }

        Log.i(TAG, "isCaptchaSolvedCallbackIntent: " + isCaptchaLaunchIntent);

        final Intent rootIntent = isCaptchaSolvedCallbackIntent ? (Intent) intent.getParcelableExtra(CAPTCHA_ORIGIN_INTENT) : intent;

        final BaseCaptchaSupport baseCaptchaSupport = new BaseCaptchaSupport(
                activity, rootIntent);
        baseCaptchaSupport
                .aliveTimeout(aliveTimeout)
                .alive();

        CaptchaSupportHolder.set(baseCaptchaSupport);
        Log.w(TAG, "Captcha support created");
        return baseCaptchaSupport;
    }

}
