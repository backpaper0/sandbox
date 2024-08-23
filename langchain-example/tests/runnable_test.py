"""
Runnableを知る。

poetry run python -m tests.runnable_test -v
"""

import unittest

from langchain_core.runnables import (
    Runnable,
    RunnableAssign,
    RunnableBinding,
    RunnableBranch,
    RunnableConfig,
    RunnableGenerator,
    RunnableLambda,
    RunnableMap,
    RunnableParallel,
    RunnablePassthrough,
    RunnablePick,
    RunnableSequence,
    RunnableSerializable,
    RunnableWithFallbacks,
    RunnableWithMessageHistory,
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
        r1 = RunnableLambda(lambda input: f"{input['in']} bar")
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
        self.assertEqual(
            ret,
            {
                "in": "foo",
                "out": "foo bar",
            },
        )


if __name__ == "__main__":
    unittest.main()
