from typing import Any, Awaitable, Callable, Tuple
from langgraph.graph import StateGraph
from langgraph.graph.state import CompiledStateGraph
from typing_extensions import TypedDict
import asyncio


class State(TypedDict):
    input: str


def create_node(
    node_name: str,
    queue: asyncio.Queue,
    barrier: asyncio.Barrier,
) -> Tuple[
    Callable[[State], Awaitable[dict[str, Any]]],
    Tuple[asyncio.Event, asyncio.Event],
    Tuple[asyncio.Event, asyncio.Event],
]:
    begin_event = asyncio.Event()
    began_event = asyncio.Event()
    begin_events = (begin_event, began_event)

    end_event = asyncio.Event()
    ended_event = asyncio.Event()
    end_events = (end_event, ended_event)

    async def node(state: State) -> dict[str, Any]:
        await begin_event.wait()
        await queue.put(f"begin: {node_name}")
        began_event.set()
        await end_event.wait()
        await queue.put(f"end: {node_name}")
        ended_event.set()
        await barrier.wait()
        return {}

    return (node, begin_events, end_events)


def create_event_controller(
    queue: asyncio.Queue,
    events: list[Tuple[asyncio.Event, asyncio.Event]],
    barrier: asyncio.Barrier,
) -> Callable[[], Awaitable[list[str]]]:
    async def control_event() -> list[str]:
        for event1, event2 in events:
            event1.set()
            await event2.wait()
        await barrier.wait()

        async def generator():
            while not queue.empty():
                value = await queue.get()
                yield value

        return [value async for value in generator()]

    return control_event


def build_parallel_graph() -> Tuple[
    CompiledStateGraph, Callable[[], Awaitable[list[str]]]
]:
    # 複数のノードが並列に処理されていることを確認するため、Eventで処理の流れを次のように制御しつつQueueにノードの開始・終了をエンキューする。
    # - node1開始
    # - node2開始
    # - node3開始
    # - node1終了
    # - node2終了
    # - node3終了

    queue = asyncio.Queue()
    barrier = asyncio.Barrier(4)

    builder = StateGraph(State)

    node1, node1_begin_events, node1_end_events = create_node("node1", queue, barrier)
    node2, node2_begin_events, node2_end_events = create_node("node2", queue, barrier)
    node3, node3_begin_events, node3_end_events = create_node("node3", queue, barrier)

    builder.add_node("node1", node1)
    builder.add_node("node2", node2)
    builder.add_node("node3", node3)

    builder.set_entry_point("node1")
    builder.set_entry_point("node2")
    builder.set_entry_point("node3")
    builder.set_finish_point("node1")
    builder.set_finish_point("node2")
    builder.set_finish_point("node3")

    events = [
        node1_begin_events,
        node2_begin_events,
        node3_begin_events,
        node1_end_events,
        node2_end_events,
        node3_end_events,
    ]
    control_event = create_event_controller(queue, events, barrier)

    return (builder.compile(), control_event)
