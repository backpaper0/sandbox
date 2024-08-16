import os
from datetime import datetime

from dotenv import load_dotenv
from fastapi import FastAPI
from langchain_core.output_parsers import StrOutputParser
from langchain_openai.chat_models import AzureChatOpenAI
from langserve import add_routes
from opentelemetry.instrumentation.langchain import LangchainInstrumentor
from promptflow.tracing import start_trace

load_dotenv()

start_trace(collection=f"{datetime.today()}")

instrumentor = LangchainInstrumentor()
if not instrumentor.is_instrumented_by_opentelemetry:
    instrumentor.instrument()

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
