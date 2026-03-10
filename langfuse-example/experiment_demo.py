import argparse
from typing import cast

from langfuse import get_client
from langfuse.api import NotFoundError
from openai import OpenAI

from evaluators import correctness_evaluator

dataset = [
    {"input": "What is the capital of France?", "expected_output": "Paris"},
    {"input": "What is the capital of Germany?", "expected_output": "Berlin"},
    {"input": "日本の首都はどこですか？", "expected_output": "東京"},
]


class MyExperiment:
    model: str
    openai: OpenAI

    def __init__(self, *, model: str) -> None:
        self.model = model
        self.openai = OpenAI()

    def task(self, *, item, **kwargs) -> str:
        question = item.input
        completion = self.openai.chat.completions.create(
            messages=[{"role": "user", "content": question}],
            model=self.model,
        )
        return cast(str, completion.choices[0].message.content)


def main(model: str) -> None:
    langfuse = get_client()

    dataset_name = "geography_quiz"
    try:
        dataset_client = langfuse.get_dataset(name=dataset_name)
    except NotFoundError:
        langfuse.create_dataset(name=dataset_name)
        for item in dataset:
            langfuse.create_dataset_item(
                dataset_name=dataset_name,
                input=item["input"],
                expected_output=item["expected_output"],
            )
        dataset_client = langfuse.get_dataset(name=dataset_name)

    experiment = MyExperiment(
        model=model,
    )

    result = dataset_client.run_experiment(
        name="Geography Quiz",
        description="Testing basic functionality",
        task=experiment.task,
        evaluators=[correctness_evaluator],
        metadata={
            "task_chat_model": model,
        },
        max_concurrency=3,
    )
    print(f"Run ID: {result.dataset_run_id}")
    print(f"Run URL: {result.dataset_run_url}")
    langfuse.flush()


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-m", "--model", default="gpt-5-mini", type=str)
    args = parser.parse_args()
    main(model=args.model)
