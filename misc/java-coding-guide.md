# [WIP]Javaコーディングガイド

## 変数にはfinalを付けること

## メソッドはpublicのみとすること

## フィールドはprivate finalのみとすること

## staticフィールド・staticメソッドは定義しない

## 積極的にバリューオブジェクトを使うこと

## 引数や戻り値には抽象的な型を使わずバリューオブジェクトを採用すること

抽象的な型の代表例。

- プリミティブ型とそのラッパークラス
- `java.lang.String`
- `java.util.Collection`
- `java.util.List`
- `java.util.Set`
- `java.util.Map`
- `java.time.LocalDateTime`
- `java.time.LocalDate`
- `java.time.LocalTime`

## 引数にnullを許可しないこと

## 戻り値としてnullを返さないこと

## クラスは積極的にイミュータブルにすること

## オーバーロードはしないこと

## アプリケーションにはレイヤーがあり、レイヤー毎に適用すべきコーディングスタイルは異なる

- HTTP通信の境界
- ドメイン
- データベースとの境界

