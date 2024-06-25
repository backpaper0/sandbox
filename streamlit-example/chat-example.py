import time

import streamlit as st

_AI_ANSWER = """わかりました。17、43、89の合計を計算します。

17 + 43 + 89 = 149

合計は149です。"""


def ai_stream():
    for word in _AI_ANSWER.split(" "):
        yield word + " "
        time.sleep(0.02)


prompt = st.chat_input(placeholder="それらを合計して。")


human_message = st.chat_message(name="human")
human_message.write("1以上100以下の整数の中から適当に3つ挙げて。")

ai_message = st.chat_message(name="ai")
ai_message.write(
    """もちろんです。以下の3つの整数をランダムに選びました:

17, 43, 89"""
)

if prompt is not None:
    human_message2 = st.chat_message(name="human")
    human_message2.write(prompt)

    ai_message2 = st.chat_message(name="ai")
    time.sleep(3)
    ai_message2.write_stream(ai_stream)
