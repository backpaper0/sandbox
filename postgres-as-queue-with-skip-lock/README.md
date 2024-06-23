# `SKIP LOCKED`を用いてPostgreSQLをキューとして扱う

## 概要

リファレンスに書かれている次の内容を試します。

>  SKIP LOCKEDでは、即座にロックできない行はすべてスキップされます。 行のロックをスキップすると、一貫性のないデータが見えることになるので、一般的な目的の作業のためには適しませんが、複数の消費者がキューのようなテーブルにアクセスするときのロック競合の回避などに利用できます。

- https://www.postgresql.jp/document/15/html/sql-select.html#SQL-FOR-UPDATE-SHARE

## 試行の方法

キューテーブルから未処理のデータを`SELECT`し（デキュー）、内容を標準出力へ書き出したあと該当行に処理済みのフラグを立てます。

確認のポイントは次の通り。

- インスタンスを複数個立てて内容が重複していないこと
- 単純に`FOR UPDATE`する場合とのスループットを比較

## 試行の手順と結果

### 準備

Docker ComposeでPostgreSQLを起動します。

```
docker compose up -d
```

アプリケーションをビルドします。

```
mvn clean package
```

### 内容が重複していないことの確認

取得したレコードの`id`（主キー）を標準出力へ書き出すので標準出力をファイルへリダイレクトして内容が重複していないことを確認します。

処理開始のタイミングを揃えるため`gate`プロファイルでアプリケーションを起動します。

```
java -jar target/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=gate
```

`dequeue`プロファイルでアプリケーションを起動します。
`dequeue`プロファイルのアプリケーションは複数のターミナルで3つほど起動してください。
その際、リダイレクト先のファイル名は`b1.log`、`b2.log`、`b3.log`としてください。

```
java -jar target/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=dequeue > b1.log
```

`gate`プロファイルのアプリケーションで任意のキーを押すと`dequeue`プロファイルのアプリケーションが処理を開始します。

10秒経つと`dequeue`プロファイルのアプリケーションが終了するので、結果を確認します。

まず`wc`で`b1.log`、`b2.log`、`b3.log`の行数の合計を算出します。
それから`b1.log`、`b2.log`、`b3.log`をマージして重複を取り除いた後の行数を算出し、行数が変化していないこと、つまり処理した内容が重複していないことを確認します。

```
$ wc -l *.log
    4193 b1.log
    4190 b2.log
    4187 b3.log
   12570 total
$ cat *.log | sort | uniq | wc -l
   12570
```

上記の通り内容は重複しておらず期待通り動作していました。
また各ファイルの行数から各インスタンスで満遍なく処理できていることがわかります。

### 単純に`FOR UPDATE`する場合とのスループットを比較


`dequeue`プロファイルのアプリケーションを起動する際、`app.lock-mode`プロパティへ`FOR_UPDATE`を設定すると`FOR UPDATE SKIP LOCKED`ではなく単純に`FOR UPDATE`するようになります。

これを利用して、`FOR UPDATE SKIP LOCKED`と`FOR UPDATE`それぞれ10秒間にどれだけのレコードを処理できるのかを確認します。

それぞれ5回ずつ実行した結果が次の通り。

`FOR UPDATE SKIP LOCKED`の結果。

||`b1.log`|`b2.log`|`b3.log`|合計|
|---|--:|--:|--:|--:|
|1回目|4717|4718|4718|14153|
|2回目|4789|4789|4795|14373|
|3回目|4729|4731|4727|14187|
|4回目|4808|4809|4808|14425|
|5回目|4148|4147|4151|12446|

`FOR UPDATE`の結果。

||`b1.log`|`b2.log`|`b3.log`|合計|
|---|--:|--:|--:|--:|
|1回目|2547|2545|2523|7615|
|2回目|2468|2421|2431|7320|
|3回目|2498|2471|2468|7437|
|4回目|2479|2497|2490|7466|
|5回目|2529|2486|2463|7478|

約1.86倍のスループットを得られました。