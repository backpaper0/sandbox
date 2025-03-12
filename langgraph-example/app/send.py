from operator import add
from typing import Annotated, Any
from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from pydantic import BaseModel
from langgraph.types import Send


class State(BaseModel):
    input: int
    output: Annotated[list[str], add]


class SubState(BaseModel):
    index: int


async def node1(state: State) -> dict[str, Any]:
    return {"output": ["node1"]}


async def send_to_node2(state: State) -> list[Send]:
    return [Send("node2", SubState(index=i)) for i in range(state.input)]


async def node2(state: SubState) -> dict[str, Any]:
    return {"output": [f"node2#{state.index}"]}


def build_use_send_graph() -> CompiledStateGraph:
    builder = StateGraph(State)

    builder.add_node(node1)
    builder.add_node(node2)

    builder.set_entry_point("node1")
    builder.add_conditional_edges("node1", send_to_node2, ["node2"])
    builder.set_finish_point("node2")

    return builder.compile()
