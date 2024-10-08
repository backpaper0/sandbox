"""
条件分岐の例
"""

import argparse
import operator
from typing import Annotated

from langgraph.graph import StateGraph
from typing_extensions import TypedDict


class State(TypedDict):
    value: Annotated[list, operator.add]
    x: bool


def node1(state: State):
    return {"value": [1]}


def node2(state: State):
    return {"value": [2]}


def node3(state: State):
    return {"value": [3]}


def node4(state: State):
    return {"value": [4]}


def cond1(state: State):
    if state["x"]:
        return "node3"
    return "node2"


graph_builder = StateGraph(State)
graph_builder.add_node("node1", node1)
graph_builder.add_node("node2", node2)
graph_builder.add_node("node3", node3)
graph_builder.add_node("node4", node4)

graph_builder.set_entry_point("node1")
# 第3引数で遷移先の候補を明示。そうしないとグラフが描けない
graph_builder.add_conditional_edges("node1", cond1, ["node2", "node3"])
graph_builder.add_edge("node2", "node4")
graph_builder.add_edge("node3", "node4")
graph_builder.set_finish_point("node4")

graph = graph_builder.compile()


parser = argparse.ArgumentParser()
parser.add_argument("-x", action="store_true", default=False)
args = parser.parse_args()

result = graph.invoke({"value": [], "x": args.x}, debug=True)
print(f"Result: {result}")

# graph.get_graph().print_ascii()
# print(graph.get_graph().draw_mermaid())
