# Java EEで起動時に処理するサンプル

Java EE環境でアプリケーションの起動時に処理を行いたい場合に取れる方法を色々と試すサンプルです。

## 動かし方

次のコマンドでアプリケーションを起動してください。

```
./mvnw package cargo:run
```

起動したら次のURLをブラウザで開いてください。

* http://localhost:8080/javaee-startup-sample

あるいは`curl http://localhost:8080/javaee-startup-sample`を実行してください。

以下のようなログが表示されます。
これはアプリケーションの起動時に実行された処理を表すログです。

```
2022-12-10T17:14:45.810207: BeforeBeanDiscovery
2022-12-10T17:14:45.843379: AfterTypeDiscovery
2022-12-10T17:14:46.052837: AfterBeanDiscovery
2022-12-10T17:14:46.117789: AfterDeploymentValidation
2022-12-10T17:14:46.146635: SingletonSessionBean
2022-12-10T17:14:46.213151: ServletContainerInitializer
2022-12-10T17:14:46.225209: Initialized(ApplicationScoped): io.undertow.servlet.spec.ServletContextImpl@648c35a8
2022-12-10T17:14:46.225393: ServletContextListener
2022-12-10T17:14:46.305682: JAX-RS
2022-12-10T17:14:46.662037: HttpServlet
```

