# Sleep as Android - Captcha support

## Introduction

This open-source library contains classes that are useful for developing
additional captcha into Sleep as Android application.

## Usage

When you use Gradle add the following maven repository and dependency with the latest version
to your `build.gradle` file to use the library:

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.urbandroid.sleep:captcha-support:0.3.3@aar'
}
```
## Captcha Manifest

Your captcha activity must be exported and single top. OPEN action is mandatory in intent filter otherwise
Sleep as Android will not find your captcha.

```xml
<manifest ... >

    <application ... >

        <activity
            android:name=".MyCleverCaptchaActivity"
            android:launchMode="singleTop"
            android:label="@string/captcha_label"
            android:exported="true"
            >
            <meta-data android:name="com.urbandroid.sleep.captcha.meta.has_difficulty" android:value="true"/>
            <intent-filter>
                <action android:name="com.urbandroid.sleep.captcha.intent.action.OPEN"/>
            </intent-filter>
        </activity>
    </application>

</manifest>
```
## CaptchaSupport object

All interaction between Sleep and Captcha is covered by CaptchaSupport object. It must
be created inside onCreate and onNewIntent method of Activity. Also proper releasing captcha support
receivers must be called from onDestroy method.

```java
    private CaptchaSupport captchaSupport;

    protected void onCreate(final Bundle savedInstanceState) {
        ...
        captchaSupport = CaptchaSupportFactory.create(this);
        ...
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        captchaSupport = CaptchaSupportFactory.create(this, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captchaSupport.destroy();
    }

```

Note that, creation captcha support object in onNewIntent has different constructor and must have 2-nd argument intent.

When CaptchaSupport object is created you can
* check in which mode (Preview, Configuration, Operational) is captcha running by CaptchaSupport.getMode()
or CaptchaSupport.isPreviewMode(), isOperationalMode(), isConfigurationMode()
* get difficulty (1-5) - use CaptchaSupport.getDifficulty()
* set up time remaining listener CaptchaSupport.setRemainingTimeListener()
* call solved method when captcha is successfully resolved
* call unsolved method when captcha was not solved but user left activity
* call alive in order to reset timeout for solving captcha
* or use advanced features like CaptchaFinder for getting list of all available captchas on mobile
and launch them via CaptchaLauncher

## Difficulty
In case your captcha need support several level of difficulty, put following lines in AndroidManifest file,
```xml
    <activity ...>
        ...
        <meta-data android:name="com.urbandroid.sleep.captcha.meta.has_difficulty" android:value="true"/>
        ...
    </activity>
```
In order to get current captcha difficulty, call CaptchaSupport.getDifficulty().
Use CaptchaDifficulty annotation to get all possible values.

## Captcha Order
When your application contains more than one captcha activity, you can find out useful to sort them in
Sleep app by declaring order.
```xml
    <activity ...>
        ...
        <meta-data android:name="com.urbandroid.sleep.captcha.meta.order" android:value="1"/>
        ...
    </activity>
    <activity ...>
        ...
        <meta-data android:name="com.urbandroid.sleep.captcha.meta.order" android:value="2"/>
        ...
    </activity>
```

## Captcha Configuration

In case your captcha requires any kind of additional configuration you have two options

### 1. Add CONFIG action into your Captcha Activity

```xml
    <activity
        android:name=".MyCleverCaptchaActivity"
        ...
        >
        <intent-filter>
            <action android:name="com.urbandroid.sleep.captcha.intent.action.OPEN"/>
            <action android:name="com.urbandroid.sleep.captcha.intent.action.CONFIG"/>
        </intent-filter>
    </activity>
```
When CaptchaSupport object (onCreate/onNewIntent) is created, you should check mode in which
captcha is running. To detect mode call either isConfigurationMode() and/or isOperationalMode().

### 2. Create special config activity referring to your captcha activity

    <activity
        android:name=".MyCleverCaptchaActivity"
      ...>

      <intent-filter>
        <action android:name="com.urbandroid.sleep.captcha.intent.action.OPEN"/>
      </intent-filter>
    </activity>

    <activity
        android:name=".MyCleverCaptchaConfigActivity"
      ...>

      <meta-data android:name="com.urbandroid.sleep.captcha.meta.for_captcha" android:value=".MyCleverCaptchaActivity"/>
      <intent-filter>
        <action android:name="com.urbandroid.sleep.captcha.intent.action.CONFIG"/>
      </intent-filter>
    </activity>

This is preferable way doing captcha configuration since it separates two aspects of captcha
and creates less messy code handling two modes in one activity (as per case 1).


## Recommendation
* Don't forget to define your activity with exported="true" and as singleTop otherwise the captcha
  will not be accessible and/or duplicated.
* CaptchaSupport object must be created in Activity.onCreate or onPostCreate method and also onNewIntent
* override Activity.onUserInteraction() method to call CaptchaSupport.alive() method
* override Activity.onBackPressed() method to call CaptchaSupport.unsolved()

## Read more and get started

Check simple github project captcha [examples] or read more on the [website][website].

[examples]: https://github.com/urbandroid-team/sleep-captcha-examples/
[issues]: https://github.com/urbandroid-team/sleep-captcha-support/issues
[website]: http://sleep.urbandroid.org/documentation/developer-api/captcha-api
