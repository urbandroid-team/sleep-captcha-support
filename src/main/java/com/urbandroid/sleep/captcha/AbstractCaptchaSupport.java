package com.urbandroid.sleep.captcha;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.finder.BaseCaptchaFinder;
import com.urbandroid.sleep.captcha.finder.CaptchaFinder;
import com.urbandroid.sleep.captcha.launcher.BaseCaptchaLauncher;
import com.urbandroid.sleep.captcha.launcher.CaptchaLauncher;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCaptchaSupport implements CaptchaSupport {

    protected final Context context;
    protected final Intent intent;

    protected final CaptchaFinder finder;
    protected final CaptchaLauncher launcher;

    protected int aliveTimeoutInSeconds = DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
    private long lastAliveSent = -1;

    private RemainingTimeListener remainingTimeListener;
    private final Handler handler = new Handler();

    private final RemainingTimeRunnable remainingTimeRunnable = new RemainingTimeRunnable();

    protected AbstractCaptchaSupport(final @NonNull Context context, final @Nullable Intent intent) {
        this.context = context;
        this.intent = intent;
        this.finder = new BaseCaptchaFinder(context);
        this.launcher = new BaseCaptchaLauncher(context, intent);
    }

    @Override
    public CaptchaSupport setRemainingTimeListener(final @Nullable RemainingTimeListener remainingTimeListener) {
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


}
