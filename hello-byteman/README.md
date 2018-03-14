# byteman-study

http://byteman.jboss.org/

## Setup

```console
wget http://repo.maven.apache.org/maven2/org/jboss/byteman/byteman-download/4.0.1/byteman-download-4.0.1-bin.zip
unzip byteman-download-4.0.1-bin.zip
export BYTEMAN_HOME=`pwd`/byteman-download-4.0.1
```

```console
mvnw package
```

## Check script

```console
$BYTEMAN_HOME/bin/bmcheck.sh -cp target/app.jar task.btm
```

## Run

```console
$BYTEMAN_HOME/bin/bmjava.sh -l task.btm -- -cp target/app.jar com.example.App
```

```console
java -javaagent:$BYTEMAN_HOME/lib/byteman.jar=script:task.btm -cp target/app.jar com.example.App
```

