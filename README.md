# react-native-hms-scan

HMS Scan Kit(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/scan-introduction-4)'s feature would be progressively integrated into this repo.

Current available feature:
1. Barcode Scanner (Default View): https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/scan-default4

## Requirements

RN 0.60.+

## Getting started

Currently this is not available on npm yet. Please clone directly to your machine and import it as local dependency.

`$ yarn add "{$DIR}/react-native-hms-scan`

Add these to _AndroidManifest.xml_
`   <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
    
    <application>
      ...
      <activity android:name="com.huawei.hms.hmsscankit.ScanKitActivity" />
      ...
    </application>


Add to _android/build.gradle_

```
  buildscript {
    repositories {
        ...
        maven {url 'http://developer.huawei.com/repo/'}
    }
    dependencies {
        ...
        classpath 'com.huawei.agconnect:agcp:1.2.1.301'
    }
  }
  
  allprojects {
    repositories {
        ...
        maven {url 'http://developer.huawei.com/repo/'}
    }
  }
```

Add to _app/build.gradle_

```
  apply plugin: "com.huawei.agconnect"
```


## Usage

Note: Handle permission of _CAMERA_ and _READ_EXTERNAL_STORAGE_ before calling startScan().

```javascript
import ReactNativeHmsScan from 'react-native-hms-scan';

// TODO: What to do with the module?
 async startScan() {
    try {
      const data = await ReactNativeHmsScan.startScan();
      // handle data as you wish
      console.log(data);
    } catch (e) {
      console.log(e);
    }
  }
```
