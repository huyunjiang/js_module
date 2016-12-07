# react_native_sound_chat

React Native Module for sound_chat_android

## install

```
npm install react_native_sound_chat --save
```

## Android

### With rnpm

```
rnpm link react_native_sound_chat
```

### Manually

* android/settings.gradle

```
include ':react_native_sound_chat'
project(':react_native_sound_chat').projectDir = new File(rootProject.projectDir, '../node_modules/react_native_sound_chat/android')
```

* android/app/build.gradle

```
dependencies {
  compile project(':react_native_sound_chat')
}

packagingOptions {
  exclude 'META-INF/NOTICE'
  exclude 'META-INF/LICENSE'
  exclude 'META-INF/DEPENDENCIES'
}
```

* add permission in AndroidManifest.xml
```
  <uses-permission android:name="android.permission.RECORD_AUDIO"/>
  <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>

* register module (in MainActivity.java)

```java
...

import com.sound.chat.SoundPackage; // <--- IMPORT

public class MainActivity extends ReactActivity {

    ...

    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new SoundPackage() // <--- ADD HERE
        );
    }
}
