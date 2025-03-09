# https://langchain-ai.github.io/langgraph/how-tos/streaming-tokens/
import asyncio
from typing import Any, Awaitable, Callable
from langchain_ollama.chat_models import ChatOllama
from langchain_core.language_models import BaseChatModel
from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from typing_extensions import TypedDict
from argparse import ArgumentParser


class State(TypedDict):
    query: str
    answer: str


def build_call_model(
    chat: BaseChatModel,
) -> Callable[[State], Awaitable[dict[str, Any]]]:
    async def call_model(state: State):
        query = state["query"]
        # なぜこれでストリーミングできるのか、、、
        # と思ったけどたぶんコールバックかな。
        message = await chat.ainvoke(query)
        content = message.content
        if isinstance(content, str):
            return {"answer": content}
        return {"answer": str(content)}

    return call_model


def build_graph(model: str) -> CompiledStateGraph:
    chat = ChatOllama(model=model)

    graph_builder = StateGraph(State)
    graph_builder.add_node("call_model", build_call_model(chat))

    graph_builder.set_entry_point("call_model")
    graph_builder.set_finish_point("call_model")

    return graph_builder.compile()


async def main(query: str, model: str) -> None:
    graph = build_graph(model)
    input = {"query": query}
    async_iterator = graph.astream(input, debug=True, stream_mode="messages")
    async for msg, metadata in async_iterator:
        if isinstance(metadata, dict) and metadata["langgraph_node"] == "call_model":
            print(msg)


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-q", "--query", type=str, required=True)
    parser.add_argument(
        "-m", "--model", default="hf.co/mmnga/llm-jp-3-7.2b-instruct3-gguf"
    )
    args = parser.parse_args()
    asyncio.run(main(query=args.query, model=args.model))
