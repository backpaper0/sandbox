import asyncio
import json
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
from sqlalchemy.ext.asyncio import create_async_engine
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from sqlalchemy.sql import select
from sqlalchemy.types import Text
from ulid import ULID

load_dotenv()

database_url = "sqlite+aiosqlite:///example.db"

engine = create_async_engine(database_url)


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


# 以下のエラーが発生するので、とりあえずエラーが出ないよう、add_messagesメソッドをアドホックにオーバーライドする
# Error in RootListenersTracer.on_chain_end callback: RuntimeError("There is no current event loop in thread 'asyncio_0'.")
# InMemoryChatMessageHistory だとエラーが発生しなかったので SQLChatMessageHistory の問題か
def get_by_session_id(session_id: str) -> BaseChatMessageHistory:
    obj = SQLChatMessageHistory(
        session_id=session_id,
        connection=engine,
        custom_message_converter=MyMessageConverter(),
        table_name="my_messages",
        session_id_field_name="chat_id",
    )

    def custom_add_messages(self, messages):  # type: ignore
        asyncio.run(self.aadd_messages(messages))

    obj.add_messages = custom_add_messages.__get__(obj, SQLChatMessageHistory)  # type: ignore
    return obj


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


async def get_my_messages(session_id: str) -> list[dict[str, Any]]:
    async with engine.connect() as con:
        result = await con.execute(select(MyMessage).where(MyMessage.chat_id == session_id).order_by(MyMessage.id.asc()))
        return [
            dict(
                id=row.id,
                message=row.message,
                chat_id=row.chat_id,
            )
            for row in result.all()
        ]


async def main() -> None:
    try:
        dump(
            (
                await chain_with_history.ainvoke(
                    {"question": "1以上100以下の整数の中から適当に3つ挙げて。"},
                    config={"configurable": {"session_id": session_id}},
                )
            ).dict()
        )

        dump(await get_my_messages(session_id))

        dump(
            (
                await chain_with_history.ainvoke(
                    {"question": "それらを合計して。"},
                    config={"configurable": {"session_id": session_id}},
                )
            ).dict()
        )

        dump(await get_my_messages(session_id))
    finally:
        await engine.dispose()


asyncio.run(main())
