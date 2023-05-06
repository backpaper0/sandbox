# Gitリポジトリのルートにgo.modがなくてもgo installできるのか実験してみた

結論としては`go install`できた。

Dockerコンテナでまっさらな環境を作って`go install`を試した。

```bash
$ docker run -it --rm golang bash
root@3e3c3015973b:/go# go install github.com/backpaper0/sandbox/golang/helloworld@latest
go: downloading github.com/backpaper0/sandbox v0.0.0-20230506105155-522a4d78265a
go: downloading github.com/backpaper0/sandbox/golang/helloworld v0.0.0-20230506105155-522a4d78265a
root@3e3c3015973b:/go# helloworld
Hello World
root@3e3c3015973b:/go# 
```

