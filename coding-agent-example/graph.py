from langgraph.graph import StateGraph

from nodes import exit, generate, user_input
from state import State

builder = StateGraph(State)


builder.add_node("user_input", user_input)
builder.add_node("generate", generate)
builder.add_node("exit", exit)


builder.set_entry_point("user_input")
builder.add_edge("generate", "user_input")
builder.set_finish_point("exit")
