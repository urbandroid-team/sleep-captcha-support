package com.urbandroid.sleep.captcha.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

public class IntentUtil {

    public static StringBuilder traceBundle(StringBuilder builder, Bundle b) {
        builder.append(" {EX:");
        if (b == null) {
            builder.append("null");
        } else {
            Set<String> keys = b.keySet();
            for (String key : keys) {
                builder.append(key).append(" ");
                Object value = b.get(key);
                if (value instanceof String) {
                    String stringValue = (String) value;
                    if (stringValue.length() > 0) {
                        builder.append("(").append(stringValue).append(") ");
                    }
                } else if (value instanceof Intent) {
                    builder.append("\n\tInner Intent:\n");
                    traceIntent(builder, (Intent) value);
                    builder.append("\n");
                } else {
                    String stringValue = value != null ? value.toString() : "NULL";
                    if (stringValue.length() > 0 && stringValue.length() < 100) {
                        builder.append("(").append(stringValue).append(") ");
                    }
                }
            }
        }
        builder.append("}");

        return builder;
    }

    public static StringBuilder traceIntent(StringBuilder builder, Intent i) {
        builder.append(" [I:");
        if (i == null) {
            builder.append("null");
        } else {
            if (i.getAction() != null) {
                builder.append("(action: ").append(i.getAction()).append(") ");
            }
            if (i.getComponent() != null) {
                builder.append("(class: ").append(i.getComponent().getClassName()).append(") ");
            }
            traceBundle(builder, i.getExtras());
        }
        builder.append("]");

        return builder;
    }

    @NonNull
    public static String traceIntent(final @Nullable Intent intent) {
        final StringBuilder builder = new StringBuilder();
        return traceIntent(builder, intent).toString();
    }

}
