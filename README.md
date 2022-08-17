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

And check your application minSdk must be greater than 24.

## Do Request

### GET Request

**Kotlin Example:**

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

**Java Example:**

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

**Value Settings:**

* `.tag` - Add tag for request.
* `.addUrlParams` - Add url query param.
* `.urlParams` - Add url query params.
* `.addHeader` - Add header for request.
* `.headers` - Add headers for request.
* `.cacheable` - Cache response.
* `.saveRecord` - Do save http transport record. Default value is true.
* `.connectTimeout` - Connect timeout time.
* `.readTimeout` - Read timeout time.
* `.readTimeout` - Read timeout time.

### Post Request

**Kotlin Example: **

```kotlin
EasyHttp.post(context, url)
  .jsonBody(obj)
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

**Java Example:**

```java
EasyHttp.post(context, url)
  .jsonBody(obj)
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
  })
```

**Value Settings:**

Have the value settings in the get example.

* `.stringBody` - Set string request body, also can set your custom content type.
* `.jsonBody` - Set json object request body, also can set your custom content type.
* `.formBody` - Set form request body.
* `.requestBody` - Set your custom okhttp request body.

### Upload File

**Kotlin Example:**

```kotlin
EasyHttp.upload(context, url)
  .addMultipartFile("file", file)
  .addMultipartParameter("text", "text")
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

**Java Example:**

```java
EasyHttp.upload(context, url)
  .addMultipartFile("file", file)
  .addMultipartParameter("text", "text")
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
  })
```

**Value Settings:**

Have the value settings in the get example.

* `.contentType` - Set upload request content type. Default value is "multipart/form-data".
* `.addMultipartParam` - Add multipart parameter.
* `.addMultipartFile` - Add multipart parameter. You can put `File`, `ByteArray`,  `Uri`.
