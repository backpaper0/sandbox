import uuid

from dotenv import load_dotenv
from langchain_core.runnables import RunnableConfig
from langfuse.langchain import CallbackHandler
from langgraph.checkpoint.memory import InMemorySaver
from langgraph.types import Command

from graph import builder

load_dotenv()

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

langfuse_handler = CallbackHandler()
session_id = str(uuid.uuid4())
config: RunnableConfig = {
    "configurable": {"thread_id": session_id},
    "callbacks": [langfuse_handler],
    "metadata": {
        "langfuse_session_id": session_id,
    },
}
result = graph.invoke({}, config)
while "__interrupt__" in result:
    query = input(result["__interrupt__"][0].value)
    result = graph.invoke(Command(resume=query), config=config)
