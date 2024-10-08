"""
埋め込みクライアントを提供するモジュールです。
"""

import os
from typing import Awaitable, Callable

from openai import AsyncAzureOpenAI, AsyncOpenAI

EmbeddingsClient = Callable[[str], Awaitable[list[float]]]


def get_embeddings_client() -> EmbeddingsClient:
    """
    埋め込みクライアントを返します。

    Returns:
        EmbeddingsClient: 埋め込みクライアントを返します。
    """

    use_azure = os.getenv("AZURE_OPENAI_ENDPOINT") is not None

    if use_azure:
        print("Azure OpenAIを使用")

    client = AsyncAzureOpenAI() if use_azure else AsyncOpenAI()

    embeddings_model = os.environ[
        "EMBEDDINGS_DEPLOYMENT_NAME" if use_azure else "EMBEDDINGS_MODEL"
    ]

    async def _f(text: str) -> list[float]:
        resp = await client.embeddings.create(input=text, model=embeddings_model)
        vector = resp.data[0].embedding
        return vector

    return _f
