from typing import Awaitable, cast
from prefect_shell import ShellOperation
from prefect import flow, task, get_run_logger
import asyncio
from pydantic import BaseModel


@task
async def sleep(t: float) -> None:
    await asyncio.sleep(t)


@task
async def task_example() -> None:
    async with ShellOperation(
        commands=[
            "docker run --rm hello-world",
        ]
    ) as operation:
        await cast(Awaitable, operation.run())


@task
async def parallel_task_example(t: float) -> None:
    async with ShellOperation(
        commands=[
            "docker run --rm busybox sleep ${SLEEP_TIME}",
        ],
        env={"SLEEP_TIME": str(t)},
    ) as operation:
        await cast(Awaitable, operation.run())


class MyParameter(BaseModel):
    foo: str
    bar: int
    baz: bool


@task
async def parameter_example(my_parameter: MyParameter) -> None:
    logger = get_run_logger()
    logger.info("Parameter: %s", my_parameter)
    logger.info("Parameter Type: %s", type(my_parameter))


class Counter(BaseModel):
    count: int = 0

    def increment(self) -> int:
        self.count += 1
        return self.count


@task(retries=5, retry_delay_seconds=1)
async def retry_example(counter: Counter) -> None:
    count = counter.increment()
    get_run_logger().info("Count: %s", count)
    if count < 3:
        raise Exception("Retrying...")
    else:
        get_run_logger().info("Success!")


@flow
async def flow_example() -> None:
    await task_example()

    parallel_example_coros = [parallel_task_example(t) for t in [1, 2, 3]]
    await asyncio.gather(*parallel_example_coros)

    my_parameter = MyParameter(foo="hello", bar=42, baz=True)
    await parameter_example(my_parameter=my_parameter)

    await retry_example(Counter())


if __name__ == "__main__":
    asyncio.run(flow_example())
