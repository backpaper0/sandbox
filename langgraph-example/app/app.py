from operator import add
from typing import Annotated, Any, AsyncIterator, Tuple, cast
from fastapi import FastAPI
from langgraph.graph import StateGraph
from pydantic import BaseModel
from dotenv import load_dotenv
from langchain_core.messages import BaseMessage, AIMessageChunk
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


@app.get("/chat/stream")
async def chat_stream(query: str) -> StreamingResponse:
    async def messages() -> AsyncIterator[str]:
        iterator = graph.astream({"query": query}, stream_mode="messages")
        cast_iterator = cast(
            AsyncIterator[Tuple[AIMessageChunk, dict[str, Any]]], iterator
        )
        async for chunk, metadata in cast_iterator:
            if (
                isinstance(chunk.content, str)
                and chunk.content
                and "langgraph_node" in metadata
                and metadata["langgraph_node"] == "chat"
            ):
                with StringIO() as writer:
                    writer.write("event: message\n")
                    for line in line_sep.split(chunk.content):
                        if isinstance(line, str):
                            writer.write(f"data: {line}\n")
                    writer.write("\n")
                    yield writer.getvalue()
        with StringIO() as writer:
            writer.write("event: end\n")
            writer.write("data:\n")
            writer.write("\n")
            yield writer.getvalue()

    return StreamingResponse(messages(), media_type="text/event-stream")


app.mount("/", StaticFiles(directory="static"))
