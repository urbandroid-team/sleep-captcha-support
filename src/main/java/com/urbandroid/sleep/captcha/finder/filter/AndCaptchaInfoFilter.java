package com.urbandroid.sleep.captcha.finder.filter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaGroup;
import com.urbandroid.sleep.captcha.domain.CaptchaInfo;
import com.urbandroid.sleep.captcha.finder.CaptchaInfoFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndCaptchaInfoFilter implements CaptchaInfoFilter {

    private final List<CaptchaInfoFilter> filters = new ArrayList<>();

    public AndCaptchaInfoFilter(final @NonNull CaptchaInfoFilter... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    public AndCaptchaInfoFilter add(final @NonNull CaptchaInfoFilter filter){
        filters.add(filter);
        return this;
    }

    @Override
    public boolean apply(@Nullable CaptchaGroup group, @Nullable CaptchaInfo captcha) {
        for (final CaptchaInfoFilter filter: filters) {
            if (!filter.apply(group, captcha)) {
                return false;
            }
        }
        return true;
    }

}
