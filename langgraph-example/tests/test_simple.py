import unittest
import app.simple as sut


class TestSimple(unittest.IsolatedAsyncioTestCase):
    async def test_simple_graph(self) -> None:
        graph = sut.build_simple_graph()

        actual = await graph.ainvoke({"input": "foo"})

        expected = {
            "input": "foo",
            "output": "foo bar baz qux",
        }

        self.assertEqual(actual, expected)
