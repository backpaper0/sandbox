import json
import os
import uuid
from typing import Any

from dotenv import load_dotenv
from langchain_community.chat_message_histories import SQLChatMessageHistory
from langchain_community.chat_message_histories.sql import BaseMessageConverter
from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.messages import AIMessage, BaseMessage, HumanMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_openai.chat_models import ChatOpenAI
from sqlalchemy import create_engine
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from sqlalchemy.sql import select
from sqlalchemy.types import Text
from ulid import ULID

load_dotenv()

database_url = os.environ["DATABASE_URL"]

engine = create_engine(database_url)


class Base(DeclarativeBase):
    pass


class MyMessage(Base):
    __tablename__ = "my_messages"
    id = mapped_column(Text, primary_key=True)
    message: Mapped[str]
    chat_id: Mapped[str]


class MyMessageConverter(BaseMessageConverter):
    def from_sql_model(self, sql_message: MyMessage) -> BaseMessage:
        message = json.loads(sql_message.message)
        match message["type"]:
            case "ai":
                return AIMessage(**message)
            case "human":
                return HumanMessage(**message)
        raise Exception("error")

    def to_sql_model(self, message: BaseMessage, session_id: str) -> MyMessage:
        return MyMessage(
            id=str(ULID()),
            message=json.dumps(message.dict(), ensure_ascii=False),
            chat_id=session_id,
        )

    def get_sql_model_class(self) -> Any:
        return MyMessage


def get_by_session_id(session_id: str) -> BaseChatMessageHistory:
    return SQLChatMessageHistory(
        session_id=session_id,
        connection=engine,
        custom_message_converter=MyMessageConverter(),
        table_name="my_messages",
        session_id_field_name="chat_id",
    )


prompt = ChatPromptTemplate.from_messages(
    [
        MessagesPlaceholder(variable_name="history"),
        ("human", "{question}"),
    ]
)

chain = prompt | ChatOpenAI(model="gpt-4o")

chain_with_history = RunnableWithMessageHistory(
    runnable=chain,  # type: ignore[arg-type]
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


def get_my_messages(session_id: str) -> list[dict[str, Any]]:
    with engine.connect() as con:
        result = con.execute(
            select(MyMessage)
            .where(MyMessage.chat_id == session_id)
            .order_by(MyMessage.id.asc())
        )
        return [
            dict(
                id=row.id,
                message=row.message,
                chat_id=row.chat_id,
            )
            for row in result.all()
        ]


dump(
    chain_with_history.invoke(
        {"question": "1以上100以下の整数の中から適当に3つ挙げて。"},
        config={"configurable": {"session_id": session_id}},
    ).dict()
)

dump(get_my_messages(session_id))


dump(
    chain_with_history.invoke(
        {"question": "それらを合計して。"},
        config={"configurable": {"session_id": session_id}},
    ).dict()
)


dump(get_my_messages(session_id))

engine.dispose()
