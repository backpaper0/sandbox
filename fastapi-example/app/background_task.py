from typing import Annotated, Any
from uuid import uuid4

import aiohttp
from fastapi import APIRouter, BackgroundTasks, Form

router = APIRouter()


async def do_task(task_id: str, content: str) -> None:
    print(f"begin: {task_id} {content}")
    async with aiohttp.ClientSession() as session:
        for i in range(10):
            async with session.get("http://localhost:8081/delay/1") as resp:
                print(f"{i + 1}: {task_id} {content} - {resp.status}")
    print(f"end: {task_id} {content}")


@router.post("/")
async def trigger_background_task(
    content: Annotated[str, Form()], background_tasks: BackgroundTasks
) -> dict[str, Any]:
    task_id = str(uuid4())
    background_tasks.add_task(do_task, task_id, content)
    return {"task_id": task_id}
