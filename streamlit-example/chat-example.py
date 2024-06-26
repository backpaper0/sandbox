import streamlit as st
from langchain_core.messages import BaseMessage, HumanMessage
from langchain_openai.chat_models import ChatOpenAI


def write_message(message: BaseMessage) -> None:
    msg = st.chat_message(name=message.type)
    msg.write(message.content)


chat = ChatOpenAI(
    model="gpt-4o",
    api_key=st.secrets["openai_api_key"],  # from ~/.streamlit/secrets.toml
)


messages: list[BaseMessage] = st.session_state.get("messages", [])


for message in messages:
    write_message(message)

prompt = st.chat_input()

if prompt is not None:
    human_message = HumanMessage(prompt)
    write_message(human_message)
    messages.append(human_message)

    input = [str(message.content) for message in messages]

    ai_message = chat.invoke(input)
    write_message(ai_message)
    messages.append(ai_message)

    st.session_state["messages"] = messages
