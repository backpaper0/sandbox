from typing import TypedDict

from langchain_core.messages import BaseMessage


class State(TypedDict):
    query: str
    messages: list[BaseMessage]
