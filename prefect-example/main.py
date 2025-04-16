from prefect import flow, task, get_run_logger
import asyncio
from pydantic import BaseModel
import docker


@task
async def sleep(t: float) -> None:
    await asyncio.sleep(t)


@task
async def task_example() -> None:
    logger = get_run_logger()
    client = docker.from_env()
    container = client.containers.run("hello-world", detach=True)
    for log in container.logs(stream=True):
        logger.info(log.decode().strip())
    container.wait()
    container.remove()


@task
async def parallel_task_example(t: float) -> None:
    logger = get_run_logger()
    client = docker.from_env()
    container = client.containers.run(
        image="busybox",
        command=["sleep", str(t)],
        detach=True,
    )
    for log in container.logs(stream=True):
        logger.info(log.decode().strip())
    container.wait()
    container.remove()


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
def flow_example() -> None:
    example_future = task_example.submit()

    parallel_example_futures = [
        parallel_task_example.submit(t, return_state=False, wait_for=example_future)
        for t in [1, 2, 3]
    ]

    my_parameter = MyParameter(foo="hello", bar=42, baz=True)
    parameter_example_future = parameter_example.submit(
        my_parameter=my_parameter, return_state=False, wait_for=parallel_example_futures
    )

    retry_example_future = retry_example.submit(
        Counter(), return_state=False, wait_for=parameter_example_future
    )
    retry_example_future.wait()


if __name__ == "__main__":
    flow_example()
