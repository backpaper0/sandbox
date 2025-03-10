import unittest
import app.condition as sut


class TestConditionalBranch(unittest.IsolatedAsyncioTestCase):
    async def test_select_node2(self) -> None:
        graph = sut.build_conditionoal_branch_graph()

        actual = await graph.ainvoke({"input": "yes"})

        expected = {
            "input": "yes",
            "output": "2",
        }

        self.assertEqual(actual, expected)

    async def test_select_node3(self) -> None:
        graph = sut.build_conditionoal_branch_graph()

        actual = await graph.ainvoke({"input": "no"})

        expected = {
            "input": "no",
            "output": "3",
        }

        self.assertEqual(actual, expected)
