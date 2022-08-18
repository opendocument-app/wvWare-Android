# Android port of [wvWare](http://wvware.sourceforge.net/)

[![build](https://github.com/ViliusSutkus89/wvWare-Android/actions/workflows/build.yml/badge.svg)](https://github.com/ViliusSutkus89/wvWare-Android/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.viliussutkus89/wvware-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.viliussutkus89%20AND%20a:wvware-android)

### Used by:
- [Documenter](https://github.com/ViliusSutkus89/Documenter) on [Google Play](https://play.google.com/store/apps/details?id=com.viliussutkus89.documenter) - reference application for pdf2htmlEX-Android and wvWare-Android libraries.
- [OpenDocument.droid](https://github.com/opendocument-app/OpenDocument.droid) on [Google Play](https://play.google.com/store/apps/details?id=at.tomtasche.reader) - It's Android's first OpenOffice Document Reader!
- Defunct [wvWare-Android application](https://github.com/ViliusSutkus89/wvWare-Android/tree/v1.2.7/application).

### Scope:
Limited to wvHtml.

### How to install:
[application/app/build.gradle](application/app/build.gradle) contains code to load the library as a dependency in Gradle.
```gradle
dependencies {
    implementation 'com.viliussutkus89:wvware-android:1.2.7'
}
```

### Usage:
Library is interfaced through Java.
```Java
import com.viliussutkus89.android.wvware.wvWare;
...
java.io.File input = new java.io.File(getFilesDir(), "my.doc");
java.io.File outputHTML = new wvWare(getApplicationContext()).setInputDOC(input).convert();
```

Encrypted documents need a password to be decrypted.

```Java
java.io.File outputHTML = new wvWare(getApplicationContext()).setInputDOC(input).setPassword("password").convert();
```

Library needs Android Context to obtain path to cache directory and asset files, which are supplied in .aar.

### C++ runtime dependency:
[Using mismatched prebuilt libraries](https://developer.android.com/ndk/guides/common-problems#using_mismatched_prebuilt_libraries) is less problematic if all the libraries used in the application are:
* Built with the same toolchain - ndk-23.2.8568313
* Linked against shared C++ STL - `android.defaultConfig.externalNativeBuild.cmake.arguments "-DANDROID_STL=c++_shared"` in app's (and all JNI dependencies) build.gradle.