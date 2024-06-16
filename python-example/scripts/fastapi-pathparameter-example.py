import uvicorn
from fastapi import FastAPI

app = FastAPI()


@app.get("/foobars/{foo}/{bar}")
async def pathparam(foo: str, bar: str):
    return {"foo": foo, "bar": bar}


uvicorn.run(app)
