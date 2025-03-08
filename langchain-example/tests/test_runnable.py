"""
Runnableを知る。

poetry run python -m tests.runnable_test -v
"""

from typing import Any
import unittest

from langchain_core.runnables import (
    RunnableAssign,
    RunnableConfig,
    RunnableLambda,
    RunnableParallel,
    RunnablePassthrough,
)


class RunnableTest(unittest.TestCase):
    def test_parallel(self) -> None:
        """
        複数のRunnableをまとめて結果をdictで取得する。
        """
        r1 = RunnableLambda(lambda input: f"{input} bar")
        r2 = RunnableLambda(lambda input: f"{input} baz")
        r3 = RunnableLambda(lambda input: f"{input} qux")
        r4 = RunnableParallel(r1=r1, r2=r2, r3=r3)
        ret = r4.invoke("foo")
        self.assertDictEqual(
            ret,
            {
                "r1": "foo bar",
                "r2": "foo baz",
                "r3": "foo qux",
            },
        )

    def test_pick_one(self) -> None:
        """
        pickで一つの結果を取り出す。
        """
        r1 = RunnableLambda(lambda input: f"{input} bar")
        r2 = RunnableLambda(lambda input: f"{input} baz")
        r3 = RunnableLambda(lambda input: f"{input} qux")
        r4 = RunnableParallel(r1=r1, r2=r2, r3=r3)
        r5 = r4.pick("r1")

        ret = r5.invoke("foo")
        self.assertEqual(ret, "foo bar")

    def test_pick_some(self) -> None:
        """
        pickで複数の結果を取り出す。
        """
        r1 = RunnableLambda(lambda input: f"{input} bar")
        r2 = RunnableLambda(lambda input: f"{input} baz")
        r3 = RunnableLambda(lambda input: f"{input} qux")
        r4 = RunnableParallel(r1=r1, r2=r2, r3=r3)
        r5 = r4.pick(["r1", "r2"])

        ret = r5.invoke("foo")
        self.assertEqual(
            ret,
            {
                "r1": "foo bar",
                "r2": "foo baz",
            },
        )

    def test_assign(self) -> None:
        """
        入力を引き回す。
        """
        r1: RunnableLambda[dict[str, str], str] = RunnableLambda(
            lambda input: f"{input['in']} bar"
        )
        r2 = RunnableParallel(out=r1)
        r3 = RunnableAssign(r2)

        ret = r3.invoke({"in": "foo"})
        self.assertEqual(
            ret,
            {
                "in": "foo",
                "out": "foo bar",
            },
        )

    def test_assign_method(self) -> None:
        """
        入力を引き回す。
        """
        r1 = RunnablePassthrough()
        r2 = r1.assign(out=RunnableLambda(lambda input: f"{input['in']} bar"))

        ret = r2.invoke({"in": "foo"})
        self.assertDictEqual(
            ret,
            {
                "in": "foo",
                "out": "foo bar",
            },
        )

    def test_with_config(self) -> None:
        """
        configを指定する。
        """

        def f(input: Any, config: RunnableConfig) -> Any:
            return {
                "input": input,
                "configurable": config.get("configurable"),
            }

        r1 = RunnableLambda(f)
        config = RunnableConfig(
            configurable={
                "xxx": "yyy",
            }
        )
        r2 = r1.with_config(config)
        ret = r2.invoke("foobar")
        self.assertDictEqual(
            ret,
            {
                "input": "foobar",
                "configurable": {
                    "xxx": "yyy",
                },
            },
        )

    def test_invoke_with_config(self) -> None:
        """
        invokeするときにconfigを指定する。
        """

        def f(input: Any, config: RunnableConfig) -> Any:
            return {
                "input": input,
                "configurable": config.get("configurable"),
            }

        r1 = RunnableLambda(f)
        config = RunnableConfig(
            configurable={
                "xxx": "yyy",
            }
        )
        ret = r1.invoke(input="foobar", config=config)
        self.assertDictEqual(
            ret,
            {
                "input": "foobar",
                "configurable": {
                    "xxx": "yyy",
                },
            },
        )


if __name__ == "__main__":
    unittest.main()
