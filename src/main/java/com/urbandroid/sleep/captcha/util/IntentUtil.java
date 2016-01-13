package com.urbandroid.sleep.captcha.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.CaptchaSupport;

import java.util.Set;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_CONFIG_ALIVE_TIMEOUT;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ORIGIN_INTENT;

public class IntentUtil {

    public static StringBuilder traceBundle(StringBuilder builder, Bundle b) {
        return traceBundle(builder, b, "");
    }

    public static StringBuilder traceBundle(StringBuilder builder, Bundle b, String tabs) {
        if (b == null) {
            return builder;
        }
        Set<String> keys = b.keySet();
        for (String key : keys) {
            builder.append("\n" + tabs + "\t\t" + key).append("=");
            Object value = b.get(key);
            if (value instanceof String) {
                String stringValue = (String) value;
                if (stringValue.length() > 0) {
                    builder.append("\"").append(stringValue).append("\" ");
                }
            } else if (value instanceof Boolean) {
                builder.append(value);
            } else if (value instanceof Number) {
                builder.append(value);
            } else if (value instanceof Intent) {
                traceIntent(builder, (Intent) value, tabs + "\t\t\t");
            } else {
                String stringValue = value != null ? value.toString() : "NULL";
                if (stringValue.length() > 0 && stringValue.length() < 100) {
                    builder.append("(").append(stringValue).append(") ");
                }
            }
        }

        return builder;
    }

    public static StringBuilder traceIntent(StringBuilder builder, Intent i) {
        return traceIntent(builder, i, "");
    }

    public static StringBuilder traceIntent(StringBuilder builder, Intent i, String tabs) {
        builder.append("\n" + tabs + "I: ");
        if (i == null) {
            builder.append("null");
        } else {
            if (i.getAction() != null) {
                builder.append("action: ").append(i.getAction()).append(" ");
            }
            if (i.getComponent() != null) {
                builder.append("class: ").append(i.getComponent().getClassName());
            }
            traceBundle(builder, i.getExtras(), tabs);
        }

        return builder;
    }

    @NonNull
    public static String traceIntent(final @Nullable Intent intent) {
        final StringBuilder builder = new StringBuilder();
        traceIntent(builder, intent);
        return builder.toString();
    }


    public static int resolveTimeout(final @Nullable Intent intent){
        final boolean isCaptchaSolvedCallbackIntent = intent != null && intent.hasExtra(CAPTCHA_ORIGIN_INTENT);
        final Intent rootIntent = isCaptchaSolvedCallbackIntent ? (Intent) intent.getParcelableExtra(CAPTCHA_ORIGIN_INTENT) : intent;

        // try if intent contains timeout
        if (rootIntent != null && rootIntent.hasExtra(CAPTCHA_CONFIG_ALIVE_TIMEOUT)) {
                int aliveTimeout = rootIntent.getIntExtra(CAPTCHA_CONFIG_ALIVE_TIMEOUT, -1);
                if (aliveTimeout != -1) {
                    return aliveTimeout;
                }
            }
        return CaptchaSupport.DEFAULT_ALIVE_TIMEOUT_IN_SECONDS;
    }

}
