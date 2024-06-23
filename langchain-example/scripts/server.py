import time
from dataclasses import dataclass
from typing import Any, AsyncIterator, Dict, Iterator

from dotenv import load_dotenv
from fastapi import FastAPI
from langchain_community.chat_message_histories import ChatMessageHistory
from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.runnables import (
    Runnable,
    RunnableConfig,
    RunnableGenerator,
    RunnableLambda,
    RunnablePassthrough,
)
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_core.runnables.utils import AddableDict
from langchain_core.tracers import ConsoleCallbackHandler
from langchain_openai import ChatOpenAI
from langserve import add_routes
from starlette.requests import Request

load_dotenv()

app = FastAPI()

common_config = RunnableConfig({"callbacks": [ConsoleCallbackHandler()]})


def per_req_config_modifier(config: Dict[str, Any], r: Request) -> Dict[str, Any]:
    return {**common_config, **config}


strOutputParser = StrOutputParser()

model = ChatOpenAI()

add_routes(
    app,
    model,
    path="/chat1",
    per_req_config_modifier=per_req_config_modifier,
)


add_routes(
    app,
    RunnablePassthrough(),
    path="/passthrough",
    per_req_config_modifier=per_req_config_modifier,
)


@dataclass
class Msg:
    text: str
    times: int


def build_lambda() -> RunnableLambda[Msg, str]:
    def f(input: Msg) -> str:
        return input.text * input.times

    return RunnableLambda(f)


add_routes(
    app,
    build_lambda(),
    path="/lambda",
    per_req_config_modifier=per_req_config_modifier,
)


@dataclass
class Msg2:
    text: str
    sleep: float


def build_generator() -> RunnableGenerator[Msg2, str]:
    async def g(input: AsyncIterator[Msg2]) -> AsyncIterator[str]:
        async for msg in input:
            for c in msg.text:
                yield c
                time.sleep(msg.sleep)

    return RunnableGenerator(g)


add_routes(
    app,
    build_generator(),
    path="/generator",
    per_req_config_modifier=per_req_config_modifier,
)


def build_chain1() -> Runnable:
    async def gen1(input: AsyncIterator[str]) -> AsyncIterator[str]:
        async for s in input:
            for i in range(1, 4):
                time.sleep(1)
                yield f"foo{s}{i}"

    async def gen2(input: AsyncIterator[str]) -> AsyncIterator[str]:
        async for s in input:
            for i in range(1, 4):
                time.sleep(0.5)
                yield f"bar{s}{i}"

    async def gen3(input: AsyncIterator[Any]) -> AsyncIterator[Any]:
        async for elm in input:
            yield elm
        yield AddableDict({"foobar": "foobar"})

    return (
        RunnablePassthrough()
        | {
            "foo": gen1,
            "bar": gen2,
        }
        | gen3
    )


add_routes(
    app,
    build_chain1(),
    path="/chain1",
    input_type=str,
    per_req_config_modifier=per_req_config_modifier,
)


def build_chat2() -> RunnableWithMessageHistory:
    store = {}

    def get_session_history(session_id: str) -> BaseChatMessageHistory:
        if session_id not in store:
            store[session_id] = ChatMessageHistory()
        return store[session_id]

    prompt = ChatPromptTemplate.from_messages(
        [
            MessagesPlaceholder("history"),
            ("human", "{question}"),
        ]
    )

    return RunnableWithMessageHistory(
        runnable=(prompt | model | strOutputParser),  # type: ignore[arg-type]
        get_session_history=get_session_history,
        input_messages_key="question",
        # output_messages_key="output",
        history_messages_key="history",
    )


add_routes(
    app,
    build_chat2(),
    path="/chat2",
    per_req_config_modifier=per_req_config_modifier,
)


# AIの回答の前後にテキストを追加する
def build_chat3() -> Runnable:

    def transform(input: Iterator[str]) -> Iterator[str]:
        yield "[回答開始]\n"
        for chunk in input:
            yield chunk
        yield "\n[回答終了]"

    async def atransform(input: AsyncIterator[str]) -> AsyncIterator[str]:
        yield "[回答開始]\n"
        async for chunk in input:
            yield chunk
        yield "\n[回答終了]"

    return model | strOutputParser | RunnableGenerator(transform, atransform)


add_routes(
    app,
    build_chat3(),
    path="/chat3",
    input_type=str,
    per_req_config_modifier=per_req_config_modifier,
)
