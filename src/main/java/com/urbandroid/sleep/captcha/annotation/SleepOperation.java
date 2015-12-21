package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Sleep operation enum.
 *
 * <p><strong>Possible values:</strong>
 * <ul>
 * <li>SleepOperation.OPERATION_NONE</li>
 * <li>SleepOperation.SNOOZE_CANCELED</li>
 * <li>SleepOperation.SHOULD_SKIP</li>
 * <li>SleepOperation.DELETE_ALARM</li>
 * <li>SleepOperation.DISABLE_ALARM</li>
 * <li>SleepOperation.EDIT_ALARM</li>
 * <li>SleepOperation.EDIT_ALARM_TIME_EXTRA</li>
 * </ul>
 */
@StringDef({
        SleepOperation.OPERATION_NONE,
        SleepOperation.SNOOZE_CANCELED,
        SleepOperation.SHOULD_SKIP,
        SleepOperation.DELETE_ALARM,
        SleepOperation.DISABLE_ALARM,
        SleepOperation.EDIT_ALARM,
        SleepOperation.EDIT_ALARM_TIME_EXTRA
       // SleepOperation.CAPTCHA_START_FOR_RESULT
})
@Retention(RetentionPolicy.SOURCE)
public @interface SleepOperation {

        String OPERATION_NONE = "no_operation";
        String SNOOZE_CANCELED = "snooze_cancel";
        String SHOULD_SKIP = "should_skip";
        String DELETE_ALARM = "delete_alarm";
        String DISABLE_ALARM = "disable_alarm";
        String EDIT_ALARM = "edit_alarm";
        String EDIT_ALARM_TIME_EXTRA = "edit_alarm_time_extra";
        //String CAPTCHA_START_FOR_RESULT = "captcha_start_for_result";

}
