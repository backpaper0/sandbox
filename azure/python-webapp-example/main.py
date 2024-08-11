import os
from typing import Any, Dict

from dotenv import load_dotenv
from fastapi import FastAPI
from langchain_core.output_parsers import StrOutputParser
from langchain_openai.chat_models import AzureChatOpenAI
from langserve import add_routes

load_dotenv()

app = FastAPI()


@app.get("/")
async def hello_fly():
    return "Hello World"


chat = AzureChatOpenAI(
    azure_deployment=os.environ["AZURE_OPENAI_DEPLOYMENT_NAME"],
)

chain = chat | StrOutputParser()

add_routes(
    app,
    chain,
    path="/chat",
)
