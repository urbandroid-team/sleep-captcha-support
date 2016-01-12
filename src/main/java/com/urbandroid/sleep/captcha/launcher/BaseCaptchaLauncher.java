package com.urbandroid.sleep.captcha.launcher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.CaptchaConstant;
import com.urbandroid.sleep.captcha.CaptchaSupportException;
import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaEvent;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.annotation.SleepOperation;
import com.urbandroid.sleep.captcha.annotation.SuppressAlarmMode;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.intent.CallbackIntentCreator;
import com.urbandroid.sleep.captcha.util.IntentUtil;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_LAUNCH;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_SOLVED;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_BACK_INFO;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_DIFFICULTY;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ORIGIN_INTENT;
import static com.urbandroid.sleep.captcha.CaptchaConstant.SUCCESS;
import static com.urbandroid.sleep.captcha.CaptchaConstant.TAG;

public class BaseCaptchaLauncher implements CaptchaLauncher {

    protected final Context context;
    protected final Intent originIntent;
    protected final String captchaClassName;

    protected @CaptchaDifficulty int difficulty = CaptchaDifficulty.VERY_SIMPLE;
    protected @SuppressAlarmMode int suppressAlarmMode = SuppressAlarmMode.FULL_ALARM_VOLUME;
    protected @CaptchaMode int mode = CaptchaMode.CAPTCHA_MODE_OPERATIONAL;
    protected @SleepOperation String operation = SleepOperation.OPERATION_NONE;
    protected CallbackIntentCreator callbackIntentCreator;
    protected int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP;

    public BaseCaptchaLauncher(final @NonNull Context context, final String captchaClassName, final @Nullable Intent originIntent) {
        this.context = context;
        this.captchaClassName = captchaClassName;
        this.originIntent = originIntent;
    }

    @Override
    public CaptchaLauncher difficulty(final @CaptchaDifficulty int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    @Override
    public CaptchaLauncher suppressAlarmMode(@SuppressAlarmMode int suppressAlarmMode) {
        this.suppressAlarmMode = suppressAlarmMode;
        return this;
    }

    @Override
    public CaptchaLauncher operation(final @SleepOperation String operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public CaptchaLauncher callbackIntentCreator(final @NonNull CallbackIntentCreator callbackIntentCreator) {
        this.callbackIntentCreator = callbackIntentCreator;
        return this;
    }

    @Override
    public CaptchaLauncher addFlags(final int flags) {
        this.flags |= flags;
        return this;
    }

    @Override
    public CaptchaLauncher mode(@CaptchaMode int mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public void start(final @NonNull CaptchaInfo captchaInfo) {
        Log.i(TAG, "Starting captcha mode: " + mode + " difficulty: " + difficulty + " operation: " + operation + " " + captchaInfo);

        final Intent intent = prepareIntent(captchaInfo);
        final StringBuilder sb = new StringBuilder();
        IntentUtil.traceIntent(sb, intent);
        Log.d(TAG, sb.toString());

        context.startActivity(intent);
    }

    @NonNull
    @Override
    public Intent prepareIntent(final @NonNull CaptchaInfo captchaInfo) {

        switch (mode) {
            case CaptchaMode.CAPTCHA_MODE_CONFIGURATION:
                return new Intent(CaptchaConstant.CAPTCHA_ACTION_CONFIG)
                        .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE, suppressAlarmMode)
                        .putExtra(CAPTCHA_CONFIG_DIFFICULTY, difficulty);

            case CaptchaMode.CAPTCHA_MODE_PREVIEW:
                return new Intent(CaptchaConstant.CAPTCHA_ACTION_LAUNCH)
                        .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(CAPTCHA_CONFIG_DIFFICULTY, difficulty)
                        .putExtra(CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE, suppressAlarmMode)
                        .putExtra(CaptchaConstant.PREVIEW, true);

            case CaptchaMode.CAPTCHA_MODE_OPERATIONAL:
                final Intent solvedCaptchaIntent =
                        callbackIntentCreator == null ?
                                new Intent(CAPTCHA_ACTION_SOLVED)
                                        .putExtra(CAPTCHA_ORIGIN_INTENT, originIntent)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .setClassName(context.getPackageName(), captchaClassName)
                                :
                                callbackIntentCreator.createSolvedIntent(context, operation);
                solvedCaptchaIntent.putExtra(operation, true);
                solvedCaptchaIntent.putExtra(CAPTCHA_BACK_INFO, captchaInfo);

                final Intent unsolvedCaptchaIntent =
                        callbackIntentCreator == null ?
                                new Intent(CAPTCHA_ACTION_SOLVED)
                                        .putExtra(CAPTCHA_ORIGIN_INTENT, originIntent)
                                        .putExtra(SUCCESS, false)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .setClassName(context.getPackageName(), captchaClassName)
                                :
                                callbackIntentCreator.createUnsolvedIntent(context, operation);
                unsolvedCaptchaIntent.putExtra(CAPTCHA_BACK_INFO, captchaInfo);

                final Intent captchaAliveIntent = new Intent(CaptchaConstant.CAPTCHA_ACTION_ALIVE);

                return new Intent(CAPTCHA_ACTION_LAUNCH)
                        .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                        .setFlags(flags)
                        // back call intents
                        .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED, solvedCaptchaIntent)
                        .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_UNSOLVED, unsolvedCaptchaIntent)
                        .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE, captchaAliveIntent)
                        // config params
                        .putExtra(CAPTCHA_CONFIG_DIFFICULTY, difficulty)
                        .putExtra(CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE, suppressAlarmMode)
                        .putExtra(operation, true);
        }

        throw new CaptchaSupportException("Unknown captcha mode: " + mode);

    }

}
