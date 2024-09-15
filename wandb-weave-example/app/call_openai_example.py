import json

import weave
from dotenv import load_dotenv
from langchain_openai.chat_models import ChatOpenAI

load_dotenv()

weave.init("call_openai_example")

chat = ChatOpenAI()

result = chat.invoke("PythonでHello Worldを出力するコードを教えて。")

print(json.dumps(result.dict(), indent=2, ensure_ascii=False))
