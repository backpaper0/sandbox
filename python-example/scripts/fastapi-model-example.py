import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()


class Song(BaseModel):
    title: str
    singer: str


@app.get("/song")
async def get_song() -> Song:
    return Song(title="Limit", singer="LUNA SEA")


uvicorn.run(app)
