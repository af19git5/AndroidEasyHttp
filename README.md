# Android Easy Http Library
<img src="images/banner.png" />

## About Android Easy Http Library

* Made on [OkHttp](http://square.github.io/okhttp/).
* Easy to do http transport.
* Visual http transport record.
* Http cookies persistence.

## Using Library In Your Application

Add this in your app build.gradle file.

```groovy
android {
    buildFeatures {
        viewBinding true
        dataBinding true
    }
}

dependencies {
    implementation 'io.github.af19git5:easy-http-android:0.0.1'
}
```

Add permission in your AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

And check your application minSdk must be greater than 23.

## How To Used

### GET Request

Kotlin Example: 

```kotlin
EasyHttp.get(context, url)
  .build()
  .getAsString(object : StringResponseListener {
    override fun onSuccess(headers: Headers, body: String) {
      // Do something...
    }

    override fun onError(e: HttpException) {
      // Do something...
    }
  })
```

Java Example:

```java
EasyHttp.get(context, url)
  .build()
  .getAsString(new StringResponseListener() {
    @Override
    public void onSuccess(@NonNull Headers headers, @NonNull String body) {
      // Do something...
    }

    @Override
    public void onError(@NonNull HttpException e) {
      // Do something...
    }
  });
```
