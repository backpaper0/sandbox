import mlflow

import pandas as pd
from dotenv import load_dotenv
import openai
from mlflow.metrics.genai import answer_correctness, answer_similarity, answer_relevance

mlflow.langchain.autolog()

[load_dotenv(dotenv_path) for dotenv_path in [".env", ".env.openai"]]


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

model = mlflow.openai.log_model(
    model="gpt-4o-mini",
    task=openai.chat.completions,
    artifact_path="model",
)

model_uri = "openai:/gpt-4o-mini"

mlflow.evaluate(
    model.model_uri,
    eval_df,
    targets="ground_truth",
    extra_metrics=[
        answer_similarity(model=model_uri),
        answer_correctness(model=model_uri),
        answer_relevance(model=model_uri),
    ],
)
