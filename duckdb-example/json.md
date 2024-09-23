# JSONを扱う

## JSONをテーブルへ突っ込んでクエリーを投げられるようにする

例えば[GitHub上のSpringのorganizationにあるリポジトリ一覧](https://github.com/orgs/spring-projects/repositories)をテーブルに突っ込む。

まずAPIを叩いてJSONを取得する。

```bash
curl -o repos.json -G -d per_page=100 https://api.github.com/orgs/spring-projects/repos
```

次にデータベースへ接続する。

```bash
duckdb repos.db
```

JSONをもとにテーブルを作成する。

```sql
create table repos as select * from 'repos.json';
```

これでクエリーを投げられるようになった。

例えばリポジトリの名前とライセンスを抽出するクエリーはこんな感じ。

```sql
select name, license.name as license from repos;
```

### `-c`オプションと`read_json`関数

`duckdb`コマンドの`-c`オプションと、`read_json`関数を使えばテーブルを作らずクエリーを投げることもできる。

```bash
duckdb -c "select name, license.name as license from read_json('https://api.github.com/orgs/spring-projects/repos?per_page=100')"
```

### 複数のJSONを読み込む

例に挙げたリポジトリ一覧のAPIは`per_page`の最大値が100なので、organization配下のリポジトリが100を超える場合、2回以上APIを叩いて複数個のJSONファイルを取得し、それをDuckDBへ取り込む必要がある。

DuckDBは複数個のJSONファイルを一度に取り込むことができる。

例として`per_page`を30に設定し、複数個のJSONファイルを取得して、DuckDBへ読み込んでみる。

まずはリポジトリ数を取得する。

```bash
seq $(($(duckdb -csv -noheader -c "select public_repos from read_json('https://api.github.com/orgs/spring-projects')") / 30 + 1)) | \
 while read page; do curl -o repos${page}.json -G -d per_page=30 -d page=${page} https://api.github.com/orgs/spring-projects/repos; done
```

次にデータベースへ接続する。

```bash
duckdb repos.db
```

JSONをもとにテーブルを作成する。
`*`を付けて複数個のJSONファイルを指定している。

```sql
create table repos as select * from 'repos*.json';
```

## 配列を行に展開する

引き続きSpringのリポジトリ一覧を例に色々と試す。

`topics`が配列になっている。

```
D select name, topics from repos limit 5;
┌─────────────────────┬──────────────────────────────────────────────────────────┐
│        name         │                          topics                          │
│       varchar       │                        varchar[]                         │
├─────────────────────┼──────────────────────────────────────────────────────────┤
│ spring-data-commons │ [data-access, ddd, framework, java, spring, spring-data] │
│ spring-data-jpa     │ [ddd, framework, java, jpa, spring, spring-data]         │
│ spring-amqp         │ []                                                       │
│ spring-batch        │ [batch, batch-processing, java, spring]                  │
│ spring-data-gemfire │ []                                                       │
└─────────────────────┴──────────────────────────────────────────────────────────┘
```

これを行に展開するには`unset`関数を使う。

```
D select name, unnest(topics) from repos limit 5;
┌─────────────────────┬────────────────┐
│        name         │ unnest(topics) │
│       varchar       │    varchar     │
├─────────────────────┼────────────────┤
│ spring-data-commons │ data-access    │
│ spring-data-commons │ ddd            │
│ spring-data-commons │ framework      │
│ spring-data-commons │ java           │
│ spring-data-commons │ spring         │
└─────────────────────┴────────────────┘
```

## グルーピングしつつ配列を作る

`array_agg`関数を使う。
例として`has_wiki`毎にリポジトリの名前を列挙する。

```
D select has_wiki, array_agg(name) as names from repos group by has_wiki;
┌──────────┬─────────────────────────────────────────────────────────────────────────────────────┐
│ has_wiki │                                        names                                        │
│ boolean  │                                      varchar[]                                      │
├──────────┼─────────────────────────────────────────────────────────────────────────────────────┤
│ false    │ [spring-data-jpa, spring-data-redis, spring-data-cassandra, spring-data-neo4j, sp…  │
│ true     │ [spring-data-commons, spring-amqp, spring-batch, spring-data-gemfire, spring-fram…  │
└──────────┴─────────────────────────────────────────────────────────────────────────────────────┘
```

## 例）`topic`ごとに`name`を列挙する

`unnest`で`topics`配列を行にしつつ、`array_agg`で`name`を配列にする。

```
D with x as (select name, unnest(topics) as topic from repos)
  select topic, to_json(array_agg(name)) as names
  from x group by topic order by count(name) desc limit 10;
┌────────────────┬───────────────────────────────────────────────────────────────────────────────┐
│     topic      │                                     names                                     │
│    varchar     │                                     json                                      │
├────────────────┼───────────────────────────────────────────────────────────────────────────────┤
│ spring         │ ["spring-data-commons","spring-data-jpa","spring-batch","spring-framework",…  │
│ framework      │ ["spring-data-commons","spring-data-jpa","spring-framework","spring-data-re…  │
│ java           │ ["spring-data-commons","spring-data-jpa","spring-batch","spring-data-redis"…  │
│ spring-data    │ ["spring-data-commons","spring-data-jpa","spring-data-redis","spring-data-c…  │
│ ddd            │ ["spring-data-commons","spring-data-jpa","spring-data-redis","spring-data-c…  │
│ spring-boot    │ ["spring-boot","sts4","spring-boot-data-geode","spring-modulith","spring-gr…  │
│ apache-geode   │ ["spring-boot-data-geode","spring-session-data-geode","spring-test-data-geo…  │
│ r2dbc          │ ["spring-data-relational","spring-data-r2dbc"]                                │
│ graphql        │ ["spring-graphql","spring-graphql-examples"]                                  │
│ spring-session │ ["spring-session-data-geode","spring-session-data-mongodb"]                   │
├────────────────┴───────────────────────────────────────────────────────────────────────────────┤
│ 10 rows                                                                              2 columns │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

CTE（あるいはサブクエリー）を使わないと実現できなさそう？
自分のSQLが低いだけかも知れないが。

`array_agg`関数にはさらに`to_json`関数を適用している。
適用しないと`varchar[]`型になるが、適用すると`json`型になる。
これは`duckdb`コマンドの`--json`オプションに影響する。

```bash
$ duckdb --json -c "
  with x as (select name, unnest(topics) as topic from repos)
  select topic, to_json(array_agg(name)) as names
  from x group by topic order by count(name) desc limit 3
  " repos.db | jq
[
  {
    "topic": "spring",
    "names": [
      "spring-data-commons",
      "spring-data-jpa",
      "spring-batch",
      "spring-framework",
      "spring-data-redis",
      "spring-integration",
      "spring-data-cassandra",
      "spring-data-neo4j",
      "spring-data-mongodb",
      "spring-security",
      "spring-data-rest",
      "spring-boot",
      "spring-data-couchbase",
      "spring-statemachine",
      "sts4",
      "spring-vault",
      "spring-data-relational",
      "spring-boot-data-geode",
      "spring-hateoas-examples",
      "spring-data-r2dbc",
      "spring-graphql-examples"
    ]
  },
  {
    "topic": "framework",
    "names": [
      "spring-data-commons",
      "spring-data-jpa",
      "spring-framework",
      "spring-data-redis",
      "spring-data-cassandra",
      "spring-data-neo4j",
      "spring-data-mongodb",
      "spring-security",
      "spring-data-rest",
      "spring-boot",
      "spring-data-couchbase",
      "spring-vault",
      "spring-data-ldap",
      "spring-data-relational",
      "spring-boot-data-geode",
      "spring-session-data-geode",
      "spring-test-data-geode",
      "spring-data-r2dbc"
    ]
  },
  {
    "topic": "java",
    "names": [
      "spring-data-commons",
      "spring-data-jpa",
      "spring-batch",
      "spring-data-redis",
      "spring-integration",
      "spring-data-cassandra",
      "spring-data-neo4j",
      "spring-data-mongodb",
      "spring-security",
      "spring-data-rest",
      "spring-boot",
      "spring-data-couchbase",
      "spring-statemachine",
      "spring-vault",
      "spring-boot-data-geode",
      "spring-session-data-geode",
      "spring-test-data-geode",
      "spring-ai"
    ]
  }
]
```

`to_json`関数が無いと次のようになる。

```bash
$ duckdb --json -c "
  with x as (select name, unnest(topics) as topic from repos)
  select topic, array_agg(name) as names
  from x group by topic order by count(name) desc limit 3
  " repos.db | jq
[
  {
    "topic": "spring",
    "names": "[spring-data-commons, spring-data-jpa, spring-batch, spring-framework, spring-data-redis, spring-integration, spring-data-cassandra, spring-data-neo4j, spring-data-mongodb, spring-security, spring-data-rest, spring-boot, spring-data-couchbase, spring-statemachine, sts4, spring-vault, spring-data-relational, spring-boot-data-geode, spring-hateoas-examples, spring-data-r2dbc, spring-graphql-examples]"
  },
  {
    "topic": "framework",
    "names": "[spring-data-commons, spring-data-jpa, spring-framework, spring-data-redis, spring-data-cassandra, spring-data-neo4j, spring-data-mongodb, spring-security, spring-data-rest, spring-boot, spring-data-couchbase, spring-vault, spring-data-ldap, spring-data-relational, spring-boot-data-geode, spring-session-data-geode, spring-test-data-geode, spring-data-r2dbc]"
  },
  {
    "topic": "java",
    "names": "[spring-data-commons, spring-data-jpa, spring-batch, spring-data-redis, spring-integration, spring-data-cassandra, spring-data-neo4j, spring-data-mongodb, spring-security, spring-data-rest, spring-boot, spring-data-couchbase, spring-statemachine, spring-vault, spring-boot-data-geode, spring-session-data-geode, spring-test-data-geode, spring-ai]"
  }
]
```

## 参考

- [JSON Extension](https://duckdb.org/docs/extensions/json.html)