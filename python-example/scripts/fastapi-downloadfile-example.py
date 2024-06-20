import uvicorn
from fastapi import FastAPI
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles

app = FastAPI()


# FileResponse へ filename を渡すとレスポンスへ
# Content-Disposition: attachment; filename*=utf-8''%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB.py
# が付与されるっぽい。
@app.get("/file")
async def download_file():
    return FileResponse(
        path="fastapi-downloadfile-example.py",
        filename="サンプル.py",
    )


app.mount(
    path="/",
    app=StaticFiles(directory="misc/fastapi-downloadfile-example-static"),
    name="static",
)

uvicorn.run(app)
