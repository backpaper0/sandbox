# Java EEで起動時に処理するサンプル

Java EE環境でアプリケーションの起動時に処理を行いたい場合に取れる方法を色々と試すサンプルです。

## 動かし方

次のコマンドでPayara Microを起動してください。

```
gradle run
```

起動したら次のURLをブラウザで開いてください。

* http://localhost:8080/

以下のようなログが表示されます。
これはアプリケーションの起動時に実行された処理を表すログです。

```
2015-12-24T20:20:21.713: BeforeBeanDiscovery
2015-12-24T20:20:21.781: AfterTypeDiscovery
2015-12-24T20:20:22.415: AfterBeanDiscovery
2015-12-24T20:20:22.556: AfterDeploymentValidation
2015-12-24T20:20:22.614: SingletonSessionBean
2015-12-24T20:20:22.693: ServletContainerInitializer
2015-12-24T20:20:22.783: Initialized(ApplicationScoped): org.apache.catalina.core.ApplicationContextFacade@290807e5
2015-12-24T20:20:22.822: HttpServlet
2015-12-24T20:20:23.162: JAX-RS
```

