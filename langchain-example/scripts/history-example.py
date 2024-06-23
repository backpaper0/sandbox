import json
import os
import uuid
from typing import Any

from dotenv import load_dotenv
from langchain_community.chat_message_histories import SQLChatMessageHistory
from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_openai.chat_models import ChatOpenAI
from sqlalchemy import create_engine

load_dotenv()

database_url = os.environ["DATABASE_URL"]

engine = create_engine(database_url)


def get_by_session_id(session_id: str) -> BaseChatMessageHistory:
    return SQLChatMessageHistory(
        session_id=session_id,
        connection=engine,
    )


prompt = ChatPromptTemplate.from_messages(
    [
        MessagesPlaceholder(variable_name="history"),
        ("human", "{question}"),
    ]
)

chain = prompt | ChatOpenAI(model="gpt-4o")

chain_with_history = RunnableWithMessageHistory(
    runnable=chain,
    get_session_history=get_by_session_id,
    input_messages_key="question",
    history_messages_key="history",
)

session_id = uuid.uuid4().hex
print(f"session_id = {session_id}")


def dump(data: dict[str, Any] | list[dict[str, Any]]) -> None:
    print()
    print(
        json.dumps(
            data,
            ensure_ascii=False,
            indent=2,
        )
    )


dump(
    chain_with_history.invoke(
        {"question": "1以上100以下の整数の中から適当に3つ挙げて。"},
        config={"configurable": {"session_id": session_id}},
    ).dict()
)

dump([m.dict() for m in get_by_session_id(session_id).messages])


dump(
    chain_with_history.invoke(
        {"question": "それらを合計して。"},
        config={"configurable": {"session_id": session_id}},
    ).dict()
)


dump([m.dict() for m in get_by_session_id(session_id).messages])

engine.dispose()
