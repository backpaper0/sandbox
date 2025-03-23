from typing import cast
import mlflow

import pandas as pd
from mlflow.metrics.genai import answer_correctness, answer_similarity, answer_relevance
from dotenv import load_dotenv
from langchain_ollama.chat_models import ChatOllama
from langchain_core.language_models import LanguageModelInput

mlflow.langchain.autolog()

[load_dotenv(dotenv_path) for dotenv_path in [".env", ".env.ollama"]]


class OllamaModel(mlflow.pyfunc.PythonModel):  # type: ignore
    def predict(self, context, model_input: list[str], params=None) -> list[str]:
        llm = ChatOllama(model="hf.co/bartowski/google_gemma-3-12b-it-GGUF")
        ai_messages = llm.batch(
            cast(list[LanguageModelInput], model_input),
            config={"max_concurrency": 5},
        )
        return [cast(str, ai_message.content) for ai_message in ai_messages]


eval_df = pd.DataFrame(
    {
        "inputs": [
            "「MLflowを試す」って言ってください。",
            "「MLflowを試す」って言ってください。余計な言葉を付け足さず、おうむ返ししてください。",
            "2 + 3は？",
            "こんにちは！",
        ],
        "ground_truth": [
            "MLflowを試す",
            "MLflowを試す",
            "5です。",
            "こんにちは！",
        ],
    }
)

model = mlflow.pyfunc.log_model(
    python_model=OllamaModel(),
    artifact_path="model",
)

# https://mlflow.org/docs/latest/api_reference/python_api/mlflow.metrics.html#mlflow.metrics.genai.make_genai_metric
# > proxy_url – (Optional) Proxy URL to be used for the judge model.
# > This is useful when the judge model is served via a proxy endpoint, not directly via LLM provider services.
# > If not specified, the default URL for the LLM provider will be used (e.g., https://api.openai.com/v1/chat/completions for OpenAI chat models).
# https://ollama.com/blog/openai-compatibility
# > Ollama now has built-in compatibility with the OpenAI Chat Completions API, making it possible to use more tooling and applications with Ollama locally.
model_uri = "openai:/hf.co/bartowski/google_gemma-3-12b-it-GGUF"
proxy_url = "http://localhost:11434/v1/chat/completions"

mlflow.evaluate(
    model.model_uri,
    eval_df,
    targets="ground_truth",
    extra_metrics=[
        answer_similarity(model=model_uri, proxy_url=proxy_url),
        answer_correctness(model=model_uri, proxy_url=proxy_url),
        answer_relevance(model=model_uri, proxy_url=proxy_url),
    ],
)
