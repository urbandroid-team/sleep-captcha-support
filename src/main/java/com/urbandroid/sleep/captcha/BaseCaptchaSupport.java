package com.urbandroid.sleep.captcha;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.annotation.CaptchaEvent;
import com.urbandroid.sleep.captcha.intent.IntentExtraSetter;

public class BaseCaptchaSupport extends AbstractCaptchaSupport {

    protected BaseCaptchaSupport(final @NonNull Activity activity, final @NonNull Intent intent, final int aliveTimeout) {
        super(activity, intent, aliveTimeout);
    }

    @Override
    protected void doAlive() {
        send(CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE, new IntentExtraSetter() {
            @Override
            public void setExtras(final @NonNull Intent intent) {
                if (intent.getPackage() == null && intent.getComponent() == null) {
                    intent.setPackage(activity.getPackageName());
                }
                intent.putExtra(CaptchaConstant.TIME_ADD, aliveTimeoutInSeconds);
            }
        });
    }

    @Override
    public void solved(){
        solved(null);
    }

    @Override
    public void solved(final @Nullable IntentExtraSetter extraSetter){
        send(CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED, extraSetter);
    }

    @Override
    public void unsolved() {
        send(CaptchaEvent.CAPTCHA_BACK_INTENT_UNSOLVED, null);
    }

    void send(final @CaptchaEvent String event, final @Nullable IntentExtraSetter extraSetter){
        if (!isOperationalMode()) {
            return;
        }

        final Intent callbackIntent = intent != null ? (Intent) intent.getParcelableExtra(event) : null;
        Log.i(TAG, "Calling " + event + (callbackIntent != null ? ": " + callbackIntent + " package: " + callbackIntent.getPackage() + " component:" + callbackIntent.getComponent(): ""));
        if (callbackIntent == null) {
            return;
        }
        if (extraSetter != null) {
            extraSetter.setExtras(callbackIntent);
        }

        switch (event) {
            case CaptchaEvent.CAPTCHA_BACK_INTENT_ALIVE:
                context.sendBroadcast(callbackIntent);
                break;
            case CaptchaEvent.CAPTCHA_BACK_INTENT_UNSOLVED:
                if (!hasOperation()) {
                    callbackIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    callbackIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(callbackIntent);
                }
                break;
            case CaptchaEvent.CAPTCHA_BACK_INTENT_SOLVED:
                final boolean nestedCaptcha = intent.getIntExtra(CaptchaConstant.CAPTCHA_PARENT_ID, 0) != 0;
                if (hasOperation() && !nestedCaptcha) {
                    context.sendBroadcast(callbackIntent);
                } else {

                    try {
                        callbackIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        callbackIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        callbackIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        callbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.urbandroid.sleep.captchapack");
                        Log.i(TAG, "Starting CAPTCHA 0 " + event + (callbackIntent != null ? ": " + callbackIntent + " package: " + callbackIntent.getPackage() + " component:" + callbackIntent.getComponent(): ""));
                        activity.startActivity(callbackIntent);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to start CAPTCHA 0 " + e.getMessage());
                        Intent copyIntent = new Intent(callbackIntent.getAction());
                        try {
                            copyIntent.addFlags(callbackIntent.getFlags());
                            copyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            copyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            copyIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                            copyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            copyIntent.setComponent(callbackIntent.getComponent());
                            copyIntent.setPackage("com.urbandroid.sleep.captchapack");
                            copyIntent.putExtras(callbackIntent.getExtras());

                            Log.i(TAG, "Starting CAPTCHA 1 " + event + (copyIntent != null ? ": " + copyIntent + " package: " + copyIntent.getPackage() + " component:" + copyIntent.getComponent(): ""));
                            activity.startActivity(copyIntent);

                        } catch (Exception e2) {
                            Log.e(TAG, "Failed to start CAPTCHA 1 " + e2.getMessage());

                            copyIntent = new Intent(callbackIntent.getAction());
                            try {
                                copyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                copyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                copyIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                copyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                copyIntent.putExtras(callbackIntent.getExtras());
                                copyIntent.setPackage("com.urbandroid.sleep.captchapack");
                                copyIntent.setClassName("com.urbandroid.sleep.captchapack", "com.urbandroid.sleep.captchapack.multi.InOrderCaptcha");
                                Log.i(TAG, "Starting CAPTCHA 2 " + event + (copyIntent != null ? ": " + copyIntent + " package: " + copyIntent.getPackage() + " component:" + copyIntent.getComponent(): ""));
                                activity.startActivity(copyIntent);
                            } catch (Exception ex) {
                                Log.e(TAG, "Failed to start CAPTCHA 2 " + ex.getMessage());

                                copyIntent = new Intent();
                                try {
                                    copyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    copyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    copyIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                    copyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    copyIntent.putExtras(callbackIntent.getExtras());
                                    copyIntent.setPackage("com.urbandroid.sleep.captchapack");
                                    copyIntent.setClassName("com.urbandroid.sleep.captchapack", "com.urbandroid.sleep.captchapack.multi.InOrderCaptcha");
                                    Log.i(TAG, "Starting CAPTCHA 3 " + event + (copyIntent != null ? ": " + copyIntent + " package: " + copyIntent.getPackage() + " component:" + copyIntent.getComponent(): ""));
                                    activity.startActivity(copyIntent);
                                } catch (Exception ex2) {
                                    Log.e(TAG, "Failed to start CAPTCHA 3 " + ex2.getMessage());
                                }


                            }

                        }

                    }


                }
                break;
        }
    }

}
