# LangGraphを試す

- [グラフ](./graph.md)の作成
  ```bash
  poetry run task graph
  ```
- ソースコードのフォーマット
  ```bash
  poetry run task format
  ```
- 静的解析
  ```bash
  poetry run task lint
  ```
- 自動修正
  ```bash
  poetry run task fix
  ```
- テスト実行
  ```bash
  poetry run task test
  ```
- 単純なチャット
  ```bash
  poetry run task chat -q "こんにちは！"
  ```
- チャット + ツール
  ```bash
  poetry run task chat_with_tool --query ReGLOSSは野球のチームですか？
  ```

  ```
  *** LLMのみ ***
  ReGLOSSは野球のチームではなく、女性のプロバスケットボールチームです。日本の女子バスケットボールリーグ（WJBL）に所属しており、主に千葉県を拠点に活動しています。チーム名の「ReGLOSS」  は、再生や輝きを意味する言葉が組み合わさったものです。野球チームではないので、混同しないようにしましょう。

  *** LLM + Wikipediaの検索 ***
  ReGLOSSは野球のチームではなく、ホロライブプロダクション傘下の「hololive DEV_IS」に所属する日本の音楽アーティストVTuberグループです。メンバーには火威青、音乃瀬奏、一条莉々華、儒烏  風亭らでん、轟はじめの5人がいます。
  ```
- FastAPIと組み合わせてServer-Sent Events
  ```bash
  poetry run task dev
  ```
  http://localhost:8000/index.html
- SQLiteへ会話を保存
  ```bash
  poetry run task checkpoint --query "1足す2は？"
  ```

  ```
  1足す2は3です。
  ```

  ```bash
  poetry run task checkpoint --query "3を掛けて。"
  ```

  ```
  3を掛けると、3×3=9になります。
  ```
