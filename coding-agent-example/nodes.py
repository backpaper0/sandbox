from typing import Literal

from langchain_core.messages import HumanMessage
from langgraph.prebuilt import create_react_agent
from langgraph.types import Command, interrupt

from state import State
from tools import edit_file, read_file, write_file

agent = create_react_agent(
    model="ollama:gpt-oss:20b",
    tools=[
        read_file,
        write_file,
        edit_file,
    ],
)


def user_input(state: State) -> Command[Literal["generate", "exit"]]:
    while True:
        query = interrupt("prompt> ")
        query = query.strip() if isinstance(query, str) else ""
        if not query:
            continue
        else:
            break
    if query == "/exit":
        return Command(goto="exit")
    return Command(goto="generate", update={"query": query})


def generate(state: State) -> State:
    user_message = HumanMessage(state["query"])
    messages = state.get("messages", []) + [user_message]
    response = agent.invoke({"messages": messages})
    messages = response["messages"]
    print(messages[-1].content)
    return {"messages": messages, "query": ""}


def exit(state: State) -> State:
    return state
