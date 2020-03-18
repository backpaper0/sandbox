# servlet-jsp-dispatch-example

ServletとJSPをforwardしたりincludeした場合のFilterのかかり方を色々試したい。

## ServletからJSPへforwardする

```
curl localhost:8080/foo.do
```

```
>>> ExampleFilter[REQUEST] (/foo.do)
>>> ExampleFilter[*] (/foo.do)
>>> com.example.Servlet2Jsp (/foo.do)
>>> ExampleFilter[FORWARD] (/WEB-INF/index.jsp)
>>> ExampleFilter[*] (/WEB-INF/index.jsp)
>>> ExampleFilter[INCLUDE] (/WEB-INF/index.jsp)
>>> ExampleFilter[*] (/WEB-INF/index.jsp)
<<< ExampleFilter[*] (/WEB-INF/index.jsp)
<<< ExampleFilter[INCLUDE] (/WEB-INF/index.jsp)
<<< ExampleFilter[*] (/WEB-INF/index.jsp)
<<< ExampleFilter[FORWARD] (/WEB-INF/index.jsp)
<<< com.example.Servlet2Jsp (/foo.do)
<<< ExampleFilter[*] (/foo.do)
<<< ExampleFilter[REQUEST] (/foo.do)
```

## ServletからServletへforwardする(その後JSPへforward)

```
curl localhost:8080/bar.do
```

```
>>> ExampleFilter[REQUEST] (/bar.do)
>>> ExampleFilter[*] (/bar.do)
>>> com.example.Servlet2Servlet (/bar.do)
>>> ExampleFilter[FORWARD] (/foo.do)
>>> ExampleFilter[*] (/foo.do)
>>> com.example.Servlet2Jsp (/foo.do)
>>> ExampleFilter[FORWARD] (/WEB-INF/index.jsp)
>>> ExampleFilter[*] (/WEB-INF/index.jsp)
>>> ExampleFilter[INCLUDE] (/WEB-INF/index.jsp)
>>> ExampleFilter[*] (/WEB-INF/index.jsp)
<<< ExampleFilter[*] (/WEB-INF/index.jsp)
<<< ExampleFilter[INCLUDE] (/WEB-INF/index.jsp)
<<< ExampleFilter[*] (/WEB-INF/index.jsp)
<<< ExampleFilter[FORWARD] (/WEB-INF/index.jsp)
<<< com.example.Servlet2Jsp (/foo.do)
<<< ExampleFilter[*] (/foo.do)
<<< ExampleFilter[FORWARD] (/foo.do)
<<< com.example.Servlet2Servlet (/bar.do)
<<< ExampleFilter[*] (/bar.do)
<<< ExampleFilter[REQUEST] (/bar.do)
```

