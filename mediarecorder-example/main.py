from fastapi import FastAPI, UploadFile
from fastapi.responses import FileResponse

app = FastAPI()


@app.post("/upload-audio")
def upload_audio(audio: UploadFile):
    with open(f"{audio.filename}", "wb") as buffer:
        buffer.write(audio.file.read())
    return {"filename": audio.filename}


@app.get("/")
def index_html():
    return FileResponse("index.html")
