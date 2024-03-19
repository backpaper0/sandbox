from typing import Any, AsyncIterator, Dict
from fastapi import Cookie, FastAPI, Depends
from pydantic import BaseModel
from sse_starlette import EventSourceResponse
import asyncio

app = FastAPI()

class Item(BaseModel):
    name: str
    price: float
    is_offer: bool | None = None

@app.get("/")
def read_root():
    return {"Hello": "World"}


@app.get("/items/{item_id}")
def read_item(item_id: int, q: str | None = None):
    return {"item_id": item_id, "q": q}

@app.put("/items/{item_id}")
def update_item(item_id: int, item: Item):
    return {"item_name": item.name, "item_id": item_id}



class CommonQueryParams:
    def __init__(self, q: str | None = None, skip: int = 0, limit: int = 100):
        self.q = q
        self.skip = skip
        self.limit = limit

@app.get("/items/")
async def read_items(commons: CommonQueryParams = Depends()):
    return commons

@app.get("/users/")
async def read_users(commons: CommonQueryParams = Depends()):
    return commons



def query_extractor(q: str | None = None):
    return q


def query_or_cookie_extractor(
    q: str = Depends(query_extractor),
    last_query: str | None = Cookie(default=None),
):
    if not q:
        return last_query
    return q


@app.get("/items2/")
async def read_query(query_or_default: str = Depends(query_or_cookie_extractor)):
    return {"q_or_cookie": query_or_default}


# curl -N localhost:8000/foobar
@app.get("/foobar")
async def foobar() -> EventSourceResponse:
    return EventSourceResponse(_foobar_streamer())

async def _foobar_streamer() -> AsyncIterator[Dict[str, Any]]:
    for i in range(10):
        await asyncio.sleep(.1)
        yield {
            "event": "data",
            "data": f"{i}",
        }
