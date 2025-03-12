import unittest
import app.command as sut


class TestCommand(unittest.IsolatedAsyncioTestCase):
    async def test_use_command_graph_through_node2(self) -> None:
        graph = sut.build_use_command_graph()

        actual = await graph.ainvoke({"input": True})

        expected = {
            "input": True,
            "output": ["node1", "node2"],
        }

        self.assertEqual(actual, expected)

    async def test_use_command_graph_through_node3(self) -> None:
        graph = sut.build_use_command_graph()

        actual = await graph.ainvoke({"input": False})

        expected = {
            "input": False,
            "output": ["node1", "node3"],
        }

        self.assertEqual(actual, expected)
