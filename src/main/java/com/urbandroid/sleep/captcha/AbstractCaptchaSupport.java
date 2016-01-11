package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.annotation.SuppressAlarmMode;
import com.urbandroid.sleep.captcha.finder.BaseCaptchaFinder;
import com.urbandroid.sleep.captcha.finder.CaptchaFinder;
import com.urbandroid.sleep.captcha.launcher.BaseCaptchaLauncher;
import com.urbandroid.sleep.captcha.launcher.CaptchaLauncher;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCaptchaSupport implements CaptchaSupport {

    protected final @NonNull Activity activity;
    protected final @NonNull Context context;
    protected final @Nullable Intent intent;

    protected final @NonNull CaptchaFinder finder;
    protected final @NonNull CaptchaLauncher launcher;

    protected int aliveTimeoutInSeconds = DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
    private long lastAliveSent = -1;

    private RemainingTimeListener remainingTimeListener;
    private final Handler handler = new Handler();

    private final RemainingTimeRunnable remainingTimeRunnable = new RemainingTimeRunnable();

    protected AbstractCaptchaSupport(final @NonNull Activity activity, final @Nullable Intent intent) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.intent = intent;
        this.finder = new BaseCaptchaFinder(context);
        this.launcher = new BaseCaptchaLauncher(context, intent);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(CaptchaConstant.ALARM_SNOOZE_ACTION);
        filter.addAction(CaptchaConstant.ACTION_FINISH_CAPTCHA);
        context.registerReceiver(finishReceiver, filter);
    }

    @Override
    public CaptchaSupport setRemainingTimeListener(final @Nullable RemainingTimeListener remainingTimeListener) {
        if (!isOperationalMode()) {
            Log.w(TAG, "No operational mode: RemainingTimeListener will be not active");
            return this;
        }
        if (getSuppressAlarmMode() == SuppressAlarmMode.FULL_ALARM_VOLUME) {
            Log.w(TAG, "SuppressAlarmMode: Full Alarm Volume - RemainingTimeListener will be not active");
            return this;
        }
        this.remainingTimeListener = remainingTimeListener;
        handler.removeCallbacks(remainingTimeRunnable);
        if (remainingTimeListener != null) {
            handler.postDelayed(remainingTimeRunnable, 500);
        }
        return this;
    }

    @Override
    public CaptchaSupport aliveTimeout(
            final @IntRange(
                    from= MIN_ALIVE_TIMEOUT_IN_SECONDS,
                    to= MAX_ALIVE_TIMEOUT_IN_SECONDS
            ) int timeoutInSeconds)
    {
        if (aliveTimeoutInSeconds < MIN_ALIVE_TIMEOUT_IN_SECONDS || aliveTimeoutInSeconds > MAX_ALIVE_TIMEOUT_IN_SECONDS) {
            Log.w(TAG, "aliveTimeout out of range <" + MIN_ALIVE_TIMEOUT_IN_SECONDS + ", " + MAX_ALIVE_TIMEOUT_IN_SECONDS +">");
            return this;
        }
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

    @Override
    public CaptchaFinder getFinder() {
        return finder;
    }
    @Override
    public CaptchaLauncher getLauncher() {
        return launcher;
    }

    private class RemainingTimeRunnable implements Runnable {
        @Override
        public void run() {
            if (remainingTimeListener == null) {
                return;
            }
            remainingTimeListener.timeRemain(getRemainingTime(), aliveTimeoutInSeconds);
            handler.postDelayed(this, 500);
        }
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(finishReceiver);
        if (remainingTimeRunnable != null) {
            handler.removeCallbacks(remainingTimeRunnable);
        }
    }

    private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            activity.finish();
        }
    };

}
