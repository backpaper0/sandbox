import os
from typing import Any

from fastapi import APIRouter
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts.chat import ChatPromptTemplate
from langchain_core.runnables import Runnable, RunnableBranch, RunnablePassthrough
from langchain_openai import ChatOpenAI
from langserve import add_routes

router = APIRouter()


_classify_prompt = ChatPromptTemplate(
    messages=[
        (
            "system",
            """
            あなたは話題を分類するエキスパートです。
            投げかけられた話題を「質問」と「挨拶」のどちらかに分類してください。
            どちらの分類にも当てはまらない場合は「その他」としてください。
            """,
        ),
        ("user", "おはようございます。"),
        ("ai", "挨拶"),
        ("user", "プログラミング言語Pythonについて教えてください。"),
        ("ai", "質問"),
        ("user", "猫を飼いたい。"),
        ("ai", "その他"),
        ("user", "{topic}"),
    ]
)

_greeting = ChatPromptTemplate(
    messages=[
        (
            "system",
            """
            これから挨拶されるので、明るく元気に返事をしてください。
            年長者として、優しくも砕けた口調で親近感を持たせると良いでしょう。
            """,
        ),
        ("user", "{input}"),
    ]
)

_qa = ChatPromptTemplate(
    messages=[
        (
            "system",
            """
            これから質問を投げかけられるので、誠実に明瞭な回答を行なってください。
            丁寧な口調で信用を得ると良いでしょう。
            """,
        ),
        ("user", "{input}"),
    ]
)


_chat = ChatOpenAI(model=os.environ["CHAT_MODEL"])

_chain: Runnable[Any, Any] = (
    RunnablePassthrough()
    | {
        "input": RunnablePassthrough(),
        "class": (
            {"topic": RunnablePassthrough()}
            | _classify_prompt
            | _chat
            | StrOutputParser()
        ),
    }
    | RunnableBranch(
        (lambda input: input["class"] == "挨拶", _greeting),
        (lambda input: input["class"] == "質問", _qa),
        lambda input: "「返答できません。」と返してください。",
    )
    | _chat
    | StrOutputParser()
)

add_routes(router, _chain, path="/chat")
