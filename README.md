# Android port of [wvWare](http://wvware.sourceforge.net/)

[![Maven Central](https://img.shields.io/maven-central/v/com.viliussutkus89/wvware-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.viliussutkus89%20AND%20a:wvware-android)

### Scope:
Currently limited to wvHtml.

### How to install:
[sample-app/app/build.gradle](sample-app/app/build.gradle) contains code to load the library as a dependency in Gradle.
```gradle
dependencies {
    implementation 'com.viliussutkus89:wvware-android:1.2.5'
}
```

wvWare-Android is distributed using [Maven Central](https://search.maven.org/artifact/com.viliussutkus89/wvware-android) repository.  
It needs be added to [top level build.gradle](sample-app/build.gradle)
```gradle
allprojects {
  repositories {
      // ...
      mavenCentral()
  }
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

### [Sample application](/sample-app)
Example demonstrates how to convert DOC files to HTML and either open the result in browser or save to storage.
Storage Access Framework (SAF) is used for file management, it requires API level 19 (KitKat).
Debug build of sample application is available in [Releases Page](https://github.com/ViliusSutkus89/wvWare-Android/releases)

### Tools to build from source:
* Meson Build system
* pkg-config
* CMake-3.10.2
* ndk-22.1.7171670

