# cordova-react-example


## Androidアプリをビルドする

Android SDKをインストールする。

次のURLにあるCommand line tools onlyからダウンロードして任意の場所へ展開する。

- [https://developer.android.com/studio#downloads](https://developer.android.com/studio#downloads)

展開した場所を環境変数`ANDROID_HOME`と`ANDROID_SDK_ROOT`へ設定する。
（`ANDROID_HOME`は非推奨になったのかな？　よくわかっていない）

```
export ANDROID_HOME=~/android-sdk
export ANDROID_SDK_ROOT=~/android-sdk
```

この場合`sdkmanager`は`~/android-sdk/tools/bin/sdkmanager`にある。


なお`sdkmanager`を直接使うときは`--sdk_root`で`ANDROID_HOME`を指定しないといけないっぽい。
マジかよ。

```
~/android-sdk/tools/bin/sdkmanager --sdk_root=$ANDROID_HOME --version
```

ここまで設定したら`cordova`コマンドでAndroidアプリをデバッグビルドできる。

```
cordova build android --debug
```

