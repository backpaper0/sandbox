from typing import Annotated, Any, Literal
from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from langgraph.types import Command
from pydantic import BaseModel
from operator import add


class State(BaseModel):
    input: bool
    output: Annotated[list[str], add]


async def node1(state: State) -> Command[Literal["node2", "node3"]]:
    return Command(
        update={"output": ["node1"]},
        goto="node2" if state.input else "node3",
    )


async def node2(state: State) -> dict[str, Any]:
    return {"output": ["node2"]}


async def node3(state: State) -> dict[str, Any]:
    return {"output": ["node3"]}


def build_use_command_graph() -> CompiledStateGraph:
    builder = StateGraph(State)

    builder.add_node(node1)
    builder.add_node(node2)
    builder.add_node(node3)

    builder.set_entry_point("node1")
    builder.set_finish_point("node2")
    builder.set_finish_point("node3")

    return builder.compile()
