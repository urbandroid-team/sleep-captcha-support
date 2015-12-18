package com.urbandroid.sleep.captcha.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.urbandroid.sleep.captcha.CaptchaConstant;
import com.urbandroid.sleep.captcha.RemoteCaptchaCallbackListener;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

import java.util.concurrent.atomic.AtomicReference;

public class SolvedBroadcastReceiver extends BroadcastReceiver {

    public static final String CAPTCHA_ACTION_NESTED_SOLVED = "com.urbandroid.sleep.captcha.intent.action.NESTED_SOLVED";

    private RemoteCaptchaCallbackListener listener;
    private CaptchaInfo captchaInfo;

    @Override
    public void onReceive(final @NonNull Context context, final @NonNull Intent intent) {

        final boolean success = intent.getBooleanExtra(CaptchaConstant.SUCCESS, false);
        if (success) {
            listener.solved(captchaInfo);
        } else {
            listener.unsolved(captchaInfo);
        }

        unregister(context);
    }

    private static final AtomicReference<SolvedBroadcastReceiver> registerReceiver = new AtomicReference<>();

    public static void register(
            final @NonNull Context context, final @NonNull CaptchaInfo captchaInfo,
            final @NonNull RemoteCaptchaCallbackListener listener)
    {
        if (registerReceiver.get() != null) {
            Log.w(CaptchaConstant.TAG, "Already registered");
            return;
        }

        final SolvedBroadcastReceiver receiver = new SolvedBroadcastReceiver();
        receiver.listener = listener;
        receiver.captchaInfo = captchaInfo;

        context.registerReceiver(receiver, new IntentFilter(CAPTCHA_ACTION_NESTED_SOLVED));
        registerReceiver.set(receiver);
        Log.i(CaptchaConstant.TAG, "RemoteCaptchaCallbackListener registered for " + captchaInfo);

    }

    public static void unregister(final @NonNull Context context){
        if (registerReceiver.get() == null) {
            return;
        }

        final SolvedBroadcastReceiver receiver = registerReceiver.get();
        context.unregisterReceiver(registerReceiver.get());
        registerReceiver.set(null);

        Log.i(CaptchaConstant.TAG, "RemoteCaptchaCallbackListener unregistered for " + receiver.captchaInfo);
    }

}
