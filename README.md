# Android port of [wvWare](http://wvware.sourceforge.net/)

![Build](https://github.com/ViliusSutkus89/wvWare-Android/workflows/Build/badge.svg)
[![Download](https://api.bintray.com/packages/viliussutkus89/maven-repo/wvware-android/images/download.svg)](https://bintray.com/viliussutkus89/maven-repo/wvware-android/_latestVersion)

### Scope:
Currently limited to wvHtml.

### How to install:
[sample-app/app/build.gradle](sample-app/app/build.gradle) contains code to load the library as a dependency in Gradle.
```gradle
dependencies {
    implementation 'com.viliussutkus89:wvware-android:1.2.3'
}
```

wvWare-Android is distributed using [JCenter](https://jcenter.bintray.com) Maven repository.  
It needs be added to [top level build.gradle](sample-app/build.gradle)
```gradle
allprojects {
  repositories {
      jcenter()
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
* ndk-20.1.5948944

