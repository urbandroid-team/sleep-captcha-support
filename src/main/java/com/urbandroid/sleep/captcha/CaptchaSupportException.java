package com.urbandroid.sleep.captcha;

public class CaptchaSupportException extends RuntimeException {

    public CaptchaSupportException(String detailMessage) {
        super(detailMessage);
    }

    public CaptchaSupportException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
