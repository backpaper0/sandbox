import os

from deepeval.metrics import GEval
from deepeval.models import GPTModel
from deepeval.test_case import LLMTestCase, LLMTestCaseParams
from langfuse import Evaluation

_model = os.environ.get("EVAL_MODEL", "gpt-5-mini")


def correctness_evaluator(*, input, output, expected_output, **kwargs) -> Evaluation:
    test_case = LLMTestCase(
        input=input,
        actual_output=output,
        expected_output=expected_output,
    )
    model = GPTModel(
        model=_model,
        temperature=0,
        cost_per_input_token=0.00000025,
        cost_per_output_token=0.000002,
    )
    correctness = GEval(
        name="Correctnetss",
        criteria="実際の出力が、期待される出力に基づいて事実として正しいかどうかを判断してください。",
        evaluation_params=[
            LLMTestCaseParams.INPUT,
            LLMTestCaseParams.ACTUAL_OUTPUT,
            LLMTestCaseParams.EXPECTED_OUTPUT,
        ],
        model=model,
    )
    correctness.measure(test_case)
    return Evaluation(
        name="Correctness",
        value=correctness.score if correctness.score else 0.0,
        comment=correctness.reason,
        metadata={"model": _model},
    )
