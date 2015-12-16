package com.urbandroid.sleep.captcha.finder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.domain.BaseCaptchaInfo;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_CONFIG;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_LAUNCH;
import static com.urbandroid.sleep.captcha.CaptchaConstant.TAG;

public class BaseCaptchaFinder implements CaptchaFinder {

    private final Context context;

    public BaseCaptchaFinder(final @NonNull Context context) {
        this.context = context;
    }

    @Override
    @NonNull
    public List<CaptchaInfo> lookup() {
        return lookup(null);
    }

    @NonNull
    public List<CaptchaInfo> lookup(final @Nullable CaptchaInfoFilter filter) {
        final List<CaptchaInfo> result = new ArrayList<>();
        final PackageManager packageManager = context.getPackageManager();

        // look up all activities with action captcha launch in intent-filter
        final Intent launchCaptchaIntent = new Intent(CAPTCHA_ACTION_LAUNCH);
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(launchCaptchaIntent, PackageManager.GET_META_DATA)) {
            final BaseCaptchaInfo captchaInfo = BaseCaptchaInfo.build(
                    context,
                    resolveInfo.activityInfo,
                    resolveInfo.activityInfo.loadLabel(packageManager).toString()
            );
            if (filter == null || filter.apply(captchaInfo)) {
                result.add(captchaInfo);
            }
        }

        // look up all activities with action captcha config in intent-filter
        final Intent configCaptchaIntent = new Intent(CAPTCHA_ACTION_CONFIG);
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(configCaptchaIntent, PackageManager.GET_META_DATA)) {
            final BaseCaptchaInfo configInfo = BaseCaptchaInfo.build(
                    context,
                    resolveInfo.activityInfo,
                    resolveInfo.activityInfo.loadLabel(packageManager).toString()
            );
            final CaptchaInfo captchaInfo = findById(result, configInfo.getId());
            if (captchaInfo != null) {
                captchaInfo.setConfigurable(true);
            }
        }

        Collections.sort(result, new Comparator<CaptchaInfo>() {
            @Override
            public int compare(final CaptchaInfo info1, final CaptchaInfo info2) {
                return Integer.valueOf(info1.getOrder()).compareTo(info2.getOrder());
            }
        });

        for (final CaptchaInfo captchaInfo : result) {
            Log.i(TAG, captchaInfo.toString());
        }

        return result;
    }

    @Nullable
    public CaptchaInfo findById(final int id) {
        final List<CaptchaInfo> list = lookup(new IdCaptchaInfoFilter(id));
        return list.isEmpty() ? null: list.get(0);
    }

    @Nullable
    protected CaptchaInfo findById(final List<CaptchaInfo> infos, final int id) {
        for (final CaptchaInfo info : infos) {
            if (info.getId() == id) {
                return info;
            }
        }
        return null;
    }
}

