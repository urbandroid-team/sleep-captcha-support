package com.urbandroid.sleep.captcha.finder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.urbandroid.sleep.captcha.domain.CaptchaInfo;

import java.util.List;

public class FallbackCaptchaFinder implements CaptchaFinder{

    private final CaptchaFinder captchaFinder;
    private final CaptchaInfo fallbackInternalCaptcha;

    public FallbackCaptchaFinder(
            final @NonNull CaptchaFinder captchaFinder,
            final int fallbackCaptchaId)
    {
        this.captchaFinder = captchaFinder;
        fallbackInternalCaptcha = findById(fallbackCaptchaId);
        if (fallbackInternalCaptcha == null || fallbackInternalCaptcha.isExternal()) {
            throw new RuntimeException("Invalid (not found or external) fallback captcha id: " + fallbackCaptchaId);
        }
    }

    @Override
    @NonNull
    public List<CaptchaInfo> lookup() {
        return captchaFinder.lookup();
    }

    @Override
    @NonNull
    public List<CaptchaInfo> lookup(final @Nullable CaptchaInfoFilter filter) {
        return captchaFinder.lookup(filter);
    }

    @Override
    @Nullable
    public CaptchaInfo findById(int id) {
        final CaptchaInfo captchaInfo = captchaFinder.findById(id);
        return captchaInfo == null ? fallbackInternalCaptcha: captchaInfo;
    }
}
