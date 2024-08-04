from fastapi import FastAPI

app = FastAPI()

@app.get("/")
async def hello_fly():
    return 'Hello World'