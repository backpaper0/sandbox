import asyncio
from typing import Annotated, Any
from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from langchain_openai.chat_models import ChatOpenAI
from dotenv import load_dotenv
from pydantic import BaseModel
from langchain_core.messages import BaseMessage, HumanMessage
from langgraph.graph.message import add_messages
from langgraph.checkpoint.sqlite.aio import AsyncSqliteSaver
from langgraph.checkpoint.base import BaseCheckpointSaver
from argparse import ArgumentParser
from langchain_core.runnables import RunnableConfig

load_dotenv()


class State(BaseModel):
    query: str
    messages: Annotated[list[BaseMessage], add_messages]
    answer: str | None = None


async def chat(state: State) -> dict[str, Any]:
    query = HumanMessage(state.query)
    chat = ChatOpenAI(model="gpt-4o-mini")
    message = await chat.ainvoke(state.messages + [query])
    answer = (
        message.content if isinstance(message.content, str) else str(message.content)
    )
    return {"messages": [query, message], "answer": answer}


def _build_graph(checkpointer: BaseCheckpointSaver) -> CompiledStateGraph:
    graph_builder = StateGraph(State)

    graph_builder.add_node("chat", chat)

    graph_builder.set_entry_point("chat")
    graph_builder.set_finish_point("chat")

    return graph_builder.compile(checkpointer=checkpointer)


async def main(query: str, thread_id: str) -> None:
    async with AsyncSqliteSaver.from_conn_string("checkpoint.db") as checkpointer:
        config = RunnableConfig()
        config["configurable"] = {"thread_id": thread_id}
        if query:
            graph = _build_graph(checkpointer)

            input = {"query": query}
            output = await graph.ainvoke(input, config)
            print(output["answer"])
        else:
            checkpoint = await checkpointer.aget(config)
            if checkpoint:
                channel_values = checkpoint["channel_values"]
                state = State(**channel_values)
                for message in state.messages:
                    print(type(message))
                    print(message.content)
                    print()


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument("-q", "--query", type=str, default="")
    parser.add_argument("-t", "--thread_id", type=str, default="0")
    args = parser.parse_args()
    asyncio.run(main(query=args.query, thread_id=args.thread_id))
