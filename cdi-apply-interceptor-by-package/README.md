# CDIでパッケージ名を元にInterceptorを適用するサンプル

## 概要

`Sample` アノテーションを付けたら `SampleInterceptor` が適用されるようにしています。
`SampleInterceptor` はメソッド実行して返ってきた値の前後に `*` を付けます。
例えば、

```java
@Sample
@ApplicationScoped
public class Hello {
    public String get() {
        return "hello";
    }
}
```

というCDI管理ビーンの `get` メソッドを実行した場合、
戻り値は `*hello*` になります。

このサンプルでは `sample.foo` パッケージのクラスには自動で `SampleInterceptor` が適用されるようにしています。
パッケージを確認してインターセプターの適用を行っているのは `SampleExtension` です。

## 動作確認

次のコマンドでPayara Microを起動してください。

```
gradle run
```

`sample.foo.FooApi` は `SampleInterceptor` が適用されます。
次のコマンドで確認できます。

```
curl http://localhost:8080/sample/api/foo
```

`sample.bar.BarApi` は `SampleInterceptor` が適用されません。
次のコマンドで確認できます。

```
curl http://localhost:8080/sample/api/bar
```

## 所感

アノテーション肥大化対策、アノテーションの付け忘れ対策として有効とは思います。
しかしクラス定義上には現れない暗黙のルールになってしまうのは良くない点でしょう。

個人的にはアノテーション肥大化にはステレオタイプでまとめて、
アノテーションの付け忘れにはテスト時・デプロイ時にエラーとして検出できるような仕組みを導入したいです。

とは言え、この方法を採用すべき上記以外の理由・場面があるか、もう少し考えてみたいと思います。

## 参考

* JSR 346の「11.5. Container lifecycle events」
