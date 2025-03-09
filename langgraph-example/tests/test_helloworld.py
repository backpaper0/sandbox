import unittest
import app.helloworld as sut


class TestHelloWorld(unittest.IsolatedAsyncioTestCase):
    async def test_simple_graph(self) -> None:
        graph = sut.build_simple_graph()

        actual = await graph.ainvoke({"input": "foo"})

        expected = {
            "input": "foo",
            "output": "foo bar baz qux",
        }

        self.assertEqual(actual, expected)
