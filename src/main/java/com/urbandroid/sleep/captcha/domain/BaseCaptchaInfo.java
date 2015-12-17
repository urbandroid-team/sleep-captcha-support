package com.urbandroid.sleep.captcha.domain;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static com.urbandroid.sleep.captcha.CaptchaConstant.META_BACK_COMPATIBILITY_ID;
import static com.urbandroid.sleep.captcha.CaptchaConstant.META_HAS_DIFFICULTY;
import static com.urbandroid.sleep.captcha.CaptchaConstant.META_ORDER;

/*
Parcelable done by http://www.parcelabler.com/
*/
public class BaseCaptchaInfo implements CaptchaInfo{

    private final int id;
    private final int order;
    private final String packageName;
    private final String activityName;
    private final String label;
    private final boolean isExternal;
    private final boolean hasDifficulty;
    private boolean configurable;

    private BaseCaptchaInfo(final Context context, final @NonNull ActivityInfo activityInfo, final @NonNull String label) {
        this.packageName = activityInfo.packageName;
        this.activityName = activityInfo.name;
        this.label = label;
        this.isExternal = !context.getPackageName().equals(packageName);
        if (isExternal || activityInfo.metaData == null || !activityInfo.metaData.containsKey(META_BACK_COMPATIBILITY_ID)) {
            id = (packageName + activityName).hashCode();
        } else {
            id = activityInfo.metaData.getInt(META_BACK_COMPATIBILITY_ID);
        }

        this.hasDifficulty = activityInfo.metaData != null && activityInfo.metaData.getBoolean(META_HAS_DIFFICULTY);
        int order = activityInfo.metaData != null ? Math.abs(activityInfo.metaData.getInt(META_ORDER)): Integer.MAX_VALUE;
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
        return configurable;
    }

    public void setConfigurable(final boolean configurable) {
        this.configurable = configurable;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Captcha Info{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", label='" + label + '\'' +
                ", configurable='" + configurable + '\'' +
                ", hasDifficulty='" + hasDifficulty + '\'' +
                ", isExternal=" + isExternal +
                '}';
    }

    protected BaseCaptchaInfo(final Parcel in) {
        id = in.readInt();
        order = in.readInt();
        packageName = in.readString();
        activityName = in.readString();
        label = in.readString();
        isExternal = in.readByte() != 0x00;
        hasDifficulty = in.readByte() != 0x00;
        configurable = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(id);
        dest.writeInt(order);
        dest.writeString(packageName);
        dest.writeString(activityName);
        dest.writeString(label);
        dest.writeByte((byte) (isExternal ? 0x01 : 0x00));
        dest.writeByte((byte) (hasDifficulty ? 0x01 : 0x00));
        dest.writeByte((byte) (configurable ? 0x01 : 0x00));
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaseCaptchaInfo> CREATOR = new Parcelable.Creator<BaseCaptchaInfo>() {
        @Override
        public BaseCaptchaInfo createFromParcel(Parcel in) {
            return new BaseCaptchaInfo(in);
        }

        @Override
        public BaseCaptchaInfo[] newArray(int size) {
            return new BaseCaptchaInfo[size];
        }
    };
}
