# cordova-plugin-simのサンプル

単純に電話番号を表示するだけ。

## サンプルを作成した手順

まずcordovaプロジェクトを作ってsimプラグインとAndroidプラットフォームを追加する。

```
cordova create cordova-plugin-sim-sample
cd cordova-plugin-sim-sample
cordova plugin add cordova-plugin-sim
cordova platform add android
```

`AndroidManifest.xml`へ電話番号を読み取るためのパーミッションを追加する。

```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```

HTMLとJavaScriptを編集したら、実機で試してみる。

```
cordova run android
```

## 備考

Androidプラットフォームを使う場合はサポートライブラリを利用するので次のページに従ってサポートライブラリのローカルリポジトリをセットアップする必要があるっぽい。

* https://developer.android.com/topic/libraries/support-library/setup.html

あと、このサンプルコードは`plugins`ディレクトリと`platforms`ディレクトリは`git commit`していないので、
それらの追加と`AndroidManifest.xml`の編集は`git clone`したら都度行う必要がある。

