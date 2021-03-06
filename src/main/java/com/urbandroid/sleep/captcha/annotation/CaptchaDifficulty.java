package com.urbandroid.sleep.captcha.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  Captcha difficulty enum.
 *
 * <p><strong>Possible values:</strong>
 * <ul>
 * <li>CaptchaDifficulty.VERY_SIMPLE</li>
 * <li>CaptchaDifficulty.SIMPLE</li>
 * <li>CaptchaDifficulty.INTERMEDIATE</li>
 * <li>CaptchaDifficulty.HARD</li>
 * <li>CaptchaDifficulty.VERY_HARD</li>
 * </ul>
 */
@IntDef({
        CaptchaDifficulty.VERY_SIMPLE,
        CaptchaDifficulty.SIMPLE,
        CaptchaDifficulty.INTERMEDIATE,
        CaptchaDifficulty.HARD,
        CaptchaDifficulty.VERY_HARD
})
@Retention(RetentionPolicy.SOURCE)
public @interface CaptchaDifficulty {

        @CaptchaDifficulty int VERY_SIMPLE = 1;
        @CaptchaDifficulty int SIMPLE = 2;
        @CaptchaDifficulty int INTERMEDIATE = 3;
        @CaptchaDifficulty int HARD = 4;
        @CaptchaDifficulty int VERY_HARD = 5;

}
