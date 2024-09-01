# MediaRecorderで音声を取得する例

## 準備

```bash
poetry install
```

## 起動

```bash
poetry run fastapi dev
```

## 動作確認

http://localhost:8000/ を開いて録音を試す。

`recording.wav`というファイルに保存されるので聴いて確認する（ただ自分の環境だと開けなかったので`ffmpeg -i recording.wav recording.mp3`で変換して聴いた）。

## 参考

- [MediaRecorder - Web API | MDN](https://developer.mozilla.org/ja/docs/Web/API/MediaRecorder)
