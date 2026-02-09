from fastapi import FastAPI

from .background_task import router as background_task_router
from .reverse_proxy import router as reverse_proxy_router

app = FastAPI()

app.include_router(reverse_proxy_router, prefix="/reverse-proxy")
app.include_router(background_task_router, prefix="/bgtask")
