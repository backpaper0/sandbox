from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from typing_extensions import TypedDict


class State(TypedDict):
    input: str
    output: str


async def node1(state: State):
    return {}


async def node2(state: State):
    return {"output": "2"}


async def node3(state: State):
    return {"output": "3"}


async def cond(state: State) -> str:
    if state["input"] == "yes":
        return "node2"
    return "node3"


def build_conditionoal_branch_graph() -> CompiledStateGraph:
    builder = StateGraph(State)

    builder.add_node(node1)
    builder.add_node(node2)
    builder.add_node(node3)

    builder.set_entry_point("node1")
    builder.add_conditional_edges("node1", cond, ["node2", "node3"])
    builder.set_finish_point("node2")
    builder.set_finish_point("node3")

    return builder.compile()
