# 習作: コーディングエージェントっぽいものを作ってみる

> $ uv run main.py
> prompt> Hello Worldを出力するだけのPythonプログラムを書いてhello.pyへ保存して。
> ファイル `hello.py` を作成しました。内容は次のとおりです：
> 
> ```python
> print("Hello World")
> ```
> 
> これで `python hello.py` を実行すると `Hello World` が表示されます。
> prompt> main関数を導入して。
> `hello.py` を `main` 関数を使うように更新しました。内容は次のとおりです：
> 
> ```python
> def main():
>     print("Hello World")
> 
> if __name__ == "__main__":
>     main()
> ```
> 
> これで `python hello.py` を実行すると `Hello World` が表示されます。
> prompt> Worldの部分はユーザーの入力を受け取って可変にして。
> `hello.py` を次のように変更しました。`World` の部分をユーザーが入力した文字列に置き換えます。
> 
> ```python
> def main():
>     word = input("Enter word: ")
>     print(f"Hello {word}")
> 
> if __name__ == "__main__":
>     main()
> ```
> 
> これで、`python hello.py` を実行すると、入力された文字列に応じて `Hello <入力>` が表示されます。
> prompt> /exit
> $
