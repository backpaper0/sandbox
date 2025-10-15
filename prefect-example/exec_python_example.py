import asyncio
import subprocess

from prefect import flow
from prefect.logging import get_run_logger


@flow
async def exec_python():
    logger = get_run_logger()

    with subprocess.Popen(
        args=["uv", "run", "main.py"],
        cwd="../python-example/hello-requests",
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
        bufsize=1,  # 行単位でフラッシュ
    ) as proc:
        assert proc.stdout is not None
        for line in proc.stdout:
            logger.info(line.rstrip())

        returncode = proc.wait()

    if returncode != 0:
        raise RuntimeError(f"`uv run main.py` exited with code {returncode}")

    logger.info("uv job completed successfully")


if __name__ == "__main__":
    asyncio.run(exec_python())
