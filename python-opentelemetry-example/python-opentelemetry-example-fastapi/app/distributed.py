import aiohttp
from fastapi import APIRouter, Request

router = APIRouter()


@router.get("/foo")
async def foo():
    async with aiohttp.ClientSession() as session:
        async with session.get("http://localhost:8000/distributed/bar") as resp:
            json_data = await resp.json()
            return {"foo": "Hello from foo!", **json_data}


@router.get("/bar")
async def bar(req: Request):
    return {"bar": "Hello from bar!", "headers": dict(req.headers)}
