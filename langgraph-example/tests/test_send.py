import unittest
import app.send as sut


class TestSend(unittest.IsolatedAsyncioTestCase):
    async def test_use_send_graph(self) -> None:
        graph = sut.build_use_send_graph()

        actual = await graph.ainvoke({"input": 5})

        expected = {
            "input": 5,
            "output": [
                "node1",
                "node2#0",
                "node2#1",
                "node2#2",
                "node2#3",
                "node2#4",
            ],
        }

        self.assertEqual(actual, expected)
