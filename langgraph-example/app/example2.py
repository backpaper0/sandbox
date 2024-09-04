"""
途中で2つに分岐するグラフを作成する例
"""

import operator
from typing import Annotated

from langgraph.graph import StateGraph
from typing_extensions import TypedDict


class State(TypedDict):
    value: Annotated[list, operator.add]


def node1(state: State):
    return {"value": [1]}


def node2(state: State):
    return {"value": [2]}


def node3(state: State):
    return {"value": [3]}


def node4(state: State):
    return {"value": [4]}


graph_builder = StateGraph(State)
graph_builder.add_node("node1", node1)
graph_builder.add_node("node2", node2)
graph_builder.add_node("node3", node3)
graph_builder.add_node("node4", node4)

graph_builder.set_entry_point("node1")
graph_builder.add_edge("node1", "node2")
graph_builder.add_edge("node1", "node3")
graph_builder.add_edge("node2", "node4")
graph_builder.add_edge("node3", "node4")
graph_builder.set_finish_point("node4")

graph = graph_builder.compile()


result = graph.invoke({"value": []}, debug=True)
print(f"Result: {result}")

# graph.get_graph().print_ascii()
# print(graph.get_graph().draw_mermaid())
