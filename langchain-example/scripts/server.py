from callback import MyCallbackHandler
from dataclasses import dataclass
from dotenv import load_dotenv
from fastapi import FastAPI
from langchain_community.chat_message_histories import ChatMessageHistory
from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import (
    ChatPromptTemplate,
    MessagesPlaceholder,
)
from langchain_core.runnables import (
    Runnable,
    RunnableConfig,
    RunnableGenerator,
    RunnableLambda,
    RunnablePassthrough,
)
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_core.runnables.utils import AddableDict
from langchain_openai import ChatOpenAI
from langserve import add_routes
from typing import Any, AsyncIterator
import time

load_dotenv()

app = FastAPI()

config = RunnableConfig({
    "callbacks": [MyCallbackHandler()]
})

strOutputParser = StrOutputParser().with_config(config)

model = ChatOpenAI().with_config(config)

add_routes(
    app,
    model,
    path="/chat1",
)



add_routes(
    app,
    RunnablePassthrough().with_config(config),
    path="/passthrough",
)



@dataclass
class Msg:
    text: str
    times: int

def build_lambda():
    def f(input: Msg) -> str:
        return input.text * input.times
    return RunnableLambda(f).with_config(config)

add_routes(
    app,
    build_lambda(),
    path="/lambda",
)



@dataclass
class Msg2:
    text: str
    sleep: float

def build_generator():
    async def g(input: AsyncIterator[Msg2]) -> AsyncIterator[str]:
        async for msg in input:
            for c in msg.text:
                yield c
                time.sleep(msg.sleep)
    return RunnableGenerator(g).with_config(config)

add_routes(
    app,
    build_generator(),
    path="/generator",
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
                time.sleep(.5)
                yield f"bar{s}{i}"

    async def gen3(input: AsyncIterator[Any]) -> AsyncIterator[Any]:
        async for elm in input:
            yield elm
        yield AddableDict({ "foobar" : "foobar" })

    return (
        {
            "foo": RunnableGenerator(gen1).with_config(config),
            "bar": RunnableGenerator(gen2).with_config(config),
        }
        | RunnableGenerator(gen3).with_config(config)
    ).with_config(config)

add_routes(
    app,
    build_chain1(),
    path="/chain1",
    input_type=str,
)



def build_chat2():
    store = {}

    def get_session_history(session_id: str) -> BaseChatMessageHistory:
        if session_id not in store:
            store[session_id] = ChatMessageHistory()
        return store[session_id]

    prompt = ChatPromptTemplate.from_messages([
        MessagesPlaceholder("history"),
        ("human", "{question}"),
    ]).with_config(config)

    return (
        RunnableWithMessageHistory(
            runnable=(
                prompt
                | model
                | strOutputParser
            ).with_config(config),
            get_session_history=get_session_history,
            input_messages_key="question",
            # output_messages_key="output",
            history_messages_key="history",
        ).with_config(config)
    )

add_routes(
    app,
    build_chat2(),
    path="/chat2",
)