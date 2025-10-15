from argparse import ArgumentParser

import docker
from prefect import flow, get_run_logger, task


def _run_container(image: str, command: list[str]) -> None:
    logger = get_run_logger()
    client = docker.from_env()
    container = client.containers.run(
        image,
        command=command,
        detach=True,
    )
    for log in container.logs(stream=True):
        logger.info(log.decode().strip())
    resp = container.wait()
    container.remove()
    logger.info("Container exit code: %s", resp["StatusCode"])
    if resp["StatusCode"] != 0:
        raise Exception("[%s] %s", resp["StatusCode"], resp.get("Error", "-"))


@task
def hello() -> None:
    _run_container(
        "python:3.11",
        command=["python", "-c", "print('hello')"],
    )


@task
def world(fail: bool) -> None:
    _run_container(
        "python:3.11",
        command=[
            "python",
            "-c",
            "raise Exception('example')" if fail else "print('world')",
        ],
    )


@flow
def docker_example(fail: bool) -> None:
    future1 = hello.submit()
    future2 = world.submit(fail=fail, return_state=False, wait_for=[future1])
    future2.wait()


if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument(
        "--fail",
        action="store_true",
    )
    args = parser.parse_args()
    docker_example(args.fail)
