package com.urbandroid.sleep.captcha.finder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CachedCaptchaFinder extends BaseCaptchaFinder {

    private final Map<Intent, Record> cache = new HashMap<>();
    private long lastUpdated = -1;
    private final static long EXPIRATION = TimeUnit.MINUTES.toMillis(3);
    private List<ApplicationInfo> cachedApps;
    private long expirationInMillis;

    public CachedCaptchaFinder(@NonNull Context context) {
        this(context, EXPIRATION);
    }

    public CachedCaptchaFinder(@NonNull Context context, long expirationInMillis) {
        super(context);
        this.expirationInMillis = expirationInMillis;
    }

    @NonNull
    @Override
    protected List<ApplicationInfo> getInstalledApps(PackageManager packageManager) {
        final long now = System.currentTimeMillis();
        if (cachedApps == null || now - lastUpdated > expirationInMillis) {
            lastUpdated = now;
            cachedApps = super.getInstalledApps(packageManager);
        }

        return cachedApps;
    }

    @NonNull
    @Override
    protected List<ResolveInfo> queryIntent(PackageManager packageManager, Intent intent) {
        final long now = System.currentTimeMillis();
        Record record = cache.get(intent);
        if (record == null || now - record.lastUpdated > expirationInMillis) {
            record = new Record(now, super.queryIntent(packageManager, intent));
            cache.put(intent, record);
        }
        return record.infos;
    }

    private class Record {

        public Record(long lastUpdated, List<ResolveInfo> infos) {
            this.lastUpdated = lastUpdated;
            this.infos = infos;
        }

        long lastUpdated;
        List<ResolveInfo> infos;

    }
}
