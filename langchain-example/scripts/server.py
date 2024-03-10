import time
from typing import AsyncGenerator, AsyncIterator, Callable, Iterator
from fastapi import FastAPI
from langserve import add_routes
from langchain_openai import ChatOpenAI
from dotenv import load_dotenv
from langchain_core.runnables import (
    Runnable,
    RunnableAssign,
    RunnableBinding,
    RunnableBranch,
    RunnableGenerator,
    RunnableLambda,
    RunnableParallel,
    RunnablePassthrough,
    RunnablePick,
    RunnableWithFallbacks,
)
from dataclasses import dataclass

load_dotenv()

app = FastAPI()

add_routes(
    app,
    ChatOpenAI(),
    path="/chat",
)

add_routes(
    app,
    RunnablePassthrough(),
    path="/passthrough",
)

@dataclass
class Msg:
    text: str
    times: int

def f(input: Msg) -> str:
    return input.text * input.times

add_routes(
    app,
    RunnableLambda(f),
    path="/lambda",
)

@dataclass
class Msg2:
    text: str
    sleep: float

async def g(input: AsyncIterator[Msg2]) -> AsyncIterator[str]:
    async for msg in input:
        for c in msg.text:
            yield c
            time.sleep(msg.sleep)

add_routes(
    app,
    RunnableGenerator(g),
    path="/generator",
)
