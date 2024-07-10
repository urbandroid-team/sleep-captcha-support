package com.urbandroid.sleep.captcha;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_CONFIG;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_DIFFICULTY;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE;
import static com.urbandroid.sleep.captcha.CaptchaConstant.PREVIEW;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.annotation.CaptchaDifficulty;
import com.urbandroid.sleep.captcha.annotation.CaptchaMode;
import com.urbandroid.sleep.captcha.annotation.SleepOperation;
import com.urbandroid.sleep.captcha.annotation.SuppressAlarmMode;
import com.urbandroid.sleep.captcha.finder.BaseCaptchaFinder;
import com.urbandroid.sleep.captcha.finder.CaptchaFinder;
import com.urbandroid.sleep.captcha.launcher.BaseCaptchaLauncher;
import com.urbandroid.sleep.captcha.launcher.CaptchaLauncher;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCaptchaSupport implements CaptchaSupport {

    protected final @NonNull Activity activity;
    protected final @NonNull Context context;
    protected final @Nullable Intent intent;

    protected final @NonNull CaptchaFinder finder;
    protected final @NonNull CaptchaLauncher launcher;

    protected int aliveTimeoutInSeconds = DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
    private long lastAliveSent = -1;
    private final int currentCaptchaId;

    private final AtomicReference<RemainingTimeListener> remainingTimeListener = new AtomicReference<>();
    private final Handler handler = new Handler();

    private final RemainingTimeRunnable remainingTimeRunnable = new RemainingTimeRunnable();

    protected AbstractCaptchaSupport(final @NonNull Activity activity, final @Nullable Intent intent, int aliveTimeout) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.intent = intent;
        currentCaptchaId = intent != null ? intent.getIntExtra(CaptchaConstant.CAPTCHA_ID, 0): 0;
        this.finder = new BaseCaptchaFinder(context);
        final BaseCaptchaLauncher launcher = new BaseCaptchaLauncher(context, activity.getClass().getName(), intent, aliveTimeout);
        launcher
            .suppressAlarmMode(getSuppressAlarmMode())
            .difficulty(getDifficulty())
            .operation(getOperation());
            //.mode(getMode());
        launcher.parentMode(getParentMode());

        this.launcher = launcher;
        aliveTimeout(aliveTimeout);
        finishReceiver.register();
    }

    @Override
    public int getCurrentCaptchaId() {
        return currentCaptchaId;
    }

    @CaptchaMode
    public int getParentMode() {
        int mode = intent != null?
                intent.getIntExtra(CaptchaConstant.CAPTCHA_PARENT_MODE, 0): 0;
        if (mode == 0) {
            mode = getMode();
        }

        //noinspection ResourceType
        return mode;
    }

    @Override
    public boolean isPreviewMode(){
        return intent != null && intent.getBooleanExtra(PREVIEW, false);
    }

    @Override
    public boolean isConfigurationMode(){
        return intent != null && CAPTCHA_ACTION_CONFIG.equals(intent.getAction());
    }

    @Override
    public boolean isOperationalMode() {
        return !isPreviewMode() && !isConfigurationMode();
    }

    @Override
    @CaptchaMode
    public int getMode() {
        if (isPreviewMode()) {
            return CaptchaMode.CAPTCHA_MODE_PREVIEW;
        }
        if (isConfigurationMode()) {
            return CaptchaMode.CAPTCHA_MODE_CONFIGURATION;
        }
        return CaptchaMode.CAPTCHA_MODE_OPERATIONAL;
    }

    @Override
    @SuppressWarnings("ResourceType")
    @CaptchaDifficulty
    public int getDifficulty(){
        return intent != null ? intent.getIntExtra(CAPTCHA_CONFIG_DIFFICULTY, CaptchaDifficulty.VERY_SIMPLE): CaptchaDifficulty.VERY_SIMPLE;
    }

    @Override
    @SuppressWarnings("ResourceType")
    @SuppressAlarmMode
    public int getSuppressAlarmMode() {
        return intent != null ? intent.getIntExtra(CAPTCHA_CONFIG_SUPPRESS_ALARM_MODE, SuppressAlarmMode.FULL_ALARM_VOLUME): SuppressAlarmMode.FULL_ALARM_VOLUME;
    }

    protected boolean hasOperation(){
        return intent != null && !intent.hasExtra(SleepOperation.OPERATION_NONE);
    }

    @SleepOperation
    private String getOperation(){
        if (intent == null || intent.hasExtra(SleepOperation.OPERATION_NONE)) {
            return SleepOperation.OPERATION_NONE;
        }
        if (intent.hasExtra(SleepOperation.DELETE_ALARM)) {
            return SleepOperation.DELETE_ALARM;
        }
        if (intent.hasExtra(SleepOperation.DISABLE_ALARM)) {
            return SleepOperation.DISABLE_ALARM;
        }
        if (intent.hasExtra(SleepOperation.EDIT_ALARM)) {
            return SleepOperation.EDIT_ALARM;
        }
        if (intent.hasExtra(SleepOperation.EDIT_ALARM_TIME_EXTRA)) {
            return SleepOperation.EDIT_ALARM_TIME_EXTRA;
        }
        if (intent.hasExtra(SleepOperation.SHOULD_SKIP)) {
            return SleepOperation.SHOULD_SKIP;
        }
        if (intent.hasExtra(SleepOperation.SNOOZE_CANCELED)) {
            return SleepOperation.SNOOZE_CANCELED;
        }
        return SleepOperation.OPERATION_NONE;
    }

    @Override
    public CaptchaSupport setRemainingTimeListener(final @Nullable RemainingTimeListener remainingTimeListener) {
        if (hasOperation()) {
            Log.w(TAG, "Sleep operation set: RemainingTimeListener will be not active");
            return this;
        }
        if (!isOperationalMode()) {
            Log.w(TAG, "No operational mode: RemainingTimeListener will be not active");
            return this;
        }
        if (getParentMode() == CaptchaMode.CAPTCHA_MODE_PREVIEW) {
            Log.w(TAG, "Parent Captcha mode is preview: RemainingTimeListener will be not active");
            return this;
        }
        if (getSuppressAlarmMode() == SuppressAlarmMode.FULL_ALARM_VOLUME) {
            Log.w(TAG, "SuppressAlarmMode: Full Alarm Volume - RemainingTimeListener will be not active");
            return this;
        }
        this.remainingTimeListener.set(remainingTimeListener);
        handler.removeCallbacks(remainingTimeRunnable);
        if (remainingTimeListener != null) {
            handler.postDelayed(remainingTimeRunnable, 500);
        }
        return this;
    }

    @Override
    public CaptchaSupport aliveTimeout(final int timeoutInSeconds) {
        if (timeoutInSeconds < MIN_ALIVE_TIMEOUT_IN_SECONDS || timeoutInSeconds > MAX_ALIVE_TIMEOUT_IN_SECONDS) {
            Log.w(TAG, "aliveTimeout out of range <" + MIN_ALIVE_TIMEOUT_IN_SECONDS + ", " + MAX_ALIVE_TIMEOUT_IN_SECONDS +">");
            return this;
        }
        Log.d(TAG, "aliveTimeout set: " + timeoutInSeconds);
        this.aliveTimeoutInSeconds = timeoutInSeconds;
        return this;
    }

    @Override
    public int getRemainingTime() {
        if (lastAliveSent == -1) {
            return MIN_ALIVE_TIMEOUT_IN_SECONDS;
        }

        final long seconds = TimeUnit.MILLISECONDS.toSeconds((lastAliveSent + aliveTimeoutInSeconds * 1000) - System.currentTimeMillis());
        if (seconds < 0) {
            return 0;
        }

        return (int) seconds;
    }

    @Override
    public final void alive() {
        doAlive();
        lastAliveSent = System.currentTimeMillis();
    }

    protected abstract void doAlive();

    @NonNull
    @Override
    public CaptchaFinder getFinder() {
        return finder;
    }

    @NonNull
    @Override
    public CaptchaLauncher getLauncher() {
        return launcher;
    }

    private class RemainingTimeRunnable implements Runnable {
        @Override
        public void run() {
            final RemainingTimeListener remainingTimeListener = AbstractCaptchaSupport.this.remainingTimeListener.get();
            if (remainingTimeListener == null) {
                return;
            }
            remainingTimeListener.timeRemain(getRemainingTime(), aliveTimeoutInSeconds);
            handler.postDelayed(this, 500);
        }
    }

    @Override
    public void destroy() {
        finishReceiver.unregister();
        if (remainingTimeRunnable != null) {
            handler.removeCallbacks(remainingTimeRunnable);
            remainingTimeListener.set(null);
        }
    }

    @Override
    public int getAliveTimeout() {
        return aliveTimeoutInSeconds;
    }

    private final FinishReceiver finishReceiver = new FinishReceiver();

    private class FinishReceiver extends BroadcastReceiver {

        private final AtomicBoolean isRegistered = new AtomicBoolean(false);

        @Override
        public void onReceive(Context context, Intent intent) {
            activity.finish();
        }

        @SuppressLint("UnspecifiedRegisterReceiverFlag")
        public void register(){

            if (isRegistered.get()){
                return;
            }

            final IntentFilter filter = new IntentFilter();
            filter.addAction(CaptchaConstant.ALARM_SNOOZE_ACTION);
            filter.addAction(CaptchaConstant.ACTION_FINISH_CAPTCHA);
            isRegistered.set(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(this, filter, Context.RECEIVER_EXPORTED);
            } else {
                context.registerReceiver(this, filter);
            }

        }

        public void unregister(){
            if (!isRegistered.get()){
                return;
            }
            context.unregisterReceiver(this);
            isRegistered.set(false);
        }
    };

}
