package com.urbandroid.sleep.captcha.domain;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * This interface represents base information about Captcha.
 */
public interface CaptchaInfo extends Parcelable {

    int FALLBACK_ID = 1;
    int DISABLED_ID = 0;

    Comparator<CaptchaInfo> ORDER_COMPARATOR = new Comparator<CaptchaInfo>() {
        @Override
        public int compare(final CaptchaInfo info1, final CaptchaInfo info2) {
            if (info1 == info2) {
                return 0;
            }
            if (info1 == null) {
                return 1;
            }
            if (info2 == null) {
                return -1;
            }
            return Integer.valueOf(info1.getOrder()).compareTo(info2.getOrder());
        }
    };

    @NonNull
    String getPackageName();

    @NonNull
    String getActivityName();

    /**
     * @return human readable captcha name/label
     */
    @NonNull
    String getLabel();

    boolean isExternal();

    /**
     * @return true if this captcha supports difficulty otherwise false
     */
    boolean hasDifficulty();

    /**
     * @return true if this captcha supports configuration mode (screen) otherwise false
     */
    boolean isConfigurable();

    /**
     * @return unique captcha id
     */
    int getId();

    /**
     * @return order number - based on that number captchas from same package are ordered
     * when listed in Sleep as Android
     */
    int getOrder();

}
