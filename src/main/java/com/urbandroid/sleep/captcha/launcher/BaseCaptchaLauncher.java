package com.urbandroid.sleep.captcha.launcher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.urbandroid.sleep.captcha.CaptchaConstant;
import com.urbandroid.sleep.captcha.CaptchaSupportException;
import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaEvent;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.annotation.SleepOperation;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.intent.SolvedCallbackIntentCreator;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_ALIVE;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_LAUNCH;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_SOLVED;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_BACK_INFO;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_DIFFICULTY;

public class BaseCaptchaLauncher implements CaptchaLauncher {

    private final Context context;

    private @CaptchaDifficulty int difficulty = CaptchaDifficulty.VERY_SIMPLE;
    private @CaptchaMode int mode = CaptchaMode.CAPTCHA_MODE_OPERATIONAL;
    private @SleepOperation String operation = SleepOperation.OPERATION_NONE;
    private SolvedCallbackIntentCreator solvedCallbackIntentCreator;
    private int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP;

    public BaseCaptchaLauncher(final @NonNull Context context) {
        this.context = context;
    }

    @Override
    public CaptchaLauncher difficulty(final @CaptchaDifficulty int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    @Override
    public CaptchaLauncher operation(final @SleepOperation String operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public CaptchaLauncher solvedCallbackIntentCreator(final @NonNull SolvedCallbackIntentCreator solvedCallbackIntentCreator) {
        this.solvedCallbackIntentCreator = solvedCallbackIntentCreator;
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
        context.startActivity(prepareIntent(captchaInfo));
    }

    @NonNull
    @Override
    public Intent prepareIntent(final @NonNull CaptchaInfo captchaInfo) {

        switch (mode) {
            case CaptchaMode.CAPTCHA_MODE_CONFIGURATION:
                return new Intent(CaptchaConstant.CAPTCHA_ACTION_CONFIG)
                        .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(CaptchaConstant.CAPTCHA_CONFIG_DIFFICULTY, difficulty);

            case CaptchaMode.CAPTCHA_MODE_PREVIEW:
                return new Intent(CaptchaConstant.CAPTCHA_ACTION_LAUNCH)
                        .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(CaptchaConstant.CAPTCHA_CONFIG_DIFFICULTY, difficulty)
                        .putExtra(CaptchaConstant.PREVIEW, true);

            case CaptchaMode.CAPTCHA_MODE_OPERATIONAL:
                final Intent solvedCaptchaIntent =
                        solvedCallbackIntentCreator == null ?
                                new Intent(CAPTCHA_ACTION_SOLVED)
                                        .putExtra(CAPTCHA_BACK_INFO, captchaInfo)
                                        .setPackage(context.getPackageName())
                                :
                                solvedCallbackIntentCreator.createSolvedIntent(context);

                final Intent unsolvedCaptchaIntent =
                        solvedCallbackIntentCreator == null ?
                                new Intent(CAPTCHA_ACTION_SOLVED)
                                        .putExtra(CAPTCHA_BACK_INFO, captchaInfo)
                                        .setPackage(context.getPackageName())
                                :
                                solvedCallbackIntentCreator.createUnsolvedIntent(context);

                final Intent captchaAliveIntent = new Intent(CAPTCHA_ACTION_ALIVE);

                return new Intent(CAPTCHA_ACTION_LAUNCH)
                        .setClassName(captchaInfo.getPackageName(), captchaInfo.getActivityName())
                        .setFlags(flags)
                        // back call intents
                        .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED, solvedCaptchaIntent)
                        .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_UNSOLVED, unsolvedCaptchaIntent)
                        .putExtra(CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE, captchaAliveIntent)
                        // config params
                        .putExtra(CAPTCHA_CONFIG_DIFFICULTY, difficulty)
                        .putExtra(operation, true);
        }

        throw new CaptchaSupportException("Unknown captcha mode: " + mode);

    }

}
