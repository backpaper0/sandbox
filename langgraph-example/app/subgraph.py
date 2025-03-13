from operator import add
from typing import Annotated
from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from pydantic import BaseModel


class State(BaseModel):
    input: str
    output1: Annotated[list[int], add]
    output2: Annotated[list[int], add]


class SubState(BaseModel):
    output2: Annotated[list[int], add]


async def node1(state: State):
    return {"output1": [1]}


async def node2(state: State):
    return {"output1": [2]}


async def node3(state: SubState):
    return {"output2": [3]}


async def node4(state: SubState):
    return {"output2": [4]}


def _build_subgraph() -> CompiledStateGraph:
    sub_builder = StateGraph(State)

    sub_builder.add_node(node3)
    sub_builder.add_node(node4)

    sub_builder.set_entry_point("node3")
    sub_builder.add_edge("node3", "node4")
    sub_builder.set_finish_point("node4")

    return sub_builder.compile()


def build_with_subgraph_graph() -> CompiledStateGraph:
    builder = StateGraph(State)

    builder.add_node(node1)
    builder.add_node("sub_graph", _build_subgraph())
    builder.add_node(node2)

    builder.set_entry_point("node1")
    builder.add_edge("node1", "sub_graph")
    builder.add_edge("sub_graph", "node2")
    builder.set_finish_point("node2")

    return builder.compile()
