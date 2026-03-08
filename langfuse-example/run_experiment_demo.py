import argparse
import json
from typing import cast

import numpy as np
from langfuse import Evaluation, get_client
from openai import OpenAI

eval_prompt_template = """
以下の2つのテキストを比較し、LLMの出力が期待する出力とどれだけ一致しているかを評価してください。

## 入力情報
- 質問: {input}
- 期待する出力: {expected_output}
- LLMの実際の出力: {output}

## 評価基準
以下の観点を総合して判断してください：
1. **事実の正確性** - 期待する出力と同じ事実・情報を含んでいるか
2. **意味的一致** - 表現が異なっても同じ意味を伝えているか
3. **完全性** - 期待する出力の重要な要素が含まれているか
4. **余分な情報** - 誤った情報や無関係な情報が含まれていないか

## 出力形式
以下のJSON形式のみで回答してください。説明文は不要です。

{{
  "score": <0.0〜1.0の数値>,
  "reason": "<採点理由を1〜2文で>"
}}

## スコアの目安
- 1.0: 完全に一致（意味・事実ともに正確）
- 0.8〜0.9: ほぼ正確（軽微な表現の違いのみ）
- 0.5〜0.7: 部分的に正確（重要な要素は含むが不完全）
- 0.2〜0.4: わずかに関連（一部正しいが主要な情報が欠落）
- 0.0〜0.1: 不正確または無関係
"""


class MyExperiment:
    task_chat_model: str
    eval_chat_model: str
    eval_embedding_model: str
    openai: OpenAI

    def __init__(
        self, *, task_chat_model: str, eval_chat_model: str, eval_embedding_model: str
    ) -> None:
        self.task_chat_model = task_chat_model
        self.eval_chat_model = eval_chat_model
        self.eval_embedding_model = eval_embedding_model
        self.openai = OpenAI()

    def task(self, *, item, **kwargs) -> str:
        question = item.input
        completion = self.openai.chat.completions.create(
            messages=[{"role": "user", "content": question}],
            model=self.task_chat_model,
        )
        return cast(str, completion.choices[0].message.content)

    def answer_correctness_evaluator(
        self, *, input, output, expected_output, **kwargs
    ) -> Evaluation:
        prompt = eval_prompt_template.format(
            input=input, output=output, expected_output=expected_output
        )
        completion = self.openai.chat.completions.create(
            messages=[{"role": "user", "content": prompt}],
            model=self.eval_chat_model,
            response_format={"type": "json_object"},
        )
        content = cast(str, completion.choices[0].message.content)
        result = json.loads(content)
        return Evaluation(
            name="answer_correctness",
            value=result["score"],
            comment=result["reason"],
            metadata={"model": self.eval_chat_model},
        )

    def answer_semantic_similarity_evaluator(
        self, *, input, output, expected_output, **kwargs
    ) -> Evaluation:
        embedding = self.openai.embeddings.create(
            input=[output, expected_output], model=self.eval_embedding_model
        )
        arr = [np.array(data.embedding) for data in embedding.data]
        a = arr[0]
        b = arr[1]
        cos_sim = np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b))
        return Evaluation(
            name="answer_semantic_similarity",
            value=cos_sim,
            metadata={"model": self.eval_embedding_model},
        )


def main(task_chat_model: str, eval_chat_model: str, eval_embedding_model: str) -> None:
    langfuse = get_client()

    dataset = langfuse.get_dataset("geography_quiz")

    experiment = MyExperiment(
        task_chat_model=task_chat_model,
        eval_chat_model=eval_chat_model,
        eval_embedding_model=eval_embedding_model,
    )

    result = dataset.run_experiment(
        name="Geography Quiz",
        description="Testing basic functionality",
        task=experiment.task,
        evaluators=[
            experiment.answer_correctness_evaluator,
            experiment.answer_semantic_similarity_evaluator,
        ],
        metadata={
            "task_chat_model": task_chat_model,
            "eval_chat_model": eval_chat_model,
            "eval_embedding_model": eval_embedding_model,
        },
        max_concurrency=3,
    )
    print(result)

    langfuse.flush()


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-m", "--model", default="gpt-5-mini", type=str)
    parser.add_argument("--eval-model", default="gpt-5-mini", type=str)
    parser.add_argument(
        "--eval-embedding-model", default="text-embedding-3-small", type=str
    )
    args = parser.parse_args()
    main(
        task_chat_model=args.model,
        eval_chat_model=args.eval_model,
        eval_embedding_model=args.eval_embedding_model,
    )
