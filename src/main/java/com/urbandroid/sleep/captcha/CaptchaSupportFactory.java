package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.util.IntentUtil;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ORIGIN_INTENT;
import static com.urbandroid.sleep.captcha.CaptchaSupport.TAG;

public class CaptchaSupportFactory {

    public static CaptchaSupport create(final @NonNull Activity activity) {
        return create(activity, activity.getIntent(), IntentUtil.resolveTimeout(activity.getIntent()));
    }

    public static CaptchaSupport create(final @NonNull Activity activity, final @Nullable Intent intent) {
        return create(activity, intent, IntentUtil.resolveTimeout(intent));
    }

    public static CaptchaSupport create(final @NonNull Activity activity, final int aliveTimeout) {
        return create(activity, activity.getIntent(), aliveTimeout);
    }

    @NonNull
    public static CaptchaSupport create(final @NonNull Activity activity, final @Nullable Intent intent, final int aliveTimeout) {

        final String captchaName = activity.getLocalClassName();
        Log.d(TAG, captchaName + ": " + IntentUtil.traceIntent(intent));
        if (CaptchaSupportHolder.get() != null) {
            CaptchaSupportHolder.get().destroy();
        }
        if (intent != null) {
            Log.d(TAG, "Child Captcha Id:" + intent.getIntExtra(CaptchaConstant.CAPTCHA_BACK_INFO, -1));
        }

        final boolean isCaptchaLaunchIntent = intent != null && CaptchaConstant.CAPTCHA_ACTION_LAUNCH.equals(intent.getAction());
        final boolean isCaptchaConfigIntent = intent != null && CaptchaConstant.CAPTCHA_ACTION_CONFIG.equals(intent.getAction());
        final boolean isCaptchaSolvedCallbackIntent = intent != null && intent.hasExtra(CAPTCHA_ORIGIN_INTENT);

        if (!isCaptchaLaunchIntent && !isCaptchaConfigIntent && !isCaptchaSolvedCallbackIntent ) {
            Log.w(TAG, captchaName + ": Creating FAKE PREVIEW captcha support since it is activity with no captcha launch action (" + CaptchaConstant.CAPTCHA_ACTION_LAUNCH + ")");
            final CaptchaSupport captchaSupport = new FallbackPreviewCaptchaSupport(activity, aliveTimeout);
            CaptchaSupportHolder.set(captchaSupport);
            Log.i(TAG, captchaName + ": Captcha support created (alive timeout:" + aliveTimeout + ")");
            return captchaSupport;
        }

        final Intent rootIntent = isCaptchaSolvedCallbackIntent ? (Intent) intent.getParcelableExtra(CAPTCHA_ORIGIN_INTENT) : intent;

        final BaseCaptchaSupport baseCaptchaSupport = new BaseCaptchaSupport(activity, rootIntent, aliveTimeout);
        baseCaptchaSupport.alive();

        CaptchaSupportHolder.set(baseCaptchaSupport);
        Log.i(TAG, captchaName + ": Captcha support created (alive timeout:" + aliveTimeout + ")");
        return baseCaptchaSupport;
    }


}
