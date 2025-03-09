from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from typing_extensions import TypedDict


class State(TypedDict):
    input: str
    output: str


async def node1(state: State):
    return {"output": f"{state['input']} bar"}


async def node2(state: State):
    return {"output": f"{state['output']} baz"}


async def node3(state: State):
    return {"output": f"{state['output']} qux"}


def build_simple_graph() -> CompiledStateGraph:
    builder = StateGraph(State)

    builder.add_node(node1)
    builder.add_node(node2)
    builder.add_node(node3)

    builder.set_entry_point("node1")
    builder.add_edge("node1", "node2")
    builder.add_edge("node2", "node3")
    builder.set_finish_point("node3")

    return builder.compile()
