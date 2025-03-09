import unittest
import app.parallel as sut
import asyncio


class TestParallel(unittest.IsolatedAsyncioTestCase):
    async def test_parallel_graph(self) -> None:
        graph, control_event = sut.build_parallel_graph()

        task = asyncio.create_task(graph.ainvoke({"input": "xxx"}))

        actual = await control_event()

        await task

        expected = [
            "begin: node1",
            "begin: node2",
            "begin: node3",
            "end: node1",
            "end: node2",
            "end: node3",
        ]

        self.assertEqual(actual, expected)
