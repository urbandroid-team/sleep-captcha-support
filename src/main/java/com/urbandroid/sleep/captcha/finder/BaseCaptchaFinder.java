package com.urbandroid.sleep.captcha.finder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.urbandroid.sleep.captcha.domain.BaseCaptchaGroup;
import com.urbandroid.sleep.captcha.domain.BaseCaptchaInfo;
import com.urbandroid.sleep.captcha.domain.CaptchaGroup;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.finder.filter.IdCaptchaInfoFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_CONFIG;
import static com.urbandroid.sleep.captcha.CaptchaConstant.CAPTCHA_ACTION_LAUNCH;
import static com.urbandroid.sleep.captcha.CaptchaConstant.TAG;

public class BaseCaptchaFinder implements CaptchaFinder {

    private final Context context;

    public BaseCaptchaFinder(final @NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public List<CaptchaGroup> findGroups(final @Nullable CaptchaInfoFilter filter) {
        final PackageManager packageManager = context.getPackageManager();
        final Map<String, CaptchaGroup> groups = new HashMap<>();
        final List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for(final CaptchaInfo captcha: lookup(filter)) {
            final CaptchaGroup group = findGroup(packageManager, groups, apps, captcha.getPackageName());
            if (group != null) {
                group.add(captcha);
            }
        }
        return new ArrayList<>(groups.values());
    }

    private CaptchaGroup findGroup(
            final @NonNull PackageManager packageManager,
            final @Nullable Map<String, CaptchaGroup> groups,
            final @NonNull List<ApplicationInfo> apps,
            final @NonNull String packageName)
    {
        final CaptchaGroup captchaGroup = groups != null? groups.get(packageName): null;
        if (captchaGroup != null) {
            return captchaGroup;
        }
        for (final ApplicationInfo app: apps) {
            if (packageName.equals(app.packageName)) {
                boolean isExternalStorage = (app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
                final CharSequence label = app.loadLabel(packageManager);
                final BaseCaptchaGroup group = new BaseCaptchaGroup(packageName, label == null ? packageName : label.toString(), isExternalStorage);
                if (groups != null && !groups.containsKey(packageName)) {
                    groups.put(packageName, group);
                }
                return group;
            }
        }
        return null;
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
        final List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // look up all activities with action captcha launch in intent-filter
        final Intent launchCaptchaIntent = new Intent(CAPTCHA_ACTION_LAUNCH);
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(launchCaptchaIntent, PackageManager.GET_META_DATA)) {
            //Log.d(TAG, "Found: " + resolveInfo);

            final BaseCaptchaInfo captchaInfo = BaseCaptchaInfo.build(
                    context,
                    resolveInfo.activityInfo,
                    resolveInfo.activityInfo.loadLabel(packageManager).toString()
            );
            final CaptchaGroup group = findGroup(packageManager, null, apps, captchaInfo.getPackageName());

            //Log.d(TAG, "\tCaptcha: " + captchaInfo);
            if (filter == null || filter.apply(group, captchaInfo)) {
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
            BaseCaptchaInfo captchaInfo = findById(result, configInfo.getId());
            if (captchaInfo == null) {
                captchaInfo = findByName(result, configInfo.getForCaptcha(), configInfo.getPackageName());
            }
            if (captchaInfo != null) {
                captchaInfo.setConfigActivityName(configInfo.getActivityName());
            }
        }

        Collections.sort(result, CaptchaInfo.ORDER_COMPARATOR);

        if (!result.isEmpty()) {
            Log.d(TAG, "Found Captchas:" );
            for (final CaptchaInfo captchaInfo : result) {
                if (!captchaInfo.getPackageName().equals(context.getPackageName())) {
                    Log.d(TAG, captchaInfo.toString());
                }
            }
        } else {
            Log.w(TAG, "No captcha found");
        }


        return result;
    }

    @Nullable
    public CaptchaInfo findById(final int id) {
        final List<CaptchaInfo> list = lookup(new IdCaptchaInfoFilter(id));
        return list.isEmpty() ? null: list.get(0);
    }

    @Nullable
    protected BaseCaptchaInfo findById(final List<CaptchaInfo> infos, final int id) {
        for (final CaptchaInfo info : infos) {
            if (info.getId() == id) {
                return (BaseCaptchaInfo) info;
            }
        }
        return null;
    }
    @Nullable
    protected BaseCaptchaInfo findByName(final List<CaptchaInfo> infos, final @NonNull String activityName, final @NonNull String packageName) {

        //Log.i(TAG, "Looking for: " + activityName + " " + packageName);
        for (final CaptchaInfo info : infos) {
            if (info.getPackageName().equals(packageName)) {
                if (info.getActivityName().equals(activityName) || info.getActivityName().equals(packageName + activityName)) {
                    return (BaseCaptchaInfo) info;
                }
            }
        }
        return null;
    }
}

