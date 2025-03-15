from operator import add
from typing import Annotated, Any, AsyncIterator, Callable, Tuple, TypeVar, cast
from fastapi import FastAPI
from langgraph.graph import StateGraph
from pydantic import BaseModel
from dotenv import load_dotenv
from langchain_core.messages import BaseMessage, AIMessageChunk, BaseMessageChunk
from langchain_openai.chat_models import ChatOpenAI
from fastapi.responses import StreamingResponse
from fastapi.staticfiles import StaticFiles
from io import StringIO
import re

load_dotenv()


class State(BaseModel):
    query: str
    messages: Annotated[list[BaseMessage], add] = []


chat_model = ChatOpenAI(model="gpt-4o-mini")


async def chat_node(state: State) -> dict[str, Any]:
    ai_message = await chat_model.ainvoke(state.query)
    return {"messages": [ai_message]}


builder = StateGraph(State)
builder.add_node("chat", chat_node)
builder.set_entry_point("chat")
builder.set_finish_point("chat")

graph = builder.compile()

app = FastAPI()

line_sep = re.compile("\r\n|\r|\n")

T = TypeVar("T")


def sse_response(
    iterator: AsyncIterator[T], serializer: Callable[[T], str] = str
) -> StreamingResponse:
    async def sse() -> AsyncIterator[str]:
        async for chunk in iterator:
            with StringIO() as writer:
                writer.write("event: message\n")
                for line in line_sep.split(serializer(chunk)):
                    if isinstance(line, str):
                        writer.write(f"data: {line}\n")
                writer.write("\n")
                yield writer.getvalue()
        with StringIO() as writer:
            writer.write("event: end\n")
            writer.write("data:\n")
            writer.write("\n")
            yield writer.getvalue()

    return StreamingResponse(sse(), media_type="text/event-stream")


async def messages(
    iterator: AsyncIterator[dict[str, Any] | Any], node: str
) -> AsyncIterator[str]:
    async for chunk, metadata in cast(
        AsyncIterator[Tuple[BaseMessageChunk, dict[str, Any]]], iterator
    ):
        if (
            isinstance(chunk, AIMessageChunk)
            and isinstance(chunk.content, str)
            and chunk.content
            and "langgraph_node" in metadata
            and metadata["langgraph_node"] == node
        ):
            yield chunk.content


@app.get("/chat/stream")
async def chat_stream(query: str) -> StreamingResponse:
    iterator = graph.astream({"query": query}, stream_mode="messages")
    node = "chat"
    return sse_response(messages(iterator, node))


app.mount("/", StaticFiles(directory="static"))
