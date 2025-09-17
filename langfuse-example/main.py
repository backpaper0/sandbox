from dotenv import load_dotenv
from langfuse import Langfuse
from langfuse.langchain import CallbackHandler
from langgraph.prebuilt import create_react_agent

load_dotenv()

langfuse = Langfuse()
langfuse_handler = CallbackHandler()


def get_weather(city: str) -> str:
    """Get weather for a given city."""
    return f"It's always sunny in {city}!"


agent = create_react_agent(
    model="ollama:gpt-oss:20b",
    tools=[get_weather],
    prompt="You are a helpful assistant",
)

agent.invoke(
    {"messages": [{"role": "user", "content": "what is the weather in sf"}]},
    config={"callbacks": [langfuse_handler]},
)
