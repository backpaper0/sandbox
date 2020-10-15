
```
for i in {demo-called,demo-caller};
do mvn -f $i/pom.xml clean package jib:dockerBuild;
docker tag $i:0.0.1-SNAPSHOT $AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-1.amazonaws.com/$i;
docker push $AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-1.amazonaws.com/$i;
done
```



```
```

```
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-1.amazonaws.com
```


[https://docs.aws.amazon.com/ja_jp/xray/latest/devguide/xray-daemon-local.html](https://docs.aws.amazon.com/ja_jp/xray/latest/devguide/xray-daemon-local.html)

```
./xray_mac -o -n ap-northeast-1
```

```
AWS_XRAY_ENABLED=true mvn jetty:run
```

```
docker run --rm -it --name demo-called -e AWS_XRAY_ENABLED=true \
 -e AWS_XRAY_DAEMON_ADDRESS=host.docker.internal:2000 \
 demo-called:0.0.1-SNAPSHOT
```

```
docker run --rm -it --name demo-caller -p 8080:8080 -e AWS_XRAY_ENABLED=true \
 -e AWS_XRAY_DAEMON_ADDRESS=host.docker.internal:2000 \
 -e CALLED_URI=http://demo-called:8080 --link demo-called \
 demo-caller:0.0.1-SNAPSHOT
```
