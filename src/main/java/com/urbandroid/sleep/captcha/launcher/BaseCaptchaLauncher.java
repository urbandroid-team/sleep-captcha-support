package com.urbandroid.sleep.captcha.launcher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.urbandroid.sleep.captcha.RemoteCaptchaCallbackListener;
import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaEvent;
import com.urbandroid.sleep.captcha.annotation.SleepOperation;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_ALIVE;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_LAUNCH;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_SOLVED;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_BACK_INFO;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_DIFFICULTY;
import static com.urbandroid.sleep.captcha.CaptchaConstant.SLEEP_PACKAGE;

public class BaseCaptchaLauncher implements CaptchaLauncher {

    private final Context context;
    private final boolean isInsideSleep;

    public BaseCaptchaLauncher(final @NonNull Context context) {
        this.context = context;
        this.isInsideSleep = SLEEP_PACKAGE.equals(context.getPackageName());
    }

    @Override
    public void startCaptcha(final @NonNull CaptchaInfo captchaInfo, final @NonNull RemoteCaptchaCallbackListener callbackListener) {
        startCaptcha(captchaInfo, callbackListener, CaptchaDifficulty.VERY_SIMPLE);
    }

    @Override
    public void startCaptcha(final @NonNull CaptchaInfo captchaInfo, final @NonNull RemoteCaptchaCallbackListener callbackListener, final @CaptchaDifficulty int difficulty) {
        SolvedBroadcastReceiver.register(context, captchaInfo, callbackListener);
        context.startActivity(prepareCaptchaIntent(captchaInfo, difficulty, Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    @NonNull
    public Intent prepareCaptchaIntent(
            final @NonNull CaptchaInfo captchaInfo,
            final @CaptchaDifficulty int difficulty,
            final int flags)
    {
        final Intent solvedCaptchaIntent = new Intent(isInsideSleep ? CAPTCHA_ACTION_SOLVED: SolvedBroadcastReceiver.CAPTCHA_ACTION_NESTED_SOLVED)
                .putExtra(CAPTCHA_BACK_INFO, captchaInfo);

        final Intent captchaAliveIntent = new Intent(CAPTCHA_ACTION_ALIVE);

        return new Intent(CAPTCHA_ACTION_LAUNCH)
                .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                .setFlags(flags)
                // back call intents
                .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED, solvedCaptchaIntent)
                .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE, captchaAliveIntent)
                // config params
                .putExtra(CAPTCHA_CONFIG_DIFFICULTY, difficulty)
                .putExtra(SleepOperation.OPERATION_NONE, true);
    }


}
