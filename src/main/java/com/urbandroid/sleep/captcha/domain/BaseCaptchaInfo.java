package com.urbandroid.sleep.captcha.domain;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.CaptchaConstant;

import static com.urbandroid.sleep.captcha.CaptchaConstant.META_BACK_COMPATIBILITY_ID;
import static com.urbandroid.sleep.captcha.CaptchaConstant.META_FOR_CAPTCHA;
import static com.urbandroid.sleep.captcha.CaptchaConstant.META_HAS_DIFFICULTY;
import static com.urbandroid.sleep.captcha.CaptchaConstant.META_ORDER;

public class BaseCaptchaInfo implements CaptchaInfo {

    private final int id;
    private final int order;
    private final String packageName;
    private final String activityName;
    private final String label;
    private final boolean isExternal;
    private final boolean hasDifficulty;
    private String configActivityName;

    private transient String forCaptcha;

    private BaseCaptchaInfo(final Context context, final @NonNull ActivityInfo activityInfo, final @NonNull String label) {
        this.packageName = activityInfo.packageName;
        this.activityName = activityInfo.name;
        this.label = label;
        this.isExternal = !context.getPackageName().equals(packageName);
        final boolean isFromSleep = CaptchaConstant.SLEEP_PACKAGE.equals(packageName);
        if ((isExternal && !isFromSleep) || activityInfo.metaData == null || !activityInfo.metaData.containsKey(META_BACK_COMPATIBILITY_ID)) {
            id = (packageName + activityName).hashCode();
        } else {
            id = activityInfo.metaData.getInt(META_BACK_COMPATIBILITY_ID);
        }

        this.hasDifficulty = activityInfo.metaData != null && activityInfo.metaData.getBoolean(META_HAS_DIFFICULTY);
        int order = activityInfo.metaData != null ? Math.abs(activityInfo.metaData.getInt(META_ORDER)): Integer.MAX_VALUE;
        forCaptcha = activityInfo.metaData != null ? activityInfo.metaData.getString(META_FOR_CAPTCHA): null;
        if (isExternal || order == 0) {
            order = Integer.MAX_VALUE;
        }
        this.order = order;
    }

    public static BaseCaptchaInfo build(final Context context, final @NonNull ActivityInfo activityInfo, final @NonNull String label) {
        return new BaseCaptchaInfo(context, activityInfo, label);
    }

    @NonNull
    @Override
    public String getPackageName() {
        return packageName;
    }

    @NonNull
    @Override
    public String getActivityName() {
        return activityName;
    }

    @NonNull
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isExternal(){
        return isExternal;
    }

    @Override
    public boolean hasDifficulty() {
        return hasDifficulty;
    }

    @Override
    public boolean isConfigurable() {
        return configActivityName != null;
    }

    @Nullable
    @Override
    public String getConfigActivityName() {
        return configActivityName;
    }

    public void setConfigActivityName(final @NonNull String configActivityName) {
        this.configActivityName = configActivityName;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public String getForCaptcha() {
        return forCaptcha;
    }

    @Override
    public String toString() {
        return "Captcha Info{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", label='" + label + '\'' +
                ", configActivityName='" + configActivityName + '\'' +
                ", hasDifficulty='" + hasDifficulty + '\'' +
                ", isExternal=" + isExternal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCaptchaInfo that = (BaseCaptchaInfo) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
