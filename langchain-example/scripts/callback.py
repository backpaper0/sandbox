from langchain_core.agents import AgentAction, AgentFinish
from langchain_core.callbacks import BaseCallbackHandler
from langchain_core.messages import BaseMessage
from langchain_core.outputs import LLMResult
from typing import Any, Dict, List, Union

def _get_name(serialized: Dict[str, Any]) -> Any:
    if "name" in serialized:
        return serialized["name"]
    if "repr" in serialized:
        return serialized["repr"]
    return serialized

class MyCallbackHandler(BaseCallbackHandler):

    def on_llm_start(self, serialized: Dict[str, Any], prompts: List[str], **kwargs: Any) -> Any:
        print(f"on_llm_start {_get_name(serialized)} prompts={prompts}")

    def on_chat_model_start(self, serialized: Dict[str, Any], messages: List[List[BaseMessage]], **kwargs: Any) -> Any:
        print(f"on_chat_model_start {_get_name(serialized)} messages={messages}")

    def on_llm_new_token(self, token: str, **kwargs: Any) -> Any:
        print(f"on_llm_new_token token={token}")

    def on_llm_end(self, response: LLMResult, **kwargs: Any) -> Any:
        print(f"on_llm_end response={response}")

    def on_llm_error(self, error: BaseException, **kwargs: Any) -> Any:
        print(f"on_llm_error error={error}")

    def on_chain_start(self, serialized: Dict[str, Any], inputs: Dict[str, Any], **kwargs: Any) -> Any:
        print(f"on_chain_start {_get_name(serialized)} inputs={inputs}")

    def on_chain_end(self, outputs: Dict[str, Any], **kwargs: Any) -> Any:
        print(f"on_chain_end outputs={outputs}")

    def on_chain_error(self, error: BaseException, **kwargs: Any) -> Any:
        print(f"on_chain_error error={error}")

    def on_tool_start(self, serialized: Dict[str, Any], input_str: str, **kwargs: Any) -> Any:
        print(f"on_tool_start {_get_name(serialized)}, input_str={input_str}")

    def on_tool_end(self, output: str, **kwargs: Any) -> Any:
        print(f"on_tool_end output={output}")

    def on_tool_error(self, error: BaseException, **kwargs: Any) -> Any:
        print(f"on_tool_error error={error}")

    def on_text(self, text: str, **kwargs: Any) -> Any:
        print(f"on_text text={text}")

    def on_agent_action(self, action: AgentAction, **kwargs: Any) -> Any:
        print(f"on_agent_action")

    def on_agent_finish(self, finish: AgentFinish, **kwargs: Any) -> Any:
        print(f"on_agent_finish")
