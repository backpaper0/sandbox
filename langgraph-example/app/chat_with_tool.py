import asyncio
from langchain_community.tools import WikipediaQueryRun
from langchain_community.utilities import WikipediaAPIWrapper
from langchain_core.messages import HumanMessage, BaseMessage, AIMessageChunk
from typing import Annotated, Any, Awaitable, Callable
from langgraph.graph.state import StateGraph, CompiledStateGraph
from langgraph.graph.message import add_messages
from langgraph.prebuilt import ToolNode, tools_condition
from pydantic import BaseModel
from langchain_openai.chat_models import ChatOpenAI
from pydantic import SecretStr
from pydantic_settings import BaseSettings, SettingsConfigDict
from langchain_core.runnables import Runnable
from argparse import ArgumentParser

import mlflow


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", extra="ignore")
    openai_api_key: str
    model: str = "gpt-4o-mini"


settings = Settings()  # type: ignore


class State(BaseModel):
    messages: Annotated[list, add_messages]


api_wrapper = WikipediaAPIWrapper(  # type: ignore
    top_k_results=1, doc_content_chars_max=1000, lang="ja"
)
tool = WikipediaQueryRun(api_wrapper=api_wrapper)
tools = [tool]
tools_node = ToolNode(tools)

chat = ChatOpenAI(model=settings.model, api_key=SecretStr(settings.openai_api_key))
chat_with_tools = chat.bind_tools(tools=tools)


def build_chatbot_node(
    chat: Runnable[Any, BaseMessage],
) -> Callable[[State], Awaitable[dict[str, Any]]]:
    async def chatbot(state: State) -> dict[str, Any]:
        ai_message = await chat.ainvoke(state.messages)
        return {"messages": [ai_message]}

    return chatbot


def build_chat_with_tools_graph() -> CompiledStateGraph:
    chatbot = build_chatbot_node(chat_with_tools)

    builder = StateGraph(State)

    builder.add_node("chatbot", chatbot)
    builder.add_node("tools", tools_node)
    builder.add_conditional_edges("chatbot", tools_condition)
    builder.add_edge("tools", "chatbot")
    builder.set_entry_point("chatbot")

    return builder.compile()


def _build_chat_only_graph() -> CompiledStateGraph:
    chatbot = build_chatbot_node(chat)

    builder = StateGraph(State)

    builder.add_node("chatbot", chatbot)
    builder.set_entry_point("chatbot")
    builder.set_finish_point("chatbot")

    return builder.compile()


async def ainvoke(graph: CompiledStateGraph, query: str, stream: bool) -> None:
    input = {"messages": [HumanMessage(query)]}
    if stream:
        async for msg, _ in graph.astream(input, stream_mode="messages"):
            if isinstance(msg, AIMessageChunk) and msg.content:
                print(msg.content)
    else:
        return_value = await graph.ainvoke(input)
        state = State(**return_value)
        msg = state.messages[-1]
        print(msg.content)


async def main(query: str, stream: bool) -> None:
    print("*** LLMのみ ***")
    with mlflow.start_run():
        await ainvoke(_build_chat_only_graph(), query, stream)

    print()

    print("*** LLM + Wikipediaの検索 ***")
    with mlflow.start_run():
        await ainvoke(build_chat_with_tools_graph(), query, stream)


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("--query", "-q", type=str, required=True)
    parser.add_argument("-s", "--stream", action="store_true")
    args = parser.parse_args()
    asyncio.run(main(query=args.query, stream=args.stream))
