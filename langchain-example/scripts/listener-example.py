import asyncio

from langchain_core.runnables import RunnableConfig, RunnableLambda
from langchain_core.tracers.schemas import Run


async def handle_start_1(run: Run, config: RunnableConfig) -> None:
    print("#1 start")


async def handle_end_1(run: Run, config: RunnableConfig) -> None:
    print("#1 end")


async def handle_start_2(run: Run, config: RunnableConfig) -> None:
    print("#2 start")


async def handle_end_2(run: Run, config: RunnableConfig) -> None:
    print("#2 end")


async def handle_start_3(run: Run, config: RunnableConfig) -> None:
    print("#3 start")


async def handle_end_3(run: Run, config: RunnableConfig) -> None:
    print("#3 end")


async def runnable_1(input: str, config: RunnableConfig) -> str:
    print("#1 runnable")
    return input


async def runnable_2(input: str, config: RunnableConfig) -> str:
    print("#2 runnable")
    return input


r1 = RunnableLambda(runnable_1).with_alisteners(  # type: ignore
    on_start=handle_start_1,
    on_end=handle_end_1,
)

r2 = RunnableLambda(runnable_2).with_alisteners(  # type: ignore
    on_start=handle_start_2,
    on_end=handle_end_2,
)

chain = (r1 | r2).with_alisteners(
    on_start=handle_start_3,
    on_end=handle_end_3,
)


async def main() -> None:
    result = await chain.ainvoke("Hello World")

    print(result)


asyncio.run(main())
