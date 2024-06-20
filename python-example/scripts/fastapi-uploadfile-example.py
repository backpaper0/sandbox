from typing import Annotated

import uvicorn
from fastapi import FastAPI, File, UploadFile
from fastapi.staticfiles import StaticFiles

app = FastAPI()


@app.post("/files/")
async def create_file(file: Annotated[bytes, File()]):
    return {"file_size": len(file)}


@app.post("/uploadfile/")
async def create_upload_file(file: UploadFile):
    return {"filename": file.filename}


app.mount(
    path="/",
    app=StaticFiles(directory="misc/fastapi-uploadfile-example-static"),
    name="static",
)

uvicorn.run(app)
