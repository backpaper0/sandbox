import unittest
import app.subgraph as sut


class TestSimple(unittest.IsolatedAsyncioTestCase):
    async def test_with_subgraph_graph(self) -> None:
        graph = sut.build_with_subgraph_graph()

        actual = await graph.ainvoke({"input": "foobar"})

        expected = {
            "input": "foobar",
            "output1": [1, 1, 2],
            "output2": [3, 4],
        }

        self.assertEqual(actual, expected)
