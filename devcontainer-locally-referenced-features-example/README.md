# DevContainer - Locally referenced Features example

`postCreateCommand`はコンテナ起動時に実行されるので DevContainer を起動しなおしたり、コンテナイメージをリビルドしたときにも必ず実行されます。
Features であればコンテナイメージのビルドプロセスに組み込まれるため、DevContainer の再起動では実行されることはなく、コンテナイメージをリビルドしたときでもキャッシュが効く可能性があります。

[`.devcontainer`内に Feature の定義を置いてインストールすることも可能](https://containers.dev/implementors/features-distribution/#addendum-locally-referenced)なため「時間のかかる処理」は Features で対応するのが良いと考えます。
「時間のかかる処理」の例：

- Node.js/Python などの言語ランタイムのインストール
- 大量のパッケージインストール（`npm install`、`pip install -r requirements.txt`など）
- データベースのセットアップや初期データ投入
- 大きなバイナリファイルのダウンロード

## Feature と postCreateCommand の違いを体験する

この DevContainer は以下の構成で実装されています：

### 基本設定 (devcontainer.json)

- **ベースイメージ**: `mcr.microsoft.com/devcontainers/base:bullseye`
- **ローカル Feature**: `./foo` (同一ディレクトリ内のカスタム Feature)
- **postCreateCommand**: `.devcontainer/install_bar.sh` (起動時実行スクリプト)

### ローカル Feature: "foo"

- **場所**: `.devcontainer/foo/`
- **機能**: コンテナビルド時に`foo`コマンドを`/usr/local/bin/`にインストール（現在時刻付きメッセージを表示）
- **特徴**: 軽量なスクリプトインストール（重い処理の例として）

### postCreateCommand: "bar"

- **場所**: `.devcontainer/install_bar.sh`
- **機能**: コンテナ起動時に`bar`コマンドを`/usr/local/bin/`にインストール（現在時刻付きメッセージを表示）
- **特徴**: 軽量なスクリプトインストール（重い処理の例として）

### 実装の意図

この構成は以下の違いを実証しています：

1. **Feature (`foo`)**: コンテナイメージビルド時に実行されるため、DevContainer 再起動時は実行されず、Docker キャッシュの恩恵を受けられる
2. **postCreateCommand (`bar`)**: 毎回の DevContainer 起動時に実行されるため、再起動のたびに処理時間がかかる

重い処理（Node.js のインストール、大量のパッケージインストールなど）は Feature として実装することで、開発効率を向上させることができます。

## 体験手順

### 1. 初回 DevContainer 起動

1. このプロジェクトを VS Code の DevContainer で開く
2. 初回は以下が実行される：
   - **Feature `foo` のインストール**（コンテナイメージビルド時）
   - **postCreateCommand `bar` のインストール**（コンテナ起動時）
3. ターミナルで動作確認：

   ```bash
   foo  # "Foo" + タイムスタンプが表示される
   bar  # "Bar" + タイムスタンプが表示される
   ```

   **期待される出力例：**

   ```
   $ foo
   Foo installed at: 2024-08-16 10:30:45

   $ bar
   Bar installed at: 2024-08-16 10:31:12
   ```

   **注意**: 表示されるタイムスタンプは、スクリプトがインストールされた時刻です。このタイムスタンプにより、スクリプトが再インストールされたかどうかを確認できます。

### 2. DevContainer 再起動（違いを体験）

1. コマンドパレットから「Dev Containers: Reopen Folder Locally」でローカルに戻る
2. `docker stop`と`docker rm`でコンテナを削除
3. 再度「Reopen in Container」で DevContainer を起動
4. `postCreateCommand`のみが実行される：
   - **Feature `foo`**: 実行されない（既にコンテナイメージに含まれている）
   - **postCreateCommand `bar`**: 実行される（毎回実行される）
5. `foo`と`bar`を実行してタイムスタンプを確認：

   ```bash
   foo
   bar
   ```

   **期待される結果：**

   ```
   $ foo
   Foo installed at: 2024-08-16 10:30:45  # 初回と同じタイムスタンプ

   $ bar
   Bar installed at: 2024-08-16 11:15:23  # 新しいタイムスタンプ
   ```

   - `foo`のタイムスタンプは初回起動時と同じ（再インストールされていない）
   - `bar`のタイムスタンプは新しい時刻（再インストールされた）

### 3. コンテナイメージのリビルド

1. コマンドパレットから「Dev Containers: Rebuild Container」を実行
2. `postCreateCommand`のみが実行される：
   - **Feature `foo`**: 実行されない（キャッシュが効いている）
   - **postCreateCommand `bar`**: 実行される（毎回実行される）
3. `foo`と`bar`を実行してタイムスタンプを確認：
   - `foo`のタイムスタンプは初回起動時と同じ（キャッシュにより再インストールされていない）
   - `bar`のタイムスタンプは新しい時刻（再インストールされた）

### 4. コンテナイメージのリビルド(キャッシュ不使用)

1. コマンドパレットから「Dev Containers: Rebuild Container Without Cache」を実行
2. 初回と同じく両方が実行される：
   - **Feature `foo`**: 実行される（キャッシュを使わないので実行される）
   - **postCreateCommand `bar`**: 実行される（毎回実行される）
3. `foo`と`bar`を実行してタイムスタンプを確認：
   - `foo`のタイムスタンプは新しい時刻（再インストールされた）
   - `bar`のタイムスタンプも新しい時刻（再インストールされた）

### 体験のポイント

- **再起動時**: Feature は実行されないため高速
- **リビルド時**: Feature も実行されるが Docker レイヤーキャッシュの恩恵を受けられる場合がある
- **postCreateCommand**: 常に実行されるため開発中の頻繁な再起動では効率が悪い

この違いにより、重い処理は Feature として実装することの重要性を実感できます。
